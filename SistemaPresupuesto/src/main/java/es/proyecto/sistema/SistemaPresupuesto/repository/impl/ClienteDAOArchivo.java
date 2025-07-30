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

import es.proyecto.sistema.SistemaPresupuesto.model.Cliente;

public class ClienteDAOArchivo implements ClienteDAOArchivoManual {
    private final String archivoPath;

    public ClienteDAOArchivo(String archivoPath) {
        this.archivoPath = archivoPath;
    }

    @Override
    public boolean guardar(Cliente cliente) {
        List<Cliente> clientes = obtenerTodosClientes();
        
        // Si es nuevo, asignar ID
        if (cliente.getId() == 0) {
            int maxId = clientes.stream().mapToInt(Cliente::getId).max().orElse(0);
            cliente.setId(maxId + 1);
        } else {
            // Si existe, eliminarlo primero para actualizar
            clientes.removeIf(c -> c.getId() == cliente.getId());
        }
        
        clientes.add(cliente);
        return guardarTodos(clientes);
    }

        @Override
    public boolean actualizar(Cliente cliente) {
        // En esta implementación, actualizar es igual a guardar
        return guardar(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        return obtenerTodosClientes().stream()
            .filter(c -> c.getDni().equalsIgnoreCase(dni))
            .findFirst();
    }

    @Override
    public Optional<Cliente> obtenerPorId(int id) {
        return obtenerTodosClientes().stream()
            .filter(c -> c.getId() == id)
            .findFirst();
    }

    @Override
    public List<Cliente> buscarTodos() {
        return new ArrayList<>(obtenerTodosClientes());
    }

    private List<Cliente> obtenerTodosClientes() {
        File archivo = new File(archivoPath);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al leer clientes del archivo", e);
        }
    }

    private boolean guardarTodos(List<Cliente> clientes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoPath))) {
            oos.writeObject(clientes);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar clientes en archivo", e);
        }
    }
}