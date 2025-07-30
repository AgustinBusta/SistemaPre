package es.proyecto.sistema.SistemaPresupuesto.repository.impl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.proyecto.sistema.SistemaPresupuesto.model.MedioPago;


public class MedioPagoDAOArchivo implements MedioPagoDAOManual{
    private final String archivoPath;

    public MedioPagoDAOArchivo(String archivoPath) {
        this.archivoPath = archivoPath;
    }

    @Override
    public List<MedioPago> obtenerTodos() {
        return cargarMediosPago();
    }

    @Override
    public Optional<MedioPago> obtenerPorId(int id) {
        return cargarMediosPago().stream()
            .filter(mp -> mp.getId() == id)
            .findFirst();
    }

    @Override
    public boolean guardar(MedioPago medioPago) {
        List<MedioPago> medios = cargarMediosPago();
        
        if (medioPago.getId() == 0) {
            int nuevoId = medios.stream()
                .mapToInt(MedioPago::getId)
                .max()
                .orElse(0) + 1;
            medioPago.setId(nuevoId);
        } else {
            medios.removeIf(mp -> mp.getId() == medioPago.getId());
        }
        
        medios.add(medioPago);
        return guardarMediosPago(medios);
    }

    @Override
    public boolean desactivar(int id) {
        List<MedioPago> medios = cargarMediosPago();
        boolean removed = medios.removeIf(mp -> mp.getId() == id);
        if (removed) {
            return guardarMediosPago(medios);
        }
        return false;
    }

    private List<MedioPago> cargarMediosPago() {
        File archivo = new File(archivoPath);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<MedioPago>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar medios de pago", e);
        }
    }

    private boolean guardarMediosPago(List<MedioPago> medios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoPath))) {
            oos.writeObject(medios);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar medios de pago", e);
        }
    }
}
