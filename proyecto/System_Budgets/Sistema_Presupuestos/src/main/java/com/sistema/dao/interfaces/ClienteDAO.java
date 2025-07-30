package main.java.com.sistema.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface ClienteDAO {
    boolean guardar(Cliente cliente);
    boolean actualizar(Cliente cliente);
    Optional<Cliente> buscarPorDni(String dni);
    Optional<Cliente> obtenerPorId(int id);
    List<Cliente> buscarTodos();
}
