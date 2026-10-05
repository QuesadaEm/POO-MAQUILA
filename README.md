# POO-MAQUILA

Sistema de gestión para maquila desarrollado bajo el paradigma de Programación Orientada a Objetos (POO) en Java.

---

## División de Trabajo

| Integrante | Áreas Asignadas | Responsabilidad principal |
| :--- | :--- | :--- |
| **Persona A** | Personal + Inventario | Módulos base de colaboradores, salarios y control de stock. |
| **Persona B** | Productos y Procesos + Main | Módulo de producción, etapas del proceso, menú principal e integración. |

---

## Estructura del Proyecto

```text
src/
├── Main.java                             [B]
├── modelo/
│   ├── personal/
│   │   ├── Colaborador.java              [A] Abstracta
│   │   ├── Operario.java                 [A] extends Colaborador
│   │   ├── Supervisor.java               [A] extends Colaborador
│   │   ├── Administrativo.java           [A] extends Colaborador
│   │   └── Jornada.java                  [A] Enum
│   ├── inventario/
│   │   ├── Insumo.java                   [A]
│   │   ├── CategoriaInsumo.java          [A] Enum
│   │   └── MovimientoInventario.java     [A] Entrada / Salida
│   └── produccion/
│       ├── Lote.java                     [B]
│       ├── EstadoProducto.java           [B] Enum
│       ├── Etapa.java                    [B] Abstracta
│       ├── EtapaCorte.java               [B]
│       ├── EtapaConfeccion.java          [B]
│       ├── EtapaAcabado.java             [B]
│       ├── EtapaControlCalidad.java       [B]
│       ├── RequerimientoInsumo.java       [B] Insumo + Cantidad
│       ├── TipoPrenda.java               [B] Enum
│       ├── EstadoLote.java               [B] Enum
│       └── RegistroAvance.java           [B] Historial
├── datos/
│   ├── Repositorio.java                  [AMBOS] Interfaz genérica
│   ├── RepositorioColaboradores.java     [A]
│   ├── RepositorioInsumos.java           [A]
│   ├── RepositorioProductos.java         [B]
│   ├── CargaDatosPersonalInventario.java [A]
│   └── CargaDatosProduccion.java         [B]
├── negocio/
│   ├── ServicioColaboradores.java        [A]
│   ├── ServicioInventario.java           [A]
│   ├── ServicioProduccion.java          [B]
│   └── ServicioConsultas.java            [B] Consultas cruzadas
├── excepciones/
│   ├── NegocioException.java             [AMBOS] Base
│   ├── ElementoNoEncontradoException.java[AMBOS]
│   ├── ExistenciasInsuficientesEx.java  [A]
│   └── TransicionInvalidaException.java  [B]
├── presentacion/
│   ├── MenuPrincipal.java                [B]
│   ├── MenuColaboradores.java            [A]
│   ├── MenuInventario.java               [A]
│   └── MenuProduccion.java              [B]
└── util/
    └── Consola.java                      [AMBOS] Lectura validada
```

> **Nota de Arquitectura:** Se incluye una capa `presentacion` independiente además de las tres capas estándar para cumplir explícitamente con la separación de consola solicitada en los requerimientos.

---

## Día 0: Contrato Inicial (Acuerdo en Conjunto)

*Tiempo estimado: 1 a 2 horas. Ningún integrante modificará estas firmas sin previo acuerdo.*

1. **`Repositorio<T>` (Interfaz genérica):**
   - `agregar(T)`
   - `buscarPorId(String)`
   - `listar()`
   - `eliminar(String)`
2. **Excepciones base:**
   - `NegocioException`
   - `ElementoNoEncontradoException`
3. **`Consola` (Utilidad con validación):**
   - `leerEntero(msg, min, max)`
   - `leerTexto(msg)`
   - `leerDecimal(msg)`
   - *Garantía:* Ningún método debe fallar o interrumpir el programa si el usuario ingresa datos inválidos.
4. **Interfaces/Firmas de integración (A ➔ B):**
   - `Colaborador`: `getId()`, `getNombre()`, `getCategoria()`, `calcularSalario()`
   - `Insumo`: `getCodigo()`, `getNombre()`, `getExistencias()`
   - `ServicioColaboradores.buscar(String id)`
   - `ServicioInventario.hayExistencias(String codigo, int cant)`
   - `ServicioInventario.consumir(String codigo, int cant) throws ExistenciasInsuficientesException`

> **Entregable Día 0:** Persona A entrega las clases base `Colaborador` e `Insumo` (atributos, constructores y getters) para permitir el inicio de pruebas en el módulo B.

---

##  Especificaciones por Módulo

### Persona A — Personal e Inventario

* **Modelo:**
  * **`Colaborador` (Abstracta):** Contiene el método abstracto `calcularSalario()` (*Polimorfismo principal*).
    * *Operario:* Horas trabajadas + bono.
    * *Supervisor:* Salario base + plus.
    * *Administrativo:* Salario fijo.
  * **`Jornada` (Enum):** Diurna, Mixta, Nocturna (incluye horas máximas por jornada).
  * **`Insumo`:** Métodos con validación interna para evitar existencias negativas.
* **Datos:**
  * Almacenamiento en `HashMap<String, T>` utilizando `id` / `codigo` como clave.
  * `CargaDatosPersonalInventario`: 6 a 8 colaboradores y 8 a 10 insumos iniciales.
* **Negocio:**
  * **`ServicioColaboradores`:** CRUD con validación de ID duplicado y salarios válidos. Reporte de planilla recorriendo colaboradores de forma polimórfica.
  * **`ServicioInventario`:** Entradas, salidas (generación de `MovimientoInventario`), consulta por categoría y alertas de stock mínimo.
* **Presentación:** `MenuColaboradores` y `MenuInventario` con submenús correspondientes.

---

### Persona B — Producción, Consultas y Arranque

* **Modelo:**
  * **`Lote` / `Producto`:** Relación de composición con `List<Etapa>`, control de etapa actual, estado y `RegistroAvance`.
  * **`Etapa` (Abstracta):**
    * `validarResponsable(Colaborador)` (ej. *Control de Calidad* solo permitido para *Supervisor*).
    * `ejecutar()` (comportamiento por etapa; ej. rechazar y regresar a etapa anterior).
  * **`EstadoProducto` (Enum):** `PENDIENTE`, `EN_PROCESO`, `RECHAZADO`, `TERMINADO`.
* **Datos:**
  * `RepositorioProductos`.
  * `CargaDatosProduccion`: 4 a 5 productos iniciales en distintos estados, vinculados a los IDs de la carga de A.
* **Negocio:**
  * **`ServicioProduccion.avanzarProducto(id)` (Integración clave):**
    1. Valida al responsable mediante `ServicioColaboradores`.
    2. Verifica y descuenta existencias mediante `ServicioInventario`.
    3. Registra avance en el historial.
    4. Actualiza el estado del producto.
  * **`ServicioConsultas` (Consultas cruzadas):**
    * Productos asignados a un colaborador específico.
    * Insumos consumidos por un producto.
    * Productos detenidos por falta de stock.
* **Presentación y Arranque:** `MenuProduccion`, `MenuPrincipal` y `Main` (inicializa servicios, carga datos y despliega el menú).

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
| **11 – 20 Oct**| Desarrollo e implementación individual de módulos. |
| **21 – 22 Oct**| Integración de módulos y pruebas de estrés / validación de entradas inválidas. |
| **23 – 25 Oct**| Grabación de video y entrega final (**Límite: 25 de Octubre, 10:00 PM**). |
