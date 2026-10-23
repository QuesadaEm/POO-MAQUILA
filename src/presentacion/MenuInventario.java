package presentacion;
import java.util.List;
import negocio.ServicioInventario;
import modelo.inventario.*;
import util.Consola;
public final class MenuInventario {
    private final ServicioInventario servicio;
    public MenuInventario(ServicioInventario servicio) { this.servicio = servicio; }
    public void mostrar() {
        while (true) {
            Consola.limpiarPantalla();
            Consola.titulo("INVENTARIO");
            System.out.println("1. Listar insumos\n2. Consultar insumo\n3. Registrar insumo\n4. Modificar insumo\n5. Registrar entrada\n6. Registrar salida\n7. Filtrar por categoria\n8. Alertas de stock minimo\n9. Historial de movimientos\n10. Eliminar insumo\n0. Volver");
            int opcion = Consola.leerEntero("Opcion: ", 0, 10);
            if (opcion == 0) return;
            Consola.accion(() -> {
                switch (opcion) {
                    case 1: listar(servicio.listar()); break;
                    case 2: detalle(servicio.buscar(Consola.leerId("Codigo: "))); break;
                    case 3: registrar(); break;
                    case 4: modificar(); break;
                    case 5: movimiento(true); break;
                    case 6: movimiento(false); break;
                    case 7: listar(servicio.porCategoria(Consola.leerEnum("Categoria", CategoriaInsumo.values()))); break;
                    case 8: listar(servicio.stockBajo()); break;
                    case 9: historial(); break;
                    case 10: eliminar(); break;
                    default: break;
                }
            });
            Consola.pausa();
        }
    }
    public void listar(List<Insumo> insumos) {
        if (insumos.isEmpty()) System.out.println("No hay insumos para esta consulta.");
        for (Insumo i : insumos) System.out.println(i.getCodigo() + " | " + i.getNombre() + " | " + i.getCategoria() + " | " + i.getExistencias() + " " + i.getUnidadMedida() + " | minimo " + i.getStockMinimo() + (i.esStockBajo() ? " | STOCK BAJO" : ""));
    }
    private void detalle(Insumo i) {
        System.out.println(i.getCodigo() + " | " + i.getNombre() + " | " + i.getCategoria());
        System.out.println("Existencias: " + i.getExistencias() + " " + i.getUnidadMedida() + " | Minimo: " + i.getStockMinimo());
        System.out.println("Movimientos: " + i.getMovimientos().size() + (i.esStockBajo() ? " | STOCK BAJO" : ""));
    }
    private void registrar() {
        // Primero la categoria, porque de ella depende el codigo (T, H, B, C, E u O).
        CategoriaInsumo categoria = Consola.leerEnum("Categoria", CategoriaInsumo.values());
        String codigo = servicio.siguienteCodigo(categoria);
        System.out.println("Codigo asignado: " + codigo);
        String nombre = Consola.leerTexto("Nombre: ");
        String unidad = Consola.leerTexto("Unidad base (cm, m, unidad): ");
        int existencias = Consola.leerEntero("Existencias iniciales: ", 0, Integer.MAX_VALUE);
        int minimo = Consola.leerEntero("Stock minimo: ", 0, Integer.MAX_VALUE);
        servicio.agregar(new Insumo(codigo, nombre, categoria, existencias, minimo, unidad));
        System.out.println("Insumo registrado: " + codigo + ".");
    }
    private void modificar() {
        Insumo i = servicio.buscar(Consola.leerId("Codigo: "));
        detalle(i);
        String nombre = Consola.leerTexto("Nuevo nombre: ");
        int minimo = Consola.leerEntero("Nuevo stock minimo: ", 0, Integer.MAX_VALUE);
        servicio.actualizar(i.getCodigo(), nombre, minimo);
        System.out.println("Insumo actualizado.");
    }
    private void movimiento(boolean entrada) {
        Insumo i = servicio.buscar(Consola.leerId("Codigo del insumo: "));
        detalle(i);
        int cantidad = Consola.leerEntero("Cantidad en " + i.getUnidadMedida() + ": ", 1, Integer.MAX_VALUE);
        String motivo = Consola.leerTexto("Motivo: ");
        if (entrada) servicio.entrada(i.getCodigo(), cantidad, motivo); else servicio.salida(i.getCodigo(), cantidad, motivo);
        System.out.println("Movimiento registrado. Existencias: " + i.getExistencias() + " " + i.getUnidadMedida() + ".");
    }
    private void historial() {
        Insumo i = servicio.buscar(Consola.leerId("Codigo del insumo: "));
        if (i.getMovimientos().isEmpty()) System.out.println("El insumo no tiene movimientos.");
        for (MovimientoInventario m : i.getMovimientos()) System.out.println(m.getFecha() + " | " + m.getTipo() + " | " + m.getCantidad() + " " + i.getUnidadMedida() + " | " + m.getMotivo());
    }
    private void eliminar() {
        Insumo i = servicio.buscar(Consola.leerId("Codigo: "));
        if (Consola.confirmar("Eliminar " + i.getNombre() + "?")) {
            servicio.eliminar(i.getCodigo()); System.out.println("Insumo eliminado.");
        }
    }
}
