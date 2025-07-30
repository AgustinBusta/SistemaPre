package es.proyecto.sistema.SistemaPresupuesto.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Articulo {
    @Id
    private int id;
    private String codigo; 
    private String nombre;
    private double PrecioUnitario;
    private int stock;

 public Articulo() {
        // Constructor requerido por JPA
    }

 public Articulo(int id, String codigo, String nombre, double precioUnitario, int stock) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.PrecioUnitario = PrecioUnitario;
        this.stock = stock;
    }

   

    // Getters y Setters

     public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(double PrecioUnitario) {
        if (PrecioUnitario >= 0) { // Validación
            this.PrecioUnitario = PrecioUnitario;
        } else {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock >= 0) {
            this.stock = stock;
        } else {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

     public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID debe ser positivo");
        }
        this.id = id;
    }

// Metodo para calcular total

    public double calcularTotal(int cantidad) {
        if (cantidad <= 0 || cantidad > stock) {
            throw new IllegalArgumentException("Cantidad inválida o sin stock suficiente");
        }
        return cantidad * PrecioUnitario;
    }
}
