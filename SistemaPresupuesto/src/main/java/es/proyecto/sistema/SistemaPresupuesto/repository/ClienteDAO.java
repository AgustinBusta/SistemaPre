package es.proyecto.sistema.SistemaPresupuesto.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; // ← Import necesario
import org.springframework.stereotype.Repository;

import es.proyecto.sistema.SistemaPresupuesto.model.Cliente;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDni(String dni);
    Optional<Cliente> findById(Integer id);
    List<Cliente> findAll();
}
