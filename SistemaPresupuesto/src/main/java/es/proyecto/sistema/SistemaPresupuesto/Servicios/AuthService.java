package es.proyecto.sistema.SistemaPresupuesto.Servicios;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.proyecto.sistema.SistemaPresupuesto.model.Vendedor;
import es.proyecto.sistema.SistemaPresupuesto.repository.VendedorDAO;

@Service
public class AuthService {
    private final VendedorDAO vendedorDAO;

    public AuthService(VendedorDAO vendedorDAO) {
        this.vendedorDAO = vendedorDAO;
    }

    public Optional<Vendedor> autenticarVendedor(String usuario, String password) {
    Optional<Vendedor> vendedorOpt = vendedorDAO.findByUsuario(usuario);
    return vendedorOpt.filter(v -> {
        String passwordHash = v.hashPassword(password, v.getSalt());
        return v.getPasswordHash().equals(passwordHash) && v.verificarPassword(password);
    });
}
}