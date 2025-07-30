package es.proyecto.sistema.SistemaPresupuesto.repository.impl;

import java.util.List;
import java.util.Optional;

import es.proyecto.sistema.SistemaPresupuesto.model.Cliente;

public interface ClienteDAOArchivoManual {
    boolean guardar(Cliente cliente);
    boolean actualizar(Cliente cliente);
    Optional<Cliente> buscarPorDni(String dni);
    Optional<Cliente> obtenerPorId(int id);
    List<Cliente> buscarTodos();
}
