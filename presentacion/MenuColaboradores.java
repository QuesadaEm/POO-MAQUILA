// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package presentacion;

import modelo.personal.Colaborador;
import negocio.ServicioColaboradores;

public class MenuColaboradores {

    private final ServicioColaboradores servicio;

    public MenuColaboradores(ServicioColaboradores servicio) {
        this.servicio = servicio;
    }

    public void mostrar() {
        System.out.println("\n--- Colaboradores (version provisional) ---");
        for (Colaborador c : servicio.listar()) {
            System.out.printf("%-5s %-16s %-15s %s%n", c.getId(), c.getNombre(), c.getCategoria(), c.getJornada());
        }
        util.Consola.pausa();
    }
}
