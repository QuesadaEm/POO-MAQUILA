package negocio;

import datos.RepositorioProductos;
import excepciones.ElementoNoEncontradoException;
import excepciones.ExistenciasInsuficientesException;
import excepciones.NegocioException;
import excepciones.TransicionInvalidaException;
import modelo.inventario.CategoriaInsumo;
import modelo.inventario.Insumo;
import modelo.personal.Colaborador;
import modelo.produccion.Etapa;
import modelo.produccion.EtapaConfeccion;
import modelo.produccion.EtapaControlCalidad;
import modelo.produccion.EtapaCorte;
import modelo.produccion.EstadoProducto;
import modelo.produccion.Producto;
import modelo.produccion.RegistroAvance;
import modelo.produccion.RequerimientoInsumo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reglas de negocio de produccion. Aqui esta la operacion que conecta
 * los tres modulos: avanzarProducto (Personal + Inventario + Produccion).
 */
public class ServicioProduccion {

    // Codigos de insumo de la carga inicial de Persona A. La tela se elige al crear el producto.
    public static final String HILO = "H01";
    public static final String BOTON = "B01";
    public static final String ETIQUETA = "E01";
    public static final String BOLSA = "O01";

    private final RepositorioProductos repositorio;
    private final ServicioColaboradores servicioColaboradores;
    private final ServicioInventario servicioInventario;
    private int contadorRegistros;
    private int contadorProductos;

    public ServicioProduccion(RepositorioProductos repositorio,
                              ServicioColaboradores servicioColaboradores,
                              ServicioInventario servicioInventario) {
        this.repositorio = repositorio;
        this.servicioColaboradores = servicioColaboradores;
        this.servicioInventario = servicioInventario;
        this.contadorRegistros = 0;
        this.contadorProductos = 0;
    }

    /**
     * Crea un producto con sus tres etapas y lo que consume cada una:
     * corte usa la tela elegida (en cm), confeccion usa hilo, botones, una etiqueta y una bolsa,
     * y control de calidad no consume insumos.
     * El id lo asigna el sistema (P001, P002, ...) para evitar errores de digitacion.
     */
    public Producto crearProducto(String nombre, String descripcion, String codigoTela,
                                  int centimetrosTela, int metrosHilo, int botones) throws NegocioException {
        if (centimetrosTela <= 0 || metrosHilo <= 0) {
            throw new NegocioException("La tela y el hilo deben ser mayores que cero.");
        }
        if (botones < 0) {
            throw new NegocioException("La cantidad de botones no puede ser negativa.");
        }

        Producto producto;
        try {
            producto = new Producto(String.format("P%03d", contadorProductos + 1), nombre, descripcion);
        } catch (IllegalArgumentException e) {
            throw new NegocioException(e.getMessage());
        }

        Insumo tela = servicioInventario.buscar(codigoTela);
        if (tela.getCategoria() != CategoriaInsumo.TELA) {
            throw new NegocioException(tela.getCodigo() + " (" + tela.getNombre() + ") no es una tela.");
        }

        Etapa corte = new EtapaCorte();
        corte.agregarRequerimiento(new RequerimientoInsumo(tela, centimetrosTela));

        Etapa confeccion = new EtapaConfeccion();
        confeccion.agregarRequerimiento(new RequerimientoInsumo(servicioInventario.buscar(HILO), metrosHilo));
        if (botones > 0) {
            confeccion.agregarRequerimiento(new RequerimientoInsumo(servicioInventario.buscar(BOTON), botones));
        }
        confeccion.agregarRequerimiento(new RequerimientoInsumo(servicioInventario.buscar(ETIQUETA), 1));
        confeccion.agregarRequerimiento(new RequerimientoInsumo(servicioInventario.buscar(BOLSA), 1));

        producto.agregarEtapa(corte);
        producto.agregarEtapa(confeccion);
        producto.agregarEtapa(new EtapaControlCalidad());

        repositorio.agregar(producto);
        contadorProductos++;
        return producto;
    }

    /** Telas disponibles para elegir al crear un producto. */
    public List<Insumo> listarTelas() {
        return servicioInventario.porCategoria(CategoriaInsumo.TELA);
    }

    public Producto buscar(String id) throws ElementoNoEncontradoException {
        return repositorio.buscarPorId(id);
    }

    public List<Producto> listar() {
        return repositorio.listar();
    }

    public List<Producto> listarPorEstado(EstadoProducto estado) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : repositorio.listar()) {
            if (p.getEstado() == estado) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /** El supervisor anota las prendas defectuosas antes de cerrar el control de calidad. */
    public void registrarInspeccion(String idProducto, int prendasDefectuosas) throws NegocioException {
        Producto producto = buscar(idProducto);
        validarQueSigaEnProceso(producto);
        Etapa etapa = producto.getEtapaActual();
        if (!(etapa instanceof EtapaControlCalidad)) {
            throw new TransicionInvalidaException("El producto " + producto.getId()
                    + " todavia no esta en control de calidad (esta en " + etapa.getNombre() + ").");
        }
        if (prendasDefectuosas < 0) {
            throw new NegocioException("Las prendas defectuosas no pueden ser negativas.");
        }
        ((EtapaControlCalidad) etapa).registrarInspeccion(prendasDefectuosas);
    }

    /**
     * Operacion que conecta los modulos:
     * 1. valida al responsable con el modulo de Personal,
     * 2. verifica y descuenta los insumos con el modulo de Inventario (todo o nada),
     * 3. ejecuta la etapa (cada una hace algo distinto),
     * 4. guarda el avance en el historial de la etapa.
     */
    public RegistroAvance avanzarProducto(String idProducto, String idColaborador, String observaciones)
            throws NegocioException {
        Producto producto = buscar(idProducto);
        validarQueSigaEnProceso(producto);
        Etapa etapa = producto.getEtapaActual();

        // 1. Personal
        Colaborador responsable = servicioColaboradores.buscar(idColaborador);
        if (!etapa.validarResponsable(responsable)) {
            throw new NegocioException(responsable.getNombre() + " (" + responsable.getCategoria()
                    + ") no puede encargarse de la etapa " + etapa.getNombre() + ".");
        }

        // 2. Inventario: primero se revisa todo y solo si alcanza se descuenta.
        List<String> faltantes = calcularFaltantes(etapa);
        if (!faltantes.isEmpty()) {
            throw new ExistenciasInsuficientesException("No se puede ejecutar " + etapa.getNombre()
                    + ". Falta: " + String.join(", ", faltantes));
        }
        Map<String, Integer> consumo = new LinkedHashMap<>();
        for (RequerimientoInsumo r : etapa.getRequerimientos()) {
            consumo.put(r.getInsumo().getCodigo(), etapa.calcularConsumo(r));
        }
        if (!consumo.isEmpty()) {
            // Inventario registra la salida con el id del producto y la etapa en el motivo.
            servicioInventario.consumirLote(consumo, producto.getId(), etapa.getNombre());
        }

        // 3. Produccion
        if (producto.getEstado() == EstadoProducto.PENDIENTE) {
            producto.cambiarEstado(EstadoProducto.EN_PROCESO);
        }
        boolean aprobado = etapa.ejecutar(producto);

        // 4. Historial
        contadorRegistros++;
        RegistroAvance registro = new RegistroAvance(String.format("AV%03d", contadorRegistros), responsable,
                etapa.getNombre() + ": " + (aprobado ? "APROBADO" : "RECHAZADO"),
                LocalDateTime.now(), observaciones);
        etapa.agregarRegistro(registro);
        return registro;
    }

    /** Solo se puede eliminar un producto que todavia no ha empezado. */
    public void eliminarProducto(String id) throws NegocioException {
        Producto producto = buscar(id);
        if (producto.getEstado() != EstadoProducto.PENDIENTE) {
            throw new TransicionInvalidaException("Solo se pueden eliminar productos PENDIENTES. "
                    + producto.getId() + " esta " + producto.getEstado() + ".");
        }
        repositorio.eliminar(producto.getId());
    }

    /** Lista lo que falta para ejecutar la etapa, por ejemplo "Tela de algodon (necesita 88, hay 60)". */
    public List<String> calcularFaltantes(Etapa etapa) {
        List<String> faltantes = new ArrayList<>();
        for (RequerimientoInsumo r : etapa.getRequerimientos()) {
            int necesario = etapa.calcularConsumo(r);
            if (!servicioInventario.hayExistencias(r.getInsumo().getCodigo(), necesario)) {
                faltantes.add(r.getInsumo().getNombre() + " (necesita " + necesario
                        + ", hay " + r.getInsumo().getExistencias() + ")");
            }
        }
        return faltantes;
    }

    private void validarQueSigaEnProceso(Producto producto) throws TransicionInvalidaException {
        if (producto.getEstado() == EstadoProducto.TERMINADO || producto.getEstado() == EstadoProducto.RECHAZADO) {
            throw new TransicionInvalidaException("El producto " + producto.getId() + " esta "
                    + producto.getEstado() + " y ya no puede avanzar.");
        }
    }
}
