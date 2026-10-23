package datos;
import java.util.*;
import modelo.inventario.Insumo;
import excepciones.*;
import util.Validaciones;
public final class RepositorioInsumos implements Repositorio<Insumo> {
    private final Map<String, Insumo> elementos = new HashMap<>();
    @Override public void agregar(Insumo elemento) {
        Validaciones.requerido(elemento, "Elemento");
        String id = elemento.getCodigo();
        if (elementos.containsKey(id)) throw new NegocioException("Ya existe el identificador " + id + ".");
        elementos.put(id, elemento);
    }
    @Override public Insumo buscarPorId(String id) {
        String clave = Validaciones.id(id);
        Insumo elemento = elementos.get(clave);
        if (elemento == null) throw new ElementoNoEncontradoException("No se encontro " + clave + ".");
        return elemento;
    }
    @Override public List<Insumo> listar() {
        List<Insumo> lista = new ArrayList<>(elementos.values());
        lista.sort(Comparator.comparing(Insumo::getCodigo));
        return Collections.unmodifiableList(lista);
    }
    @Override public void eliminar(String id) { elementos.remove(buscarPorId(id).getCodigo()); }
}
