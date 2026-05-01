package com.StayFlow.Service.Impl;

import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.PropiedadMapper;
import com.StayFlow.model.Favorito;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Usuario;
import com.StayFlow.Repository.FavoritoRepository;
import com.StayFlow.Repository.PropiedadRepository;
import com.StayFlow.Repository.UsuarioRepository;
import com.StayFlow.Service.Interfaces.IFavoritoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoritoServiceImpl implements IFavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PropiedadRepository propiedadRepository;
    private final PropiedadMapper propiedadMapper;

    public FavoritoServiceImpl(FavoritoRepository favoritoRepository, 
                               UsuarioRepository usuarioRepository, 
                               PropiedadRepository propiedadRepository,
                               PropiedadMapper propiedadMapper) {
        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.propiedadRepository = propiedadRepository;
        this.propiedadMapper = propiedadMapper;
    }

    // Metodo auxiliar para obtener el usuario autenticado
    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
    }

    @Override
    @Transactional
    public boolean toggleFavorito(Integer idPropiedad) {
        Usuario usuario = getUsuarioAutenticado();
        Propiedad propiedad = propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));

        Optional<Favorito> favoritoExistente = favoritoRepository.findByUsuarioAndPropiedad(usuario, propiedad);

        if (favoritoExistente.isPresent()) {
            // Si ya existe, significa que le dio click para quitarlo de favoritos
            favoritoRepository.delete(favoritoExistente.get());
            return false; // Retornamos false indicando que se quitó
        } else {
            // Si no existe, lo agrega a favoritos
            Favorito nuevoFavorito = new Favorito(usuario, propiedad);
            favoritoRepository.save(nuevoFavorito);
            return true; // Retornamos true indicando que se agregó
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropiedadResponseDTO> obtenerMisFavoritos() {
        Usuario usuario = getUsuarioAutenticado();
        
        // Busca los favoritos y extrae la propiedad
        List<Propiedad> propiedadesFavoritas = favoritoRepository.findByUsuario(usuario).stream()
                .map(Favorito::getPropiedad)
                .filter(prop -> !prop.isEstaEliminado()) // Evitar mostrar casas eliminadas
                .collect(Collectors.toList());

        // Reutiliza PropiedadMapper
        return propiedadMapper.toResponseDTOList(propiedadesFavoritas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> obtenerIdsMisFavoritos() {
        Usuario usuario = getUsuarioAutenticado();
        
        return favoritoRepository.findByUsuario(usuario).stream()
                .map(fav -> fav.getPropiedad().getIdPropiedad())
                .collect(Collectors.toList());
    }
}