import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedioPagoDAOJDBC implements MedioPagoDAO {
    private final Connection conexion;

    public MedioPagoDAOJDBC(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<MedioPago> obtenerTodos() {
        List<MedioPago> mediosPago = new ArrayList<>();
        String sql = "SELECT * FROM medios_pago WHERE activo = true";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                mediosPago.add(mapToMedioPago(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener medios de pago", e);
        }
        return mediosPago;
    }

    @Override
    public Optional<MedioPago> obtenerPorId(int id) {
        String sql = "SELECT * FROM medios_pago WHERE id = ? AND activo = true";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapToMedioPago(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener medio de pago por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean guardar(MedioPago medioPago) {
        if (medioPago.getId() == 0) {
            return insertar(medioPago);
        } else {
            return actualizar(medioPago);
        }
    }

    private boolean insertar(MedioPago medioPago) {
        String sql = "INSERT INTO medios_pago (tipo_pago, porcentaje_ajuste, activo) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medioPago.getTipoPago().name());
            stmt.setDouble(2, medioPago.getPorcentajeAjuste());
            stmt.setBoolean(3, true);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        medioPago.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar medio de pago", e);
        }
    }

    private boolean actualizar(MedioPago medioPago) {
        String sql = "UPDATE medios_pago SET tipo_pago = ?, porcentaje_ajuste = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, medioPago.getTipoPago().name());
            stmt.setDouble(2, medioPago.getPorcentajeAjuste());
            stmt.setInt(3, medioPago.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar medio de pago", e);
        }
    }

    @Override
    public boolean desactivar(int id) {
        String sql = "UPDATE medios_pago SET activo = false WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar medio de pago", e);
        }
    }

    private MedioPago mapToMedioPago(ResultSet rs) throws SQLException {
        return new MedioPago(
            rs.getInt("id"),
            MedioPago.TipoPago.valueOf(rs.getString("tipo_pago")),
            rs.getDouble("porcentaje_ajuste")
        );
    }
}