package es.proyecto.sistema.SistemaPresupuesto.util;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import es.proyecto.sistema.SistemaPresupuesto.model.Presupuesto;

public class GeneradorInformes {
    public String generarReportePresupuesto(Presupuesto presupuesto) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        StringBuilder sb = new StringBuilder();
        sb.append("PRESUPUESTO #").append(presupuesto.getId()).append("\n");
        sb.append("Fecha: ").append(presupuesto.getFechaCreacion().format(dateFormat)).append("\n");
        sb.append("Válido hasta: ").append(presupuesto.getFechaValidez().format(dateFormat)).append("\n");
        sb.append("Cliente: ").append(presupuesto.getCliente().getNombre()).append("\n");
        sb.append("Vendedor: ").append(presupuesto.getVendedor().getNombreCompleto()).append("\n\n");
        sb.append("ITEMS:\n");
        
        presupuesto.getItems().forEach(item -> 
            sb.append(String.format(
                " - %d x %s (%s) = %s\n",
                item.getCantidad(),
                item.getArticulo().getNombre(),
                currencyFormat.format(item.getPrecioUnitario()),
                currencyFormat.format(item.getSubtotal())
            ))
        );
        
        sb.append("\nTOTAL: ").append(currencyFormat.format(presupuesto.calcularTotalBruto()));
        sb.append("\nMEDIO DE PAGO: ").append(presupuesto.getMedioPagoSeleccionado().getTipoPago());
        sb.append("\nTOTAL CON AJUSTE: ").append(currencyFormat.format(presupuesto.calcularTotalConAjuste()));
        
        return sb.toString();
    }
}