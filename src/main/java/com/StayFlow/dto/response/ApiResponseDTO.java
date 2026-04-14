package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta estándar de la API para todas las operaciones")
public class ApiResponseDTO<T> {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo de la operación", example = "Usuario registrado exitosamente")
    private String message;

    @Schema(description = "Datos de la respuesta")
    private T data;

    @Schema(description = "Código de estado HTTP", example = "200")
    private int statusCode;

    public ApiResponseDTO() {}

    public ApiResponseDTO(boolean success, String message, T data, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data, 200);
    }

    public static <T> ApiResponseDTO<T> success(String message) {
        return new ApiResponseDTO<>(true, message, null, 200);
    }

    public static <T> ApiResponseDTO<T> created(String message, T data) {
        return new ApiResponseDTO<>(true, message, data, 201);
    }

    public static <T> ApiResponseDTO<T> error(String message, int statusCode) {
        return new ApiResponseDTO<>(false, message, null, statusCode);
    }


    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
}