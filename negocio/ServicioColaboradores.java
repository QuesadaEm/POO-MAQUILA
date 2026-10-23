// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
// Lo unico que Produccion usa de aqui es buscar(id).
package negocio;

import excepciones.ElementoNoEncontradoException;
import modelo.personal.Colaborador;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioColaboradores {

    private final Map<String, Colaborador> colaboradores = new LinkedHashMap<>();

    public void registrar(Colaborador colaborador) {
        colaboradores.put(colaborador.getId(), colaborador);
    }

    public Colaborador buscar(String id) throws ElementoNoEncontradoException {
        Colaborador c = colaboradores.get(id == null ? null : id.trim().toUpperCase());
        if (c == null) {
            throw new ElementoNoEncontradoException("No existe el colaborador " + id);
        }
        return c;
    }

    public List<Colaborador> listar() {
        return new ArrayList<>(colaboradores.values());
    }
}
