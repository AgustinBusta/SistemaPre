package es.proyecto.sistema.SistemaPresupuesto.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.proyecto.sistema.SistemaPresupuesto.model.Vendedor;

@Repository
public interface VendedorDAO extends JpaRepository<Vendedor, Integer> {
    Optional<Vendedor> findByUsuario(String usuario);
    Optional<Vendedor> findByUsuarioAndPasswordHash(String usuario, String passwordHash);
    Optional<Vendedor> findById(Integer id); // Usa el método estándar de JpaRepository
}