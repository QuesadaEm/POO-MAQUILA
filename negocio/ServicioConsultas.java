package negocio;

import excepciones.ElementoNoEncontradoException;
import modelo.inventario.Insumo;
import modelo.personal.Colaborador;
import modelo.produccion.Etapa;
import modelo.produccion.EstadoProducto;
import modelo.produccion.Producto;
import modelo.produccion.RegistroAvance;
import modelo.produccion.RequerimientoInsumo;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consultas que cruzan informacion de Personal, Inventario y Produccion.
 */
public class ServicioConsultas {

    private final ServicioProduccion servicioProduccion;
    private final ServicioColaboradores servicioColaboradores;

    public ServicioConsultas(ServicioProduccion servicioProduccion, ServicioColaboradores servicioColaboradores) {
        this.servicioProduccion = servicioProduccion;
        this.servicioColaboradores = servicioColaboradores;
    }

    /** Productos en los que el colaborador ha trabajado alguna etapa. */
    public List<Producto> productosPorColaborador(String idColaborador) throws ElementoNoEncontradoException {
        Colaborador colaborador = servicioColaboradores.buscar(idColaborador);
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : servicioProduccion.listar()) {
            if (trabajoEnProducto(colaborador, producto)) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    /** Total de cada insumo que se ha gastado en un producto, segun su historial. */
    public Map<Insumo, Integer> insumosConsumidosPorProducto(String idProducto) throws ElementoNoEncontradoException {
        Producto producto = servicioProduccion.buscar(idProducto);
        Map<Insumo, Integer> consumo = new LinkedHashMap<>();
        for (Etapa etapa : producto.getEtapas()) {
            int vecesEjecutada = etapa.getRegistros().size();
            for (RequerimientoInsumo r : etapa.getRequerimientos()) {
                int cantidad = etapa.calcularConsumo(r) * vecesEjecutada;
                if (cantidad > 0) {
                    consumo.merge(r.getInsumo(), cantidad, Integer::sum);
                }
            }
        }
        return consumo;
    }

    /** Productos sin terminar cuya etapa actual no se puede ejecutar por falta de insumos. */
    public Map<Producto, List<String>> productosDetenidosPorFaltaDeStock() {
        Map<Producto, List<String>> resultado = new LinkedHashMap<>();
        for (Producto producto : servicioProduccion.listar()) {
            boolean sigueActivo = producto.getEstado() == EstadoProducto.PENDIENTE
                    || producto.getEstado() == EstadoProducto.EN_PROCESO;
            if (sigueActivo) {
                List<String> faltantes = servicioProduccion.calcularFaltantes(producto.getEtapaActual());
                if (!faltantes.isEmpty()) {
                    resultado.put(producto, faltantes);
                }
            }
        }
        return resultado;
    }

    public Map<EstadoProducto, Integer> resumenPorEstado() {
        Map<EstadoProducto, Integer> resumen = new EnumMap<>(EstadoProducto.class);
        for (EstadoProducto estado : EstadoProducto.values()) {
            resumen.put(estado, 0);
        }
        for (Producto producto : servicioProduccion.listar()) {
            resumen.merge(producto.getEstado(), 1, Integer::sum);
        }
        return resumen;
    }

    private boolean trabajoEnProducto(Colaborador colaborador, Producto producto) {
        for (Etapa etapa : producto.getEtapas()) {
            for (RegistroAvance registro : etapa.getRegistros()) {
                if (registro.getResponsable().getId().equals(colaborador.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
