package modelo.produccion;

import modelo.personal.Colaborador;
import modelo.personal.Supervisor;

/**
 * Revision final. Solo la hace un supervisor.
 * Si encuentra prendas defectuosas, el producto queda RECHAZADO.
 */
public class EtapaControlCalidad extends Etapa {

    private int prendasDefectuosas;

    public EtapaControlCalidad() {
        super("Control de calidad", "Inspeccion final de las prendas", 3);
        this.prendasDefectuosas = 0;
    }

    @Override
    public boolean validarResponsable(Colaborador colaborador) {
        return colaborador instanceof Supervisor;
    }

    @Override
    public boolean ejecutar(Producto producto) {
        if (prendasDefectuosas > 0) {
            producto.cambiarEstado(EstadoProducto.RECHAZADO);
            return false;
        }
        producto.avanzarEtapa();
        return true;
    }

    /** El supervisor anota cuantas prendas encontro con defectos antes de ejecutar la etapa. */
    public void registrarInspeccion(int prendasDefectuosas) {
        if (prendasDefectuosas < 0) {
            throw new IllegalArgumentException("Las prendas defectuosas no pueden ser negativas.");
        }
        this.prendasDefectuosas = prendasDefectuosas;
    }

    public int getPrendasDefectuosas() {
        return prendasDefectuosas;
    }
}
