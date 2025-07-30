import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public class Vendedor {
    private int id;
    private String nombre;
    private String apellido;
    private String usuario;
    private String passwordHash;
    private byte[] salt;
    private boolean activo;

    public Vendedor(int id, String nombre, String apellido, String usuario, String password) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido");
        
        this.id = id;
        setNombre(nombre);
        setApellido(apellido);
        setUsuario(usuario);
        setPassword(password);  // Se hashea automáticamente
        this.activo = true;       
    }

    //Getters

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getUsuario() { return usuario; }
    public boolean isActivo() { return activo; }

    //Setters con validaciones

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido no puede estar vacío");
        }
        this.apellido = apellido.trim();
    }

    public void setUsuario(String usuario) {
        if (usuario == null || !Pattern.matches("^[a-zA-Z0-9_]{5,20}$", usuario)) {
            throw new IllegalArgumentException("Usuario debe tener 5-20 caracteres alfanuméricos");
        }
        this.usuario = usuario;
    }

        public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID no puede ser negativo");
        }
        this.id = id;
    }

    //Manejo seguro de contraseñas
    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        this.salt = generateSalt();
        this.passwordHash = hashPassword(password, this.salt);
    }

     private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
     }

     private String hashPassword(String password, byte[] salt) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

    public boolean verificarPassword(String password) {
        return this.passwordHash.equals(hashPassword(password, this.salt));
    }

    //Metodos de negocio
    public void desactivar() {
        this.activo = false;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    //Sobrescrituras
    @Override
    public String toString() {
        return String.format(
            "Vendedor #%d: %s %s (%s) - %s",
            id, nombre, apellido, usuario, activo ? "Activo" : "Inactivo"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendedor)) return false;
        return this.id == ((Vendedor) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
