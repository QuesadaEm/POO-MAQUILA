package excepciones;
/** Una operacion rechazada por las reglas del sistema. */
public class NegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NegocioException(String mensaje) { super(mensaje); }
}
