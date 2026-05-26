package com.StayFlow.service.impl;

import com.StayFlow.dto.request.ResenaRequestDTO;
import com.StayFlow.dto.response.ResenaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.ResenaMapper;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Resena;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;
import com.StayFlow.repository.PropiedadRepository;
import com.StayFlow.repository.ResenaRepository;
import com.StayFlow.repository.ReservaRepository;
import com.StayFlow.service.interfaces.IResenaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResenaServiceImpl implements IResenaService {

    private final ResenaRepository resenaRepository;
    private final ReservaRepository reservaRepository;
    private final PropiedadRepository propiedadRepository;
    private final ResenaMapper resenaMapper;

    public ResenaServiceImpl(ResenaRepository resenaRepository, ReservaRepository reservaRepository, PropiedadRepository propiedadRepository, ResenaMapper resenaMapper) {
        this.resenaRepository = resenaRepository;
        this.reservaRepository = reservaRepository;
        this.propiedadRepository = propiedadRepository;
        this.resenaMapper = resenaMapper;
    }

    @Override
    @Transactional
    public ResenaResponseDTO crearResena(ResenaRequestDTO request, Integer idUsuarioAutenticado) {
        // 1. Valida que la reserva exista
        Reserva reserva = reservaRepository.findById(request.getIdReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        // 2. Valida que la reserva pertenezca al usuario que está haciendo la petición
        if (!reserva.getCliente().getIdUsuario().equals(idUsuarioAutenticado)) {
            throw new BusinessException("No tienes permiso para reseñar una reserva que no es tuya.");
        }

        // 3. Valida que el viaje haya finalizado (Check-out)
        if (reserva.getEstadoReserva() != EstadoReserva.check_out) {
            throw new BusinessException("Solo puedes dejar una reseña una vez finalizada tu estancia (Check-out realizado).");
        }

        // 4. Valida que no haya comentado antes
        if (resenaRepository.existsByReserva_IdReserva(reserva.getIdReserva())) {
            throw new BusinessException("Ya has dejado una reseña para este viaje.");
        }

        // 5. Crea y guarda la reseña
        Resena resena = new Resena();
        resena.setReserva(reserva);
        resena.setUsuario(reserva.getCliente());
        resena.setPuntuacion(request.getPuntuacion());
        resena.setComentario(request.getComentario());
        Resena resenaGuardada = resenaRepository.save(resena);

        //Actualizar la propiedad (Promedio dinámico)
        Propiedad propiedad = reserva.getHabitacion().getPropiedad();
        
        Integer oldCount = propiedad.getCantidadResenas() == null ? 0 : propiedad.getCantidadResenas();
        Double oldAvg = propiedad.getCalificacionPromedio() == null ? 0.0 : propiedad.getCalificacionPromedio();
        
        // Fórmula de nuevo promedio: ((PromedioViejo * CantidadVieja) + NuevaPuntuacion) / (CantidadVieja + 1)
        Double newAvg = ((oldAvg * oldCount) + request.getPuntuacion()) / (oldCount + 1);
        
        // Redondear a 1 decimal
        propiedad.setCalificacionPromedio(Math.round(newAvg * 10.0) / 10.0);
        propiedad.setCantidadResenas(oldCount + 1);
        
        propiedadRepository.save(propiedad);

        return resenaMapper.toResponseDTO(resenaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> obtenerResenasPorPropiedad(Integer idPropiedad) {
        List<Resena> resenas = resenaRepository.findByReserva_Habitacion_Propiedad_IdPropiedadOrderByIdResenaDesc(idPropiedad);
        return resenaMapper.toResponseDTOList(resenas);
    }

}