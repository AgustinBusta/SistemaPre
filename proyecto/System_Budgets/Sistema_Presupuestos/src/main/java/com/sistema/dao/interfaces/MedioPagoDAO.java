package main.java.com.sistema.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface MedioPagoDAO {
    List<MedioPago> obtenerTodos();
    Optional<MedioPago> obtenerPorId(int id);
    boolean guardar(MedioPago medioPago);
    boolean desactivar(int id);
}
