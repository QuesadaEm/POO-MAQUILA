package modelo.personal;
import util.Validaciones;
public final class Operario extends Colaborador {
    private double salarioHora;
    private double bono;
    public Operario(String id, String nombre, Jornada jornada, double salarioHora, double bono) {
        super(id, nombre, jornada, "Operario");
        actualizarRemuneracion(salarioHora, bono);
    }
    public void actualizarRemuneracion(double salarioHora, double bono) {
        double nuevosalarioHora = Validaciones.monto(salarioHora, "salarioHora", false);
        double nuevobono = Validaciones.monto(bono, "bono", true);
        this.salarioHora = nuevosalarioHora;
        this.bono = nuevobono;
    }
    public double getSalarioHora() { return salarioHora; }
    public double getBono() { return bono; }
    @Override public double calcularSalario() {
        return Math.round((salarioHora * getJornada().getHorasMaximas() + bono) * 100.0) / 100.0;
    }
}
