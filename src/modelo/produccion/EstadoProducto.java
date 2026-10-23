package modelo.produccion;

/**
 * Estados por los que pasa un producto en la linea de produccion.
 */
public enum EstadoProducto {
    PENDIENTE,
    EN_PROCESO,
    RECHAZADO,
    TERMINADO
}
