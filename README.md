# POO-MAQUILA

Sistema de gestión para una maquila textil, desarrollado bajo el paradigma de Programación Orientada a Objetos (POO) en Java.

---

## División de Trabajo

| Integrante | Áreas Asignadas | Responsabilidad principal |
| :--- | :--- | :--- |
| **Persona A** | Personal + Inventario | Módulos base de colaboradores, salarios y control de stock. |
| **Persona B** | Producción + Main | Módulo de producción, etapas del proceso, consultas cruzadas, menú principal e integración. |

---

## Cómo abrir y ejecutar

Es un proyecto de **NetBeans (Ant)**: `File > Open Project`, elegir la carpeta `POO-MAQUILA` y ejecutar con **Run (F6)**.
La clase principal es `Main` y el proyecto está configurado para Java 11 con codificación UTF-8.

---

## Estructura del Proyecto

```text
POO-MAQUILA/
├── build.xml, manifest.mf, nbproject/       Archivos del proyecto NetBeans
├── test/                                    Test Packages
└── src/
    ├── Main.java                                [B]
    ├── modelo/
    │   ├── personal/
    │   │   ├── Colaborador.java                 [A] Abstracta
    │   │   ├── Operario.java                    [A] extends Colaborador
    │   │   ├── Supervisor.java                  [A] extends Colaborador
    │   │   ├── Administrativo.java              [A] extends Colaborador
    │   │   └── Jornada.java                     [A] Enum
    │   ├── inventario/
    │   │   ├── Insumo.java                      [A]
    │   │   ├── CategoriaInsumo.java             [A] Enum
    │   │   ├── MovimientoInventario.java        [A] Entrada / Salida
    │   │   └── TipoMovimiento.java              [A] Enum
    │   └── produccion/
    │       ├── Producto.java                    [B] Compuesto por sus etapas
    │       ├── EstadoProducto.java              [B] Enum
    │       ├── Etapa.java                       [B] Abstracta
    │       ├── EtapaCorte.java                  [B]
    │       ├── EtapaConfeccion.java             [B]
    │       ├── EtapaControlCalidad.java         [B]
    │       ├── RequerimientoInsumo.java         [B] Insumo + Cantidad por prenda
    │       └── RegistroAvance.java              [B] Historial
    ├── datos/
    │   ├── Repositorio.java                     [AMBOS] Interfaz genérica
    │   ├── RepositorioColaboradores.java        [A]
    │   ├── RepositorioInsumos.java              [A]
    │   ├── RepositorioProductos.java            [B]
    │   ├── CargaDatosPersonalInventario.java    [A]
    │   └── CargaDatosProduccion.java            [B]
    ├── negocio/
    │   ├── ServicioColaboradores.java           [A]
    │   ├── ServicioInventario.java              [A]
    │   ├── ServicioProduccion.java              [B]
    │   └── ServicioConsultas.java               [B] Consultas cruzadas
    ├── excepciones/
    │   ├── NegocioException.java                [AMBOS] Base
    │   ├── ElementoNoEncontradoException.java   [AMBOS]
    │   ├── ExistenciasInsuficientesException.java [A]
    │   └── TransicionInvalidaException.java     [B]
    ├── presentacion/
    │   ├── MenuPrincipal.java                   [B]
    │   ├── MenuColaboradores.java               [A]
    │   ├── MenuInventario.java                  [A]
    │   └── MenuProduccion.java                  [B]
    └── util/
        ├── Consola.java                         [AMBOS] Lectura validada y limpieza de pantalla
    └── Validaciones.java                    [A] Reglas comunes de validación
```

> **Nota de Arquitectura:** Se incluye una capa `presentacion` independiente además de las tres capas estándar (modelo, datos y negocio) para cumplir explícitamente con la separación de consola solicitada en los requerimientos.

---

## Día 0: Contrato Inicial (Acuerdo en Conjunto)

*Ningún integrante modificará estas firmas sin previo acuerdo.*

1. **`Repositorio<T>` (Interfaz genérica):**
   - `agregar(T)`
   - `buscarPorId(String)`
   - `listar()`
   - `eliminar(String)`
2. **Excepciones base:**
   - `NegocioException` (no verificada: hereda de `RuntimeException`)
   - `ElementoNoEncontradoException` (hereda de `NegocioException`)
3. **`Consola` (Utilidad con validación):**
   - `leerEntero(msg, min, max)`
   - `leerTexto(msg)` / `leerTextoOpcional(msg)`
   - `leerDecimal(msg)`
   - `pausa()` y `limpiarPantalla()`
   - *Garantía:* ningún método debe fallar o interrumpir el programa si el usuario ingresa datos inválidos.
4. **Firmas de integración (A ➔ B):**
   - `Colaborador`: `getId()`, `getNombre()`, `getCategoria()`, `calcularSalario()`
   - `Operario` y `Supervisor`: se usan para validar quién puede ejecutar cada etapa.
   - `Insumo`: `getCodigo()`, `getNombre()`, `getExistencias()`, `getUnidadMedida()`
   - `ServicioColaboradores.buscar(String id) throws ElementoNoEncontradoException`
   - `ServicioInventario.buscar(String codigo) throws ElementoNoEncontradoException`
   - `ServicioInventario.hayExistencias(String codigo, int cant)`
   - `ServicioInventario.consumir(String codigo, int cant) throws ExistenciasInsuficientesException`
   - `ExistenciasInsuficientesException(String mensaje)`, que hereda de `NegocioException`
   - `ServicioInventario.consumirLote(Map<String, Integer>, productoId, etapa)`: descuenta todo o nada y deja el producto en el movimiento.
5. **IDs de los datos iniciales (carga de Persona A):**
   - Colaboradores: operarios `OP01`–`OP04`, supervisores `SU01`–`SU02`, administrativos `AD01`–`AD02`.
   - Insumos: telas `T01`–`T03` (en cm), hilos `H01`–`H02` (en m), botones `B01`, cierres `C01`, etiquetas `E01`, bolsas `O01`, elástico `O02`.

---

## Especificaciones por Módulo

### Persona A — Personal e Inventario

* **Modelo:**
  * **`Colaborador` (Abstracta):** contiene el método abstracto `calcularSalario()` (*polimorfismo principal*).
    * *Operario:* salario por hora + bono.
    * *Supervisor:* salario base + plus.
    * *Administrativo:* salario fijo.
  * **`Jornada` (Enum):** Diurna, Mixta y Nocturna, con las horas máximas de cada una.
  * **`Insumo`:** métodos con validación interna para evitar existencias negativas y alerta de stock bajo.
* **Datos:**
  * Almacenamiento en `HashMap<String, T>` usando el `id` o `codigo` como llave.
  * `CargaDatosPersonalInventario`: 6 a 8 colaboradores y 8 a 10 insumos iniciales.
* **Negocio:**
  * **`ServicioColaboradores`:** CRUD con validación de ID duplicado y salarios válidos. Reporte de planilla que recorre los colaboradores de forma polimórfica.
  * **`ServicioInventario`:** entradas, salidas (que generan un `MovimientoInventario`), consulta por categoría y alertas de stock mínimo.
* **Presentación:** `MenuColaboradores` y `MenuInventario` con sus submenús.

---

### Persona B — Producción, Consultas y Arranque

* **Modelo:**
  * **`Producto`:** composición con `List<Etapa>` (1 a 1..\*), etapa actual, estado. El id lo asigna el sistema (`P001`, `P002`, ...).
  * **`Etapa` (Abstracta):** composición con sus `RequerimientoInsumo` y sus `RegistroAvance`.
    * `validarResponsable(Colaborador)`: cada etapa define quién puede hacerla.
    * `ejecutar(Producto)`: cada etapa hace algo distinto (*polimorfismo*).
    * `calcularConsumo(RequerimientoInsumo)`: `EtapaCorte` lo sobrescribe para sumar el desperdicio.
  * **Etapas del proceso:**

    | Orden | Etapa | Responsable | Consume | Regla propia |
    | :---: | :--- | :--- | :--- | :--- |
    | 1 | `EtapaCorte` | Operario | La tela elegida al crear el producto (cm) | Suma 10 % de desperdicio (redondeado hacia arriba). |
    | 2 | `EtapaConfeccion` | Operario | Hilo `H01`, botones `B01`, 1 etiqueta `E01` y 1 bolsa `O01` | Tiempo estimado de 45 minutos. |
    | 3 | `EtapaControlCalidad` | Supervisor | Nada | Si hay al menos una prenda defectuosa, el producto queda `RECHAZADO`. |

  * **`EstadoProducto` (Enum):** `PENDIENTE`, `EN_PROCESO`, `RECHAZADO`, `TERMINADO`.
* **Datos:**
  * `RepositorioProductos` (`LinkedHashMap`: búsqueda por id y listado en orden de registro).
  * `CargaDatosProduccion`: 5 productos iniciales en distintos estados, creados con los IDs de la carga de A.
* **Negocio:**
  * **`ServicioProduccion.avanzarProducto(idProducto, idColaborador, observaciones)` (integración clave):**
    1. Valida al responsable mediante `ServicioColaboradores`.
    2. Verifica todas las existencias mediante `ServicioInventario` y solo descuenta si alcanzan todas (todo o nada).
    3. Ejecuta la etapa y actualiza el estado del producto.
    4. Registra el avance en el historial (`RegistroAvance`).
  * Además: `crearProducto`, `registrarInspeccion` y `eliminarProducto` (solo productos `PENDIENTE`).
  * **`ServicioConsultas` (consultas cruzadas):**
    * Productos en los que trabajó un colaborador.
    * Insumos consumidos por un producto.
    * Productos detenidos por falta de stock.
    * Resumen de productos por estado.
* **Presentación y Arranque:** `MenuProduccion`, `MenuPrincipal` y `Main` (crea los servicios, carga los datos y despliega el menú).

---

## Flujo de Trabajo y Git

* **Ramas e Integración:**
  * Cada integrante trabaja exclusivamente en su propia rama (`feature/personal-inventario` y `feature/produccion`).
  * Los archivos marcados como **`[AMBOS]`** únicamente se modifican previo común acuerdo.
* **Diagrama UML:**
  * Cada integrante elabora la sección de sus clases.
  * Persona B diagrama las relaciones entre los distintos módulos.
* **Video Demostrativo:**
  * Exposición individual (~3 minutos por integrante).
  * Introducción y conclusiones realizadas en conjunto.

---

## Cronograma de Trabajo

| Fechas | Entregable / Hito |
| :--- | :--- |
| **5 – 9 Oct** | Análisis, definición de supuestos y validación del diagrama con el docente. |
| **10 Oct** | Definición del **Contrato del Día 0**. |
| **11 – 20 Oct** | Desarrollo e implementación individual de módulos. |
| **21 – 22 Oct** | Integración de módulos y pruebas de estrés / validación de entradas inválidas. |
| **23 – 25 Oct** | Grabación de video y entrega final (**Límite: 25 de Octubre, 10:00 PM**). |
