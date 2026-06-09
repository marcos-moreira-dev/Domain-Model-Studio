# UML Clases

## Introducción
UML Clases ayuda a pensar la estructura estática del software: clases, atributos, operaciones, responsabilidades y relaciones. Es útil cuando ya no basta con entender el dominio, sino que se necesita empezar a diseñar cómo se organizará el modelo de software.

## Pregunta que responde
¿Qué clases importantes necesita el sistema, qué responsabilidad tiene cada una, qué datos y operaciones maneja y cómo se relaciona con otras clases?

## Idea central
Un diagrama de clases no es una copia directa de tablas ni de pantallas. Es una representación de estructura de software, responsabilidades y relaciones entre tipos.

## Qué es
![Anatomía de una clase UML con nombre, atributos y operaciones.](figure:uml-class-anatomy)
- UML Clases es un diagrama estructural de UML que ayuda a representar clases, atributos, operaciones, responsabilidades y relaciones estáticas entre piezas del software.
- A diferencia del modelo conceptual, que intenta entender el negocio, el diagrama de clases se acerca más al diseño de software: muestra cómo se organizarán objetos, servicios, contratos, entidades de dominio, DTOs, enumeraciones o interfaces.
- Una clase no debe entenderse como una tabla dibujada de forma bonita. Una clase puede tener datos, pero también comportamiento y responsabilidad. Por ejemplo, `OrdenReparacion` no solo tiene fecha, cliente y estado; también puede ofrecer operaciones como registrar diagnóstico, aprobar cotización, calcular saldo o cerrar la orden.
- En sistemas administrativos, este diagrama sirve para pensar con más precisión qué piezas internas sostendrán casos de uso como registrar pago, crear orden, consultar historial, aplicar permisos o generar reportes.
- UML significa Unified Modeling Language, lenguaje unificado de modelado. En este capítulo se usa de forma práctica: no como formalismo pesado, sino como una guía para razonar mejor sobre estructura de software.

## Para qué sirve
- Sirve para convertir una comprensión del dominio en una estructura técnica más clara.
- Ayuda a discutir qué clases existen, qué responsabilidad tiene cada una, qué datos mantiene, qué operaciones ofrece y cómo se relaciona con otras.
- Permite detectar responsabilidades mezcladas antes de programar. Si una clase llamada `OrdenReparacion` también genera reportes financieros, administra usuarios, manda correos, descuenta inventario y calcula impuestos, probablemente está cargando demasiada responsabilidad.
- Ayuda a razonar sobre relaciones importantes: asociación, dependencia, composición, agregación, herencia, implementación de interfaces y multiplicidad.
- En backend, puede servir para diseñar entidades de dominio, servicios, repositorios, DTOs, validadores, políticas de negocio y contratos de integración.
- En arquitectura, ayuda a separar mejor capas: dominio, aplicación, infraestructura y presentación.
- En mantenimiento, permite que otra persona entienda rápidamente qué piezas existen y qué relación tienen sin leer todo el código.

## Qué representa
Representa clases, atributos, operaciones, visibilidad, asociaciones, multiplicidades, dependencias, composición, herencia, interfaces, enumeraciones y paquetes.

## Qué no representa
No representa procesos de negocio, navegación de pantallas, despliegue físico, endpoints completos ni necesariamente tablas SQL definitivas. Puede inspirar esas piezas, pero no las sustituye.

## Elementos principales
![Asociación con multiplicidad entre Cliente y OrdenReparacion.](figure:uml-class-association-multiplicity)
- Clase: tipo de objeto con nombre, atributos, operaciones y responsabilidad. Ejemplo: `Cliente`, `Equipo`, `OrdenReparacion`, `Pago`.
- Atributo: dato mantenido por una clase. Ejemplo: `fechaRecepcion`, `estado`, `monto`, `telefonoPrincipal`.
- Operación: comportamiento que la clase puede realizar. Ejemplo: `registrarDiagnostico()`, `calcularSaldoPendiente()`, `anular()`.
- Visibilidad: indica si un atributo u operación es público, privado, protegido o de paquete. Los signos comunes son `+`, `-`, `#` y `~`.
- Asociación: relación estructural entre clases. Ejemplo: un `Cliente` tiene muchas `OrdenReparacion`.
- Multiplicidad: indica cuántos objetos pueden participar en una relación. Ejemplo: `1`, `0..1`, `0..*`, `1..*`.
- Dependencia: una clase usa temporalmente a otra sin poseerla estructuralmente.
- Agregación: relación todo-parte débil.
- Composición: relación todo-parte fuerte, donde la parte depende del ciclo de vida del todo.
- Herencia o generalización: una clase especializada hereda de una clase más general.
- Interfaz: contrato de operaciones que una o varias clases pueden implementar.
- Enumeración: conjunto cerrado de valores, como `EstadoOrden` con `RECIBIDA`, `DIAGNOSTICADA`, `EN_REPARACION`, `ENTREGADA`.
- Paquete o módulo: agrupación visual de clases relacionadas para evitar diagramas gigantes e ilegibles.

## Clase, atributos y operaciones
- Una clase debe tener un nombre claro, normalmente en singular y con significado técnico o de dominio. Ejemplos: `Cliente`, `Equipo`, `OrdenReparacion`, `Pago`, `Usuario`, `Rol`.
- Los atributos describen datos que la clase necesita mantener. Ejemplo: `OrdenReparacion` puede tener `numero`, `fechaRecepcion`, `estado`, `problemaReportado`.
- Las operaciones describen comportamiento. Ejemplo: `OrdenReparacion` puede tener `registrarDiagnostico()`, `aprobarCotizacion()`, `anular()` o `puedeEntregarse()`.
- La pregunta clave para una operación es: ¿esta responsabilidad pertenece realmente a esta clase?
- Ejemplo incorrecto: `Cliente.generarReporteFinanciero()`. Generar reportes financieros probablemente pertenece a un servicio de reportes, no a `Cliente`.
- Ejemplo más razonable: `ReporteFinancieroService.generarReporteMensual()`.

## Visibilidad y tipos
- La visibilidad ayuda a comunicar encapsulamiento.
- `+` significa público.
- `-` significa privado.
- `#` significa protegido.
- `~` significa visibilidad de paquete o interna.
- Ejemplo: `- estado: EstadoOrden` indica un atributo privado llamado estado.
- Ejemplo: `+ cerrarOrden(): void` indica una operación pública.
- Los tipos ayudan a entender qué clase de valor se espera. Ejemplo: `fechaRecepcion: LocalDate`, `monto: BigDecimal`, `estado: EstadoOrden`.
- En una guía de análisis no hace falta obsesionarse con tipos exactos de lenguaje. Pero en diseño técnico sí conviene ser preciso.

## Dominio, DTO, servicio y persistencia
![Diferencia entre clase de dominio, DTO, servicio y repositorio.](figure:uml-class-domain-dto-service)
- Una clase de dominio modela reglas importantes del negocio. Ejemplo: `OrdenReparacion` puede controlar cuándo una orden puede pasar de diagnosticada a aprobada.
- Un DTO transporta datos. Ejemplo: `CrearPagoRequest` contiene datos enviados desde una pantalla o API.
- Un servicio coordina una operación. Ejemplo: `PagoService` valida permisos, revisa saldo, crea el pago y actualiza la orden.
- Un repositorio guarda o recupera datos. Ejemplo: `PagoRepository` persiste pagos.
- Mezclar todo en una sola clase vuelve el sistema difícil de probar y mantener.
- Una buena regla es separar datos de entrada, reglas de dominio, coordinación de casos de uso y persistencia.

## Cómo leer un diagrama de clases
- Empieza por los paquetes o módulos. Pregunta qué área del sistema estás mirando: clientes, reparaciones, pagos, usuarios, reportes.
- Luego identifica clases principales. Busca las que representan conceptos fuertes del negocio o servicios importantes.
- Lee las relaciones con multiplicidad. Ejemplo: un cliente tiene muchas órdenes; una orden tiene muchos pagos.
- Revisa composiciones. Pregunta qué partes dependen fuertemente de un todo.
- Revisa herencias. Pregunta si de verdad hay una relación “es un tipo de”.
- Revisa interfaces. Pregunta qué contrato se quiere estabilizar.
- Revisa operaciones. Pregunta si cada clase tiene responsabilidades coherentes.
- Revisa dependencias. Pregunta si una clase conoce demasiadas cosas.

## Relaciones y lectura
![Composición entre OrdenReparacion y DetalleRepuesto.](figure:uml-class-composition)
- Un diagrama de clases debe poder leerse en frases claras. Si el diagrama no se puede explicar en lenguaje natural, probablemente tiene nombres ambiguos o relaciones mal elegidas.
- Asociación: `Cliente 1 ─ 0..* OrdenReparacion` se lee como: un cliente puede tener muchas órdenes de reparación, y cada orden pertenece a un cliente.
- Composición: `OrdenReparacion ◆── DetalleRepuesto` se lee como: los detalles de repuesto pertenecen fuertemente a una orden; no tienen mucho sentido aislados de ella.
- Dependencia: `ReporteService ----> OrdenRepository` se lee como: el servicio de reportes usa el repositorio de órdenes para consultar información, pero no lo contiene como parte conceptual del dominio.
- Herencia: `Usuario` generaliza `Administrador`, `Tecnico` o `Cajero` solo si realmente existe una relación “es un tipo de”. No debe usarse herencia solo porque los nombres se parecen.
- Interfaz: `Notificador` puede ser implementada por `EmailNotificador` y `WhatsAppNotificador`. Esto permite cambiar el mecanismo de notificación sin cambiar todo el sistema.
- La multiplicidad es obligatoria cuando la cantidad importa. No es lo mismo que una orden tenga un solo pago, varios pagos o ningún pago todavía.
- La dirección de una relación debe tener sentido. En un diseño técnico, no siempre conviene que todas las clases conozcan a todas las demás.

## Cómo construirlo paso a paso
- Parte del modelo conceptual y de los casos de uso.
- Selecciona un módulo o caso de uso concreto; no intentes dibujar todo el sistema de una vez.
- Identifica clases de dominio importantes.
- Agrega atributos principales, sin llenar ruido mecánico.
- Agrega operaciones que representen comportamiento real.
- Agrega servicios cuando una operación coordina varias clases.
- Agrega repositorios si necesitas mostrar persistencia.
- Agrega DTOs si necesitas mostrar entrada o salida de datos.
- Define asociaciones y multiplicidades.
- Usa composición cuando exista relación todo-parte fuerte.
- Usa interfaces cuando quieras representar contratos intercambiables.
- Agrupa por paquete o módulo.
- Revisa si alguna clase tiene demasiadas responsabilidades.
- Simplifica el diagrama hasta que se pueda explicar en voz alta.

## Microejemplo administrativo
- Sistema: tienda de reparación de celulares.
- Clases de dominio candidatas: `Cliente`, `Equipo`, `OrdenReparacion`, `DiagnosticoTecnico`, `Pago`, `Repuesto`, `DetalleRepuesto`, `Garantia`.
- Servicios posibles: `OrdenService`, `PagoService`, `DiagnosticoService`, `ReporteService`, `AutorizacionService`.
- DTOs posibles: `CrearOrdenRequest`, `RegistrarPagoRequest`, `OrdenDetalleResponse`.
- Repositorios posibles: `OrdenRepository`, `ClienteRepository`, `PagoRepository`, `RepuestoRepository`.
- Relaciones importantes: un `Cliente` puede tener varias `OrdenReparacion`; una `OrdenReparacion` puede tener varios `Pago`; una `OrdenReparacion` puede componer varios `DetalleRepuesto`; cada `DetalleRepuesto` referencia un `Repuesto`.
- Operación concreta: registrar pago. `PagoService` recibe un `RegistrarPagoRequest`, consulta la orden, valida saldo, crea `Pago`, actualiza saldo de `OrdenReparacion` y guarda los cambios mediante repositorios.
- Este diseño no obliga a programarlo exactamente así, pero da una estructura razonable para conversar con una IA o con otro desarrollador.

## Casos especiales
![Herencia e interfaz como mecanismos distintos.](figure:uml-class-inheritance-interface)
- Clase de dominio: representa una idea central del negocio con reglas importantes. Ejemplo: `OrdenReparacion`, `Pago`, `Garantia`.
- DTO significa Data Transfer Object. Un DTO sirve para transportar datos entre capas o por una API, no para concentrar comportamiento profundo de negocio. Ejemplo: `CrearOrdenRequest` u `OrdenResumenResponse`.
- Servicio: coordina operaciones que no pertenecen naturalmente a una sola entidad. Ejemplo: `PagoService`, `ReporteService`, `AutorizacionService`.
- Repositorio: abstrae acceso a persistencia. Ejemplo: `OrdenRepository`, `ClienteRepository`.
- Entidad persistente: clase orientada a guardar datos en una base. Puede parecerse a una clase de dominio, pero no siempre debe mezclar toda la lógica del negocio.
- Clase de valor: representa un dato con reglas propias. Ejemplo: `Email`, `Cedula`, `Dinero`, `Telefono`.
- Enumeración: útil cuando un conjunto de valores es cerrado y estable, como `EstadoOrden`. Si los valores deben configurarse por el usuario, quizá convenga un catálogo o entidad.
- Clase asociativa: útil cuando una relación tiene datos propios. Ejemplo: `DetalleRepuesto` entre `OrdenReparacion` y `Repuesto`, con cantidad y precio unitario.
- Paquetes: cuando hay muchas clases, conviene agrupar por módulo: `clientes`, `reparaciones`, `pagos`, `usuarios`, `reportes`.
- En sistemas grandes, el diagrama puede tener niveles de detalle: solo nombres, nombres con atributos, o nombres con atributos y operaciones. Mostrar todo siempre puede volverlo ilegible.

## Cuándo usarlo
![Agrupación por paquetes para mantener legible un diagrama de clases.](figure:uml-class-packages)
- Úsalo cuando ya existe una comprensión mínima del dominio y necesitas pensar estructura interna de software.
- Úsalo para diseñar backend, entidades de dominio, servicios, repositorios, DTOs, validadores o contratos.
- Úsalo cuando un caso de uso necesita ser explicado técnicamente: registrar pago, anular orden, registrar diagnóstico, generar reporte.
- Úsalo para revisar si una clase tiene demasiadas responsabilidades.
- Úsalo para analizar dependencias entre módulos técnicos.
- Úsalo cuando quieres pedirle a una IA que proponga estructura de código con clases, atributos, métodos y relaciones.
- Úsalo después de modelo conceptual, diccionario de datos y casos de uso. Si se usa demasiado pronto, puede convertir el levantamiento de negocio en una discusión prematura de código.

## Cuándo no usarlo
- No lo uses como sustituto del modelo conceptual. El modelo conceptual entiende el negocio; UML Clases diseña estructura de software.
- No lo uses como diagrama físico de base de datos. Para diseño relacional físico existen otras herramientas.
- No lo uses para explicar procesos completos de negocio. Para eso conviene flujo operativo o BPMN.
- No lo uses para explicar navegación de interfaz. Para eso está flujo de pantallas.
- No lo uses para dibujar wireframes, botones, formularios o pantallas.
- No lo llenes con getters, setters y ruido mecánico si eso no aporta comprensión.
- No lo uses para justificar herencias forzadas. La herencia mal usada puede crear acoplamiento y rigidez.
- No lo conviertas en un diagrama enorme con todas las clases del sistema al mismo tiempo. Es mejor separar por módulo o por objetivo.

## Errores comunes
![Errores comunes: convertir tablas, pantallas o clases gigantes en diseño de clases.](figure:uml-class-common-errors)
- Confundir clase con tabla. Algunas clases se parecen a tablas, pero UML Clases no es diseño físico de base de datos.
- Confundir clase con pantalla. `PantallaCliente` puede existir en interfaz, pero no debe mezclarse con el modelo de dominio si el diagrama intenta explicar reglas de negocio.
- Crear clases sin responsabilidad. Una clase con solo datos y ningún criterio de comportamiento puede ser válida como DTO, pero no siempre como clase de dominio.
- Crear clases gigantes. Una clase que hace pagos, reportes, inventario, usuarios y facturación tiene demasiadas responsabilidades.
- Abusar de herencia. No todo parecido justifica una jerarquía.
- Usar composición y agregación sin entender ciclo de vida.
- Omitir multiplicidades. Una relación sin multiplicidad puede dejar ambiguo si hay uno, ninguno o muchos.
- Mezclar dominio, DTO, servicio, repositorio y pantalla sin indicar nivel.
- Crear nombres genéricos como `Manager`, `Data`, `Info`, `Registro` o `Helper` sin responsabilidad clara.
- Dibujar todos los detalles del código. Un diagrama útil selecciona lo importante.

## Relación con otros diagramas
- Modelo conceptual: aporta entidades y relaciones del negocio que pueden inspirar clases de dominio.
- Diccionario de datos: precisa atributos, tipos lógicos, validaciones y reglas.
- Mapa de módulos: ayuda a agrupar clases por responsabilidad funcional.
- Roles y permisos: puede generar clases como `Usuario`, `Rol`, `Permiso` o servicios de autorización.
- UML Casos de uso: indica qué operaciones debe soportar el diseño.
- UML Secuencia: muestra cómo colaboran clases, servicios y repositorios para ejecutar una operación.
- UML Estados: define ciclos de vida de clases importantes como `OrdenReparacion`.
- BPMN y flujo operativo: revelan procesos que crean o modifican objetos.
- C4 Contenedores: indica en qué contenedor vive el conjunto de clases, normalmente backend/API.
- Despliegue técnico: no define clases, pero muestra dónde corre el software que las contiene.

## Qué pedirle a la IA después de entenderlo
- A partir de este modelo conceptual, propón un diagrama UML de clases inicial.
- Convierte estas entidades y relaciones en clases, atributos, asociaciones y multiplicidades.
- Revisa este diagrama de clases y detecta clases con demasiadas responsabilidades.
- Identifica qué clases deberían ser entidades de dominio, DTOs, servicios, enums, repositorios o clases de valor.
- Propón agrupación por paquetes para estas clases.
- A partir de este caso de uso, indica qué clases colaborarían para ejecutarlo.
- Revisa si estoy confundiendo clases con tablas, pantallas o formularios.
- Genera un diagrama de clases para el módulo de reparaciones con Cliente, Equipo, OrdenReparacion, DiagnosticoTecnico, Pago y Repuesto.

## Ficha rápida
- Representa: clases, atributos, operaciones, responsabilidades y relaciones estáticas.
- No representa: procesos completos, pantallas, diseño físico de base de datos ni despliegue.
- Sirve para: pensar estructura de software y colaboración estática entre piezas.
- Relación más común: asociación con multiplicidad.
- Relación más delicada: herencia, porque puede crear rigidez si se usa mal.
- Error clásico: copiar tablas como clases sin pensar comportamiento ni responsabilidades.
- Pregunta clave: ¿qué responsabilidad tiene cada clase y cómo se relaciona con las demás?

## Auditoría de frontera teórica

UML Clases pertenece al diseño estructural de software. No debe confundirse con modelo conceptual ni con base de datos física.

El modelo conceptual descubre conceptos del negocio. UML Clases decide estructura de software, responsabilidades, atributos, operaciones, interfaces, dependencias y paquetes. Una tabla SQL persiste datos, pero una clase puede representar comportamiento, contrato, servicio, DTO, valor o entidad de dominio.

Si el diagrama solo copia tablas con campos, puede estar perdiendo el valor de UML Clases. Si mezcla botones, pantallas o layouts, ya se salió de su frontera y debe ir hacia wireframes o flujo de pantallas.

## Checklist final de estudio
- Puedo leer clases, atributos, operaciones, visibilidad y relaciones.
- Puedo distinguir UML Clases de modelo conceptual, tablas, DTOs y pantallas.
- Puedo usar multiplicidad, composición, herencia e interfaces con criterio.
- Puedo agrupar clases por paquete o módulo para evitar marañas.
- Puedo revisar responsabilidades y detectar clases demasiado grandes o vagas.

## Referencias / base teórica
- UML Clases pertenece a UML, una familia de diagramas para modelar estructura y comportamiento de sistemas software.
- Para esta guía se usa con orientación práctica: sistemas administrativos, backend, dominio, servicios, DTOs y arquitectura mantenible.
- La lectura profesional del diagrama debe priorizar responsabilidades, relaciones, multiplicidades y claridad humana antes que llenar detalles mecánicos.
