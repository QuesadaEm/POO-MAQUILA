package excepciones;

/**
 * [B] Se lanza cuando se intenta hacer algo que el estado actual
 * del producto no permite (por ejemplo, avanzar un producto TERMINADO).
 */
public class TransicionInvalidaException extends NegocioException {

    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
