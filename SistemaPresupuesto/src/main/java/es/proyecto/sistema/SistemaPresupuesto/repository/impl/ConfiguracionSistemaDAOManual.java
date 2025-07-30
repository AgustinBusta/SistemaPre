package es.proyecto.sistema.SistemaPresupuesto.repository.impl;

import es.proyecto.sistema.SistemaPresupuesto.model.ConfiguracionSistema;

public interface ConfiguracionSistemaDAOManual {
    ConfiguracionSistema obtenerConfiguracion();
    boolean guardarConfiguracion(ConfiguracionSistema config);
}
