# UML Casos de uso

## Introducción
UML Casos de uso ofrece una vista funcional del sistema desde el punto de vista de los actores. Ayuda a aclarar qué metas observables pueden lograr las personas o sistemas externos usando el sistema.

## Pregunta que responde
¿Quién usa el sistema y qué objetivos funcionales puede lograr dentro del límite del sistema?

## Idea central
Un caso de uso representa una meta funcional observable, no una pantalla, un botón, una clase ni una tabla.

## Qué es
UML Casos de uso es una forma de representar qué funcionalidades ofrece un sistema y qué actores interactúan con esas funcionalidades. Su valor principal está en mirar el sistema desde fuera: no pregunta todavía cómo se programa una función, qué clase la ejecuta, qué tabla guarda los datos o qué pantalla aparece primero. Pregunta algo más básico: quién necesita lograr algo con el sistema y qué objetivo observable consigue.

En un sistema administrativo, un caso de uso puede ser Registrar cliente, Crear orden de reparación, Registrar diagnóstico, Registrar pago, Generar reporte o Administrar usuarios. Cada uno expresa una meta funcional que tiene sentido para una persona o sistema externo. Por eso, este diagrama sirve mucho al inicio del análisis: ayuda a acordar alcance con el cliente sin hundirse todavía en detalles técnicos.

![Símbolos básicos de actores, casos de uso y frontera del sistema.](figure:uml-use-case-symbols)

Un actor puede ser una persona, un rol operativo, una organización o incluso otro sistema. Un caso de uso se dibuja como un óvalo porque representa una funcionalidad observable, no una pantalla ni una clase. El límite del sistema se dibuja como un rectángulo que contiene los casos de uso que pertenecen al software que se está diseñando.

La idea central es sencilla: el actor está fuera del sistema y usa el sistema para cumplir una meta. El sistema responde ofreciendo una funcionalidad.

## Para qué sirve
Sirve para acordar alcance funcional. Cuando alguien dice quiero un sistema para mi negocio, el diagrama de casos de uso ayuda a convertir esa frase general en una lista de capacidades concretas: registrar clientes, consultar órdenes, asignar técnicos, registrar pagos, emitir reportes o configurar permisos.

También sirve para descubrir actores. En una tienda de reparación de celulares pueden aparecer Recepción, Técnico, Caja, Administrador, Dueño, Cliente, Sistema de facturación y Servicio de notificaciones. Algunos actores usarán pantallas directamente; otros participarán de forma indirecta, por ejemplo recibiendo una notificación o confirmando un pago.

![Ejemplo administrativo con actores y casos de uso principales.](figure:uml-use-case-basic-admin-example)

El diagrama también ayuda a separar lo que pertenece al sistema de lo que queda fuera. Si el cliente entrega físicamente un equipo, eso ocurre en el mundo real; si recepción registra la orden en el sistema, eso sí puede ser un caso de uso. Si un banco procesa una transferencia, el banco puede ser sistema externo; si nuestro software solo registra el pago confirmado, ese registro pertenece a nuestro sistema.

En términos prácticos, este diagrama sirve como puente entre conversación de negocio y documentación funcional. Después se puede bajar a flujos operativos, BPMN, pantallas, wireframes, permisos, secuencias y pruebas.

## Qué representa
Representa actores, casos de uso, límite del sistema, asociaciones, relaciones include, relaciones extend, generalización y vínculos con especificaciones textuales.

## Qué no representa
No representa flujo detallado de pantallas, reglas completas de permisos, diseño de clases, secuencias técnicas ni implementación backend.

## Elementos principales
Los elementos principales son actor, caso de uso, límite del sistema, asociación, include, extend y generalización.

El actor representa quién o qué interactúa con el sistema desde fuera. Puede ser una persona como Técnico o Caja, pero también un sistema externo como Facturación electrónica. Lo importante es que el actor no es una tabla ni una entidad del dominio. Cliente como actor no es lo mismo que Cliente como entidad del modelo conceptual: el primero interactúa con el sistema; el segundo representa información del negocio.

El caso de uso representa una funcionalidad con valor observable. Debe nombrarse normalmente con verbo más objeto: Registrar pago, Crear orden de reparación, Consultar historial de cliente, Generar reporte mensual. Un nombre como Pago o Cliente es demasiado pobre porque no indica qué objetivo se cumple.

El límite del sistema indica qué casos de uso pertenecen al software que se está diseñando. Los actores quedan fuera del rectángulo. Este límite es clave para no prometer responsabilidades ajenas.

![Límite del sistema con actores externos y funcionalidades internas.](figure:uml-use-case-system-boundary)

La asociación es una línea entre actor y caso de uso. Indica participación, no orden temporal. Si Recepción se conecta con Crear orden de reparación, eso no significa que sea el primer paso de un flujo; solo indica que ese actor participa en esa funcionalidad.

Include representa comportamiento obligatorio reutilizado por otro caso. Extend representa comportamiento opcional o condicional que amplía un caso base. La generalización permite decir que un actor especializado hereda capacidades de uno más general, aunque debe usarse con moderación.

## Relaciones y lectura
Un diagrama de casos de uso debe poder leerse con frases simples. Por ejemplo: Recepción puede registrar clientes. Técnico puede registrar diagnósticos. Caja puede registrar pagos. Administrador puede administrar usuarios. Sistema de facturación puede recibir datos para emitir comprobantes.

La asociación se lee como participación funcional. No describe detalle interno, no indica clics y no muestra navegación. Para navegación se usa flujo de pantallas. Para pasos del negocio se usa flujo operativo o BPMN. Para interacción técnica entre pantalla, backend y base de datos se usa UML Secuencia.

Include se lee como siempre incluye. Por ejemplo, Crear orden de reparación puede incluir Buscar o registrar cliente si toda orden necesita estar asociada a un cliente. Otro ejemplo: Registrar pago puede incluir Validar monto si siempre debe validarse antes de guardar.

Extend se lee como puede extender bajo cierta condición. Por ejemplo, Registrar pago puede extenderse con Imprimir comprobante si el usuario decide imprimirlo. Crear orden de reparación puede extenderse con Aplicar descuento si existe una condición especial.

![Diferencia entre include obligatorio y extend condicional.](figure:uml-use-case-include-extend)

Una buena lectura evita frases técnicas prematuras. No se lee como Controller llama a Service, ni como botón abre modal. Se lee desde el objetivo funcional: el actor logra algo usando el sistema.

## Cómo leer el diagrama
Primero identifica el rectángulo del sistema. Todo lo que está dentro son funcionalidades que el software ofrece. Todo actor está fuera. Luego lee cada actor y sus asociaciones.

Ejemplo de lectura:

- Recepción usa el sistema para Registrar cliente y Crear orden de reparación.
- Técnico usa el sistema para Consultar órdenes asignadas y Registrar diagnóstico.
- Caja usa el sistema para Registrar pago e Imprimir comprobante.
- Administrador usa el sistema para Administrar usuarios y Configurar permisos.
- Sistema de facturación participa en Emitir comprobante tributario.

Luego revisa si hay include o extend. Si Crear orden incluye Buscar cliente, significa que esa búsqueda es parte obligatoria o reutilizada del caso base. Si Registrar pago se extiende con Imprimir comprobante, significa que imprimir es condicional u opcional.

Finalmente revisa si los nombres tienen verbo y objeto. Un diagrama lleno de óvalos llamados Clientes, Pagos y Reportes no comunica objetivos; solo lista temas.

## Cómo construirlo paso a paso
Primero define el sistema central con un nombre claro. No escribas solo Sistema. Usa algo como Sistema administrativo de reparaciones y ventas.

Segundo, identifica actores externos. Pregunta quién usa el sistema, quién recibe información, quién envía información y qué sistemas externos participan. No mezcles todavía roles internos con permisos finos.

Tercero, escribe objetivos funcionales con verbo y objeto. Ejemplos: Registrar cliente, Crear orden, Registrar diagnóstico, Registrar pago, Consultar reporte, Administrar usuarios.

Cuarto, elimina elementos que sean pantallas, botones, tablas o pasos demasiado pequeños. Pantalla de clientes, Botón guardar y Tabla pagos no son casos de uso.

Quinto, dibuja el límite del sistema y coloca los casos dentro. Los actores quedan fuera.

Sexto, conecta actores con casos de uso según participación real. No conectes todo con todo solo porque un administrador puede ver muchas cosas.

Séptimo, usa include y extend solo si aportan claridad. Include para comportamiento obligatorio reutilizado. Extend para comportamiento opcional o condicional.

Octavo, complementa los casos importantes con una especificación textual. El diagrama da vista general; la especificación da detalle funcional.

## Microejemplo administrativo
Sistema: tienda de reparación de celulares.

Actores:

- Recepción.
- Técnico.
- Caja.
- Administrador.
- Cliente.
- Sistema de facturación.

Casos de uso principales:

- Registrar cliente.
- Registrar equipo.
- Crear orden de reparación.
- Consultar estado de orden.
- Registrar diagnóstico.
- Actualizar avance de reparación.
- Registrar pago.
- Imprimir comprobante.
- Administrar usuarios.
- Configurar permisos.
- Generar reporte de reparaciones.

Lectura funcional:

Recepción registra clientes, equipos y órdenes. Técnico consulta órdenes asignadas y registra diagnóstico. Caja registra pagos. Administrador administra usuarios, permisos y reportes. El cliente puede aprobar una cotización o recibir estado de reparación. El sistema de facturación puede emitir un comprobante a partir de datos enviados.

Un caso como Crear orden de reparación puede incluir Buscar o registrar cliente. Registrar pago puede incluir Validar saldo pendiente. Imprimir comprobante puede extender Registrar pago si se ejecuta solo cuando el usuario desea imprimir.

## Casos especiales
Un actor no siempre es una persona con usuario y contraseña. Cliente puede ser actor aunque no inicie sesión si aprueba una cotización, recibe una notificación o consulta el estado de una orden mediante un enlace. Sistema de facturación puede ser actor si intercambia información con nuestro sistema.

![Sistema externo tratado como actor UML.](figure:uml-use-case-external-system-actor)

Un mismo usuario físico puede cumplir varios actores. En un negocio pequeño, la misma persona puede hacer recepción y caja. En el diagrama puede aparecer como dos actores si funcionalmente son responsabilidades distintas. Luego roles y permisos decidirán si un usuario concreto tiene ambos permisos.

Otro caso especial es el caso de uso demasiado grande. Gestionar reparaciones puede sonar cómodo, pero es ambiguo. Puede descomponerse en Crear orden, Registrar diagnóstico, Aprobar cotización, Actualizar avance, Registrar entrega y Anular orden. Lo contrario también es un error: Escribir nombre, Presionar guardar o Abrir modal son demasiado pequeños y pertenecen a la interfaz.

Además, un caso de uso importante puede necesitar una especificación textual complementaria. El óvalo dice el nombre de la funcionalidad, pero la especificación explica precondiciones, flujo principal, alternativas, errores y postcondiciones.

![Ficha textual complementaria para un caso de uso.](figure:uml-use-case-textual-spec)

Ejemplo: Registrar pago puede requerir que la orden exista, que el usuario tenga permiso, que el monto sea mayor que cero y que no supere el saldo pendiente. El flujo principal puede terminar con pago registrado; las alternativas pueden incluir monto inválido, orden anulada o usuario sin permiso.

## Cuándo usarlo
Úsalo cuando necesites acordar qué hará el sistema antes de entrar a pantallas o código. Es especialmente útil en reuniones de levantamiento, documentos de alcance, primeras propuestas y planificación de módulos.

También conviene usarlo cuando hay varios actores con necesidades distintas. Si Recepción, Técnico, Caja y Administrador hacen cosas diferentes, los casos de uso ayudan a visualizar esa diferencia sin construir todavía una matriz detallada de permisos.

Úsalo cuando quieras revisar si una funcionalidad pertenece al sistema o a un actor externo. Por ejemplo, el sistema puede Registrar pago, pero el Banco procesa transferencia. El sistema puede Generar reporte, pero el Contador externo interpreta el reporte.

También es útil antes de escribir pruebas funcionales. Si existe un caso de uso llamado Registrar diagnóstico, después se puede comprobar si el sistema permite al técnico seleccionar una orden, ingresar diagnóstico, validar datos y cambiar el estado de la orden cuando corresponde.

## Cuándo no usarlo
No lo uses para representar pasos secuenciales. Si quieres mostrar Cliente entrega equipo, Recepción registra orden, Técnico diagnostica y Caja cobra, probablemente necesitas Flujo operativo o BPMN.

No lo uses para representar navegación. Si quieres mostrar Login, Dashboard, Reparaciones, Detalle de orden y Registrar pago, necesitas Flujo de pantallas.

No lo uses para representar estructura interna. Si quieres mostrar Cliente, OrdenReparacion, Pago, Repuesto y sus relaciones, eso pertenece a Modelo conceptual o UML Clases, según el nivel.

No lo uses para representar detalles de código. Endpoint, Controller, Service, Repository, DTO o tabla SQL no pertenecen al diagrama de casos de uso.

Tampoco conviene abusar de include y extend. A veces una asociación simple y una especificación textual clara comunican mejor que un diagrama lleno de flechas punteadas.

## Errores comunes
Nombrar casos de uso como módulos es un error frecuente. Clientes, Ventas o Reparaciones pueden ser módulos, pero no expresan una meta. Mejor Registrar cliente, Crear venta o Registrar diagnóstico.

Dibujar actores dentro del sistema también es incorrecto. El actor interactúa con el sistema desde fuera. Si aparece dentro del límite, se pierde la idea de frontera.

Otro error es usar include para cualquier relación. Include no significa está relacionado con. Significa que un caso de uso siempre incorpora otro comportamiento obligatorio o reutilizado.

También es común meter pantallas dentro del diagrama. Abrir pantalla de pago no es caso de uso. Registrar pago sí lo es. El recorrido de pantallas se documenta en otro diagrama.

![Errores frecuentes en casos de uso.](figure:uml-use-case-common-errors)

Otro error serio es mezclar actor con entidad. Cliente como actor representa una persona externa que interactúa con el sistema. Cliente como entidad representa datos del negocio. Pueden tener el mismo nombre, pero no son lo mismo.

Finalmente, evita casos demasiado abstractos como Gestionar sistema o Administrar todo. Son tan grandes que no ayudan a construir, probar ni negociar alcance.

## Relación con otros diagramas
C4 Contexto ayuda a identificar actores externos y sistemas externos antes de hacer casos de uso.

Mapa de módulos agrupa casos de uso por áreas funcionales como Clientes, Reparaciones, Pagos, Inventario y Reportes.

Roles y permisos detalla qué usuarios o roles están autorizados para ejecutar cada caso de uso.

Flujo operativo muestra el procedimiento real donde aparecen varios casos de uso.

BPMN formaliza procesos de negocio que pueden contener varios casos de uso.

Flujo de pantallas muestra qué pantallas necesita el usuario para ejecutar un caso de uso.

Diccionario de datos define campos y reglas usados por los casos de uso.

UML Actividad puede detallar el flujo interno de un caso de uso.

UML Secuencia muestra cómo colaboran pantalla, backend, servicios y base de datos para ejecutar el caso.

UML Clases define las clases y responsabilidades que soportan esos comportamientos.

## Qué pedirle a la IA después de entenderlo
Puedes pedir:

- A partir de esta descripción del negocio, identifica actores y casos de uso principales.
- Genera un diagrama UML de casos de uso para una tienda de reparación de celulares.
- Agrupa estos casos de uso por módulo: Clientes, Reparaciones, Pagos, Inventario y Reportes.
- Revisa estos casos de uso y detecta cuáles son demasiado grandes, demasiado pequeños o realmente son pantallas.
- Propón relaciones include y extend solo cuando tengan sentido.
- A partir de este mapa de módulos, genera casos de uso por actor.
- Convierte este caso de uso en una especificación textual con flujo principal, alternativas, precondiciones y postcondiciones.
- Relaciona estos casos de uso con roles y permisos.

## Ficha rápida
UML Casos de uso representa actores externos y objetivos funcionales observables.

No representa pantallas, botones, tablas, clases, endpoints ni pasos técnicos.

Actor significa quien interactúa con el sistema desde fuera; puede ser persona, organización o sistema externo.

Caso de uso debe nombrarse con verbo y objeto: Registrar pago, Crear orden, Consultar reporte.

Include significa comportamiento obligatorio reutilizado.

Extend significa comportamiento opcional o condicional.

El límite del sistema separa lo que el software hace de lo que pertenece a actores o sistemas externos.

La pregunta clave es: quién logra qué objetivo usando el sistema.

## Auditoría de frontera teórica

UML Casos de uso pertenece al análisis funcional. Representa metas observables que actores logran usando el sistema.

Un caso de uso no es una pantalla, un botón ni un permiso. Registrar pago puede requerir una pantalla y permisos, pero el caso de uso expresa la meta funcional. Los permisos responden quién está autorizado. El flujo de pantallas responde por dónde navega. UML Actividad o Secuencia detallan cómo se ejecuta.

La frontera correcta es esta: casos de uso dice qué funcionalidad existe para qué actor; actividad, secuencia, estados, pantallas y clases explican distintos aspectos de cómo se realiza.

## Checklist final de estudio
- Puedo identificar actores humanos y sistemas externos.
- Puedo nombrar casos de uso como metas funcionales observables.
- Puedo dibujar el límite del sistema y asociaciones sin convertirlo en pantallas.
- Puedo usar include y extend solo cuando tenga sentido.
- Puedo complementar el diagrama con una especificación textual mínima.

## Referencias / base teórica
Este capítulo se apoya en UML como lenguaje de modelado para visualizar y documentar sistemas software. La guía usa una interpretación práctica orientada a sistemas administrativos: suficiente para levantar alcance, conversar con clientes, ordenar funcionalidades y preparar diagramas posteriores sin convertir el capítulo en una lectura pesada de especificación.
