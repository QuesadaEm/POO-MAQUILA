// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.personal;

public class Supervisor extends Colaborador {

    private double salarioBase;
    private double plus;

    public Supervisor(String id, String nombre, Jornada jornada, double salarioBase, double plus) {
        super(id, nombre, jornada);
        this.salarioBase = salarioBase;
        this.plus = plus;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + plus;
    }
}
