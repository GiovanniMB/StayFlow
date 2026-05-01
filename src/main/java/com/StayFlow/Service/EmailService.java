package com.StayFlow.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

 

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCodigoConfirmacion(String destinatario, String nombre, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(destinatario);
            helper.setSubject("StayFlow - Confirma tu cuenta");
            
            String urlConfirmacion = "http://localhost:5173/confirmar-email?codigo=" + codigo;
            
            String htmlContent = construirHTMLConfirmacion(nombre, urlConfirmacion);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email de confirmación", e);
        }
    }

    public void enviarCodigoRecuperacion(String destinatario, String nombre, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(destinatario);
            helper.setSubject("StayFlow - Recuperación de contraseña");
            
            String urlRecuperacion = "http://localhost:5173/reset-password?codigo=" + codigo;
            
            String htmlContent = construirHTMLRecuperacion(nombre, urlRecuperacion);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email de recuperación", e);
        }
    }

    private String construirHTMLConfirmacion(String nombre, String urlConfirmacion) {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "<meta charset='UTF-8'>\n" +
               "<title>StayFlow - Confirma tu cuenta</title>\n" +
               "<style>\n" +
               "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }\n" +
               ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n" +
               ".header { background: #007bff; color: white; padding: 30px; text-align: center; }\n" +
               ".header h1 { margin: 0; font-size: 28px; }\n" +
               ".content { padding: 30px; text-align: center; }\n" +
               ".boton { display: inline-block; background: #28a745; color: white; padding: 14px 40px; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; font-size: 16px; }\n" +
               ".footer { background: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; border-top: 1px solid #ddd; }\n" +
               ".advertencia { background: #fff3cd; border: 1px solid #ffc107; padding: 15px; border-radius: 5px; margin: 20px 0; font-size: 12px; color: #856404; text-align: center; }\n" +
               "</style>\n" +
               "</head>\n" +
               "<body>\n" +
               "<div class='container'>\n" +
               "<div class='header'>\n" +
               "<h1>✅ StayFlow</h1>\n" +
               "<p>Hola, <strong>" + nombre + "</strong></p>\n" +
               "</div>\n" +
               "<div class='content'>\n" +
               "<h3>Confirma tu cuenta</h3>\n" +
               "<p>Gracias por registrarte en <strong>StayFlow</strong>. Para comenzar a usar nuestra plataforma, confirma tu direcci\u00f3n de correo electr\u00f3nico.</p>\n" +
               "<a href='" + urlConfirmacion + "' class='boton'>✅ Confirmar mi cuenta</a>\n" +
               "<div class='advertencia'>\n" +
               "⚠️ Este enlace expirar\u00e1 en <strong>24 horas</strong>.<br>\n" +
               "Si no creaste esta cuenta, ignora este mensaje.\n" +
               "</div>\n" +
               "</div>\n" +
               "<div class='footer'>\n" +
               "<p>StayFlow - Tu plataforma de reservas</p>\n" +
               "<p>Este es un correo autom\u00e1tico, por favor no responder</p>\n" +
               "</div>\n" +
               "</div>\n" +
               "</body>\n" +
               "</html>";
    }

    private String construirHTMLRecuperacion(String nombre, String urlRecuperacion) {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "<meta charset='UTF-8'>\n" +
               "<title>StayFlow - Recuperación de contraseña</title>\n" +
               "<style>\n" +
               "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }\n" +
               ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n" +
               ".header { background: #dc3545; color: white; padding: 30px; text-align: center; }\n" +
               ".header h1 { margin: 0; font-size: 28px; }\n" +
               ".content { padding: 30px; text-align: center; }\n" +
               ".boton { display: inline-block; background: #ffc107; color: #333; padding: 14px 40px; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; font-size: 16px; }\n" +
               ".footer { background: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; border-top: 1px solid #ddd; }\n" +
               ".advertencia { background: #fff3cd; border: 1px solid #ffc107; padding: 15px; border-radius: 5px; margin: 20px 0; font-size: 12px; color: #856404; text-align: center; }\n" +
               "</style>\n" +
               "</head>\n" +
               "<body>\n" +
               "<div class='container'>\n" +
               "<div class='header'>\n" +
               "<h1>🔐 StayFlow</h1>\n" +
               "<p>Hola, <strong>" + nombre + "</strong></p>\n" +
               "</div>\n" +
               "<div class='content'>\n" +
               "<h3>Recuperaci\u00f3n de contrase\u00f1a</h3>\n" +
               "<p>Hemos recibido una solicitud para restablecer tu contrase\u00f1a. Si no realizaste esta solicitud, ignora este mensaje.</p>\n" +
               "<a href='" + urlRecuperacion + "' class='boton'>🔑 Restablecer contrase\u00f1a</a>\n" +
               "<div class='advertencia'>\n" +
               "⚠️ Este enlace expirar\u00e1 en <strong>1 hora</strong>.<br>\n" +
               "Por seguridad, no compartas este enlace con nadie.\n" +
               "</div>\n" +
               "</div>\n" +
               "<div class='footer'>\n" +
               "<p>StayFlow - Tu plataforma de reservas</p>\n" +
               "<p>Este es un correo autom\u00e1tico, por favor no responder</p>\n" +
               "</div>\n" +
               "</div>\n" +
               "</body>\n" +
               "</html>";
    }
}