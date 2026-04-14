package com.StayFlow.Service;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Schema(description = "Servicio para el envío de correos electrónicos (confirmación de cuenta, recuperación de contraseña)")
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Operation(summary = "Envía código de confirmación de cuenta al email del usuario")
    @Schema(description = "Se utiliza después del registro para verificar la dirección de correo electrónico")
    public void enviarCodigoConfirmacion(String destinatario, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("StayFlow - Confirma tu cuenta");
        message.setText("Bienvenido a StayFlow!\n\n" +
                        "Para activar tu cuenta, ingresa el siguiente código:\n\n" +
                        "CÓDIGO DE VERIFICACIÓN: " + codigo + "\n\n" +
                        "Este código expirará en 24 horas.\n\n" +
                        "Saludos,\nEquipo StayFlow");
        
        mailSender.send(message);
    }

    @Operation(summary = "Envía código de recuperación de contraseña")
    @Schema(description = "Se utiliza cuando el usuario olvida su contraseña y solicita restablecerla")
    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("StayFlow - Recuperación de contraseña");
        message.setText("Has solicitado restablecer tu contraseña.\n\n" +
                        "CÓDIGO DE RECUPERACIÓN: " + codigo + "\n\n" +
                        "Este código expirará en 1 hora.\n\n" +
                        "Si no solicitaste esto, ignora el mensaje.\n\n" +
                        "Saludos,\nEquipo StayFlow");
        
        mailSender.send(message);
    }
}