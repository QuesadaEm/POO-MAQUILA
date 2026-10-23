package modelo.produccion;

import modelo.personal.Colaborador;
import modelo.personal.Operario;

/**
 * Costura y armado de la prenda. La hace un operario.
 */
public class EtapaConfeccion extends Etapa {

    /** Minutos estimados de trabajo. */
    private int tiempoEstimado;

    public EtapaConfeccion() {
        super("Confeccion", "Costura, armado y etiquetado de la prenda", 2);
        this.tiempoEstimado = 45;
    }

    @Override
    public boolean validarResponsable(Colaborador colaborador) {
        return colaborador instanceof Operario;
    }

    @Override
    public boolean ejecutar(Producto producto) {
        producto.avanzarEtapa();
        return true;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }
}
