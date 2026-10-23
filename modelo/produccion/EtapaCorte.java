package modelo.produccion;

import modelo.personal.Colaborador;
import modelo.personal.Operario;

/**
 * Corte de las piezas de tela. La hace un operario y desperdicia
 * un porcentaje de la tela, por eso consume mas de lo que pide la prenda.
 */
public class EtapaCorte extends Etapa {

    private double porcentajeDesperdicio;

    public EtapaCorte() {
        super("Corte", "Corte de las piezas de tela segun el patron", 1);
        this.porcentajeDesperdicio = 0.10;
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

    @Override
    public int calcularConsumo(RequerimientoInsumo requerimiento) {
        // Se redondea hacia arriba: no se puede pedir media unidad de tela.
        return (int) Math.ceil(requerimiento.getCantidadPorPrenda() * (1 + porcentajeDesperdicio));
    }

    public double getPorcentajeDesperdicio() {
        return porcentajeDesperdicio;
    }
}
