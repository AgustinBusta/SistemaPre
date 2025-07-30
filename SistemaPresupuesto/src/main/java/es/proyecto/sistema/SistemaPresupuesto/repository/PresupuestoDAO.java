package es.proyecto.sistema.SistemaPresupuesto.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.proyecto.sistema.SistemaPresupuesto.model.Presupuesto;

@Repository
public interface PresupuestoDAO extends JpaRepository<Presupuesto, Integer> {
    boolean actualizar(Presupuesto presupuesto);
    Optional<Presupuesto> obtenerPorId(int id);
    List<Presupuesto> buscarPorCliente(int clienteId);
    List<Presupuesto> buscarPorVendedor(int vendedorId);
    List<Presupuesto> buscarPorEstado(Presupuesto.EstadoPresupuesto estado);
    int obtenerUltimoId();

}