package presentacion;

import util.Consola;

public class MenuPrincipal {

    private final MenuColaboradores menuColaboradores;
    private final MenuInventario menuInventario;
    private final MenuProduccion menuProduccion;

    public MenuPrincipal(MenuColaboradores menuColaboradores, MenuInventario menuInventario,
                         MenuProduccion menuProduccion) {
        this.menuColaboradores = menuColaboradores;
        this.menuInventario = menuInventario;
        this.menuProduccion = menuProduccion;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.limpiarPantalla();
            System.out.println("========== MAQUILA TEXTIL ==========");
            System.out.println("1. Personal");
            System.out.println("2. Inventario");
            System.out.println("3. Produccion");
            System.out.println("0. Salir");
            opcion = Consola.leerEntero("Opcion: ", 0, 3);
            switch (opcion) {
                case 1: menuColaboradores.mostrar(); break;
                case 2: menuInventario.mostrar(); break;
                case 3: menuProduccion.mostrar(); break;
                default: System.out.println("Hasta luego!"); break;
            }
        } while (opcion != 0);
    }
}
