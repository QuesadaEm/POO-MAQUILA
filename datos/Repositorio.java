package datos;

import excepciones.ElementoNoEncontradoException;
import excepciones.NegocioException;

import java.util.List;

/**
 * [AMBOS] Contrato comun de todos los repositorios.
 * Cada repositorio guarda sus elementos en memoria usando el id como llave.
 */
public interface Repositorio<T> {

    void agregar(T elemento) throws NegocioException;

    T buscarPorId(String id) throws ElementoNoEncontradoException;

    List<T> listar();

    void eliminar(String id) throws ElementoNoEncontradoException;
}
