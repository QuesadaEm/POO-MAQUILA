import datos.CargaDatosPersonalInventario;
import datos.CargaDatosProduccion;
import datos.RepositorioColaboradores;
import datos.RepositorioInsumos;
import datos.RepositorioProductos;
import negocio.ServicioColaboradores;
import negocio.ServicioConsultas;
import negocio.ServicioInventario;
import negocio.ServicioProduccion;
import presentacion.MenuColaboradores;
import presentacion.MenuInventario;
import presentacion.MenuPrincipal;
import presentacion.MenuProduccion;
import util.Consola;

/**
 * Punto de entrada: crea los repositorios y servicios, carga los datos iniciales y abre el menu.
 */
public class Main {

    public static void main(String[] args) {
        RepositorioColaboradores repositorioColaboradores = new RepositorioColaboradores();
        RepositorioInsumos repositorioInsumos = new RepositorioInsumos();
        RepositorioProductos repositorioProductos = new RepositorioProductos();

        // Personal e Inventario reciben los productos para no eliminar algo que se esta usando.
        ServicioColaboradores servicioColaboradores = new ServicioColaboradores(repositorioColaboradores, repositorioProductos);
        ServicioInventario servicioInventario = new ServicioInventario(repositorioInsumos, repositorioProductos);
        ServicioProduccion servicioProduccion = new ServicioProduccion(
                repositorioProductos, servicioColaboradores, servicioInventario);
        ServicioConsultas servicioConsultas = new ServicioConsultas(servicioProduccion, servicioColaboradores);

        // Primero Personal e Inventario, porque Produccion usa sus ids.
        CargaDatosPersonalInventario.cargar(servicioColaboradores, servicioInventario);
        CargaDatosProduccion.cargar(servicioProduccion);

        try {
            new MenuPrincipal(
                    new MenuColaboradores(servicioColaboradores),
                    new MenuInventario(servicioInventario),
                    new MenuProduccion(servicioProduccion, servicioConsultas)
            ).mostrar();
        } catch (Consola.FinEntrada e) {
            System.out.println("\nFin de la entrada. Sesion finalizada.");
        }
    }
}
