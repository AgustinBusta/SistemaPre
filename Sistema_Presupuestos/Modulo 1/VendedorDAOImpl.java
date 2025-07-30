import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VendedorDAOImpl implements VendedorDAO {
    private final Map<Integer, Vendedor> vendedores = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Optional<Vendedor> obtenerPorUsuarioYPassword(String usuario, String password) {
        return vendedores.values().stream()
            .filter(v -> v.getUsuario().equals(usuario) && v.verificarPassword(password))
            .findFirst();
    }

    @Override
    public Optional<Vendedor> obtenerPorId(int id) {
        return Optional.ofNullable(vendedores.get(id));
    }

    @Override
    public boolean guardar(Vendedor vendedor) {
        if (vendedor.getId() == 0) {
            vendedor.setId(idGenerator.getAndIncrement());
        }
        vendedores.put(vendedor.getId(), vendedor);
        return true;
    }

     @Override
    public boolean actualizar(Vendedor vendedor) {
        if (!vendedores.containsKey(vendedor.getId())) {
            return false; // No existe el vendedor a actualizar
        }
        vendedores.put(vendedor.getId(), vendedor);
        return true;
    }

    @Override
    public List<Vendedor> obtenerTodos() {
        return new ArrayList<>(vendedores.values());
    }
}