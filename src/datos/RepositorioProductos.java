package datos;

import excepciones.ElementoNoEncontradoException;
import excepciones.NegocioException;
import modelo.produccion.Producto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda los productos en memoria. Se usa un LinkedHashMap para buscar
 * por id directamente y, a la vez, listar en el orden en que se registraron.
 */
public class RepositorioProductos implements Repositorio<Producto> {

    private final Map<String, Producto> productos = new LinkedHashMap<>();

    @Override
    public void agregar(Producto producto) throws NegocioException {
        if (producto == null) {
            throw new NegocioException("El producto no puede ser nulo.");
        }
        if (productos.containsKey(producto.getId())) {
            throw new NegocioException("Ya existe un producto con el id " + producto.getId() + ".");
        }
        productos.put(producto.getId(), producto);
    }

    @Override
    public Producto buscarPorId(String id) throws ElementoNoEncontradoException {
        Producto producto = productos.get(normalizar(id));
        if (producto == null) {
            throw new ElementoNoEncontradoException("No existe un producto con el id " + id + ".");
        }
        return producto;
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    @Override
    public void eliminar(String id) throws ElementoNoEncontradoException {
        if (productos.remove(normalizar(id)) == null) {
            throw new ElementoNoEncontradoException("No existe un producto con el id " + id + ".");
        }
    }

    private String normalizar(String id) {
        return id == null ? "" : id.trim().toUpperCase();
    }
}
