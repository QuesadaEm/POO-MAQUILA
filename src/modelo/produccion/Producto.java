package modelo.produccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Producto que se fabrica en la maquila.
 * Se compone de sus etapas (composicion 1 a 1..*): las etapas no existen sin el producto.
 */
public class Producto {

    private String id;
    private String nombre;
    private String descripcion;
    private EstadoProducto estado;
    private List<Etapa> etapas;
    private int indiceEtapaActual;

    public Producto(String id, String nombre, String descripcion) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id del producto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        this.id = id.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.estado = EstadoProducto.PENDIENTE;
        this.etapas = new ArrayList<>();
        this.indiceEtapaActual = 0;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    /** Agrega una etapa y mantiene la lista ordenada por el orden de la etapa. */
    public void agregarEtapa(Etapa etapa) {
        if (etapa == null) {
            throw new IllegalArgumentException("La etapa no puede ser nula.");
        }
        if (estado != EstadoProducto.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden agregar etapas a un producto pendiente.");
        }
        etapas.add(etapa);
        etapas.sort(Comparator.comparingInt(Etapa::getOrden));
    }

    public List<Etapa> getEtapas() {
        return Collections.unmodifiableList(etapas);
    }

    /** Devuelve la etapa en la que esta el producto, o null si ya termino o no tiene etapas. */
    public Etapa getEtapaActual() {
        if (estado == EstadoProducto.TERMINADO || etapas.isEmpty()) {
            return null;
        }
        return etapas.get(indiceEtapaActual);
    }

    /**
     * Pasa a la siguiente etapa. Si la etapa actual era la ultima, el producto queda TERMINADO.
     */
    public void avanzarEtapa() {
        if (estado == EstadoProducto.TERMINADO || estado == EstadoProducto.RECHAZADO) {
            throw new IllegalStateException("El producto " + id + " ya no esta en proceso.");
        }
        if (indiceEtapaActual < etapas.size() - 1) {
            indiceEtapaActual++;
            estado = EstadoProducto.EN_PROCESO;
        } else {
            estado = EstadoProducto.TERMINADO;
        }
    }

    public void cambiarEstado(EstadoProducto nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = nuevoEstado;
    }

    @Override
    public String toString() {
        Etapa actual = getEtapaActual();
        String etapaTexto = actual == null ? "-" : actual.getNombre();
        return String.format("%-6s %-28s %-11s etapa: %s", id, nombre, estado, etapaTexto);
    }
}
