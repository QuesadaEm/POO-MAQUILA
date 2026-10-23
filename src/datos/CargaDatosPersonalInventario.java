package datos;
import modelo.personal.*;
import modelo.inventario.*;
import negocio.*;
public final class CargaDatosPersonalInventario {
    private CargaDatosPersonalInventario() { }
    public static void cargar(ServicioColaboradores personal, ServicioInventario inventario) {
        personal.agregar(new Operario("OP01", "Ana Rojas", Jornada.DIURNA, 2200, 1500));
        personal.agregar(new Operario("OP02", "Luis Vega", Jornada.MIXTA, 2400, 1200));
        personal.agregar(new Operario("OP03", "Maria Solis", Jornada.NOCTURNA, 2800, 2000));
        personal.agregar(new Operario("OP04", "Diego Mora", Jornada.DIURNA, 2100, 1000));
        personal.agregar(new Supervisor("SU01", "Sofia Campos", Jornada.DIURNA, 23000, 3000));
        personal.agregar(new Supervisor("SU02", "Carlos Jimenez", Jornada.MIXTA, 23500, 2500));
        personal.agregar(new Administrativo("AD01", "Elena Castro", Jornada.DIURNA, 21000));
        personal.agregar(new Administrativo("AD02", "Andres Navarro", Jornada.DIURNA, 20500));
        inventario.agregar(new Insumo("T01", "Tela de algodon", CategoriaInsumo.TELA, 3000, 500, "cm"));
        inventario.agregar(new Insumo("T02", "Tela de mezclilla", CategoriaInsumo.TELA, 2500, 400, "cm"));
        inventario.agregar(new Insumo("T03", "Tela impermeable", CategoriaInsumo.TELA, 100, 150, "cm"));
        inventario.agregar(new Insumo("H01", "Hilo blanco", CategoriaInsumo.HILO, 1200, 100, "m"));
        inventario.agregar(new Insumo("H02", "Hilo negro", CategoriaInsumo.HILO, 1000, 100, "m"));
        inventario.agregar(new Insumo("B01", "Boton blanco", CategoriaInsumo.BOTON, 120, 20, "unidad"));
        inventario.agregar(new Insumo("C01", "Cierre metalico", CategoriaInsumo.CIERRE, 50, 10, "unidad"));
        inventario.agregar(new Insumo("E01", "Etiqueta textil", CategoriaInsumo.ETIQUETA, 100, 20, "unidad"));
        inventario.agregar(new Insumo("O01", "Bolsa de empaque", CategoriaInsumo.OTRO, 80, 15, "unidad"));
        inventario.agregar(new Insumo("O02", "Elastico", CategoriaInsumo.OTRO, 500, 80, "cm"));
    }
}
