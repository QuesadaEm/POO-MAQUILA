package modelo.produccion;

import modelo.inventario.Insumo;

/**
 * Cuanto de un insumo necesita una etapa por cada prenda.
 */
public class RequerimientoInsumo {

    private Insumo insumo;
    private int cantidadPorPrenda;

    public RequerimientoInsumo(Insumo insumo, int cantidad) {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad por prenda debe ser mayor que cero.");
        }
        this.insumo = insumo;
        this.cantidadPorPrenda = cantidad;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public int getCantidadPorPrenda() {
        return cantidadPorPrenda;
    }
}
