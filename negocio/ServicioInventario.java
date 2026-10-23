// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
// Produccion usa buscar(codigo), hayExistencias(codigo, cant) y consumir(codigo, cant).
package negocio;

import excepciones.ElementoNoEncontradoException;
import excepciones.ExistenciasInsuficientesException;
import modelo.inventario.Insumo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioInventario {

    private final Map<String, Insumo> insumos = new LinkedHashMap<>();

    public void registrar(Insumo insumo) {
        insumos.put(insumo.getCodigo(), insumo);
    }

    public Insumo buscar(String codigo) throws ElementoNoEncontradoException {
        Insumo i = insumos.get(codigo == null ? null : codigo.trim().toUpperCase());
        if (i == null) {
            throw new ElementoNoEncontradoException("No existe el insumo " + codigo);
        }
        return i;
    }

    public List<Insumo> listar() {
        return new ArrayList<>(insumos.values());
    }

    public boolean hayExistencias(String codigo, int cantidad) {
        Insumo i = insumos.get(codigo);
        return i != null && i.getExistencias() >= cantidad;
    }

    public void consumir(String codigo, int cantidad) throws ExistenciasInsuficientesException {
        if (!hayExistencias(codigo, cantidad)) {
            throw new ExistenciasInsuficientesException("No hay existencias suficientes de " + codigo);
        }
        insumos.get(codigo).retirarExistencias(cantidad);
    }
}
