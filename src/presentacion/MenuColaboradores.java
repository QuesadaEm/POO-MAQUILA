package presentacion;
import negocio.ServicioColaboradores;
import modelo.personal.*;
import util.Consola;
public final class MenuColaboradores {
    private final ServicioColaboradores servicio;
    public MenuColaboradores(ServicioColaboradores servicio) { this.servicio = servicio; }
    public void mostrar() {
        while (true) {
            Consola.limpiarPantalla();
            Consola.titulo("PERSONAL");
            System.out.println("1. Listar colaboradores\n2. Consultar colaborador\n3. Registrar colaborador\n4. Modificar colaborador\n5. Eliminar colaborador\n6. Planilla por jornada\n0. Volver");
            int opcion = Consola.leerEntero("Opcion: ", 0, 6);
            if (opcion == 0) return;
            Consola.accion(() -> {
                switch (opcion) {
                    case 1: listar(); break;
                    case 2: detalle(servicio.buscar(Consola.leerId("Id del colaborador: "))); break;
                    case 3: registrar(); break;
                    case 4: modificar(); break;
                    case 5: eliminar(); break;
                    case 6: planilla(); break;
                    default: break;
                }
            });
            Consola.pausa();
        }
    }
    public void listar() {
        if (servicio.listar().isEmpty()) System.out.println("No hay colaboradores registrados.");
        for (Colaborador c : servicio.listar()) System.out.println(c.getId() + " | " + c.getNombre() + " | " + c.getCategoria() + " | " + c.getJornada());
    }
    private void detalle(Colaborador c) {
        System.out.println(c.getId() + " | " + c.getNombre() + " | " + c.getCategoria());
        System.out.println("Jornada: " + c.getJornada() + " / " + c.getJornada().getHorasMaximas() + " horas");
        if (c instanceof Operario) {
            Operario o = (Operario) c;
            System.out.println("Por hora: " + Consola.dinero(o.getSalarioHora()) + " | Bono: " + Consola.dinero(o.getBono()));
        } else if (c instanceof Supervisor) {
            Supervisor s = (Supervisor) c;
            System.out.println("Base por jornada: " + Consola.dinero(s.getSalarioBase()) + " | Plus: " + Consola.dinero(s.getPlus()));
        } else System.out.println("Fijo por jornada: " + Consola.dinero(((Administrativo) c).getSalarioFijo()));
        System.out.println("Remuneracion por jornada: " + Consola.dinero(c.calcularSalario()));
    }
    private void registrar() {
        // Primero el tipo, porque de el depende el id (OP, SU o AD) y los montos que se piden.
        System.out.println("1. Operario\n2. Supervisor\n3. Administrativo");
        int tipo = Consola.leerEntero("Categoria: ", 1, 3);
        String id = servicio.siguienteId(tipo == 1 ? "OP" : tipo == 2 ? "SU" : "AD");
        System.out.println("Id asignado: " + id);
        String nombre = Consola.leerTexto("Nombre: ");
        Jornada jornada = Consola.leerEnum("Jornada", Jornada.values());
        Colaborador colaborador;
        if (tipo == 1) colaborador = new Operario(id, nombre, jornada,
                Consola.leerDecimal("Pago por hora: ", 0.01, 1000000000), Consola.leerDecimal("Bono por jornada: "));
        else if (tipo == 2) colaborador = new Supervisor(id, nombre, jornada,
                Consola.leerDecimal("Base por jornada: ", 0.01, 1000000000), Consola.leerDecimal("Plus por jornada: "));
        else colaborador = new Administrativo(id, nombre, jornada, Consola.leerDecimal("Salario fijo por jornada: ", 0.01, 1000000000));
        servicio.agregar(colaborador);
        System.out.println("Colaborador registrado: " + id + ".");
    }
    private void modificar() {
        Colaborador actual = servicio.buscar(Consola.leerId("Id del colaborador: "));
        detalle(actual);
        String nombre = Consola.leerTexto("Nuevo nombre: ");
        Jornada jornada = Consola.leerEnum("Nueva jornada", Jornada.values());
        double pago = Consola.leerDecimal(actual instanceof Operario ? "Nuevo pago por hora: " : "Nuevo salario por jornada: ", 0.01, 1000000000);
        double complemento = actual instanceof Administrativo ? 0 : Consola.leerDecimal(actual instanceof Operario ? "Nuevo bono: " : "Nuevo plus: ");
        servicio.actualizar(actual.getId(), nombre, jornada, pago, complemento);
        System.out.println("Colaborador actualizado.");
    }
    private void eliminar() {
        Colaborador c = servicio.buscar(Consola.leerId("Id del colaborador: "));
        if (Consola.confirmar("Eliminar " + c.getNombre() + "?")) {
            servicio.eliminar(c.getId()); System.out.println("Colaborador eliminado.");
        }
    }
    private void planilla() {
        Consola.titulo("REMUNERACION BRUTA POR UNA JORNADA");
        for (Colaborador c : servicio.listar()) System.out.println(c.getId() + " | " + c.getNombre() + " | " + c.getCategoria() + " | " + Consola.dinero(c.calcularSalario()));
        System.out.println("Total de la jornada: " + Consola.dinero(servicio.totalPlanilla()));
    }
}
