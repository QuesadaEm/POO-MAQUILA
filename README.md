# POO-MAQUILA
División
Persona A: Personal + Inventario. Son dos áreas, pero más sencillas.
Persona B: Productos y Procesos + Main/menú principal. Es un área más compleja y además incluye la integración.
Estructura de archivos
src/
├── Main.java                             		 [B]
├── modelo/
│  ├── personal/
│   │   ├── Colaborador.java              	 [A] abstracta
│   │   ├── Operario.java                 	 [A] extends Colaborador
│   │   ├── Supervisor.java               	 [A] extends Colaborador
│   │   ├── Administrativo.java           	 [A] extends Colaborador
│   │    └── Jornada.java                   	[A] enum
│  ├── inventario/
│   │   ├── Insumo.java                  	  [A]
│   │   ├── CategoriaInsumo.java            [A] enum
│   │   └── MovimientoInventario.java       [A] entrada/salida
│   └── produccion/
│       ├── Lote.java                  	    [B]
│       ├── EstadoProducto.java            	[B] enum
│       ├── Etapa.java                     	[B] abstracta
│       ├── EtapaCorte.java               	[B]
│       ├── EtapaConfeccion.java           	[B]
│       ├── EtapaAcabado.java           	  [B]
│       ├── EtapaControlCalidad.java        [B]
│       ├── RequerimientoInsumo.java        [B] insumo + cantidad
│       ├── TipoPrenda.java           	    [B] enum
│       ├── EstadoLote                      [B] enum
│       └── RegistroAvance.java            	[B] historial
├── datos/
│   ├── Repositorio.java                   	[AMBOS] interfaz genérica
│   ├── RepositorioColaboradores.java     	[A]
│   ├── RepositorioInsumos.java            	[A]
│   ├── RepositorioProductos.java          	[B]
│   ├── CargaDatosPersonalInventario.java  	[A]
│    └── CargaDatosProduccion.java          [B]
├── negocio/
│   ├── ServicioColaboradores.java         	[A]
│   ├── ServicioInventario.java            	[A]
│   ├── ServicioProduccion.java           	[B]
│    └── ServicioConsultas.java             [B] consultas cruzadas
├── excepciones/
│   ├── NegocioException.java              		 [AMBOS] base
│   ├── ElementoNoEncontradoException.java	   [AMBOS]
│   ├── ExistenciasInsuficientesException.java [A]
│    └── TransicionInvalidaException.java   	  [B]
├── presentacion/
│   ├── MenuPrincipal.java                 			[B]
│   ├── MenuColaboradores.java             		  [A]
│   ├── MenuInventario.java                	    [A]
│    └── MenuProduccion.java               	    [B]
└── util/
└── Consola.java                       	[AMBOS] lectura validada
Una aclaración: el enunciado pide explícitamente la capa de consola separada (sección 8), por eso aparece presentacion además de sus tres capas.
Día 0: contrato en conjunto (1–2 horas)
Esto es lo único que hacen juntos. Después de terminarlo, nadie lo cambia sin avisarle al otro.
1.	Repositorio<T>: agregar(T), buscarPorId(String), listar(), eliminar(String).
2.	NegocioException y ElementoNoEncontradoException.
3.	Consola: leerEntero(msg, min, max), leerTexto(msg), leerDecimal(msg). Ninguno de estos métodos debe caerse si el usuario ingresa algo inválido.
4.	Las firmas que B va a usar de A: 
o	Colaborador: getId(), getNombre(), getCategoria(), calcularSalario()
o	Insumo: getCodigo(), getNombre(), getExistencias()
o	ServicioColaboradores.buscar(String id)
o	ServicioInventario.hayExistencias(String codigo, int cant)
o	ServicioInventario.consumir(String codigo, int cant) throws ExistenciasInsuficientesException
A entrega Colaborador e Insumo básicos (atributos, constructor y getters) el primer día, para que B pueda probar con objetos reales.
Persona A
Modelo
•	Colaborador es abstracta, con el método abstracto calcularSalario(). Este es el polimorfismo principal: el operario cobra por horas más un bono, el supervisor tiene salario base más un plus y el administrativo tiene salario fijo.
•	Jornada es un enum (diurna, mixta, nocturna) con las horas máximas de cada una.
•	Insumo valida en sus propios métodos que las existencias nunca sean negativas.
Datos
•	Los repositorios guardan la información en un HashMap<String, T> usando el id como llave.
•	La carga inicial debe incluir 6–8 colaboradores y 8–10 insumos.
Negocio
•	El CRUD de colaboradores debe validar ids duplicados y salarios válidos.
•	Inventario necesita entradas, salidas (que generan un MovimientoInventario), consulta por categoría y alerta de stock mínimo.
•	También el reporte de planilla, que recorre la lista llamando a calcularSalario() de forma polimórfica.
Menús
•	MenuColaboradores y MenuInventario, cada uno con sus submenús.
Persona B
Modelo
•	Producto tiene una List<Etapa> (composición), la etapa actual, un estado y un historial de RegistroAvance.
•	Etapa es abstracta, con: 
o	validarResponsable(Colaborador): por ejemplo, el control de calidad solo lo hace un supervisor.
o	ejecutar(): cada etapa hace algo distinto. Por ejemplo, el control de calidad puede rechazar el producto y devolverlo a la etapa anterior.
•	EstadoProducto es un enum: PENDIENTE, EN_PROCESO, RECHAZADO, TERMINADO.
Datos
•	RepositorioProductos.
•	La carga inicial debe incluir 4–5 productos en distintos estados, que usen ids de colaboradores e insumos que existan en la carga de A.
Negocio
•	ServicioProduccion.avanzarProducto(id). Esta es la operación que conecta los módulos: 
1.	Valida al responsable usando ServicioColaboradores.
2.	Verifica y descuenta los insumos usando ServicioInventario.
3.	Registra el avance en el historial.
4.	Cambia el estado del producto.
•	ServicioConsultas resuelve las consultas que cruzan información: 
o	productos asignados a un colaborador,
o	insumos consumidos por un producto,
o	productos detenidos por falta de stock.
Menús y arranque
•	MenuProduccion, MenuPrincipal y Main. Main solo crea los servicios, carga los datos y abre el menú.
Tareas compartidas
•	Diagrama: cada uno hace la parte de sus clases y luego las unen. B dibuja las relaciones entre módulos.
•	Video: cada uno explica su parte en unos 3 minutos, y entre ambos hacen la introducción y las conclusiones.
•	Git: cada uno trabaja en su propia rama y no edita archivos del otro. Los archivos marcados [AMBOS] se tocan solo de común acuerdo.
Calendario sugerido (entrega 25 de octubre, 10 pm)
Fechas	Qué
5–9 oct	Análisis, supuestos y diagrama → validarlo con el profe
10 oct	Contrato del día 0
11–20 oct	Implementación por separado
21–22 oct	Integración y pruebas con entradas inválidas
23–25 oct	Video y entrega
Si quieren, les puedo ayudar a armar el diagrama de clases con atributos y multiplicidades para llevárselo al profe.

