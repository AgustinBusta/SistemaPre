package es.proyecto.sistema.SistemaPresupuesto.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Presupuesto {
    @Id
    private int id;
    private LocalDate fechaCreacion;
    private LocalDate fechaValidez;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "presupuesto_id") // Relación unidireccional
    private List<ItemPresupuesto> items;

    @Enumerated(EnumType.STRING)
    private EstadoPresupuesto estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // Nombre de la columna en la tabla
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "medio_pago_id")
    private MedioPago medioPagoSeleccionado;
    
    //Enum para los estados del presupuesto
    public enum EstadoPresupuesto {
        PENDIENTE,
        CONFIRMADO,
        CANCELADO
    }

    public Presupuesto() {
        // Constructor requerido por JPA
    }

    //Constructor
    public Presupuesto(int id, Vendedor vendedor) {
        if (id <= 0) throw new IllegalArgumentException("ID debe ser positivo");
        Objects.requireNonNull(vendedor, "El vendedor no puede ser nulo");

        this.id = id;
        this.fechaCreacion = LocalDate.now();
        this.vendedor = vendedor;
        this.items = new ArrayList<>();
        this.estado = EstadoPresupuesto.PENDIENTE;
    }


    //Métodos principales
    public void agregarItem(Articulo articulo, int cantidad) {
        Objects.requireNonNull(articulo, "El artículo no puede ser nulo");

        //Buscar si el artículo ya existe en los ítems
        for (ItemPresupuesto item : items) {
            if (item.getArticulo().equals(articulo)) {
                // Si existe, actualizar la cantidad
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }

        //Si no existe, agregar nuevo ítem
        items.add(new ItemPresupuesto(articulo, cantidad));
    }

    public void eliminarItem(Articulo articulo) {
        Objects.requireNonNull(articulo, "El artículo no puede ser nulo");
        items.removeIf(item -> item.getArticulo().equals(articulo));
    }

    public double calcularTotalBruto() {
        return items.stream()
                .mapToDouble(ItemPresupuesto::getSubtotal)
                .sum();
    }

    public double calcularTotalConAjuste() {
        if (medioPagoSeleccionado == null) {
            return calcularTotalBruto();
        }
        return medioPagoSeleccionado.aplicarAjuste(calcularTotalBruto());
    }

    public void confirmarPresupuesto(Cliente cliente, MedioPago medioPago) {
        Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        Objects.requireNonNull(medioPago, "El medio de pago no puede ser nulo");

        if (items.isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un presupuesto sin ítems");
        }

        this.cliente = cliente;
        this.medioPagoSeleccionado = medioPago;
        this.estado = EstadoPresupuesto.CONFIRMADO;
        this.fechaValidez = generarFechaValidez(15); // 15 días por defecto
    }

    public LocalDate generarFechaValidez(int diasValidez) {
        if (diasValidez <= 0) {
            throw new IllegalArgumentException("Los días de validez deben ser positivos");
        }
        return fechaCreacion.plusDays(diasValidez);
    }

    //Getters
    public int getId() {
        return id;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaValidez() {
        return fechaValidez;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPresupuesto> getItems() {
        return new ArrayList<>(items); // Devuelve copia para evitar modificaciones externas
    }

    public EstadoPresupuesto getEstado() {
        return estado;
    }

    public MedioPago getMedioPagoSeleccionado() {
        return medioPagoSeleccionado;
    }

    //Setters
    public void setFechaValidez(LocalDate fechaValidez) {
        if (fechaValidez.isBefore(fechaCreacion)) {
            throw new IllegalArgumentException("La fecha de validez no puede ser anterior a la creación");
        }
        this.fechaValidez = fechaValidez;
    }

    public void cancelarPresupuesto() {
        this.estado = EstadoPresupuesto.CANCELADO;
    }

    //Sobrescrituras
    @Override
    public String toString() {
        return String.format(
            "Presupuesto #%d - Estado: %s\n" +
            "Fecha: %s | Válido hasta: %s\n" +
            "Vendedor: %s\n" +
            "Cliente: %s\n" +
            "Medio de Pago: %s\n" +
            "Total Bruto: $%.2f | Total con ajuste: $%.2f\n" +
            "Ítems (%d):\n%s",
            id, estado,
            fechaCreacion, fechaValidez,
            vendedor.getNombreCompleto(),
            cliente != null ? cliente.getNombre() : "No asignado",
            medioPagoSeleccionado != null ? medioPagoSeleccionado.getTipoPago() : "No seleccionado",
            calcularTotalBruto(), calcularTotalConAjuste(),
            items.size(), itemsToString()
        );
    }

    private String itemsToString() {
        StringBuilder sb = new StringBuilder();
        items.forEach(item -> sb.append("  - ").append(item.toString()).append("\n"));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Presupuesto)) return false;
        return this.id == ((Presupuesto) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}