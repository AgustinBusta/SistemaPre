package es.proyecto.sistema.SistemaPresupuesto.repository.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import es.proyecto.sistema.SistemaPresupuesto.model.MedioPago;


public class MedioPagoDAOMemoria implements MedioPagoDAOManual {
    private final Map<Integer, MedioPago> mediosPago = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public List<MedioPago> obtenerTodos() {
        return new ArrayList<>(mediosPago.values());
    }

    @Override
    public Optional<MedioPago> obtenerPorId(int id) {
        return Optional.ofNullable(mediosPago.get(id));
    }

    @Override
    public boolean guardar(MedioPago medioPago) {
        if (medioPago.getId() == 0) {
            medioPago.setId(idGenerator.getAndIncrement());
        }
        mediosPago.put(medioPago.getId(), medioPago);
        return true;
    }

    @Override
    public boolean desactivar(int id) {
        if (mediosPago.containsKey(id)) {
            mediosPago.remove(id);
            return true;
        }
        return false;
    }
}