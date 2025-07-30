package es.proyecto.sistema.SistemaPresupuesto.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.proyecto.sistema.SistemaPresupuesto.model.Articulo;

@Repository
public interface ArticuloDAO extends JpaRepository<Articulo, Integer> {
    Optional<Articulo> findById(Integer id);
    Optional<Articulo> findByCodigo(String codigo);
    Optional<Articulo> findByNombre(String nombre);
}