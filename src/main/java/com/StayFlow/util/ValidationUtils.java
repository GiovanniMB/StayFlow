package com.StayFlow.util;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Schema(description = "Utilidad para validar datos de usuarios (email, contraseña, teléfono, etc.)")
public class ValidationUtils {


    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int PHONE_LENGTH = 10;
    private static final int CODIGO_LENGTH = 6;


    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\d{10}$");
    
    private static final Pattern CODIGO_PATTERN = 
        Pattern.compile("^\\d{6}$");


    @Schema(description = "Valida el formato de un email")
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Schema(description = "Valida email y lanza excepción si es inválido")
    public static void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    

    @Schema(description = "Valida que la contraseña cumpla los requisitos (mínimo 6 caracteres)")
    public static boolean isValidPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return password.length() >= MIN_PASSWORD_LENGTH && 
               password.length() <= MAX_PASSWORD_LENGTH;
    }

    @Schema(description = "Valida contraseña y lanza excepción si es inválida")
    public static void validatePassword(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                "La contraseña debe tener entre " + MIN_PASSWORD_LENGTH + 
                " y " + MAX_PASSWORD_LENGTH + " caracteres"
            );
        }
    }

   

    @Schema(description = "Valida que el nombre solo contenga letras y espacios")
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches() &&
               name.length() >= MIN_NAME_LENGTH &&
               name.length() <= MAX_NAME_LENGTH;
    }

    @Schema(description = "Valida nombre y lanza excepción si es inválido")
    public static void validateName(String name, String campo) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException(
                "El campo '" + campo + "' debe tener entre " + MIN_NAME_LENGTH + 
                " y " + MAX_NAME_LENGTH + " caracteres y solo contener letras"
            );
        }
    }

   

    @Schema(description = "Valida que el teléfono tenga 10 dígitos numéricos")
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return true; // Teléfono es opcional
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    @Schema(description = "Valida teléfono y lanza excepción si es inválido")
    public static void validatePhone(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException(
                "El teléfono debe tener " + PHONE_LENGTH + " dígitos numéricos"
            );
        }
    }

    

    @Schema(description = "Valida que el código tenga 6 dígitos numéricos")
    public static boolean isValidCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }
        return CODIGO_PATTERN.matcher(codigo).matches();
    }

    @Schema(description = "Valida código y lanza excepción si es inválido")
    public static void validateCodigo(String codigo) {
        if (!isValidCodigo(codigo)) {
            throw new IllegalArgumentException(
                "El código debe tener " + CODIGO_LENGTH + " dígitos numéricos"
            );
        }
    }

   

    @Schema(description = "Valida que un campo no esté vacío")
    public static void validateNotBlank(String value, String campo) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio");
        }
    }

    

    @Schema(description = "Valida todos los campos de un nuevo usuario")
    public static void validateNewUser(String nombre, String apellidoPaterno, 
                                       String email, String password, String telefono) {
        validateName(nombre, "nombre");
        validateName(apellidoPaterno, "apellidoPaterno");
        validateEmail(email);
        validatePassword(password);
        validatePhone(telefono);
    }
}