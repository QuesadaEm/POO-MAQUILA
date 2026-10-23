package excepciones;
public class ExistenciasInsuficientesException extends NegocioException {
    private static final long serialVersionUID = 1L;
    public ExistenciasInsuficientesException(String mensaje) { super(mensaje); }
}
