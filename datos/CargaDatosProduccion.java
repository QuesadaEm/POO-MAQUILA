package datos;

import excepciones.NegocioException;
import negocio.ServicioProduccion;

/**
 * Datos iniciales de produccion. Los productos se crean y se avanzan
 * con el mismo servicio que usa el menu, asi quedan en estados distintos
 * y con historial real. Usa los ids de colaboradores (C001-C008) e
 * insumos (INS01-INS05) acordados con Persona A.
 * Los ids de los productos los asigna el sistema (P001-P005).
 */
public class CargaDatosProduccion {

    public static void cargar(ServicioProduccion produccion) {
        try {
            // Queda PENDIENTE, sin empezar.
            produccion.crearProducto("Camisa formal manga larga", "Camisa de vestir talla M", 2, 120, 8);

            // Cortado, queda EN_PROCESO en confeccion.
            String pantalon = produccion.crearProducto("Pantalon de mezclilla", "Pantalon talla 32", 2, 150, 1).getId();
            produccion.avanzarProducto(pantalon, "C001", "Corte sin novedades");

            // Pasa todas las etapas y queda TERMINADO.
            String camiseta = produccion.crearProducto("Camiseta basica", "Camiseta cuello redondo talla S", 1, 80, 0).getId();
            produccion.avanzarProducto(camiseta, "C002", "");
            produccion.avanzarProducto(camiseta, "C003", "Costuras reforzadas");
            produccion.registrarInspeccion(camiseta, 0);
            produccion.avanzarProducto(camiseta, "C004", "Aprobada");

            // El control de calidad encuentra defectos y queda RECHAZADO.
            String camisa = produccion.crearProducto("Camisa manga corta", "Camisa casual talla L", 2, 100, 6).getId();
            produccion.avanzarProducto(camisa, "C001", "");
            produccion.avanzarProducto(camisa, "C008", "");
            produccion.registrarInspeccion(camisa, 2);
            produccion.avanzarProducto(camisa, "C005", "Costuras torcidas en las mangas");

            // Pide mas tela de la que hay, queda detenido por falta de stock.
            produccion.crearProducto("Abrigo largo", "Abrigo de invierno talla M", 80, 300, 10);
        } catch (NegocioException e) {
            System.out.println("Aviso: no se pudieron cargar todos los datos de produccion: " + e.getMessage());
        }
    }
}
