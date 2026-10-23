// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.personal;

public abstract class Colaborador {

    private String id;
    private String nombre;
    private Jornada jornada;
    private String categoria;

    public Colaborador(String id, String nombre, Jornada jornada) {
        this.id = id;
        this.nombre = nombre;
        this.jornada = jornada;
        this.categoria = getClass().getSimpleName();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public abstract double calcularSalario();
}
