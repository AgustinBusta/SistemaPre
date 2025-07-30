package es.proyecto.sistema.SistemaPresupuesto.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.proyecto.sistema.SistemaPresupuesto.model.MedioPago;

@Repository
public interface MedioPagoDAO extends JpaRepository<MedioPago, Integer> {
    List<MedioPago> obtenerTodos();
    Optional<MedioPago> obtenerPorId(int id);
    boolean guardar(MedioPago medioPago);
    boolean desactivar(int id);
}