package presentacion;

import excepciones.NegocioException;
import modelo.inventario.Insumo;
import modelo.produccion.Etapa;
import modelo.produccion.EtapaControlCalidad;
import modelo.produccion.EstadoProducto;
import modelo.produccion.Producto;
import modelo.produccion.RegistroAvance;
import modelo.produccion.RequerimientoInsumo;
import negocio.ServicioConsultas;
import negocio.ServicioProduccion;
import util.Consola;

import java.util.List;
import java.util.Map;

public class MenuProduccion {

    private final ServicioProduccion produccion;
    private final ServicioConsultas consultas;

    public MenuProduccion(ServicioProduccion produccion, ServicioConsultas consultas) {
        this.produccion = produccion;
        this.consultas = consultas;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.limpiarPantalla();
            System.out.println("===== PRODUCCION =====");
            System.out.println("1. Listar productos");
            System.out.println("2. Ver detalle de un producto");
            System.out.println("3. Crear producto");
            System.out.println("4. Avanzar producto a la siguiente etapa");
            System.out.println("5. Eliminar producto pendiente");
            System.out.println("6. Consultas cruzadas");
            System.out.println("0. Volver");
            opcion = Consola.leerEntero("Opcion: ", 0, 6);
            try {
                switch (opcion) {
                    case 1: listarProductos(); break;
                    case 2: verDetalle(); break;
                    case 3: crearProducto(); break;
                    case 4: avanzarProducto(); break;
                    case 5: eliminarProducto(); break;
                    case 6: menuConsultas(); break;
                    default: break;
                }
            } catch (NegocioException e) {
                System.out.println("\n  ERROR: " + e.getMessage());
            }
            if (opcion != 0) {
                Consola.pausa();
            }
        } while (opcion != 0);
    }

    private void listarProductos() {
        System.out.println("\n1. Todos");
        int i = 2;
        for (EstadoProducto estado : EstadoProducto.values()) {
            System.out.println(i++ + ". " + estado);
        }
        int filtro = Consola.leerEntero("Mostrar: ", 1, EstadoProducto.values().length + 1);
        List<Producto> lista = filtro == 1
                ? produccion.listar()
                : produccion.listarPorEstado(EstadoProducto.values()[filtro - 2]);
        imprimirProductos(lista);
    }

    private void verDetalle() throws NegocioException {
        Producto p = produccion.buscar(pedirIdProducto(produccion.listar()));
        System.out.println("\n" + p.getId() + " - " + p.getNombre());
        System.out.println("Descripcion: " + p.getDescripcion());
        System.out.println("Estado: " + p.getEstado());
        for (Etapa etapa : p.getEtapas()) {
            String marca = etapa == p.getEtapaActual() ? "  <- etapa actual" : "";
            System.out.println("\n  [" + etapa.getOrden() + "] " + etapa.getNombre() + marca);
            for (RequerimientoInsumo r : etapa.getRequerimientos()) {
                System.out.println("      usa " + etapa.calcularConsumo(r) + " " + r.getInsumo().getUnidadMedida()
                        + " de " + r.getInsumo().getNombre());
            }
            for (RegistroAvance registro : etapa.getRegistros()) {
                System.out.println("      " + registro);
            }
        }
    }

    private void crearProducto() throws NegocioException {
        String nombre = Consola.leerTexto("Nombre: ");
        String descripcion = Consola.leerTextoOpcional("Descripcion (opcional): ");
        int tela = Consola.leerEntero("Metros de tela por prenda: ", 1, 1000);
        int hilo = Consola.leerEntero("Metros de hilo por prenda: ", 1, 100000);
        int botones = Consola.leerEntero("Botones por prenda (0 si no lleva): ", 0, 1000);
        Producto p = produccion.crearProducto(nombre, descripcion, tela, hilo, botones);
        System.out.println("\n  Producto " + p.getId() + " creado en estado " + p.getEstado() + ".");
    }

    private void avanzarProducto() throws NegocioException {
        List<Producto> enProceso = produccion.listarPorEstado(EstadoProducto.PENDIENTE);
        enProceso.addAll(produccion.listarPorEstado(EstadoProducto.EN_PROCESO));
        Producto p = produccion.buscar(pedirIdProducto(enProceso));
        Etapa etapa = p.getEtapaActual();
        if (etapa == null) {
            System.out.println("\n  El producto ya esta " + p.getEstado() + ".");
            return;
        }
        System.out.println("Etapa actual: " + etapa.getNombre());
        String idColaborador = Consola.leerTexto("Id del responsable: ");
        if (etapa instanceof EtapaControlCalidad) {
            int defectuosas = Consola.leerEntero("Prendas defectuosas encontradas: ", 0, 100000);
            produccion.registrarInspeccion(p.getId(), defectuosas);
        }
        String observaciones = Consola.leerTextoOpcional("Observaciones (opcional): ");
        RegistroAvance registro = produccion.avanzarProducto(p.getId(), idColaborador, observaciones);
        System.out.println("\n  " + registro.getResultado() + ". El producto ahora esta " + p.getEstado() + ".");
    }

    private void eliminarProducto() throws NegocioException {
        String id = pedirIdProducto(produccion.listarPorEstado(EstadoProducto.PENDIENTE));
        produccion.eliminarProducto(id);
        System.out.println("\n  Producto eliminado.");
    }

    private void menuConsultas() throws NegocioException {
        System.out.println("\n--- Consultas cruzadas ---");
        System.out.println("1. Productos en los que trabajo un colaborador");
        System.out.println("2. Insumos consumidos por un producto");
        System.out.println("3. Productos detenidos por falta de stock");
        System.out.println("4. Resumen de productos por estado");
        System.out.println("0. Volver");
        switch (Consola.leerEntero("Opcion: ", 0, 4)) {
            case 1:
                imprimirProductos(consultas.productosPorColaborador(Consola.leerTexto("Id del colaborador: ")));
                break;
            case 2:
                Map<Insumo, Integer> consumo = consultas.insumosConsumidosPorProducto(pedirIdProducto(produccion.listar()));
                if (consumo.isEmpty()) {
                    System.out.println("\n  Ese producto todavia no ha consumido insumos.");
                }
                for (Map.Entry<Insumo, Integer> e : consumo.entrySet()) {
                    System.out.println("  " + e.getKey().getNombre() + ": " + e.getValue() + " " + e.getKey().getUnidadMedida());
                }
                break;
            case 3:
                Map<Producto, List<String>> detenidos = consultas.productosDetenidosPorFaltaDeStock();
                if (detenidos.isEmpty()) {
                    System.out.println("\n  No hay productos detenidos.");
                }
                for (Map.Entry<Producto, List<String>> e : detenidos.entrySet()) {
                    System.out.println("  " + e.getKey().getId() + " " + e.getKey().getNombre()
                            + " -> falta " + String.join(", ", e.getValue()));
                }
                break;
            case 4:
                for (Map.Entry<EstadoProducto, Integer> e : consultas.resumenPorEstado().entrySet()) {
                    System.out.printf("  %-11s %d%n", e.getKey(), e.getValue());
                }
                break;
            default:
                break;
        }
    }

    /** Muestra los productos que aplican y pide el id, para que el usuario no tenga que adivinarlo. */
    private String pedirIdProducto(List<Producto> opciones) {
        imprimirProductos(opciones);
        return Consola.leerTexto("\nId del producto: ");
    }

    private void imprimirProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("\n  No hay productos para mostrar.");
            return;
        }
        System.out.println();
        for (Producto p : productos) {
            System.out.println("  " + p);
        }
    }
}
