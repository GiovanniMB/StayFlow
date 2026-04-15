package com.StayFlow.mapper;

import com.StayFlow.dto.request.RegistroRequestDTO;
import com.StayFlow.dto.response.UsuarioResponseDTO;
import com.StayFlow.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Schema(description = "Mapper para convertir entre entidad Usuario y DTOs")
public class UsuarioMapper {

    @Schema(description = "Convierte de RegistroRequestDTO a entidad Usuario")
    public Usuario toEntity(RegistroRequestDTO request) {
        if (request == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        
        return usuario;
    }

    @Schema(description = "Convierte de entidad Usuario a UsuarioResponseDTO (sin datos sensibles)")
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setNombre(usuario.getNombre());
        response.setApellidoPaterno(usuario.getApellidoPaterno());
        response.setApellidoMaterno(usuario.getApellidoMaterno());
        response.setEmail(usuario.getEmail());
        response.setAvatarUrl(usuario.getAvatarUrl());
        response.setTelefono(usuario.getTelefono());
        response.setEmailConfirmado(usuario.isEmailConfirmado());
        response.setFechaRegistro(usuario.getFechaRegistro());
        response.setFechaActualizacion(usuario.getFechaActualizacion());
        
        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombreRol())
                .collect(Collectors.toList());
            response.setRoles(roles);
        }
        
        return response;
    }

    @Schema(description = "Convierte una lista de entidades Usuario a lista de UsuarioResponseDTO")
    public List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        
        return usuarios.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Schema(description = "Actualiza una entidad Usuario existente con datos de un DTO de perfil")
    public void updateEntity(Usuario existing, Usuario source) {
        if (source == null) {
            return;
        }
        
        if (source.getNombre() != null) {
            existing.setNombre(source.getNombre());
        }
        if (source.getApellidoPaterno() != null) {
            existing.setApellidoPaterno(source.getApellidoPaterno());
        }
        if (source.getApellidoMaterno() != null) {
            existing.setApellidoMaterno(source.getApellidoMaterno());
        }
        if (source.getTelefono() != null) {
            existing.setTelefono(source.getTelefono());
        }
        if (source.getAvatarUrl() != null) {
            existing.setAvatarUrl(source.getAvatarUrl());
        }
    }
}