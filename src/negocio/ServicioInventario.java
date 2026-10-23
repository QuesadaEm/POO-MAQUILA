package negocio;
import java.util.*;
import datos.*;
import excepciones.*;
import modelo.inventario.*;
import modelo.produccion.*;
import util.Validaciones;
public final class ServicioInventario {
    private final Repositorio<Insumo> insumos;
    private final Repositorio<Producto> productos;
    public ServicioInventario(Repositorio<Insumo> insumos, Repositorio<Producto> productos) {
        this.insumos = insumos; this.productos = productos;
    }
    public void agregar(Insumo insumo) { insumos.agregar(insumo); }
    /**
     * Siguiente codigo libre segun la categoria: T tela, H hilo, B boton, C cierre,
     * E etiqueta y O otro (por ejemplo T04). Lo asigna el sistema para evitar errores de digitacion.
     */
    public String siguienteCodigo(CategoriaInsumo categoria) {
        Validaciones.requerido(categoria, "Categoria");
        String prefijo = categoria.name().substring(0, 1);
        int mayor = 0;
        for (Insumo i : listar()) {
            String codigo = i.getCodigo();
            if (codigo.startsWith(prefijo) && codigo.substring(1).matches("\\d+")) {
                mayor = Math.max(mayor, Integer.parseInt(codigo.substring(1)));
            }
        }
        return String.format("%s%02d", prefijo, mayor + 1);
    }
    public Insumo buscar(String codigo) { return insumos.buscarPorId(codigo); }
    public List<Insumo> listar() { return insumos.listar(); }
    public List<Insumo> porCategoria(CategoriaInsumo categoria) {
        Validaciones.requerido(categoria, "Categoria");
        List<Insumo> encontrados = new ArrayList<>();
        for (Insumo insumo : listar()) if (insumo.getCategoria() == categoria) encontrados.add(insumo);
        return Collections.unmodifiableList(encontrados);
    }
    public List<Insumo> stockBajo() {
        List<Insumo> encontrados = new ArrayList<>();
        for (Insumo insumo : listar()) if (insumo.esStockBajo()) encontrados.add(insumo);
        return Collections.unmodifiableList(encontrados);
    }
    public void actualizar(String codigo, String nombre, int minimo) { buscar(codigo).actualizar(nombre, minimo); }
    public void entrada(String codigo, int cantidad, String motivo) { buscar(codigo).agregarExistencias(cantidad, motivo); }
    public void salida(String codigo, int cantidad, String motivo) { buscar(codigo).retirarExistencias(cantidad, motivo, ""); }
    public boolean hayExistencias(String codigo, int cantidad) {
        Validaciones.positivo(cantidad, "Cantidad");
        return buscar(codigo).getExistencias() >= cantidad;
    }
    public void consumir(String codigo, int cantidad) { salida(codigo, cantidad, "Consumo manual"); }
    /** Primero verifica el lote completo; una falta de stock no deja consumos parciales. */
    public void consumirLote(Map<String, Integer> cantidades, String productoId, String etapa) {
        String id = Validaciones.id(productoId);
        String nombreEtapa = Validaciones.texto(etapa, "Etapa", 80);
        String motivo = "Produccion " + id + " / " + nombreEtapa;
        Map<Insumo, Integer> lote = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> item : Validaciones.requerido(cantidades, "Cantidades").entrySet()) {
            Insumo insumo = buscar(item.getKey());
            int cantidad = Validaciones.positivo(Validaciones.requerido(item.getValue(), "Cantidad"), "Cantidad");
            long acumulado = (long) lote.getOrDefault(insumo, 0) + cantidad;
            if (acumulado > Integer.MAX_VALUE) throw new NegocioException("Consumo fuera de rango.");
            lote.put(insumo, (int) acumulado);
        }
        for (Map.Entry<Insumo, Integer> item : lote.entrySet()) {
            if (item.getKey().getExistencias() < item.getValue())
                throw new ExistenciasInsuficientesException("Stock insuficiente de " + item.getKey().getCodigo() + ": disponible " + item.getKey().getExistencias() + ", requerido " + item.getValue() + ". No se descontaron insumos.");
        }
        for (Map.Entry<Insumo, Integer> item : lote.entrySet()) item.getKey().retirarExistencias(item.getValue(), motivo, id);
    }
    public void eliminar(String codigo) {
        Insumo insumo = buscar(codigo);
        for (Producto producto : productos.listar()) for (Etapa etapa : producto.getEtapas()) {
            for (RequerimientoInsumo requerimiento : etapa.getRequerimientos()) {
                if (requerimiento.getInsumo() == insumo) throw new NegocioException("El insumo forma parte de la receta de " + producto.getId() + ".");
            }
        }
        if (!insumo.getMovimientos().isEmpty()) throw new NegocioException("No se elimina un insumo con movimientos historicos.");
        insumos.eliminar(insumo.getCodigo());
    }
}
