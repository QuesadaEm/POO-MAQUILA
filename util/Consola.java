package util;

import java.util.Scanner;

/**
 * [AMBOS] Lectura validada desde la consola.
 * Ningun metodo se cae si el usuario escribe algo invalido: vuelve a preguntar.
 */
public final class Consola {

    private static final Scanner ENTRADA = new Scanner(System.in);

    private Consola() {
    }

    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!ENTRADA.hasNextLine()) {
                // Se cerro la entrada (por ejemplo, Ctrl+D): se sale de forma ordenada.
                System.out.println();
                System.exit(0);
            }
            String texto = ENTRADA.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("  El valor no puede quedar vacio.");
        }
    }

    public static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        if (!ENTRADA.hasNextLine()) {
            System.out.println();
            System.exit(0);
        }
        return ENTRADA.nextLine().trim();
    }

    public static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("  Debe ser un numero entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  \"" + texto + "\" no es un numero entero valido.");
            }
        }
    }

    public static double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                return Double.parseDouble(texto.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("  \"" + texto + "\" no es un numero valido.");
            }
        }
    }

    public static void pausa() {
        leerTextoOpcional("\nPresione Enter para continuar...");
    }

    /**
     * Limpia la pantalla. En la consola de Windows usa "cls" y en Linux/Mac un codigo ANSI.
     * La ventana Output de NetBeans no se puede limpiar desde Java (no es una consola real),
     * asi que ahi se imprimen lineas en blanco para empujar el texto viejo hacia arriba.
     */
    public static void limpiarPantalla() {
        try {
            if (System.console() == null) {
                for (int i = 0; i < 40; i++) {
                    System.out.println();
                }
            } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
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
}
