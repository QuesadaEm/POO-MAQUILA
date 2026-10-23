package modelo.personal;
import util.Validaciones;
public final class Supervisor extends Colaborador {
    private double salarioBase;
    private double plus;
    public Supervisor(String id, String nombre, Jornada jornada, double salarioBase, double plus) {
        super(id, nombre, jornada, "Supervisor");
        actualizarRemuneracion(salarioBase, plus);
    }
    public void actualizarRemuneracion(double salarioBase, double plus) {
        double nuevosalarioBase = Validaciones.monto(salarioBase, "salarioBase", false);
        double nuevoplus = Validaciones.monto(plus, "plus", true);
        this.salarioBase = nuevosalarioBase;
        this.plus = nuevoplus;
    }
    public double getSalarioBase() { return salarioBase; }
    public double getPlus() { return plus; }
    @Override public double calcularSalario() {
        return Math.round((salarioBase + plus) * 100.0) / 100.0;
    }
}
