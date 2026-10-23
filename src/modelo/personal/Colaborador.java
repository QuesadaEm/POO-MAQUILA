package modelo.personal;
import util.Validaciones;
/** Identidad y condiciones comunes de los colaboradores. */
public abstract class Colaborador {
    private final String id;
    private String nombre;
    private Jornada jornada;
    private final String categoria;
    protected Colaborador(String id, String nombre, Jornada jornada, String categoria) {
        this.id = Validaciones.id(id);
        this.nombre = Validaciones.texto(nombre, "Nombre", 80);
        this.jornada = Validaciones.requerido(jornada, "Jornada");
        this.categoria = categoria;
    }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Jornada getJornada() { return jornada; }
    public String getCategoria() { return categoria; }
    public final void actualizarDatos(String nombre, Jornada jornada) {
        String nuevoNombre = Validaciones.texto(nombre, "Nombre", 80);
        Validaciones.requerido(jornada, "Jornada");
        this.nombre = nuevoNombre;
        this.jornada = jornada;
    }
    /** Remuneracion bruta de una jornada, expresada en colones. */
    public abstract double calcularSalario();
}
