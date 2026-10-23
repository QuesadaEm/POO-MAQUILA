package datos;
import java.util.*;
import modelo.personal.Colaborador;
import excepciones.*;
import util.Validaciones;
public final class RepositorioColaboradores implements Repositorio<Colaborador> {
    private final Map<String, Colaborador> elementos = new HashMap<>();
    @Override public void agregar(Colaborador elemento) {
        Validaciones.requerido(elemento, "Elemento");
        String id = elemento.getId();
        if (elementos.containsKey(id)) throw new NegocioException("Ya existe el identificador " + id + ".");
        elementos.put(id, elemento);
    }
    @Override public Colaborador buscarPorId(String id) {
        String clave = Validaciones.id(id);
        Colaborador elemento = elementos.get(clave);
        if (elemento == null) throw new ElementoNoEncontradoException("No se encontro " + clave + ".");
        return elemento;
    }
    @Override public List<Colaborador> listar() {
        List<Colaborador> lista = new ArrayList<>(elementos.values());
        lista.sort(Comparator.comparing(Colaborador::getId));
        return Collections.unmodifiableList(lista);
    }
    @Override public void eliminar(String id) { elementos.remove(buscarPorId(id).getId()); }
}
