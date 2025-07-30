package es.proyecto.sistema.SistemaPresupuesto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
    public class ConfiguracionSistema {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private int diasValidezPresupuesto;
        private String empresaNombre;
        private String empresaDireccion;
        private String empresaTelefono;
        private String empresaEmail;
        private String empresaLogo; // Ruta al logo de la empresa
        private String moneda; // Moneda utilizada en el sistema
        private String idioma; // Idioma del sistema
        private boolean permitirEdicionPresupuesto; // Permitir edición de presupuestos una vez creados
        private boolean permitirEliminacionPresupuesto; // Permitir eliminación de presupuestos
        private boolean permitirEdicionArticulo; // Permitir edición de artículos
        private boolean permitirEliminacionArticulo; // Permitir eliminación de artículos
        private boolean permitirEdicionCliente; // Permitir edición de clientes
        private boolean permitirEliminacionCliente; // Permitir eliminación de clientes
        private boolean permitirEdicionMedioPago; // Permitir edición de medios de pago
        private boolean permitirEliminacionMedioPago; // Permitir eliminación de medios de pago
        private boolean permitirEdicionConfiguracion; // Permitir edición de configuración del sistema
        private boolean permitirEliminacionConfiguracion; // Permitir eliminación de configuración del sistema
        private boolean permitirEdicionUsuario; // Permitir edición de usuarios
        private boolean permitirEliminacionUsuario; // Permitir eliminación de usuarios
        private boolean permitirRegistroUsuario; // Permitir registro de nuevos usuarios
        private boolean permitirCambioContrasena; // Permitir cambio de contraseña por parte del usuario
        private boolean permitirRecuperacionContrasena; // Permitir recuperación de contraseña
        private boolean permitirExportacionPDF; // Permitir exportación de presupuestos a PDF
        private boolean permitirExportacionExcel; // Permitir exportación de presupuestos a Excel
        private boolean permitirImpresionPresupuesto; // Permitir impresión de presupuestos
        private boolean permitirEnvioEmailPresupuesto; // Permitir envío de presupuestos por email
        
        
        // Getters y setters
         public void setDiasValidezPresupuesto(int dias) {
        this.diasValidezPresupuesto = dias;
    }

    public int getDiasValidezPresupuesto() {
        return diasValidezPresupuesto;
    }

    public void setEmpresaNombre(String nombre) {
        this.empresaNombre = nombre;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }
    }
