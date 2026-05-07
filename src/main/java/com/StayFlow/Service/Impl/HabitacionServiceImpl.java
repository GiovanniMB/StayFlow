package com.StayFlow.Service.Impl;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.StayFlow.Service.Interfaces.IHabitacionService;
import com.StayFlow.Service.Interfaces.ILogSistemaService;
import com.StayFlow.dto.request.CamaRequestDTO;
import com.StayFlow.dto.request.HabitacionRequestDTO;
import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.HabitacionMapper;
import com.StayFlow.model.CategoriaFoto;
import com.StayFlow.model.FotoHabitacion;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.LogSistema.Accion;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Servicio;
import com.StayFlow.model.TipoCama;
import com.StayFlow.model.TipoHabitacion;
import com.StayFlow.Repository.CategoriaFotoRepository;
import com.StayFlow.Repository.FotoHabitacionRepository;
import com.StayFlow.Repository.HabitacionRepository;
import com.StayFlow.Repository.PropiedadRepository;
import com.StayFlow.Repository.ServicioRepository;
import com.StayFlow.Repository.TipoCamaRepository;
import com.StayFlow.Repository.TipoHabitacionRepository;

@Service
public class HabitacionServiceImpl implements IHabitacionService {

    @Value("${stayflow.upload.dir}")
    private String uploadDir;

    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HabitacionRepository habitacionRepository;
    private final PropiedadRepository propiedadRepository;
    private final TipoCamaRepository tipoCamaRepository;
    private final ServicioRepository servicioRepository;
    private final HabitacionMapper habitacionMapper;
    private final ILogSistemaService logSistemaService;
    private final FotoHabitacionRepository fotoHabitacionRepository;
    private final CategoriaFotoRepository categoriaFotoRepository;

    public HabitacionServiceImpl(TipoHabitacionRepository tipoHabitacionRepository,
                                 HabitacionRepository habitacionRepository,
                                 PropiedadRepository propiedadRepository,
                                 TipoCamaRepository tipoCamaRepository,
                                 ServicioRepository servicioRepository,
                                 ILogSistemaService logSistemaService,
                                 HabitacionMapper habitacionMapper,
                                 FotoHabitacionRepository fotoHabitacionRepository,
                                 CategoriaFotoRepository categoriaFotoRepository) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.habitacionRepository = habitacionRepository;
        this.propiedadRepository = propiedadRepository;
        this.tipoCamaRepository = tipoCamaRepository;
        this.servicioRepository = servicioRepository;
        this.habitacionMapper = habitacionMapper;
        this.logSistemaService = logSistemaService;
        this.fotoHabitacionRepository = fotoHabitacionRepository;
        this.categoriaFotoRepository = categoriaFotoRepository;
    }

    @Override
    @Transactional
    public TipoHabitacionResponseDTO crearTipoHabitacion(Integer idPropiedad, TipoHabitacionRequestDTO request) {
        Propiedad propiedad = buscarPropiedadYValidarDueno(idPropiedad);
        TipoHabitacion nuevoTipo = habitacionMapper.toTipoEntity(request);
        
        if (propiedad.isSeRentaPorHabitaciones() && request.getPrecioBaseNoche().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("En la modalidad de Hotel/Hostal, el precio de la categoría debe ser mayor a $0.00");
        }
        if (!propiedad.isSeRentaPorHabitaciones()) {
            nuevoTipo.setPrecioBaseNoche(BigDecimal.ZERO);
        }
        
        nuevoTipo.setPropiedad(propiedad);
        nuevoTipo.setServicios(procesarServicios(request.getIdServicios(), request.getNuevosServicios()));
        
        procesarCamas(nuevoTipo, request.getCamas());

        TipoHabitacion tipoGuardado = tipoHabitacionRepository.save(nuevoTipo);
        logSistemaService.registrarLog("tipohabitacion", tipoGuardado.getIdTipoHabitacion(), Accion.INSERT);

        return habitacionMapper.toTipoResponseDTO(tipoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacionResponseDTO> obtenerTiposPorPropiedad(Integer idPropiedad) {
        propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));
        return habitacionMapper.toTipoResponseDTOList(tipoHabitacionRepository.findByPropiedadIdPropiedadAndEstaEliminadoFalse(idPropiedad));
    }

    @Override
    @Transactional
    public TipoHabitacionResponseDTO actualizarTipoHabitacion(Integer idTipoHabitacion, TipoHabitacionRequestDTO request) {
        TipoHabitacion tipoExistente = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoExistente.getPropiedad());

        tipoExistente.setNombreTipo(request.getNombreTipo());
        tipoExistente.setCapacidad(request.getCapacidad());
        Propiedad propiedad = tipoExistente.getPropiedad();
        
        if (propiedad.isSeRentaPorHabitaciones() && request.getPrecioBaseNoche().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("En la modalidad de Hotel/Hostal, el precio de la categoría debe ser mayor a $0.00");
        }
        
        tipoExistente.setPrecioBaseNoche(propiedad.isSeRentaPorHabitaciones() ? request.getPrecioBaseNoche() : BigDecimal.ZERO);
        tipoExistente.setTieneBanoPrivado(request.getTieneBanoPrivado());
        tipoExistente.setServicios(procesarServicios(request.getIdServicios(), request.getNuevosServicios()));

        tipoExistente.getCamas().clear();
        procesarCamas(tipoExistente, request.getCamas());

        TipoHabitacion tipoActualizado = tipoHabitacionRepository.save(tipoExistente);
        logSistemaService.registrarLog("tipohabitacion", tipoActualizado.getIdTipoHabitacion(), Accion.UPDATE);
        
        return habitacionMapper.toTipoResponseDTO(tipoActualizado);
    }

    @Override
    @Transactional
    public void eliminarTipoHabitacion(Integer idTipoHabitacion) {
        TipoHabitacion tipoExistente = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoExistente.getPropiedad());
        tipoExistente.setEstaEliminado(true);
        tipoHabitacionRepository.save(tipoExistente);
        logSistemaService.registrarLog("tipohabitacion", tipoExistente.getIdTipoHabitacion(), Accion.DELETE_LOGICO);
    }

    @Override
    @Transactional
    public HabitacionResponseDTO crearHabitacion(Integer idTipoHabitacion, HabitacionRequestDTO request) {
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoHabitacion.getPropiedad());

        Habitacion nuevaHabitacion = new Habitacion();
        nuevaHabitacion.setPropiedad(tipoHabitacion.getPropiedad());
        nuevaHabitacion.setTipoHabitacion(tipoHabitacion);
        nuevaHabitacion.setNumeroHabitacion(request.getNumeroHabitacion());

        Habitacion habitacionGuardada = habitacionRepository.save(nuevaHabitacion);
        logSistemaService.registrarLog("habitacion", habitacionGuardada.getIdHabitacion(), Accion.INSERT);

        return habitacionMapper.toHabitacionResponseDTO(habitacionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> obtenerHabitacionesPorTipo(Integer idTipoHabitacion) {
        tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        return habitacionMapper.toHabitacionResponseDTOList(habitacionRepository.findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion));
    }

    @Override
    @Transactional
    public HabitacionResponseDTO actualizarHabitacion(Integer idHabitacion, HabitacionRequestDTO request) {
        Habitacion habitacionExistente = habitacionRepository.findByIdHabitacionAndEstaEliminadoFalse(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion", "id", idHabitacion));
        validarDueno(habitacionExistente.getPropiedad());

        habitacionExistente.setNumeroHabitacion(request.getNumeroHabitacion());

        Habitacion habitacionActualizada = habitacionRepository.save(habitacionExistente);
        logSistemaService.registrarLog("habitacion", habitacionActualizada.getIdHabitacion(), Accion.UPDATE);

        return habitacionMapper.toHabitacionResponseDTO(habitacionActualizada);
    }

    @Override
    @Transactional
    public void eliminarHabitacion(Integer idHabitacion) {
        Habitacion habitacionExistente = habitacionRepository.findByIdHabitacionAndEstaEliminadoFalse(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion", "id", idHabitacion));
        validarDueno(habitacionExistente.getPropiedad());
        habitacionExistente.setEstaEliminado(true);
        habitacionRepository.save(habitacionExistente);
        logSistemaService.registrarLog("habitacion", idHabitacion, Accion.DELETE_LOGICO);
    }

    private Propiedad buscarPropiedadYValidarDueno(Integer idPropiedad) {
        Propiedad propiedad = propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));
        validarDueno(propiedad);
        return propiedad;
    }

    private void validarDueno(Propiedad propiedad) {
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (propiedad.getDueno() == null || !propiedad.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("Acceso denegado: No tienes permiso para modificar una propiedad que no te pertenece.");
        }
    }

    private void procesarCamas(TipoHabitacion tipoHabitacion, List<CamaRequestDTO> camasRequest) {
        if (camasRequest != null && !camasRequest.isEmpty()) {
            for (CamaRequestDTO camaDTO : camasRequest) {
                TipoCama tipoCama = tipoCamaRepository.findById(camaDTO.getIdTipoCama())
                        .orElseThrow(() -> new ResourceNotFoundException("TipoCama", "id", camaDTO.getIdTipoCama()));
                tipoHabitacion.addCama(tipoCama, camaDTO.getCantidad());
            }
        }
    }

    @Override
    @Transactional
    public void subirFotoHabitacion(Integer idTipoHabitacion, Integer idCategoriaFoto, MultipartFile archivo, boolean esPrincipal) {
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoHabitacion.getPropiedad());

        CategoriaFoto categoriaFoto = categoriaFotoRepository.findById(idCategoriaFoto)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaFoto", "id", idCategoriaFoto));

        try {
            BufferedImage imagenReal = ImageIO.read(archivo.getInputStream());
            if (imagenReal == null) {
                throw new BusinessException("El archivo subido está corrupto o no es un formato de imagen válido.");
            }
            int anchoMinimo = 1200; 
            if (imagenReal.getWidth() < anchoMinimo) {
                throw new BusinessException("La imagen es muy pequeña (" + imagenReal.getWidth() + "px de ancho). Para que tu recámara luzca increíble, sube fotos de al menos " + anchoMinimo + "px de ancho.");
            }

            String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path rutaFisica = Paths.get(uploadDir + nombreUnico);
            Files.copy(archivo.getInputStream(), rutaFisica, StandardCopyOption.REPLACE_EXISTING);

            FotoHabitacion nuevaFoto = new FotoHabitacion(
                    tipoHabitacion.getPropiedad(), 
                    tipoHabitacion, 
                    categoriaFoto, 
                    "/uploads/" + nombreUnico, 
                    esPrincipal
            );
            fotoHabitacionRepository.save(nuevaFoto);

        } catch (Exception e) {
            if(e instanceof BusinessException) throw (BusinessException) e;
            throw new BusinessException("Error al guardar la imagen físicamente: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void subirFotoPropiedad(Integer idPropiedad, Integer idCategoriaFoto, MultipartFile archivo, boolean esPrincipal) {
        Propiedad propiedad = buscarPropiedadYValidarDueno(idPropiedad); 

        CategoriaFoto categoriaFoto = categoriaFotoRepository.findById(idCategoriaFoto)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriaFoto", "id", idCategoriaFoto));

        try {
            BufferedImage imagenReal = ImageIO.read(archivo.getInputStream());
            if (imagenReal == null) {
                throw new BusinessException("El archivo subido está corrupto o no es un formato de imagen válido.");
            }
            int anchoMinimo = 1200; 
            if (imagenReal.getWidth() < anchoMinimo) {
                throw new BusinessException("La imagen es muy pequeña (" + imagenReal.getWidth() + "px de ancho). Para que tu fachada luzca profesional, sube fotos de al menos " + anchoMinimo + "px de ancho.");
            }

            String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path rutaFisica = Paths.get(uploadDir + nombreUnico);
            Files.copy(archivo.getInputStream(), rutaFisica, StandardCopyOption.REPLACE_EXISTING);

            FotoHabitacion nuevaFoto = new FotoHabitacion(
                    propiedad, 
                    null, 
                    categoriaFoto, 
                    "/uploads/" + nombreUnico, 
                    esPrincipal
            );
            fotoHabitacionRepository.save(nuevaFoto);

        } catch (Exception e) {
            if(e instanceof BusinessException) throw (BusinessException) e;
            throw new BusinessException("Error al guardar la imagen físicamente: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void eliminarFoto(Integer idFoto) {
        FotoHabitacion foto = fotoHabitacionRepository.findById(idFoto)
                .orElseThrow(() -> new ResourceNotFoundException("FotoHabitacion", "id", idFoto));
        
        foto.setEstaEliminado(true);
        fotoHabitacionRepository.save(foto);

        try {
            String nombreArchivo = foto.getUrlFoto().substring(foto.getUrlFoto().lastIndexOf("/") + 1);
            Path rutaFisica = Paths.get(uploadDir + nombreArchivo);
            Files.deleteIfExists(rutaFisica);
        } catch (Exception e) {
            System.err.println("No se pudo borrar el archivo físico: " + e.getMessage());
        }
    }

    private List<Servicio> procesarServicios(List<Integer> idServicios, List<String> nuevosServicios) {
        List<Servicio> serviciosFinales = new ArrayList<>();

        if (idServicios != null && !idServicios.isEmpty()) {
            List<Servicio> serviciosEncontrados = servicioRepository.findAllById(idServicios);
            if (serviciosEncontrados.size() != idServicios.size()) {
                throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
            }
            serviciosFinales.addAll(serviciosEncontrados);
        }

        if (nuevosServicios != null && !nuevosServicios.isEmpty()) {
            for (String nombreNuevo : nuevosServicios) {
                String nombreLimpio = nombreNuevo.trim();
                if (!nombreLimpio.isEmpty()) {
                    Servicio servicio = servicioRepository.findByNombreServicioIgnoreCase(nombreLimpio)
                            .orElseGet(() -> {
                                Servicio nuevo = new Servicio();
                                String nombreCapitalizado = nombreLimpio.substring(0, 1).toUpperCase() + nombreLimpio.substring(1).toLowerCase();
                                nuevo.setNombreServicio(nombreCapitalizado);
                                return servicioRepository.save(nuevo);
                            });
                    
                    if (!serviciosFinales.contains(servicio)) {
                        serviciosFinales.add(servicio);
                    }
                }
            }
        }
        return serviciosFinales;
    }
}