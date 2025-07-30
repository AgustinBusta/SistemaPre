package es.proyecto.sistema.SistemaPresupuesto.Servicios;

import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.stereotype.Service;

import es.proyecto.sistema.SistemaPresupuesto.model.Articulo;
import es.proyecto.sistema.SistemaPresupuesto.model.Cliente;
import es.proyecto.sistema.SistemaPresupuesto.model.MedioPago;
import es.proyecto.sistema.SistemaPresupuesto.model.Presupuesto;
import es.proyecto.sistema.SistemaPresupuesto.model.Vendedor;
import es.proyecto.sistema.SistemaPresupuesto.repository.ArticuloDAO;
import es.proyecto.sistema.SistemaPresupuesto.repository.ClienteDAO;
import es.proyecto.sistema.SistemaPresupuesto.repository.ConfiguracionSistemaDAO;
import es.proyecto.sistema.SistemaPresupuesto.repository.MedioPagoDAO;
import es.proyecto.sistema.SistemaPresupuesto.repository.PresupuestoDAO;
import es.proyecto.sistema.SistemaPresupuesto.util.GeneradorInformes;

@Service
public class PresupuestoService {
    private final ArticuloDAO articuloDAO;
    private final ClienteDAO clienteDAO;
    private final PresupuestoDAO presupuestoDAO;
    private final MedioPagoDAO medioPagoDAO;
    private final ConfiguracionSistemaDAO configuracionDAO;

    public PresupuestoService(ArticuloDAO articuloDAO, ClienteDAO clienteDAO, 
                            PresupuestoDAO presupuestoDAO, MedioPagoDAO medioPagoDAO,
                            ConfiguracionSistemaDAO configuracionDAO) {
        this.articuloDAO = articuloDAO;
        this.clienteDAO = clienteDAO;
        this.presupuestoDAO = presupuestoDAO;
        this.medioPagoDAO = medioPagoDAO;
        this.configuracionDAO = configuracionDAO;
    }

    public Presupuesto crearNuevoPresupuesto(Vendedor vendedorConectado) {
        int nuevoId = presupuestoDAO.obtenerUltimoId() + 1;
        return new Presupuesto(nuevoId, vendedorConectado);
    }

    public List<Articulo> buscarArticulo(String busqueda, TipoBusqueda tipo) {
    return switch (tipo) {
        case CODIGO -> articuloDAO.findByCodigo(busqueda).map(List::of).orElse(List.of());
        case NOMBRE -> articuloDAO.findByNombre(busqueda).map(List::of).orElse(List.of());
    };
}

    public void agregarItemAPresupuesto(Presupuesto presupuesto, Articulo articulo, int cantidad) {
        if (articulo.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente");
        }
        presupuesto.agregarItem(articulo, cantidad);
    }

    public void eliminarItemDePresupuesto(Presupuesto presupuesto, Articulo articulo) {
        presupuesto.eliminarItem(articulo);
    }

    public List<TotalMedioPago> calcularTotalesMediosPago(Presupuesto presupuesto) {
    return medioPagoDAO.obtenerTodos().stream()
        .map(mp -> new TotalMedioPago(
            mp, 
            mp.aplicarAjuste(presupuesto.calcularTotalBruto())
        ))
        .collect(Collectors.toList()); // ← Cambia .toList() por esto
}

    public void confirmarYGuardarPresupuesto(Presupuesto presupuesto, Cliente cliente, MedioPago medioPago) {
        // Si el cliente es nuevo, guardarlo primero
        if (cliente.getId() == 0) {
            clienteDAO.save(cliente);
        }
        
        // Persistir
        if (presupuesto.getId() == 0) {
            presupuestoDAO.save(presupuesto);
        } else {
            presupuestoDAO.actualizar(presupuesto);
        }
    }

    public String imprimirPresupuesto(Presupuesto presupuesto) {
        return new GeneradorInformes().generarReportePresupuesto(presupuesto);
    }

    public List<MedioPago> obtenerMediosDePago() {
        return medioPagoDAO.obtenerTodos();
    }

    public int obtenerConfiguracionValidez() {
        return configuracionDAO.obtenerConfiguracion().getDiasValidezPresupuesto();
    }

    public enum TipoBusqueda {
        CODIGO, NOMBRE
    }

    public record TotalMedioPago(MedioPago medioPago, double total) {}
}