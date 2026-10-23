package modelo.personal;
/** Horas por jornada utilizadas en la simulacion de remuneraciones. */
public enum Jornada {
    DIURNA(8), MIXTA(7), NOCTURNA(6);
    private final int horasMaximas;
    Jornada(int horasMaximas) { this.horasMaximas = horasMaximas; }
    public int getHorasMaximas() { return horasMaximas; }
}
