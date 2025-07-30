import java.util.Arrays;
import java. util.regex.Pattern;
import java.util.stream.Collectors;

public class Cliente {
    private int id;
    private String nombre;
    private String dni;
    private String telefono;
    private String email;


    public Cliente(String nombre, String dni, String telefono, String email) {
        if (id <= 0) throw new IllegalArgumentException("ID debe ser positivo");
        
        this.id = id;
        setNombre(nombre);
        setDni(dni);
        setTelefono(telefono);
        setEmail(email);
    }

    //Getters inmutables

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }

    //Setters con validaciones

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public void setDni(String dni) {
        // Validación básica de DNI (adaptar según país)
        if (dni == null || !Pattern.matches("^\\d{8}[A-Za-z]?$", dni)) {
            throw new IllegalArgumentException("Formato de DNI inválido");
        }
        this.dni = dni;
    }

    public void setTelefono(String telefono) {
        // Validación internacional simplificada
        if (telefono == null || !Pattern.matches("^[+]?\\d{9,15}$", telefono)) {
            throw new IllegalArgumentException("Teléfono debe tener 9-15 dígitos");
        }
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        if (email == null || !Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = email.toLowerCase(); // Normalización
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID no puede ser negativo");
        }
        this.id = id;
    }
    
    public String getIniciales() {
        return Arrays.stream(nombre.split(" "))
            .filter(palabra -> !palabra.isEmpty())
            .map(palabra -> palabra.substring(0, 1).toUpperCase())
            .collect(Collectors.joining());
    }
    
      public String toString() {
        return String.format(
            "Cliente #%d: %s | DNI: %s | Contacto: %s / %s",
            id, nombre, dni, telefono, email
        );
    }

     public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cliente)) return false;
        return this.dni.equals(((Cliente) obj).dni); // DNIs únicos
    }


}
