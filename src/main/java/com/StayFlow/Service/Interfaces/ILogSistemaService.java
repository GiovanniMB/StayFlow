package com.StayFlow.Service.Interfaces;

import com.StayFlow.model.LogSistema.Accion;

public interface ILogSistemaService {
    
    /**
     * Registra una acción en la bitácora del sistema.
     * El usuario que realiza la acción se extrae automáticamente del token JWT actual.
     * * @param tablaAfectada El nombre de la tabla (ej. "propiedad", "habitacion")
     * @param idRegistroAfectado El ID del registro que fue insertado, actualizado o eliminado
     * @param accion El tipo de acción (INSERT, UPDATE, DELETE_LOGICO)
     */
    void registrarLog(String tablaAfectada, Integer idRegistroAfectado, Accion accion);
}