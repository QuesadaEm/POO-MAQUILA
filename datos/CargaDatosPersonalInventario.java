// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package datos;

import modelo.inventario.CategoriaInsumo;
import modelo.inventario.Insumo;
import modelo.personal.Administrativo;
import modelo.personal.Jornada;
import modelo.personal.Operario;
import modelo.personal.Supervisor;
import negocio.ServicioColaboradores;
import negocio.ServicioInventario;

public class CargaDatosPersonalInventario {

    public static void cargar(ServicioColaboradores colaboradores, ServicioInventario inventario) {
        colaboradores.registrar(new Operario("C001", "Ana Rojas", Jornada.DIURNA, 1800, 15000));
        colaboradores.registrar(new Operario("C002", "Luis Mora", Jornada.DIURNA, 1800, 10000));
        colaboradores.registrar(new Operario("C003", "Karla Vargas", Jornada.MIXTA, 1850, 12000));
        colaboradores.registrar(new Supervisor("C004", "Jorge Solis", Jornada.DIURNA, 650000, 80000));
        colaboradores.registrar(new Supervisor("C005", "Maria Jimenez", Jornada.NOCTURNA, 650000, 95000));
        colaboradores.registrar(new Administrativo("C006", "Sofia Castro", Jornada.DIURNA, 550000));
        colaboradores.registrar(new Administrativo("C007", "Pedro Araya", Jornada.DIURNA, 520000));
        colaboradores.registrar(new Operario("C008", "Diego Quesada", Jornada.NOCTURNA, 1900, 8000));

        inventario.registrar(new Insumo("INS01", "Tela de algodon", CategoriaInsumo.TELA, 60, 10, "m"));
        inventario.registrar(new Insumo("INS02", "Hilo de poliester", CategoriaInsumo.HILO, 2000, 300, "m"));
        inventario.registrar(new Insumo("INS03", "Boton de 4 huecos", CategoriaInsumo.BOTON, 200, 40, "unid"));
        inventario.registrar(new Insumo("INS04", "Etiqueta de marca", CategoriaInsumo.ETIQUETA, 50, 10, "unid"));
        inventario.registrar(new Insumo("INS05", "Bolsa de empaque", CategoriaInsumo.OTRO, 50, 10, "unid"));
        inventario.registrar(new Insumo("INS06", "Cierre metalico", CategoriaInsumo.CIERRE, 30, 5, "unid"));
    }
}
