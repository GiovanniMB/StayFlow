package com.StayFlow.Repository;

import com.StayFlow.model.Favorito;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    
    // Obtiene toda la lista de favoritos de un usuario específico
    List<Favorito> findByUsuario(Usuario usuario);
    
    // Busca un registro exacto (para poder eliminarlo si el usuario le vuelve a dar click al corazón)
    Optional<Favorito> findByUsuarioAndPropiedad(Usuario usuario, Propiedad propiedad);
}