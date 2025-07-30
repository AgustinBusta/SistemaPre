package es.proyecto.sistema.SistemaPresupuesto.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // ← Import necesario

import es.proyecto.sistema.SistemaPresupuesto.model.ConfiguracionSistema;

@Repository
public interface ConfiguracionSistemaDAO extends JpaRepository<ConfiguracionSistema, Integer> {
    ConfiguracionSistema obtenerConfiguracion();
    boolean guardarConfiguracion(ConfiguracionSistema config);
}
