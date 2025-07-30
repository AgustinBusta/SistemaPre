package es.proyecto.sistema.SistemaPresupuesto.repository.impl;

import java.util.List;
import java.util.Optional;

import es.proyecto.sistema.SistemaPresupuesto.model.MedioPago;

public interface MedioPagoDAOManual {
    List<MedioPago> obtenerTodos();
    Optional<MedioPago> obtenerPorId(int id);
    boolean guardar(MedioPago medioPago);
    boolean desactivar(int id);
}