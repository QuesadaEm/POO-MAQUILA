// PROVISIONAL (Persona A): version minima para que compile la parte de Produccion. Reemplazar por la de tu companero.
package modelo.personal;

public enum Jornada {
    DIURNA(8),
    MIXTA(7),
    NOCTURNA(6);

    private final int horasMaximas;

    Jornada(int horasMaximas) {
        this.horasMaximas = horasMaximas;
    }

    public int getHorasMaximas() {
        return horasMaximas;
    }
}
