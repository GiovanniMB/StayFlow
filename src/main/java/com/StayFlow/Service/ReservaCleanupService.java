package com.StayFlow.service;

import com.StayFlow.repository.ReservaRepository;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservaCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaCleanupService.class);
    private final ReservaRepository reservaRepository;
    private final EmailService emailService;

    public ReservaCleanupService(ReservaRepository reservaRepository, EmailService emailService) {
        this.reservaRepository = reservaRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void liberarReservasPendientes() {
        LocalDateTime hace5Minutos = LocalDateTime.now().minusMinutes(5);
        int liberadas = reservaRepository.cancelarReservasExpiradas(hace5Minutos);
        if (liberadas > 0) {
            logger.info("Limpieza automática: {} reservas pendientes expiradas.", liberadas);
        }
    }

    // Correos de recordatorio para reservas próximas y check-outs, con lógica para enviar diferentes mensajes según el estado de la reserva (confirmada o en check-in) y usando Thymeleaf para templates dinámicos. Además, un verdugo que aplica penalizaciones por check-out tardío.
    @Scheduled(cron = "0 0 8 * * ?")
    //@Scheduled(fixedRate = 20000) // Para pruebas, cada 20 segundos
    public void enviarRecordatoriosDiarios() {
        LocalDate hoy = LocalDate.now();
        List<EstadoReserva> estadosActivos = java.util.Arrays.asList(EstadoReserva.confirmada, EstadoReserva.check_in);
        List<Reserva> reservasActivas = reservaRepository.findByEstadoReservaIn(estadosActivos); 

        for (Reserva r : reservasActivas) {
            if (r.getEstadoReserva() == EstadoReserva.confirmada) {
                long diasFaltantes = ChronoUnit.DAYS.between(hoy, r.getFechaEntrada());
                
                if (diasFaltantes == 6) {
                    Map<String, Object> vars = new HashMap<>();
                    vars.put("titulo", "Tu viaje se acerca - Políticas");
                    vars.put("mensaje", "Faltan 6 días para tu viaje a <b>" + r.getHabitacion().getPropiedad().getNombreComercial() + 
                                     "</b>. Te recordamos que si deseas cancelar y recibir un reembolso del 100%, tienes hasta mañana para hacerlo.");
                    vars.put("colorBoton", "#10b981"); // Verde
                    
                    emailService.enviarCorreoTemplate(r.getCliente().getEmail(), "Tu viaje se acerca", "notificacion-general", vars);
                }
            } else if (r.getEstadoReserva() == EstadoReserva.check_in) {
                if (hoy.isEqual(r.getFechaSalida())) {
                    Map<String, Object> vars = new HashMap<>();
                    vars.put("titulo", "Recordatorio de Salida");
                    vars.put("mensaje", "Esperamos que hayas disfrutado tu estancia. Recuerda que tu check-out debe ser antes de las <b>" + 
                                     r.getHabitacion().getPropiedad().getHoraCheckOut() + "</b> para evitar un cargo de penalización de 1 día extra.");
                    vars.put("colorBoton", "#f59e0b"); // Naranja
                    
                    emailService.enviarCorreoTemplate(r.getCliente().getEmail(), "Recordatorio de Salida", "notificacion-general", vars);
                }
            }
        }
        logger.info("Recordatorios diarios enviados con éxito.");
    }

    // Este método se ejecuta cada hora para verificar si hay reservas en check-in que no han hecho check-out a tiempo. Si encuentra alguna, le aplica una penalización de un día extra (extendiendo la fecha de salida y aumentando el monto total) y envía un correo notificando la penalización al cliente.
    // Lo llamamos Verdugo porque es el encargado de "castigar" si se incumple la hora de check-out, aplicando la lógica de penalización automáticamente.

    @Scheduled(cron = "0 0 8 * * ?")
    //@Scheduled(fixedRate = 20000) // Para pruebas, cada 20 segundos
    public void ejecutarVerdugoCheckOut() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        List<Reserva> enEstancia = reservaRepository.findByEstadoReserva(EstadoReserva.check_in);

        for (Reserva r : enEstancia) {
            LocalDate fechaSalida = r.getFechaSalida();
            // Aseguramos que horaCheckOut no sea null
            LocalTime horaMaximaSalida = r.getHabitacion().getPropiedad().getHoraCheckOut();
            if (horaMaximaSalida == null) horaMaximaSalida = LocalTime.of(11, 0); 

            // Lógica: Si hoy es después de la salida, O si es el mismo día y ya pasó la hora
            boolean debeMultarse = hoy.isAfter(fechaSalida) || 
                                  (hoy.isEqual(fechaSalida) && ahora.isAfter(horaMaximaSalida));

            if (debeMultarse) {
                r.setFechaSalida(r.getFechaSalida().plusDays(1));
                java.math.BigDecimal multa = r.getHabitacion().getTipoHabitacion().getPrecioBaseNoche();
                r.setMontoTotal(r.getMontoTotal().add(multa));
                
                reservaRepository.save(r);

                Map<String, Object> vars = new HashMap<>();
                vars.put("titulo", "Penalización aplicada");
                vars.put("mensaje", "No registramos tu check-out a tiempo de la habitación " + r.getHabitacion().getNumeroHabitacion() + 
                                 ". Se ha aplicado un cargo automático de <b>$" + multa + " MXN</b> por un día extra.");
                vars.put("colorBoton", "#ef4444"); // Rojo
                
                emailService.enviarCorreoTemplate(r.getCliente().getEmail(), "Penalización por Check-out tardío", "notificacion-general", vars);
                
                logger.info("Verdugo ejecutado: Penalización aplicada a la reserva {}", r.getIdReserva());
            }
        }
    }
}