package datos;

import excepciones.NegocioException;
import negocio.ServicioProduccion;

/**
 * Datos iniciales de produccion. Los productos se crean y se avanzan
 * con el mismo servicio que usa el menu, asi quedan en estados distintos
 * y con historial real. Usa los ids de colaboradores (OP01, SU01, ...) e
 * insumos (T01, H01, ...) de la carga de Persona A.
 * Los ids de los productos los asigna el sistema (P001-P005).
 */
public class CargaDatosProduccion {

    public static void cargar(ServicioProduccion produccion) {
        try {
            // Queda PENDIENTE, sin empezar.
            produccion.crearProducto("Camisa formal manga larga", "Camisa de vestir talla M", "T01", 160, 120, 8);

            // Cortado, queda EN_PROCESO en confeccion.
            String pantalon = produccion.crearProducto("Pantalon de mezclilla", "Pantalon talla 32", "T02", 120, 150, 1).getId();
            produccion.avanzarProducto(pantalon, "OP01", "Corte sin novedades");

            // Pasa todas las etapas y queda TERMINADO.
            String camiseta = produccion.crearProducto("Camiseta basica", "Camiseta cuello redondo talla S", "T01", 100, 80, 0).getId();
            produccion.avanzarProducto(camiseta, "OP02", "");
            produccion.avanzarProducto(camiseta, "OP03", "Costuras reforzadas");
            produccion.registrarInspeccion(camiseta, 0);
            produccion.avanzarProducto(camiseta, "SU01", "Aprobada");

            // El control de calidad encuentra defectos y queda RECHAZADO.
            String camisa = produccion.crearProducto("Camisa manga corta", "Camisa casual talla L", "T01", 150, 100, 6).getId();
            produccion.avanzarProducto(camisa, "OP01", "");
            produccion.avanzarProducto(camisa, "OP04", "");
            produccion.registrarInspeccion(camisa, 2);
            produccion.avanzarProducto(camisa, "SU02", "Costuras torcidas en las mangas");

            // La tela impermeable (T03) no alcanza: queda detenido por falta de stock.
            produccion.crearProducto("Abrigo largo", "Abrigo de invierno talla M", "T03", 180, 300, 10);
        } catch (NegocioException e) {
            System.out.println("Aviso: no se pudieron cargar todos los datos de produccion: " + e.getMessage());
        }
    }
}
