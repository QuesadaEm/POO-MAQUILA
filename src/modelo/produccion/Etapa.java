package modelo.produccion;

import modelo.personal.Colaborador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Etapa del proceso de produccion. Cada tipo de etapa decide
 * quien puede ser responsable y que pasa al ejecutarla (polimorfismo).
 */
public abstract class Etapa {

    private String nombre;
    private String descripcion;
    private int orden;
    private List<RequerimientoInsumo> requerimientos;
    private List<RegistroAvance> registros;

    public Etapa(String nombre, String descripcion, int orden) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la etapa es obligatorio.");
        }
        if (orden < 1) {
            throw new IllegalArgumentException("El orden de la etapa debe ser 1 o mayor.");
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
        this.requerimientos = new ArrayList<>();
        this.registros = new ArrayList<>();
    }

    /** Indica si el colaborador puede encargarse de esta etapa. */
    public abstract boolean validarResponsable(Colaborador colaborador);

    /**
     * Ejecuta el trabajo de la etapa sobre el producto.
     * Devuelve true si el producto paso la etapa y false si no la paso.
     */
    public abstract boolean ejecutar(Producto producto);

    /**
     * Cantidad del insumo que consume esta etapa.
     * Por defecto es la cantidad por prenda; EtapaCorte la sobrescribe para sumar el desperdicio.
     */
    public int calcularConsumo(RequerimientoInsumo requerimiento) {
        return requerimiento.getCantidadPorPrenda();
    }

    public void agregarRequerimiento(RequerimientoInsumo requerimiento) {
        if (requerimiento == null) {
            throw new IllegalArgumentException("El requerimiento no puede ser nulo.");
        }
        requerimientos.add(requerimiento);
    }

    public List<RequerimientoInsumo> getRequerimientos() {
        return Collections.unmodifiableList(requerimientos);
    }

    public void agregarRegistro(RegistroAvance registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo.");
        }
        registros.add(registro);
    }

    public List<RegistroAvance> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getOrden() {
        return orden;
    }
}
