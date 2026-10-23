package util;
import excepciones.NegocioException;
import java.util.Locale;
public final class Validaciones {
    private Validaciones() { }
    public static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.trim().isEmpty()) throw new NegocioException(campo + " es obligatorio.");
        String limpio = valor.trim();
        if (limpio.length() > maximo) throw new NegocioException(campo + " admite hasta " + maximo + " caracteres.");
        for (int i = 0; i < limpio.length(); i++) {
            if (Character.isISOControl(limpio.charAt(i))) throw new NegocioException(campo + " contiene caracteres de control.");
        }
        return limpio;
    }
    public static String id(String valor) {
        String limpio = texto(valor, "Identificador", 20).toUpperCase(Locale.ROOT);
        if (!limpio.matches("[A-Z0-9][A-Z0-9_-]*"))
            throw new NegocioException("El identificador solo admite letras sin tilde, numeros, guion y guion bajo.");
        return limpio;
    }
    public static int noNegativo(int valor, String campo) {
        if (valor < 0) throw new NegocioException(campo + " no puede ser negativo.");
        return valor;
    }
    public static int positivo(int valor, String campo) {
        if (valor <= 0) throw new NegocioException(campo + " debe ser mayor que cero.");
        return valor;
    }
    public static double monto(double valor, String campo, boolean permiteCero) {
        if (!Double.isFinite(valor) || valor < 0 || (!permiteCero && valor == 0) || valor > 1000000000.0)
            throw new NegocioException(campo + " debe ser un numero " + (permiteCero ? "no negativo" : "positivo") + " de hasta 1000000000.");
        double redondeado = Math.round(valor * 100.0) / 100.0;
        if (!permiteCero && redondeado == 0) throw new NegocioException(campo + " debe ser al menos 0.01.");
        return redondeado;
    }
    public static <T> T requerido(T valor, String campo) {
        if (valor == null) throw new NegocioException(campo + " es obligatorio.");
        return valor;
    }
}
