package es.proyecto.sistema.SistemaPresupuesto.repository.impl;
import java.io.FileInputStream;
import java.io.FileOutputStream; // Import correcto
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import es.proyecto.sistema.SistemaPresupuesto.model.ConfiguracionSistema;

public class ConfiguracionSistemaDAOProperties implements ConfiguracionSistemaDAOManual {
    private final String configFilePath;

    public ConfiguracionSistemaDAOProperties(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    @Override
    public ConfiguracionSistema obtenerConfiguracion() {
        Properties props = new Properties();
        ConfiguracionSistema config = new ConfiguracionSistema();
        
        try (InputStream input = new FileInputStream(configFilePath)) {
            props.load(input);
            
            config.setDiasValidezPresupuesto(
                Integer.parseInt(props.getProperty("diasValidezPresupuesto", "15")));
            config.setEmpresaNombre(
                props.getProperty("empresaNombre", "Mi Empresa"));
            // Más propiedades...
            
        } catch (IOException e) {
            // Usar valores por defecto si hay error
        }
        
        return config;
    }

    @Override
    public boolean guardarConfiguracion(ConfiguracionSistema config) {
        Properties props = new Properties();
        props.setProperty("diasValidezPresupuesto", 
            String.valueOf(config.getDiasValidezPresupuesto()));
        props.setProperty("empresaNombre", config.getEmpresaNombre());
        // Más propiedades...
        
        try (OutputStream output = new FileOutputStream(configFilePath)) {
            props.store(output, "Configuración del Sistema");
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar configuración", e);
        }
    }
}
