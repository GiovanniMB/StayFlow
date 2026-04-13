package com.StayFlow.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.model.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Repository
@Schema(description = "Repositorio para operaciones CRUD de la entidad Usuario")
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Schema(description = "Busca un usuario por su email")
    Optional<Usuario> findByEmail(String email);

    @Schema(description = "Verifica si ya existe un usuario con el email proporcionado")
    boolean existsByEmail(String email);

    @Schema(description = "Busca un usuario por su código de confirmación")
    Optional<Usuario> findByCodigoConfirmacion(String codigo);
    
    @Schema(description = "Busca usuarios que no han confirmado su email y cuya expiración es anterior a la fecha actual")
    List<Usuario> findByEmailConfirmadoFalseAndCodigoExpiracionBefore(LocalDateTime fecha);
    
    @Schema(description = "Busca usuario por email ignorando mayúsculas/minúsculas")
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> buscarPorEmailIgnoreCase(@Param("email") String email);

    @Schema(description = "Busca usuarios por nombre completo (contiene)")
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(CONCAT(u.nombre, ' ', u.apellidoPaterno, ' ', COALESCE(u.apellidoMaterno, ''))) " +
           "LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))")
    List<Usuario> buscarPorNombreCompleto(@Param("nombreCompleto") String nombreCompleto);

    @Schema(description = "Obtiene usuarios que tienen un rol específico")
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.idRol = :idRol")
    List<Usuario> findByRolId(@Param("idRol") Integer idRol);
    
    @Schema(description = "Obtiene usuarios activos (no eliminados lógicamente)")
    @Query(value = "SELECT * FROM usuario WHERE esta_eliminado = 0", nativeQuery = true)
    List<Usuario> findUsuariosActivos();
    
    @Schema(description = "Actualiza el código de confirmación y su expiración")
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.codigoConfirmacion = :codigo, " +
           "u.codigoExpiracion = :expiracion WHERE u.idUsuario = :idUsuario")
    int actualizarCodigoConfirmacion(@Param("idUsuario") Integer idUsuario,
                                      @Param("codigo") String codigo,
                                      @Param("expiracion") LocalDateTime expiracion);

    @Schema(description = "Marca un email como confirmado y limpia el código de confirmación")
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.emailConfirmado = true, " +
           "u.codigoConfirmacion = null, " +
           "u.codigoExpiracion = null " +
           "WHERE u.idUsuario = :idUsuario")
    int confirmarEmail(@Param("idUsuario") Integer idUsuario);

    @Schema(description = "Actualiza la URL del avatar del usuario")
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.avatarUrl = :avatarUrl WHERE u.idUsuario = :idUsuario")
    int actualizarAvatar(@Param("idUsuario") Integer idUsuario,
                          @Param("avatarUrl") String avatarUrl);

    @Schema(description = "Eliminación lógica de un usuario")
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.estaEliminado = true WHERE u.idUsuario = :idUsuario")
    int eliminarLogico(@Param("idUsuario") Integer idUsuario);

    @Schema(description = "Actualiza el teléfono de un usuario")
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.telefono = :telefono WHERE u.idUsuario = :idUsuario")
    int actualizarTelefono(@Param("idUsuario") Integer idUsuario,
                            @Param("telefono") String telefono);

    @Schema(description = "Verifica si un email ya está confirmado")
    @Query("SELECT u.emailConfirmado FROM Usuario u WHERE u.email = :email")
    Boolean isEmailConfirmado(@Param("email") String email);

    @Schema(description = "Verifica si un código de confirmación es válido y no ha expirado")
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM Usuario u WHERE u.codigoConfirmacion = :codigo " +
           "AND u.codigoExpiracion > :ahora")
    boolean isCodigoValido(@Param("codigo") String codigo, 
                            @Param("ahora") LocalDateTime ahora);
}