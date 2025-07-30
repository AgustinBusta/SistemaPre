package es.proyecto.sistema.SistemaPresupuesto.util;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import es.proyecto.sistema.SistemaPresupuesto.model.ItemPresupuesto;
import es.proyecto.sistema.SistemaPresupuesto.model.Presupuesto;

// Para una impresión real con PDF, necesitarías librerías como iText o Apache PDFBox.
// Esto es un ejemplo simplificado que simula la generación de un "documento"
@Component
public class ImpresionUtil {

    public void imprimirPresupuesto(Presupuesto presupuesto) throws Exception {
        if (presupuesto == null) {
            throw new IllegalArgumentException("El presupuesto no puede ser nulo para imprimir.");
        }

        // Simulación de generación de contenido para impresión (ej. a una consola o archivo de texto)
        System.out.println("\n--- INICIO DE PRESUPUESTO ---");
        System.out.println("ID Presupuesto: " + presupuesto.getId());
        System.out.println("Fecha Creación: " + presupuesto.getFechaCreacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Validez hasta: " + presupuesto.getFechaValidez().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Vendedor: " + presupuesto.getVendedor().getNombre() + " " + presupuesto.getVendedor().getApellido());

        if (presupuesto.getCliente() != null) {
            System.out.println("Cliente: " + presupuesto.getCliente().getNombre() + " (DNI: " + presupuesto.getCliente().getDni() + ")");
        } else {
            System.out.println("Cliente: N/A (Presupuesto sin cliente asociado)");
        }

        System.out.println("\n--- Detalles del Presupuesto ---");
        System.out.printf("%-10s %-30s %-10s %-15s %-15s\n", "Código", "Artículo", "Cant.", "P. Unit.", "Subtotal");
        System.out.println("----------------------------------------------------------------------------------");
        for (ItemPresupuesto item : presupuesto.getItems()) {
            System.out.printf("%-10s %-30s %-10d %-15.2f %-15.2f\n",
                item.getArticulo().getCodigo(),
                item.getArticulo().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getSubtotal());
        }
        System.out.println("----------------------------------------------------------------------------------");

        // Calcular y mostrar totales nuevamente para la impresión
        // (Esto lo haría el PresupuestoService normalmente, pero para la demo lo hago aquí)
        // Necesitarías el PresupuestoService o el MedioPagoDAO para obtener los porcentajes
        // Para esta simulación, asumiré que tengo acceso a los porcentajes o los calculo con datos del presupuesto
        System.out.println("\n--- Totales ---");
        if (presupuesto.getMedioPagoSeleccionado() != null) {
             System.out.printf("Total Bruto: $%.2f\n", presupuesto.calcularTotalBruto());
             System.out.printf("Medio de Pago Seleccionado: %s (Ajuste: %.2f%%)\n",
                                presupuesto.getMedioPagoSeleccionado().getTipoPago().name(),
                                presupuesto.getMedioPagoSeleccionado().getPorcentajeAjuste() * 100);
             System.out.printf("Total Final: $%.2f\n", presupuesto.calcularTotalConAjuste());
        } else {
             System.out.printf("Total Bruto: $%.2f\n", presupuesto.calcularTotalBruto());
             System.out.println("No se seleccionó medio de pago final.");
        }


        // Aquí iría la lógica para generar un PDF o enviar a impresora
        // Por ejemplo, usando iText:
        /*
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("presupuesto_" + presupuesto.getId() + ".pdf"));
        document.open();
        document.add(new Paragraph("Presupuesto ID: " + presupuesto.getId()));
        // ... añadir más contenido
        document.close();
        */

        System.out.println("--- FIN DE PRESUPUESTO ---\n");
    }
}
