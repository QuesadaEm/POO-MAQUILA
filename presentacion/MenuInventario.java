// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package presentacion;

import modelo.inventario.Insumo;
import negocio.ServicioInventario;

public class MenuInventario {

    private final ServicioInventario servicio;

    public MenuInventario(ServicioInventario servicio) {
        this.servicio = servicio;
    }

    public void mostrar() {
        System.out.println("\n--- Inventario (version provisional) ---");
        for (Insumo i : servicio.listar()) {
            System.out.printf("%-6s %-20s %6d %-5s%s%n", i.getCodigo(), i.getNombre(), i.getExistencias(),
                    i.getUnidadMedida(), i.esStockBajo() ? "  STOCK BAJO!" : "");
        }
        util.Consola.pausa();
    }
}
