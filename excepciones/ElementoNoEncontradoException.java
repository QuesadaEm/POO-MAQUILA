package excepciones;

/**
 * [AMBOS] Se lanza cuando se busca un elemento por id y no existe.
 */
public class ElementoNoEncontradoException extends NegocioException {

    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
