package excepciones;

/**
 * [AMBOS] Excepcion base de las reglas de negocio.
 * Todas las excepciones propias del proyecto heredan de esta.
 */
public class NegocioException extends Exception {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
