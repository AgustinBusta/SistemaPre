package es.proyecto.sistema.SistemaPresupuesto.model;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemPresupuesto {
    @Id
    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    //Constructor principal
    public ItemPresupuesto(Articulo articulo, int cantidad) {
        Objects.requireNonNull(articulo, "El artículo no puede ser nulo");
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (cantidad > articulo.getStock()) {
            throw new IllegalStateException("No hay suficiente stock disponible");
        }

        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioUnitario = articulo.getPrecioUnitario();
        this.subtotal = calcularSubtotal();
    }

    //Getters
    public Articulo getArticulo() {
        return articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    //Setters con validaciones
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (cantidad > articulo.getStock()) {
            throw new IllegalStateException("No hay suficiente stock disponible");
        }
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    //Métodos de negocio
    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }

    public void actualizarPrecioUnitario(double nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        this.precioUnitario = nuevoPrecio;
        this.subtotal = calcularSubtotal();
    }

    //Sobrescrituras
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPresupuesto)) return false;
        ItemPresupuesto that = (ItemPresupuesto) o;
        return Objects.equals(articulo.getId(), that.articulo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(articulo.getId());
    }

    @Override
    public String toString() {
        return String.format(
            "%d x %s (P.U.: $%.2f) = $%.2f",
            cantidad, articulo.getNombre(), precioUnitario, subtotal
        );
    }
}
