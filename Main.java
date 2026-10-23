import datos.CargaDatosPersonalInventario;
import datos.CargaDatosProduccion;
import datos.RepositorioProductos;
import negocio.ServicioColaboradores;
import negocio.ServicioConsultas;
import negocio.ServicioInventario;
import negocio.ServicioProduccion;
import presentacion.MenuColaboradores;
import presentacion.MenuInventario;
import presentacion.MenuPrincipal;
import presentacion.MenuProduccion;

/**
 * Punto de entrada: crea los servicios, carga los datos iniciales y abre el menu.
 */
public class Main {

    public static void main(String[] args) {
        ServicioColaboradores servicioColaboradores = new ServicioColaboradores();
        ServicioInventario servicioInventario = new ServicioInventario();
        ServicioProduccion servicioProduccion = new ServicioProduccion(
                new RepositorioProductos(), servicioColaboradores, servicioInventario);
        ServicioConsultas servicioConsultas = new ServicioConsultas(servicioProduccion, servicioColaboradores);

        // Primero Personal e Inventario, porque Produccion usa sus ids.
        CargaDatosPersonalInventario.cargar(servicioColaboradores, servicioInventario);
        CargaDatosProduccion.cargar(servicioProduccion);

        new MenuPrincipal(
                new MenuColaboradores(servicioColaboradores),
                new MenuInventario(servicioInventario),
                new MenuProduccion(servicioProduccion, servicioConsultas)
        ).mostrar();
    }
}
