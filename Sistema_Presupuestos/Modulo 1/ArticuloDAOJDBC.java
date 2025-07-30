// Implementación con base de datos
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticuloDAOJDBC implements ArticuloDAO {
    private final Connection conexion;

    public ArticuloDAOJDBC(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Optional<Articulo> obtenerPorId(int id) {
        String sql = "SELECT * FROM articulos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapToArticulo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener artículo por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Articulo> buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM articulos WHERE codigo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapToArticulo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar artículo por código", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Articulo> buscarPorNombre(String nombre) {
        List<Articulo> articulos = new ArrayList<>();
        String sql = "SELECT * FROM articulos WHERE nombre LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                articulos.add(mapToArticulo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar artículos por nombre", e);
        }
        return articulos;
    }

    @Override
    public boolean guardar(Articulo articulo) {
        String sql = "INSERT INTO articulos (codigo, nombre, precio_unitario, stock) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, articulo.getCodigo());
            stmt.setString(2, articulo.getNombre());
            stmt.setDouble(3, articulo.getPrecioUnitario());
            stmt.setInt(4, articulo.getStock());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    articulo.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar artículo", e);
        }
    }

    @Override
    public boolean actualizar(Articulo articulo) {
        String sql = "UPDATE articulos SET codigo = ?, nombre = ?, precio_unitario = ?, stock = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, articulo.getCodigo());
            stmt.setString(2, articulo.getNombre());
            stmt.setDouble(3, articulo.getPrecioUnitario());
            stmt.setInt(4, articulo.getStock());
            stmt.setInt(5, articulo.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar artículo", e);
        }
    }

    private Articulo mapToArticulo(ResultSet rs) throws SQLException {
        return new Articulo(
            rs.getInt("id"),
            rs.getString("codigo"),
            rs.getString("nombre"),
            rs.getDouble("precio_unitario"),
            rs.getInt("stock")
        );
    }
}
