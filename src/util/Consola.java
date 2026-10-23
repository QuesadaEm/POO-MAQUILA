package util;
import java.util.*;
import excepciones.NegocioException;
/** Una unica entrada por lineas evita saltos de lectura entre texto y numeros. */
public final class Consola {
    private static final Scanner ENTRADA = new Scanner(System.in, "UTF-8");
    private Consola() { }
    public static final class FinEntrada extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
    private static String linea(String mensaje) {
        System.out.print(mensaje);
        if (!ENTRADA.hasNextLine()) throw new FinEntrada();
        return ENTRADA.nextLine().trim();
    }
    public static String leerTexto(String mensaje) {
        while (true) {
            String valor = linea(mensaje);
            try { return Validaciones.texto(valor, "El texto", 200); }
            catch (NegocioException e) { System.out.println(e.getMessage()); }
        }
    }
    public static String leerId(String mensaje) {
        while (true) {
            try { return Validaciones.id(leerTexto(mensaje)); }
            catch (NegocioException e) { System.out.println(e.getMessage()); }
        }
    }
    public static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            String valor = linea(mensaje);
            try {
                int numero = Integer.parseInt(valor);
                if (numero >= minimo && numero <= maximo) return numero;
            } catch (NumberFormatException e) { /* Se vuelve a solicitar la misma entrada. */ }
            System.out.println("Ingrese un numero entero entre " + minimo + " y " + maximo + ".");
        }
    }
    public static double leerDecimal(String mensaje) { return leerDecimal(mensaje, 0, 1000000000.0); }
    public static double leerDecimal(String mensaje, double minimo, double maximo) {
        while (true) {
            String valor = linea(mensaje).replace(',', '.');
            try {
                double numero = Double.parseDouble(valor);
                if (Double.isFinite(numero) && numero >= minimo && numero <= maximo) return numero;
            } catch (NumberFormatException e) { /* La coma decimal tambien es valida. */ }
            System.out.println("Ingrese un decimal entre " + minimo + " y " + maximo + ", sin separador de miles.");
        }
    }
    public static <T extends Enum<T>> T leerEnum(String mensaje, T[] opciones) {
        System.out.println(mensaje);
        for (int i = 0; i < opciones.length; i++) System.out.println((i + 1) + ". " + opciones[i]);
        return opciones[leerEntero("Opcion: ", 1, opciones.length) - 1];
    }
    public static boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = linea(mensaje + " (s/n): ");
            if (respuesta.equalsIgnoreCase("s")) return true;
            if (respuesta.equalsIgnoreCase("n")) return false;
            System.out.println("Ingrese s o n.");
        }
    }
    /** Lee una linea que puede quedar vacia (por ejemplo, observaciones opcionales). */
    public static String leerTextoOpcional(String mensaje) {
        return linea(mensaje);
    }

    public static void pausa() {
        linea("\nPresione Enter para continuar...");
    }

    /**
     * Limpia la pantalla. En la consola de Windows usa "cls" y en Linux/Mac un codigo ANSI.
     * La ventana Output de NetBeans no se puede limpiar desde Java (no es una consola real),
     * asi que ahi se imprimen lineas en blanco para empujar el texto viejo hacia arriba.
     */
    public static void limpiarPantalla() {
        try {
            if (System.console() == null) {
                for (int i = 0; i < 40; i++) System.out.println();
            } else if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si no se puede limpiar, se sigue normalmente.
            System.out.println();
        }
    }
    public static void titulo(String titulo) { System.out.println("\n=== " + titulo + " ==="); }
    public static String dinero(double monto) { return String.format(Locale.ROOT, "CRC %.2f", monto); }
    public static void accion(Runnable accion) {
        try { accion.run(); }
        catch (NegocioException e) { System.out.println("No se realizo la operacion: " + e.getMessage()); }
    }
}
