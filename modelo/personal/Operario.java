// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.personal;

public class Operario extends Colaborador {

    private double salarioHora;
    private double bono;

    public Operario(String id, String nombre, Jornada jornada, double salarioHora, double bono) {
        super(id, nombre, jornada);
        this.salarioHora = salarioHora;
        this.bono = bono;
    }

    @Override
    public double calcularSalario() {
        return salarioHora * getJornada().getHorasMaximas() * 26 + bono;
    }
}
