// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.inventario;

import java.time.LocalDate;

public class MovimientoInventario {

    private String id;
    private Insumo insumo;
    private int cantidad;
    private TipoMovimiento tipo;
    private LocalDate fecha;
    private String motivo;

    public MovimientoInventario(String id, Insumo insumo, int cantidad, TipoMovimiento tipo,
                                LocalDate fecha, String motivo) {
        this.id = id;
        this.insumo = insumo;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    public String getId() { return id; }
    public Insumo getInsumo() { return insumo; }
    public int getCantidad() { return cantidad; }
    public TipoMovimiento getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
}
