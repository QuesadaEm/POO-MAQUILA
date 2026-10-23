package modelo.produccion;

import modelo.personal.Colaborador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Historial: quien ejecuto una etapa, cuando y con que resultado.
 */
public class RegistroAvance {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String id;
    private Colaborador responsable;
    private String resultado;
    private LocalDateTime fecha;
    private String observaciones;

    public RegistroAvance(String id, Colaborador responsable, String resultado,
                          LocalDateTime fecha, String observaciones) {
        if (responsable == null) {
            throw new IllegalArgumentException("El registro necesita un responsable.");
        }
        this.id = id;
        this.responsable = responsable;
        this.resultado = resultado;
        this.fecha = fecha;
        this.observaciones = observaciones == null ? "" : observaciones;
    }

    public String getId() {
        return id;
    }

    public Colaborador getResponsable() {
        return responsable;
    }

    public String getResultado() {
        return resultado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    @Override
    public String toString() {
        String texto = id + "  " + fecha.format(FORMATO) + "  " + resultado
                + "  por " + responsable.getNombre() + " (" + responsable.getId() + ")";
        return observaciones.isEmpty() ? texto : texto + "  - " + observaciones;
    }
}
