// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.inventario;

public class Insumo {

    private String codigo;
    private String nombre;
    private CategoriaInsumo categoria;
    private int existencias;
    private int stockMinimo;
    private String unidadMedida;

    public Insumo(String codigo, String nombre, CategoriaInsumo categoria,
                  int existencias, int stockMinimo, String unidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.existencias = existencias;
        this.stockMinimo = stockMinimo;
        this.unidadMedida = unidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaInsumo getCategoria() {
        return categoria;
    }

    public int getExistencias() {
        return existencias;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void agregarExistencias(int cantidad) {
        existencias += cantidad;
    }

    public void retirarExistencias(int cantidad) {
        if (cantidad > existencias) {
            throw new IllegalArgumentException("No hay existencias suficientes de " + nombre);
        }
        existencias -= cantidad;
    }

    public boolean esStockBajo() {
        return existencias <= stockMinimo;
    }
}
