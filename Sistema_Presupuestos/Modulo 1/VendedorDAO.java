import java.util.List;
import java.util.Optional;

public interface VendedorDAO {
    Optional<Vendedor> obtenerPorUsuarioYPassword(String usuario, String password);
    Optional<Vendedor> obtenerPorId(int id);
    boolean guardar(Vendedor vendedor);
    boolean actualizar(Vendedor vendedor);
    List<Vendedor> obtenerTodos();
}