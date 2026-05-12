package com.StayFlow.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.StayFlow.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    
    // Obtener reseñas de una propiedad (Huésped -> Propiedad)
    // Filtramos que el autor de la reseña sea el mismo que el cliente de la reserva
    @Query("SELECT r FROM Resena r WHERE r.reserva.habitacion.propiedad.idPropiedad = :idPropiedad " +
           "AND r.usuario.idUsuario = r.reserva.cliente.idUsuario")
    List<Resena> findResenasByPropiedad(@Param("idPropiedad") Integer idPropiedad);

    // Obtener reseñas sobre un Huésped (Anfitrión -> Huésped)
    // Filtramos que el autor NO sea el cliente (por ende es el dueño)
    @Query("SELECT r FROM Resena r WHERE r.reserva.cliente.idUsuario = :idUsuario " +
           "AND r.usuario.idUsuario != r.reserva.cliente.idUsuario")
    List<Resena> findResenasSobreHuesped(@Param("idUsuario") Integer idUsuario);
    
    // Verificar si el usuario ya comentó en esta reserva para evitar duplicados
    boolean existsByReserva_IdReservaAndUsuario_IdUsuario(Integer idReserva, Integer idUsuario);
}