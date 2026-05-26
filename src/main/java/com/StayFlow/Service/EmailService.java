package com.StayFlow.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // Metodos para enviar correos de confirmación de cuenta y recuperación de contraseña, con HTML personalizado y soporte para templates Thymeleaf

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

    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(mensaje, false);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    // Thymeleaf: Método para enviar correos usando templates HTML con variables dinámicas
    public void enviarCorreoTemplate(String destinatario, String asunto, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true indica que es multipart (soporta HTML e imágenes incrustadas)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            // Carga las variables dinámicas en el contexto de Thymeleaf
            Context context = new Context();
            context.setVariables(variables);
            
            // Procesa el HTML
            String html = templateEngine.process(templateName, context);      
            helper.setText(html, true);

            // Incrusta el logo dinámicamente como un recurso CID (Content-ID)
            ClassPathResource image = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", image);

            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Error al enviar el correo con template: " + e.getMessage());
        }
    }

    // Cuerpo de los métodos de envío de correos para confirmar email y recuperación de contraseña (No usamos las templates para mostrarlo en la exposición, pero se mantienen por si queremos migrar a templates en el futuro)
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