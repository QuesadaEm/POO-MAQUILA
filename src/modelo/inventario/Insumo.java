package modelo.inventario;
import java.time.LocalDate;
import java.util.*;
import excepciones.*;
import util.Validaciones;
public final class Insumo {
    private final String codigo;
    private String nombre;
    private final CategoriaInsumo categoria;
    private int existencias;
    private int stockMinimo;
    private final String unidadMedida;
    private final List<MovimientoInventario> movimientos = new ArrayList<>();
    public Insumo(String codigo, String nombre, CategoriaInsumo categoria, int existencias,
                  int stockMinimo, String unidadMedida) {
        this.codigo = Validaciones.id(codigo);
        this.nombre = Validaciones.texto(nombre, "Nombre del insumo", 80);
        this.categoria = Validaciones.requerido(categoria, "Categoria");
        this.stockMinimo = Validaciones.noNegativo(stockMinimo, "Stock minimo");
        this.unidadMedida = Validaciones.texto(unidadMedida, "Unidad de medida", 16);
        Validaciones.noNegativo(existencias, "Existencias");
        if (existencias > 0) agregarExistencias(existencias, "Saldo inicial");
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public CategoriaInsumo getCategoria() { return categoria; }
    public int getExistencias() { return existencias; }
    public int getStockMinimo() { return stockMinimo; }
    public String getUnidadMedida() { return unidadMedida; }
    public List<MovimientoInventario> getMovimientos() { return Collections.unmodifiableList(movimientos); }
    public boolean esStockBajo() { return existencias <= stockMinimo; }
    public void actualizar(String nombre, int stockMinimo) {
        String nuevoNombre = Validaciones.texto(nombre, "Nombre del insumo", 80);
        Validaciones.noNegativo(stockMinimo, "Stock minimo");
        this.nombre = nuevoNombre; this.stockMinimo = stockMinimo;
    }
    public void agregarExistencias(int cantidad) { agregarExistencias(cantidad, "Entrada manual"); }
    public void agregarExistencias(int cantidad, String motivo) {
        Validaciones.positivo(cantidad, "Cantidad");
        if ((long) existencias + cantidad > Integer.MAX_VALUE) throw new NegocioException("La entrada supera la capacidad del inventario.");
        MovimientoInventario movimiento = crearMovimiento(cantidad, TipoMovimiento.ENTRADA, motivo, "");
        existencias += cantidad;
        movimientos.add(movimiento);
    }
    public void retirarExistencias(int cantidad) { retirarExistencias(cantidad, "Salida manual", ""); }
    public void retirarExistencias(int cantidad, String motivo, String productoId) {
        Validaciones.positivo(cantidad, "Cantidad");
        if (cantidad > existencias) throw new ExistenciasInsuficientesException("Stock insuficiente de " + codigo + ": disponible " + existencias + ", solicitado " + cantidad + ".");
        MovimientoInventario movimiento = crearMovimiento(cantidad, TipoMovimiento.SALIDA, motivo, productoId);
        existencias -= cantidad;
        movimientos.add(movimiento);
    }
    private MovimientoInventario crearMovimiento(int cantidad, TipoMovimiento tipo, String motivo, String productoId) {
        return new MovimientoInventario(UUID.randomUUID().toString(), this, cantidad, tipo, LocalDate.now(), motivo, productoId);
    }
}
