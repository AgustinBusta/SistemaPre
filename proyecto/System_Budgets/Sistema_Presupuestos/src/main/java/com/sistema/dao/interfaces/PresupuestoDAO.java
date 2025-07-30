package main.java.com.sistema.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface PresupuestoDAO {
    boolean guardar(Presupuesto presupuesto);
    boolean actualizar(Presupuesto presupuesto);
    Optional<Presupuesto> obtenerPorId(int id);
    List<Presupuesto> buscarPorCliente(int clienteId);
    List<Presupuesto> buscarPorVendedor(int vendedorId);
    List<Presupuesto> buscarPorEstado(Presupuesto.EstadoPresupuesto estado);
}
