package modelo.inventario;
import java.time.LocalDate;
import util.Validaciones;
/** Registro inmutable; la identidad del producto es opcional para movimientos manuales. */
public final class MovimientoInventario {
    private final String id;
    private final Insumo insumo;
    private final int cantidad;
    private final TipoMovimiento tipo;
    private final LocalDate fecha;
    private final String motivo;
    private final String productoId;
    public MovimientoInventario(String id, Insumo insumo, int cantidad, TipoMovimiento tipo,
                                LocalDate fecha, String motivo) {
        this(id, insumo, cantidad, tipo, fecha, motivo, "");
    }
    public MovimientoInventario(String id, Insumo insumo, int cantidad, TipoMovimiento tipo,
                                LocalDate fecha, String motivo, String productoId) {
        this.id = Validaciones.texto(id, "Id del movimiento", 60);
        this.insumo = Validaciones.requerido(insumo, "Insumo");
        this.cantidad = Validaciones.positivo(cantidad, "Cantidad");
        this.tipo = Validaciones.requerido(tipo, "Tipo de movimiento");
        this.fecha = Validaciones.requerido(fecha, "Fecha");
        this.motivo = Validaciones.texto(motivo, "Motivo", 200);
        this.productoId = productoId == null || productoId.isEmpty() ? "" : Validaciones.id(productoId);
    }
    public String getId() { return id; }
    public Insumo getInsumo() { return insumo; }
    public int getCantidad() { return cantidad; }
    public TipoMovimiento getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public String getMotivo() { return motivo; }
    public String getProductoId() { return productoId; }
}
