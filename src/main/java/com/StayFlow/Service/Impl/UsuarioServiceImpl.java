package com.StayFlow.service.impl;

import com.StayFlow.dto.request.LoginRequestDTO;
import com.StayFlow.dto.request.RegistroRequestDTO;
import com.StayFlow.dto.response.LoginResponseDTO;
import com.StayFlow.dto.response.UsuarioResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.UsuarioMapper;
import com.StayFlow.model.RefreshToken;
import com.StayFlow.model.Rol;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.RefreshTokenRepository;
import com.StayFlow.repository.RolRepository;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.security.JwtUtil;
import com.StayFlow.service.EmailService;
import com.StayFlow.service.interfaces.IUsuarioService;
import com.StayFlow.util.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Schema(description = "Implementación del servicio de usuarios con toda la lógica de negocio")
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UsuarioMapper usuarioMapper;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              RefreshTokenRepository refreshTokenRepository,
                              PasswordEncoder passwordEncoder,
                              JwtUtil jwtUtil,
                              EmailService emailService,
                              UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.usuarioMapper = usuarioMapper;
    }

   
    @Override
    @Transactional
    @Operation(summary = "Registra un nuevo usuario en el sistema")
    public UsuarioResponseDTO registrar(RegistroRequestDTO request) {
        ValidationUtils.validateNewUser(
            request.getNombre(),
            request.getApellidoPaterno(),
            request.getEmail(),
            request.getPassword(),
            request.getTelefono()
        );

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está registrado", "USER_001");
        }

        Rol rolArrendatario = rolRepository.findById(2)
            .orElseThrow(() -> new BusinessException("Rol no encontrado", "ROL_001"));

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRoles(List.of(rolArrendatario));
        
        String codigo = String.format("%06d", (int)(Math.random() * 1000000));
        usuario.setCodigoConfirmacion(codigo);
        usuario.setCodigoExpiracion(LocalDateTime.now().plusHours(24));
        usuario.setEmailConfirmado(false);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        emailService.enviarCodigoConfirmacion(usuarioGuardado.getEmail(), codigo);
        
       return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    
    @Override
    @Transactional
    @Operation(summary = "Autentica a un usuario y genera tokens JWT")
    public LoginResponseDTO login(LoginRequestDTO request) {
        
    	ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validateNotBlank(request.getPassword(), "password");

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException("Credenciales inválidas", "AUTH_001"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("Credenciales inválidas", "AUTH_001");
        }

        if (!usuario.isEmailConfirmado()) {
            throw new BusinessException("Por favor confirme su email antes de iniciar sesión", "USER_002");
        }

        String token = jwtUtil.generateToken(usuario.getEmail());
        String refreshTokenStr = jwtUtil.generateRefreshToken(usuario.getEmail());

        RefreshToken refreshToken = new RefreshToken(
            refreshTokenStr,
            LocalDateTime.now().plusDays(7),
            usuario
        );
        refreshTokenRepository.save(refreshToken);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setRefreshToken(refreshTokenStr);
        response.setEmail(usuario.getEmail());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setRol(usuario.getRoles().get(0).getNombreRol());

        return response;
    }

    
    @Override
    @Transactional
    @Operation(summary = "Renueva el token de acceso usando refresh token")
    public LoginResponseDTO refreshToken(String refreshTokenStr) {
        ValidationUtils.validateNotBlank(refreshTokenStr, "refreshToken");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
            .orElseThrow(() -> new BusinessException("Refresh token inválido", "AUTH_002"));

        if (!refreshToken.isActivo()) {
            throw new BusinessException("Refresh token desactivado", "AUTH_003");
        }

        if (refreshToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            refreshToken.setActivo(false);
            refreshTokenRepository.save(refreshToken);
            throw new BusinessException("Refresh token expirado", "AUTH_004");
        }

        Usuario usuario = refreshToken.getUsuario();
        
        String nuevoToken = jwtUtil.generateToken(usuario.getEmail());
        
        String nuevoRefreshToken = jwtUtil.generateRefreshToken(usuario.getEmail());
        
        refreshToken.setActivo(false);
        refreshTokenRepository.save(refreshToken);

        RefreshToken nuevoRefreshTokenEntity = new RefreshToken(
            nuevoRefreshToken,
            LocalDateTime.now().plusDays(7),
            usuario
        );
        refreshTokenRepository.save(nuevoRefreshTokenEntity);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(nuevoToken);
        response.setRefreshToken(nuevoRefreshToken);
        response.setEmail(usuario.getEmail());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setRol(usuario.getRoles().get(0).getNombreRol());

        return response;
    }

   
    @Override
    @Transactional
    @Operation(summary = "Cierra la sesión del usuario invalidando el refresh token")
    public void logout(String refreshTokenStr) {
        ValidationUtils.validateNotBlank(refreshTokenStr, "refreshToken");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
            .orElseThrow(() -> new BusinessException("Refresh token inválido", "AUTH_002"));

        refreshToken.setActivo(false);
        refreshTokenRepository.save(refreshToken);
    }

   
    @Override
    @Operation(summary = "Obtiene el perfil de un usuario por su ID")
    public UsuarioResponseDTO getPerfil(Integer idUsuario) {
        if (idUsuario == null) {
            throw new BusinessException("ID de usuario no puede ser nulo", "USER_003");
        }
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
        
        return usuarioMapper.toResponseDTO(usuario);
    }

    
    @Override
    @Transactional
    @Operation(summary = "Actualiza los datos del perfil de un usuario")
    public UsuarioResponseDTO actualizarPerfil(Integer idUsuario, Usuario usuarioActualizado) {
        
    	if (usuarioActualizado.getNombre() != null) {
            ValidationUtils.validateName(usuarioActualizado.getNombre(), "nombre");
        }
        if (usuarioActualizado.getApellidoPaterno() != null) {
            ValidationUtils.validateName(usuarioActualizado.getApellidoPaterno(), "apellidoPaterno");
        }
        if (usuarioActualizado.getTelefono() != null) {
            ValidationUtils.validatePhone(usuarioActualizado.getTelefono());
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));
        
        if (usuarioActualizado.getNombre() != null) {
            usuario.setNombre(usuarioActualizado.getNombre());
        }
        if (usuarioActualizado.getApellidoPaterno() != null) {
            usuario.setApellidoPaterno(usuarioActualizado.getApellidoPaterno());
        }
        if (usuarioActualizado.getApellidoMaterno() != null) {
            usuario.setApellidoMaterno(usuarioActualizado.getApellidoMaterno());
        }
        if (usuarioActualizado.getTelefono() != null) {
            usuario.setTelefono(usuarioActualizado.getTelefono());
        }
        if (usuarioActualizado.getAvatarUrl() != null) {
            usuario.setAvatarUrl(usuarioActualizado.getAvatarUrl());
        }
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

   
    @Override
    @Transactional
    @Operation(summary = "Confirma el email de un usuario mediante código de verificación")
    public void confirmarEmail(String codigo) {
        
    	ValidationUtils.validateCodigo(codigo);
        
        Usuario usuario = usuarioRepository.findByCodigoConfirmacion(codigo)
            .orElseThrow(() -> new BusinessException("Código inválido", "USER_004"));

        if (usuario.getCodigoExpiracion().isBefore(LocalDateTime.now())) {
            throw new BusinessException("El código ha expirado", "USER_005");
        }

        usuario.setEmailConfirmado(true);
        usuario.setCodigoConfirmacion(null);
        usuario.setCodigoExpiracion(null);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    @Operation(summary = "Recupera la contraseña enviando un código al email del usuario")
    public void recuperarPassword(String email) {
        
    	ValidationUtils.validateEmail(email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
        
        String codigo = String.format("%06d", (int)(Math.random() * 1000000));
        usuario.setCodigoConfirmacion(codigo);
        usuario.setCodigoExpiracion(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);
        
        emailService.enviarCodigoRecuperacion(usuario.getEmail(), codigo);
    }

    @Override
    @Transactional
    @Operation(summary = "Restablece la contraseña usando el código de recuperación")
    public void resetPassword(String codigo, String nuevaPassword) {
        
    	ValidationUtils.validateCodigo(codigo);
        ValidationUtils.validatePassword(nuevaPassword);
        
        Usuario usuario = usuarioRepository.findByCodigoConfirmacion(codigo)
            .orElseThrow(() -> new BusinessException("Código inválido", "USER_004"));
        
        if (usuario.getCodigoExpiracion().isBefore(LocalDateTime.now())) {
            throw new BusinessException("El código ha expirado", "USER_005");
        }
        
        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuario.setCodigoConfirmacion(null);
        usuario.setCodigoExpiracion(null);
        usuarioRepository.save(usuario);
    }
}