package com.StayFlow.specification;

import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Servicio;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PropiedadSpecification {

    public static Specification<Propiedad> buscarConFiltros(String termino, LocalDate checkin, LocalDate checkout, List<Integer> serviciosIds, BigDecimal precioMaximo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Solo propiedades públicas y no eliminadas
            predicates.add(cb.isFalse(root.get("estaEliminado")));
            predicates.add(cb.equal(root.get("estadoPropiedad"), Propiedad.EstadoPropiedad.PUBLICADA));

            // Filtro de Texto (Con diccionario de sinónimos quemado ya que por la etapa en la que se encuentra el proyecto no es viable implementar un sistema de sinónimos más robusto o directo en la base de datos)
           if (termino != null && !termino.trim().isEmpty()) {
                String busqueda = termino.toLowerCase().trim();
                
                // Normalizamos acentos básicos para el switch
                busqueda = busqueda.replace("á", "a").replace("é", "e")
                                   .replace("í", "i").replace("ó", "o").replace("ú", "u");

                switch (busqueda) {
                    // CDMX y Área Metropolitana
                    case "cdmx": case "df": case "d.f.": case "distrito federal": case "capital":
                        busqueda = "ciudad de mexico"; break;
                    case "edomex": case "edo mex": case "edo. mex.": case "estado de mexico":
                        busqueda = "mexico"; break; 
                    
                    // Norte
                    case "mty": case "nl": case "n.l.":
                        busqueda = "monterrey"; break; 
                    case "tj": case "tij": case "bc": case "b.c.":
                        busqueda = "baja california"; break;
                    case "bcs": case "b.c.s.": case "cabos":
                        busqueda = "baja california sur"; break;
                    case "chih": case "cuu":
                        busqueda = "chihuahua"; break;
                    case "coah": case "saltillo":
                        busqueda = "coahuila"; break;
                    case "son": case "hmo":
                        busqueda = "sonora"; break;
                    case "tamps": case "tam":
                        busqueda = "tamaulipas"; break;
                        
                    // Pacífico / Occidente
                    case "gdl": case "jal":
                        busqueda = "jalisco"; break; // Convierte GDL a Jalisco para abarcar todo el estado
                    case "sin": case "cul": case "mzt":
                        busqueda = "sinaloa"; break;
                    case "nay": case "tepic":
                        busqueda = "nayarit"; break;
                    case "col":
                        busqueda = "colima"; break;
                    case "mich":
                        busqueda = "michoacan"; break;
                        
                    // Centro / Bajío
                    case "ags": case "aguas":
                        busqueda = "aguascalientes"; break;
                    case "gto": case "leon":
                        busqueda = "guanajuato"; break;
                    case "qro":
                        busqueda = "queretaro"; break;
                    case "slp":
                        busqueda = "san luis potosi"; break;
                    case "zac":
                        busqueda = "zacatecas"; break;
                    case "hgo": case "pachuca":
                        busqueda = "hidalgo"; break;
                    case "pue":
                        busqueda = "puebla"; break;
                    case "tlax":
                        busqueda = "tlaxcala"; break;
                    case "mor": case "cuerna":
                        busqueda = "morelos"; break;
                        
                    // Sur / Sureste
                    case "gro": case "aca": case "acapulco":
                        busqueda = "guerrero"; break;
                    case "oax":
                        busqueda = "oaxaca"; break;
                    case "chis": 
                        busqueda = "chiapas"; break;
                    case "tab": case "villahermosa":
                        busqueda = "tabasco"; break;
                    case "camp":
                        busqueda = "campeche"; break;
                    case "yuc": case "mid": case "merida":
                        busqueda = "yucatan"; break;
                    case "qroo": case "q. roo": case "cancun": case "cun":
                        busqueda = "quintana roo"; break;
                }

                String pattern = "%" + busqueda + "%";

                // Busca en nombre de la propiedad o en la ciudad/estado de la dirección
                Join<Object, Object> direccionJoin = root.join("direccion", JoinType.LEFT);
                Join<Object, Object> coloniaJoin = direccionJoin.join("colonia", JoinType.LEFT);
                Join<Object, Object> municipioJoin = coloniaJoin.join("municipio", JoinType.LEFT);
                Join<Object, Object> estadoJoin = municipioJoin.join("estado", JoinType.LEFT);

                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("nombreComercial")), pattern),
                    cb.like(cb.lower(coloniaJoin.get("nombre")), pattern),
                    cb.like(cb.lower(municipioJoin.get("nombre")), pattern),
                    cb.like(cb.lower(estadoJoin.get("nombre")), pattern)
                ));
            }

            // Filtro de Servicios (Comodidades)
            if (serviciosIds != null && !serviciosIds.isEmpty()) {
                Join<Propiedad, Servicio> serviciosJoin = root.join("servicios", JoinType.INNER);
                predicates.add(serviciosJoin.get("idServicio").in(serviciosIds));
            }

            // Filtro de Fechas (Disponibilidad real)
            if (checkin != null && checkout != null) {
                // Subquery: Buscar reservas que se empalmen con las fechas
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<Reserva> reservaRoot = subquery.from(Reserva.class);
                subquery.select(cb.literal(1)); // Solo nos interesa si existe

                Join<Object, Object> habitacionJoin = reservaRoot.join("habitacion");
                
                Predicate empalmeFechas = cb.and(
                    cb.lessThan(reservaRoot.get("fechaEntrada"), checkout),
                    cb.greaterThan(reservaRoot.get("fechaSalida"), checkin)
                );
                
                Predicate reservaValida = cb.notEqual(reservaRoot.get("estadoReserva"), Reserva.EstadoReserva.cancelada);
                Predicate esDeEstaPropiedad = cb.equal(habitacionJoin.get("propiedad"), root);

                subquery.where(cb.and(empalmeFechas, reservaValida, esDeEstaPropiedad));

                // Agrega a los predicados que no exista un empalme
                predicates.add(cb.not(cb.exists(subquery)));
            }

            // Filtro de Precio Máximo
            if (precioMaximo != null) {
                // Caso A: Casa Completa (Compara con precioNoche)
                Predicate casaCompleta = cb.and(
                    cb.isFalse(root.get("seRentaPorHabitaciones")),
                    cb.lessThanOrEqualTo(root.get("precioNoche"), precioMaximo)
                );

                // Caso B: Hotel/Hostal (Busca si alguna habitación cuesta igual o menos)
                Join<Object, Object> tiposHabitacionJoin = root.join("tiposHabitacion", JoinType.LEFT);
                Predicate esHotel = cb.and(
                    cb.isTrue(root.get("seRentaPorHabitaciones")),
                    cb.isFalse(tiposHabitacionJoin.get("estaEliminado")), 
                    cb.lessThanOrEqualTo(tiposHabitacionJoin.get("precioBaseNoche"), precioMaximo)
                );

                predicates.add(cb.or(casaCompleta, esHotel));
            }

            // Evita duplicados si hay muchos joins
            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}