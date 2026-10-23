package modelo.personal;
import util.Validaciones;
public final class Administrativo extends Colaborador {
    private double salarioFijo;
    public Administrativo(String id, String nombre, Jornada jornada, double salarioFijo) {
        super(id, nombre, jornada, "Administrativo");
        actualizarRemuneracion(salarioFijo);
    }
    public void actualizarRemuneracion(double salarioFijo) {
        double nuevosalarioFijo = Validaciones.monto(salarioFijo, "salarioFijo", false);
        this.salarioFijo = nuevosalarioFijo;
    }
    public double getSalarioFijo() { return salarioFijo; }
    @Override public double calcularSalario() {
        return Math.round((salarioFijo) * 100.0) / 100.0;
    }
}
