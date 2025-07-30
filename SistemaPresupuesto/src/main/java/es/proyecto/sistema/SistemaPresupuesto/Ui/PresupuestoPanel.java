package es.proyecto.sistema.SistemaPresupuesto.Ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.proyecto.sistema.SistemaPresupuesto.Servicios.PresupuestoService;
import es.proyecto.sistema.SistemaPresupuesto.model.Articulo;
import es.proyecto.sistema.SistemaPresupuesto.model.Cliente;
import es.proyecto.sistema.SistemaPresupuesto.model.ItemPresupuesto;
import es.proyecto.sistema.SistemaPresupuesto.model.MedioPago;
import es.proyecto.sistema.SistemaPresupuesto.model.Presupuesto;
import es.proyecto.sistema.SistemaPresupuesto.model.Vendedor;
import es.proyecto.sistema.SistemaPresupuesto.util.ImpresionUtil;

@Component
public class PresupuestoPanel extends JFrame {

    private final PresupuestoService presupuestoService; // Inyectado por Spring
    private final ImpresionUtil impresionUtil; // Inyectado por Spring

    private Vendedor vendedorConectado; // Para asociar el presupuesto al vendedor
    private Presupuesto presupuestoActual; // El presupuesto en edición

    // Componentes de UI
    private JTextField txtArticuloBusqueda, txtCantidad, txtClienteNombre, txtClienteDni;
    private JButton btnBuscarArticulo, btnAgregarItem, btnEliminarItem, btnConfirmar, btnImprimir;
    private JTable tablaItems;
    private DefaultTableModel tablaModelo;
    private JLabel lblTotalEfectivo, lblTotalDebito, lblTotalCredito;
    private JComboBox<MedioPago> cmbMedioPago;

    @Autowired
    public PresupuestoPanel(PresupuestoService presupuestoService, ImpresionUtil impresionUtil) {
        this.presupuestoService = presupuestoService;
        this.impresionUtil = impresionUtil;

        setTitle("Sistema de Presupuestos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        initComponents();
        addListeners();
        inicializarNuevoPresupuesto();
    }

    // Método para setear el vendedor una vez logueado
    public void setVendedorConectado(Vendedor vendedor) {
        this.vendedorConectado = vendedor;
        inicializarNuevoPresupuesto(); // Reinicia el presupuesto al tener el vendedor
    }

    private void inicializarNuevoPresupuesto() {
        if (vendedorConectado != null) {
            presupuestoActual = presupuestoService.crearNuevoPresupuesto(vendedorConectado);
            limpiarFormulario();
            actualizarTotales();
            cargarMediosDePago();
        } else {
            // Esto no debería pasar si el flujo es correcto (login -> panel)
            // Pero es bueno manejarlo
            JOptionPane.showMessageDialog(this, "Vendedor no autenticado. Por favor, inicie sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            // Podrías redirigir al login o cerrar la aplicación
            this.dispose();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Layout principal

        // --- Panel Superior: Búsqueda y Añadir Artículo ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Agregar Artículo"));

        panelSuperior.add(new JLabel("Artículo (Nombre/Código):"));
        txtArticuloBusqueda = new JTextField(20);
        panelSuperior.add(txtArticuloBusqueda);

        btnBuscarArticulo = new JButton("Buscar");
        panelSuperior.add(btnBuscarArticulo);

        panelSuperior.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField("1", 5); // Default 1
        panelSuperior.add(txtCantidad);

        btnAgregarItem = new JButton("Agregar al Presupuesto");
        panelSuperior.add(btnAgregarItem);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central: Grilla de Ítems ---
        String[] columnas = {"Código", "Nombre", "Cantidad", "P. Unitario", "Subtotal"};
        tablaModelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        tablaItems = new JTable(tablaModelo);
        JScrollPane scrollPane = new JScrollPane(tablaItems);
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Inferior: Totales, Cliente, Acciones ---
        JPanel panelInferior = new JPanel(new GridLayout(3, 1, 10, 10)); // Grid para organizar subpaneles
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Subpanel 1: Datos del Cliente
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelCliente.add(new JLabel("Nombre:"));
        txtClienteNombre = new JTextField(20);
        panelCliente.add(txtClienteNombre);
        panelCliente.add(new JLabel("DNI:"));
        txtClienteDni = new JTextField(10);
        panelCliente.add(txtClienteDni);
        panelInferior.add(panelCliente);


        // Subpanel 2: Totales por Medio de Pago
        JPanel panelTotales = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Totales por Medio de Pago"));
        lblTotalEfectivo = new JLabel("Efectivo: $0.00");
        lblTotalDebito = new JLabel("Débito: $0.00");
        lblTotalCredito = new JLabel("Crédito: $0.00");
        panelTotales.add(lblTotalEfectivo);
        panelTotales.add(lblTotalDebito);
        panelTotales.add(lblTotalCredito);
        panelInferior.add(panelTotales);

        // Subpanel 3: Acciones (Medio de Pago, Botones)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelAcciones.add(new JLabel("Medio de Pago:"));
        cmbMedioPago = new JComboBox<>();
        panelAcciones.add(cmbMedioPago);

        btnEliminarItem = new JButton("Eliminar Ítem Seleccionado");
        panelAcciones.add(btnEliminarItem);
        btnConfirmar = new JButton("Confirmar Presupuesto");
        panelAcciones.add(btnConfirmar);
        btnImprimir = new JButton("Imprimir Presupuesto");
        panelAcciones.add(btnImprimir);
        panelInferior.add(panelAcciones);


        add(panelInferior, BorderLayout.SOUTH);
    }

    private void addListeners() {
        btnBuscarArticulo.addActionListener(e -> buscarArticulo());
        btnAgregarItem.addActionListener(e -> agregarItem());
        btnEliminarItem.addActionListener(e -> eliminarItem());
        btnConfirmar.addActionListener(e -> confirmarPresupuesto());
        btnImprimir.addActionListener(e -> imprimirPresupuesto());

        // Listener para actualizar totales cuando cambia la selección del medio de pago
        cmbMedioPago.addActionListener(e -> actualizarTotales());
    }

    private void buscarArticulo() {
        String busqueda = txtArticuloBusqueda.getText().trim();
        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre o código de artículo para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Asumiendo que buscarArticulo() en el service puede buscar por nombre o código
        List<Articulo> articulos = presupuestoService.buscarArticulo(busqueda, null); // Devuelve una lista
        Optional<Articulo> articuloOpt = articulos.stream().findFirst();
        articuloOpt.ifPresentOrElse(
            articulo -> {
                JOptionPane.showMessageDialog(this, "Artículo encontrado: " + articulo.getNombre() + " ($" + articulo.getPrecioUnitario() + ")", "Artículo Encontrado", JOptionPane.INFORMATION_MESSAGE);
            },
            () -> JOptionPane.showMessageDialog(this, "Artículo no encontrado.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE)
        );
    }

    private void agregarItem() {
        String busqueda = txtArticuloBusqueda.getText().trim();
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida. Ingrese un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Articulo> articulos = presupuestoService.buscarArticulo(busqueda, null); // ← Corrección aquí
        Optional<Articulo> articuloOpt = articulos.stream().findFirst(); // ← Corrección aquí

        articuloOpt.ifPresentOrElse(
            articulo -> {
                // Aquí usamos el servicio para añadir el ítem al presupuesto actual
                presupuestoService.agregarItemAPresupuesto(presupuestoActual, articulo, cantidad);
                actualizarTablaItems();
                actualizarTotales();
                txtArticuloBusqueda.setText(""); // Limpiar campo
                txtCantidad.setText("1"); // Resetear cantidad
            },
            () -> JOptionPane.showMessageDialog(this, "Artículo no encontrado para agregar.", "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    private void eliminarItem() {
        int filaSeleccionada = tablaItems.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener el artículo para eliminar (asumiendo que el código está en la primera columna)
        String codigoArticulo = tablaModelo.getValueAt(filaSeleccionada, 0).toString();
        List<Articulo> articulos = presupuestoService.buscarArticulo(codigoArticulo, null); // ← Corrección aquí
        Optional<Articulo> articuloOpt = articulos.stream().findFirst(); // ← Corrección aquí
        articuloOpt.ifPresent(articulo -> {
            presupuestoService.eliminarItemDePresupuesto(presupuestoActual, articulo);
            actualizarTablaItems();
            actualizarTotales();
        });
    }

    private void actualizarTablaItems() {
        tablaModelo.setRowCount(0); // Limpiar tabla
        for (ItemPresupuesto item : presupuestoActual.getItems()) {
            tablaModelo.addRow(new Object[]{
                item.getArticulo().getCodigo(),
                item.getArticulo().getNombre(),
                item.getCantidad(),
                String.format("%.2f", item.getPrecioUnitario()),
                String.format("%.2f", item.getSubtotal())
            });
        }
    }

    private void cargarMediosDePago() {
        cmbMedioPago.removeAllItems();
        List<MedioPago> medios = presupuestoService.obtenerMediosDePago();
        for (MedioPago mp : medios) {
            cmbMedioPago.addItem(mp);
        }
        // Seleccionar Efectivo por defecto si existe
        medios.stream()
      .filter(mp -> mp.getTipoPago() == MedioPago.TipoPago.EFECTIVO)
      .findFirst()
      .ifPresent(mp -> cmbMedioPago.setSelectedItem(mp));
    }

    private void actualizarTotales() {
        if (presupuestoActual == null) {
            return;
        }

        List<PresupuestoService.TotalMedioPago> totalesList = presupuestoService.calcularTotalesMediosPago(presupuestoActual);
        Map<String, Double> totalesPorMedio = totalesList.stream()
        .collect(java.util.stream.Collectors.toMap(
            t -> t.medioPago().getTipoPago().name(), // Usa el nombre del enum como clave
            PresupuestoService.TotalMedioPago::total));


        lblTotalEfectivo.setText(String.format("Efectivo: $%.2f", totalesPorMedio.getOrDefault("Efectivo", 0.0)));
        lblTotalDebito.setText(String.format("Débito: $%.2f", totalesPorMedio.getOrDefault("Débito", 0.0)));
        lblTotalCredito.setText(String.format("Crédito: $%.2f", totalesPorMedio.getOrDefault("Crédito", 0.0)));
    }

    private void confirmarPresupuesto() {
        // Validar datos del cliente
        String nombreCliente = txtClienteNombre.getText().trim();
        String dniCliente = txtClienteDni.getText().trim();

        if (nombreCliente.isEmpty() || dniCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre y DNI del cliente para confirmar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (presupuestoActual.getItems().isEmpty()) {
             JOptionPane.showMessageDialog(this, "El presupuesto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(nombreCliente, dniCliente, "", "");
        // Puedes agregar más validaciones o buscar el cliente existente por DNI

        MedioPago medioPagoSeleccionado = (MedioPago) cmbMedioPago.getSelectedItem();
        if (medioPagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un medio de pago.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            presupuestoService.confirmarYGuardarPresupuesto(presupuestoActual, cliente, medioPagoSeleccionado);
            JOptionPane.showMessageDialog(this, "Presupuesto confirmado y guardado con éxito. ID: " + presupuestoActual.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Iniciar un nuevo presupuesto para la siguiente operación
            inicializarNuevoPresupuesto();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al confirmar presupuesto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void imprimirPresupuesto() {
        if (presupuestoActual == null || presupuestoActual.getId() == 0 || presupuestoActual.getEstado() != Presupuesto.EstadoPresupuesto.CONFIRMADO) {
            JOptionPane.showMessageDialog(this, "Primero debe confirmar un presupuesto para poder imprimirlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea imprimir el presupuesto " + presupuestoActual.getId() + "?", "Confirmar Impresión", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                impresionUtil.imprimirPresupuesto(presupuestoActual);
                JOptionPane.showMessageDialog(this, "Presupuesto impreso con éxito (o generado PDF).", "Impresión", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al imprimir presupuesto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void limpiarFormulario() {
        tablaModelo.setRowCount(0); // Limpiar la tabla
        txtArticuloBusqueda.setText("");
        txtCantidad.setText("1");
        txtClienteNombre.setText("");
        txtClienteDni.setText("");
        lblTotalEfectivo.setText("Efectivo: $0.00");
        lblTotalDebito.setText("Débito: $0.00");
        lblTotalCredito.setText("Crédito: $0.00");
        // Asegúrate de resetear el medio de pago seleccionado si es necesario
        if (cmbMedioPago.getItemCount() > 0) {
            cmbMedioPago.setSelectedIndex(0);
        }
    }
}
