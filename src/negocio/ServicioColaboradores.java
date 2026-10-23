package negocio;
import java.util.*;
import datos.*;
import modelo.personal.*;
import modelo.produccion.*;
import excepciones.NegocioException;
import util.Validaciones;
public final class ServicioColaboradores {
    private final Repositorio<Colaborador> colaboradores;
    private final Repositorio<Producto> productos;
    public ServicioColaboradores(Repositorio<Colaborador> colaboradores, Repositorio<Producto> productos) {
        this.colaboradores = colaboradores; this.productos = productos;
    }
    public void agregar(Colaborador colaborador) { colaboradores.agregar(colaborador); }
    /**
     * Siguiente id libre para un tipo de colaborador: OP para operarios, SU para supervisores
     * y AD para administrativos (por ejemplo OP05). Lo asigna el sistema para evitar errores de digitacion.
     */
    public String siguienteId(String prefijo) {
        int mayor = 0;
        for (Colaborador c : listar()) {
            String id = c.getId();
            if (id.startsWith(prefijo) && id.substring(prefijo.length()).matches("\\d+")) {
                mayor = Math.max(mayor, Integer.parseInt(id.substring(prefijo.length())));
            }
        }
        return String.format("%s%02d", prefijo, mayor + 1);
    }
    public Colaborador buscar(String id) { return colaboradores.buscarPorId(id); }
    public List<Colaborador> listar() { return colaboradores.listar(); }
    /** Se valida una copia antes de modificar el objeto que ya tiene referencias. */
    public void actualizar(String id, String nombre, Jornada jornada, double pago, double complemento) {
        Colaborador actual = buscar(id);
        if (actual instanceof Operario) {
            Operario nuevo = new Operario(actual.getId(), nombre, jornada, pago, complemento);
            actual.actualizarDatos(nuevo.getNombre(), nuevo.getJornada());
            ((Operario) actual).actualizarRemuneracion(nuevo.getSalarioHora(), nuevo.getBono());
        } else if (actual instanceof Supervisor) {
            Supervisor nuevo = new Supervisor(actual.getId(), nombre, jornada, pago, complemento);
            actual.actualizarDatos(nuevo.getNombre(), nuevo.getJornada());
            ((Supervisor) actual).actualizarRemuneracion(nuevo.getSalarioBase(), nuevo.getPlus());
        } else {
            Validaciones.monto(complemento, "Complemento", true);
            if (complemento != 0) throw new NegocioException("El administrativo tiene unicamente salario fijo.");
            Administrativo nuevo = new Administrativo(actual.getId(), nombre, jornada, pago);
            actual.actualizarDatos(nuevo.getNombre(), nuevo.getJornada());
            ((Administrativo) actual).actualizarRemuneracion(nuevo.getSalarioFijo());
        }
    }
    public void eliminar(String id) {
        Colaborador colaborador = buscar(id);
        for (Producto producto : productos.listar()) {
            // Integracion: en el diagrama aprobado el historial esta en cada Etapa (RegistroAvance).
            for (Etapa etapa : producto.getEtapas()) {
                for (RegistroAvance registro : etapa.getRegistros()) {
                    if (registro.getResponsable() == colaborador) throw new NegocioException("No se puede eliminar: figura en el historial de " + producto.getId() + ".");
                }
            }
        }
        colaboradores.eliminar(colaborador.getId());
    }
    public double totalPlanilla() {
        double total = 0;
        // La misma llamada resuelve el calculo propio de cada categoria.
        for (Colaborador colaborador : listar()) total += colaborador.calcularSalario();
        return Math.round(total * 100.0) / 100.0;
    }
}
