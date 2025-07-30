import java.util.List;
import java.util.Optional;

public interface ArticuloDAO {
    Optional<Articulo> obtenerPorId(int id);
    Optional<Articulo> buscarPorCodigo(String codigo);
    List<Articulo> buscarPorNombre(String nombre);
    boolean guardar(Articulo articulo);
    boolean actualizar(Articulo articulo);
}