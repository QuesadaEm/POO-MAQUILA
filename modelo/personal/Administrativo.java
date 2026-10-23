// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.personal;

public class Administrativo extends Colaborador {

    private double salarioFijo;

    public Administrativo(String id, String nombre, Jornada jornada, double salarioFijo) {
        super(id, nombre, jornada);
        this.salarioFijo = salarioFijo;
    }

    @Override
    public double calcularSalario() {
        return salarioFijo;
    }
}
