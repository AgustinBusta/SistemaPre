package es.proyecto.sistema.SistemaPresupuesto.model;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MedioPago {
     public enum TipoPago {
        EFECTIVO(0),
        TARJETA_CREDITO(5),
        TARJETA_DEBITO(1),
        TRANSFERENCIA(2);

         private final double porcentajeAjusteDefault;
        
        TipoPago(double porcentaje) {
            this.porcentajeAjusteDefault = porcentaje;
        }
        
        public double getPorcentajeDefault() {
            return porcentajeAjusteDefault;
        }
    }
    @Id
    private int id;
    private TipoPago tipoPago;  // Usamos el enum
    private double porcentajeAjuste;
    private boolean activo;

    public MedioPago(int id, TipoPago tipoPago, double porcentajeAjuste) {
        if (id <= 0) throw new IllegalArgumentException("ID debe ser positivo");
        
        this.id = id;
        setTipoPago(tipoPago);
        setPorcentajeAjuste(porcentajeAjuste);
        this.activo = true;
    }

    // Constructor con porcentaje por defecto
    public MedioPago(int id, TipoPago tipoPago) {
        this(id, tipoPago, tipoPago.getPorcentajeDefault());
    }

    //Getters
    public int getId() { return id; }
    public TipoPago getTipoPago() { return tipoPago; }
    public double getPorcentajeAjuste() { return porcentajeAjuste; }
    public boolean isActivo() { return activo; }

    //Setters con validaciones
    public void setTipoPago(TipoPago tipoPago) {
        Objects.requireNonNull(tipoPago, "El tipo de pago no puede ser nulo");
        this.tipoPago = tipoPago;
    }

    public void setPorcentajeAjuste(double porcentaje) {
        if (porcentaje < -100 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre -100 y 100");
        }
        this.porcentajeAjuste = porcentaje;
    }

     public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID no puede ser negativo");
        }
        this.id = id;
    }

    //Métodos de negocio
    public double aplicarAjuste(double monto) {
        return monto * (1 + (porcentajeAjuste / 100));
    }

    public void desactivar() {
        this.activo = false;
    }

    //Sobrescrituras
    @Override
    public String toString() {
        return String.format(
            "MedioPago #%d: %s (%.2f%%) - %s",
            id, tipoPago, porcentajeAjuste, activo ? "Activo" : "Inactivo"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedioPago)) return false;
        return this.id == ((MedioPago) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
