# Modelo conceptual

## Introducción
El modelo conceptual es el punto de partida para comprender el dominio del negocio antes de pensar en tablas, clases, pantallas o código. Su utilidad está en ordenar el vocabulario: qué cosas existen, qué datos las describen y cómo se relacionan.

## Pregunta que responde
El modelo conceptual responde: qué conceptos fundamentales existen en el dominio, qué información básica describe a cada concepto y qué relaciones significativas existen entre ellos. No responde todavía qué pantalla se usará, qué tabla exacta habrá en PostgreSQL, qué endpoint expondrá el backend o qué clase tendrá cierto método.

En una entrevista con un negocio, esta pregunta puede reformularse así: de todo lo que me estás contando, qué objetos, personas, documentos, transacciones o registros necesitan existir en el sistema para que el trabajo tenga sentido. Si el cliente dice “recibimos el celular, anotamos el daño, luego el técnico revisa y después cobramos”, el modelo conceptual debe ayudar a descubrir entidades como Equipo, OrdenReparacion, DiagnosticoTecnico y Pago.

Una buena prueba consiste en leer el modelo en voz alta. Si el diagrama permite decir frases como “un cliente puede tener muchos equipos” o “una orden puede recibir varios pagos”, entonces está representando reglas del negocio. Si solo permite decir “tabla uno se conecta con tabla dos”, todavía está demasiado cerca de la implementación.

## Idea central
La idea central es que el modelo conceptual representa el significado del negocio, no la implementación técnica del sistema. Una entidad conceptual puede terminar convertida en una tabla, una clase, un formulario o un módulo, pero no nace como eso. Nace como una cosa relevante del mundo que el sistema necesita entender.

Cliente, Equipo, OrdenReparacion, Pago y Tecnico son conceptos del negocio. En cambio, btnGuardar, tabla_ordenes, ClienteController, pantallaClientes o GET /clientes son detalles de interfaz, código o infraestructura. Esos detalles pueden aparecer después, pero no deben contaminar el primer análisis conceptual.

El modelo conceptual trabaja en un nivel donde todavía se puede conversar con personas no técnicas. Si un dueño de negocio no puede reconocer lo que aparece en el diagrama, quizá el diagrama se adelantó demasiado hacia la tecnología. La meta inicial es que el negocio diga: sí, esas son las cosas que manejamos y esas son las relaciones importantes.

## Qué es
Un modelo conceptual es una representación del dominio de negocio antes de hablar de pantallas, tablas, código, servidores o librerías. Su propósito es responder una pregunta inicial: qué cosas importantes existen en la realidad que se quiere administrar, qué datos las describen y cómo se relacionan entre sí. En la familia ER, entidad-relación, esas cosas suelen expresarse como entidades, atributos, relaciones, cardinalidades, opcionalidad e identificadores.

En un sistema administrativo, el modelo conceptual funciona como una traducción ordenada del lenguaje del cliente. Cuando una persona explica su negocio, menciona clientes, equipos, pagos, órdenes, productos, técnicos, garantías, estados, comprobantes o reportes. El modelo conceptual toma esas palabras, elimina ambigüedades y las convierte en una estructura comprensible. No intenta programar todavía; intenta entender.

La idea central es sencilla: antes de decidir cómo se implementará el sistema, hay que entender qué representa. Si se omite este paso, se puede terminar diseñando pantallas bonitas o tablas aparentemente correctas, pero con conceptos mal separados. Por ejemplo, en una tienda de reparación de celulares no basta con decir “hay reparaciones”. Conviene descubrir si existe Cliente, Equipo, OrdenReparacion, DiagnosticoTecnico, Repuesto, Pago, Garantia y Tecnico, y también cómo se conectan entre sí.

El modelo conceptual es una especie de mapa semántico inicial. No resuelve todo el sistema, pero le da una base sólida. Una vez entendido el dominio, se puede pasar con más seguridad al diccionario de datos, los módulos, los permisos, los flujos, los diagramas UML, la arquitectura y finalmente el código.

![Símbolos básicos del modelo conceptual: entidad, atributo y relación.](figure:conceptual-chen-symbols)

## Para qué sirve
Sirve para construir una base común entre el negocio, el analista y quien desarrollará el sistema. Permite conversar sobre el significado de los datos sin saltar demasiado pronto a SQL, Java, TypeScript, formularios o APIs. También ayuda a detectar reglas estructurales que luego afectarán formularios, validaciones, reportes, permisos y arquitectura.

Un buen modelo conceptual permite responder preguntas como estas: un cliente puede tener varios equipos o solo uno; una orden de reparación pertenece a un equipo o puede existir sin equipo; una orden puede tener pagos parciales; un técnico puede atender muchas órdenes; un repuesto puede usarse en muchas reparaciones; una garantía se asocia a una reparación, a una venta o a ambas. Cada respuesta cambia el diseño posterior.

También sirve como entrada para otros documentos. El diccionario de datos detalla sus atributos. El mapa de módulos agrupa funcionalidades alrededor de conceptos importantes. Los roles y permisos definen quién puede operar sobre esos datos. Los flujos operativos muestran cómo esos conceptos cambian durante el trabajo. UML Clases puede transformar parte del modelo conceptual en estructura de software. Por eso este capítulo debe estar al inicio de la guía.

## Qué representa
Representa conceptos del dominio y relaciones semánticas entre ellos. Una entidad puede ser una persona, un objeto físico, un documento, una transacción, una responsabilidad o un registro con identidad propia. Un atributo representa una característica de una entidad o de una relación. Una relación representa un vínculo con significado entre dos o más entidades. La cardinalidad y la opcionalidad indican cuántas instancias participan y si esa participación es obligatoria.

Por ejemplo, Cliente, Equipo y OrdenReparacion pueden ser entidades. nombreCompleto, telefonoPrincipal, marca, modelo, imei, fechaRecepcion y estado pueden ser atributos. La relación entre Cliente y Equipo puede leerse como “un cliente registra o posee equipos”. La relación entre Equipo y OrdenReparacion puede leerse como “un equipo puede tener varias órdenes de reparación en el tiempo”.

El modelo también puede representar identificadores lógicos, entidades débiles, entidades asociativas, atributos compuestos, atributos derivados y atributos multivaluados. No todos estos recursos son necesarios en un primer boceto, pero conocerlos evita forzar todo como entidades simples con atributos simples.

## Qué no representa
No representa interfaz, navegación, botones, menús, pestañas, rutas web, componentes frontend, endpoints, clases internas, código, infraestructura ni despliegue. Si aparecen frases como “pantalla de clientes”, “botón guardar”, “GET /clientes”, “ClienteService”, “tabla_clientes” o “VARCHAR(255)”, probablemente se mezclaron niveles de abstracción.

Tampoco representa el diccionario de datos completo. El modelo puede indicar que Cliente tiene teléfono, pero no necesariamente define si ese teléfono es obligatorio, si permite WhatsApp, si debe ocultarse parcialmente, si se valida con formato ecuatoriano o si puede repetirse. Esa precisión pertenece al diccionario de datos.

No representa el flujo temporal de un proceso. “Recibir equipo”, “diagnosticar”, “reparar” y “entregar” son acciones o pasos operativos. Pueden revelar entidades, pero no son entidades por sí mismas. Si se quiere representar orden temporal, conviene usar flujo operativo, BPMN o UML Actividad. Si se quiere representar estados como Recibida, Diagnosticada o Entregada, conviene usar UML Estados.

## Elementos principales
Los elementos principales son entidad, atributo, relación, cardinalidad, opcionalidad e identificador. Esos elementos forman la base de la lectura entidad-relación. Con ellos ya se puede explicar buena parte de un sistema administrativo.

La entidad responde “qué cosa existe”. El atributo responde “qué dato describe esa cosa”. La relación responde “cómo se conecta una cosa con otra”. La cardinalidad responde “cuántas pueden participar”. La opcionalidad responde “si debe o puede participar”. El identificador responde “cómo distingo una instancia de otra”.

En un modelo inicial no conviene llenar todo de detalles. Es mejor empezar con pocas entidades bien nombradas y relaciones claras. Luego se agregan casos especiales cuando el negocio los exige.

## Por qué se hace antes de programar
Se hace antes de programar porque muchos errores caros nacen de entender mal el dominio. Una tabla mal separada, un formulario que pide datos equivocados, un reporte que mezcla conceptos o una regla de negocio duplicada suelen aparecer cuando se empezó por la pantalla o por la base de datos sin entender primero qué significa cada cosa.

El modelo conceptual permite detectar preguntas tempranas. Si una orden puede tener varios pagos, el sistema no debe asumir un solo pago. Si un equipo puede volver varias veces por reparación, no basta con guardar “última reparación” dentro de Equipo. Si un repuesto puede usarse en muchas órdenes, hace falta modelar la relación y posiblemente una entidad intermedia. Si una orden puede estar anulada, rechazada, entregada o en garantía, más adelante hará falta pensar estados y permisos.

Programar sin modelo conceptual es posible, pero aumenta la probabilidad de rehacer. Modelar primero no elimina todos los errores, pero reduce ambigüedad, ayuda a conversar con el cliente y prepara mejor los siguientes documentos.

## Entidad
Una entidad representa una cosa relevante del negocio sobre la cual se necesita guardar, consultar o razonar información. No tiene que ser física; también puede ser un documento, una transacción o un registro con identidad propia.

![Entidad como concepto principal del dominio.](figure:conceptual-entity-symbol)

Ejemplos de entidades en una tienda de reparación de celulares: Cliente, Equipo, OrdenReparacion, DiagnosticoTecnico, Pago, Repuesto, Garantia y Tecnico.

Una buena entidad suele tener identidad propia, datos relevantes, participación en relaciones y reconocimiento dentro del lenguaje del negocio. Cliente no es solo un nombre escrito en una pantalla; es una persona atendida por el negocio. OrdenReparacion no es solo una fila de tabla; es el registro que agrupa problema reportado, equipo, diagnóstico, estado, pagos y entrega.

Error común: tomar una pantalla como entidad. “Pantalla de clientes” no es entidad; Cliente sí. Otro error: tomar una acción como entidad. “Reparar” es una acción; OrdenReparacion o ServicioReparacion pueden ser entidades según el caso.

## Atributo
Un atributo describe una característica de una entidad o de una relación. En Cliente pueden existir nombreCompleto, cedula, telefonoPrincipal y correo. En Equipo pueden existir marca, modelo, imei, color y estadoFisico. En OrdenReparacion pueden existir fechaRecepcion, problemaReportado, estado y observaciones.

![Atributo como dato que describe una entidad.](figure:conceptual-attribute-symbol)

Un atributo no debería convertirse en entidad solo por costumbre. Debe convertirse en entidad cuando necesite identidad propia, historial, reglas, varias ocurrencias o relaciones con otros conceptos. telefonoPrincipal puede ser atributo si solo interesa un teléfono principal. Pero si el negocio necesita varios teléfonos, prioridad, autorización de WhatsApp y observaciones, TelefonoCliente puede convertirse en entidad.

También ocurre lo contrario: algo que parecía entidad puede ser atributo. Si “color” solo describe el equipo, probablemente es atributo. Si “color” forma parte de un catálogo gestionado, con códigos, equivalencias y reglas de inventario, podría tener más peso.

## Relación
Una relación indica cómo se conectan dos o más entidades. Debe poder leerse como una frase del negocio. Cliente registra OrdenReparacion. Equipo tiene OrdenReparacion. Tecnico atiende OrdenReparacion. OrdenReparacion genera Pago. OrdenReparacion usa Repuesto.

![Relación como vínculo con significado entre entidades.](figure:conceptual-relationship-symbol)

Una relación vaga como “tiene” puede servir en un borrador, pero conviene refinarla cuando el significado importe. No es lo mismo “Cliente posee Equipo” que “Cliente entrega Equipo” o “Cliente compra Equipo”. Cada verbo sugiere reglas distintas.

Para revisar una relación, conviene leerla desde ambos lados. Un Cliente registra muchas Órdenes. Cada Orden pertenece a un Cliente. Una Orden recibe varios Pagos. Cada Pago corresponde a una Orden. Si la frase suena extraña, quizá la relación está mal nombrada o las entidades no están bien separadas.

## Cardinalidad
La cardinalidad indica cuántas instancias de una entidad pueden relacionarse con otra. Es una de las partes más importantes porque transforma una línea en una regla de negocio.

![Cardinalidades comunes: uno, opcional y muchos.](figure:conceptual-cardinality-symbol)

Ejemplos: un Cliente puede tener muchas Órdenes; una Orden pertenece a un Cliente. Una Orden puede tener cero, uno o varios Pagos; cada Pago pertenece a una Orden. Un Técnico puede atender muchas Órdenes; una Orden puede estar asignada a un Técnico.

Cardinalidades típicas: 1, 0..1, 0..*, 1..*. En pata de gallo suelen verse como símbolos en los extremos de la relación. En Chen pueden aparecer como anotaciones cercanas a las entidades o relaciones.

Sin cardinalidad, el modelo queda incompleto. Si alguien dibuja Cliente — Orden sin indicar cantidad, no se sabe si el sistema permitirá muchas órdenes por cliente, una sola, órdenes sin cliente o clientes sin órdenes.

## Opcionalidad
La opcionalidad indica si una participación es obligatoria o posible. No es lo mismo “debe tener” que “puede tener”. Esa diferencia afecta formularios, validaciones y reglas de negocio.

Ejemplo: una OrdenReparacion debe pertenecer a un Cliente, pero puede no tener Pagos al momento de crearse. Un Equipo puede tener IMEI registrado, pero quizá no siempre se conoce. Un Cliente puede tener correo, pero tal vez el dato obligatorio sea el teléfono.

La opcionalidad suele aparecer combinada con cardinalidad. “0..* pagos” significa que una orden puede tener cero o muchos pagos. “1..* pagos” significa que debe tener al menos uno. Esa diferencia cambia el diseño del sistema.

## Identificador
Un identificador permite distinguir una instancia de otra. En análisis conceptual no siempre debe comenzar como un id técnico. numeroOrden, cedula o imei pueden ser identificadores naturales candidatos, pero deben revisarse con cuidado porque pueden faltar, repetirse, cambiar o no ser confiables.

Ejemplos: Cliente puede identificarse por cédula, pero algunos negocios atienden clientes sin documento formal. Equipo puede identificarse por IMEI, pero puede no estar disponible. OrdenReparacion puede tener un número interno propio, más confiable para el negocio.

El identificador conceptual ayuda a hablar con el cliente. La clave técnica definitiva pertenece a una etapa posterior de diseño lógico o físico.

## Entidad débil
Una entidad débil depende de otra para tener sentido. No suele existir de manera útil sin una entidad principal.

![Entidad débil como elemento dependiente de una entidad principal.](figure:conceptual-weak-entity-symbol)

Ejemplos: DetalleRepuesto depende de OrdenReparacion. HistorialEstadoOrden depende de OrdenReparacion. DetallePago depende de Pago u Orden, según el diseño.

Una entidad débil suele aparecer cuando hay información repetida o detallada dentro del contexto de una entidad principal. Si una orden tiene muchos cambios de estado, cada cambio puede ser un HistorialEstadoOrden con fecha, estado anterior, estado nuevo, usuario y motivo.

## Entidad asociativa
Una entidad asociativa aparece cuando una relación muchos a muchos tiene datos propios. Es uno de los casos más importantes en sistemas administrativos.

![Entidad asociativa para resolver una relación muchos a muchos con datos propios.](figure:conceptual-associative-entity-symbol)

Ejemplo: una OrdenReparacion puede usar muchos Repuestos y un Repuesto puede aparecer en muchas Órdenes. Si la relación necesita cantidad, precioUnitario, descuento u observación, conviene crear DetalleRepuestoOrden.

La entidad asociativa evita esconder datos dentro de una línea. En vez de decir solamente Orden usa Repuesto, se registra cómo lo usa, en qué cantidad y con qué precio aplicado.

## Atributo multivaluado
Un atributo multivaluado puede tener varios valores para la misma entidad.

![Atributo multivaluado como dato con varias ocurrencias.](figure:conceptual-multivalued-attribute-symbol)

Ejemplos: un Cliente puede tener varios teléfonos; un Equipo puede tener varios accesorios entregados; una Orden puede tener varias observaciones.

En Chen puede representarse como atributo multivaluado. En diseño posterior, muchas veces se transforma en entidad o tabla separada. La decisión depende de si esos valores necesitan más información, historial, validación o búsqueda.

## Atributo derivado
Un atributo derivado se calcula a partir de otros datos.

![Atributo derivado como dato calculado desde otros valores.](figure:conceptual-derived-attribute-symbol)

Ejemplos: saldoPendiente = totalOrden - pagosRealizados. diasEnReparacion = fechaActual - fechaRecepcion. totalOrden = manoObra + repuestos - descuento.

El modelo conceptual puede nombrar un atributo derivado porque es importante para el negocio, pero debe indicar que no se captura manualmente como dato base. Luego el diccionario de datos y el backend deberán decidir si se calcula al vuelo, se materializa o se guarda con trazabilidad.

## Vista Chen
Chen es una notación clásica de la familia ER. Suele representar entidades con rectángulos, atributos con óvalos y relaciones con rombos. Esta separación visual ayuda mucho al aprendizaje porque obliga a distinguir qué es una cosa del negocio, qué es una característica y qué es un vínculo.

Chen es especialmente útil cuando se está explorando el significado. Si una relación aparece como rombo, el lector se pregunta qué verbo o intención del negocio expresa. Si un atributo aparece como óvalo, se puede discutir si realmente describe a la entidad o si merece convertirse en entidad propia. Si una entidad aparece como rectángulo, se puede preguntar si tiene identidad, historial y relaciones suficientes.

No obstante, Chen puede ocupar bastante espacio cuando el modelo crece. Por eso es excelente para enseñar teoría, revisar conceptos y discutir con calma, pero en sistemas grandes puede necesitar vistas parciales.

## Vista pata de gallo
La notación pata de gallo suele representar entidades como cajas con atributos dentro, y las relaciones como líneas con símbolos de cardinalidad en los extremos. Es más compacta que Chen y suele parecerse más a modelos lógicos de datos.

Ejemplo de lectura: Cliente 1 a muchos OrdenReparacion significa que un cliente puede tener muchas órdenes y que cada orden pertenece a un cliente. La pata de gallo permite ver muchas relaciones en menos espacio.

La pata de gallo es muy práctica cuando ya se quiere acercar el modelo conceptual al diseño lógico. No significa que ya estemos en SQL físico, pero sí permite una lectura más parecida a estructuras de datos.

## Chen y pata de gallo como dos vistas del mismo modelo
Chen y pata de gallo no son dos modelos incompatibles. Son dos formas visuales de representar análisis entidad-relación.

![Comparación entre Chen y pata de gallo para una misma relación.](figure:conceptual-chen-vs-crow-foot)

Chen suele ser más explícito y pedagógico. Pata de gallo suele ser más compacta y cercana al diseño lógico. Ambas pueden expresar la misma idea: un cliente puede registrar muchas órdenes y cada orden pertenece a un cliente.

En la interfaz del programa, lo más sano es entenderlas como vistas o notaciones del modelo conceptual, no como dos dominios separados. El usuario cambia la forma de leer el modelo, no el significado del negocio.

## Relaciones y lectura
Un modelo conceptual se lee como lenguaje del negocio. No se lee como código ni como esquema SQL. Para revisar una relación, conviene leerla desde ambos extremos: un Cliente puede registrar muchos Equipos; cada Equipo pertenece a un Cliente. Un Equipo puede tener muchas Órdenes de reparación; cada Orden de reparación corresponde a un Equipo. Una Orden puede tener cero, uno o varios Pagos; cada Pago se registra para una Orden.

La lectura desde ambos extremos ayuda a descubrir errores. Si se dibuja Cliente 1 a 1 Equipo, habría que preguntar si un cliente de verdad solo puede tener un equipo en toda la historia del negocio. Si se dibuja OrdenReparacion 1 a 1 Pago, habría que preguntar si se aceptan abonos parciales. Si se dibuja Tecnico 1 a 1 Orden, habría que preguntar si un técnico puede atender varias órdenes en un día.

Las relaciones también revelan reglas ocultas. Si una orden puede tener muchos repuestos y un repuesto puede aparecer en muchas órdenes, existe una relación muchos a muchos. Si además la relación necesita cantidad, precio aplicado, descuento u observación, conviene crear una entidad asociativa como DetalleRepuestoOrden. Así el modelo deja de ser una línea pobre y empieza a representar información real del negocio.

## Cómo leer un modelo conceptual
Un modelo conceptual debe poder leerse en voz alta. Si no se puede leer como frases del negocio, probablemente está mal nombrado o incompleto.

Ejemplo de lectura: un Cliente puede tener muchos Equipos. Un Equipo puede tener muchas Órdenes de reparación. Una Orden de reparación pertenece a un Cliente. Una Orden puede tener varios Pagos. Un Técnico puede atender varias Órdenes.

Al leer, conviene buscar verbos, cantidades y obligaciones. ¿El cliente registra equipos o solo órdenes? ¿La orden siempre tiene equipo? ¿El pago siempre cubre toda la orden? ¿El técnico siempre se asigna desde el inicio o puede asignarse después? Esas preguntas refinan el modelo.

## Cómo construirlo paso a paso
Una receta práctica: escuchar cómo el negocio habla de su trabajo. Subrayar sustantivos importantes. Proponer entidades candidatas. Eliminar pantallas, botones, reportes y acciones que no sean entidades. Identificar atributos importantes de cada entidad. Identificar relaciones con verbos claros. Definir cardinalidades. Definir opcionalidad. Buscar relaciones muchos a muchos. Crear entidades asociativas cuando la relación tenga datos propios. Revisar nombres con lenguaje del negocio. Validar el modelo con ejemplos reales.

Ejemplo de validación: Juan trajo un iPhone 11 para reparación. Se creó la orden 00045. El técnico Carlos diagnosticó cambio de pantalla. El cliente pagó en dos partes. Ese caso debería poder representarse en el modelo. Si no se puede, hay conceptos faltantes o relaciones mal definidas.

La construcción debe ser iterativa. El primer modelo casi nunca es perfecto. Se ajusta cuando aparecen excepciones: clientes sin cédula, equipos sin IMEI, pagos parciales, garantías, devoluciones, repuestos sin stock o técnicos externos.

## Microejemplo administrativo
Supón una tienda de reparación de celulares. Cliente representa a la persona atendida. Equipo representa el dispositivo físico. OrdenReparacion representa el registro de atención sobre un equipo. DiagnosticoTecnico representa la evaluación del técnico. Repuesto representa piezas usadas o vendidas. Pago representa dinero recibido. Tecnico representa al responsable técnico. Garantia representa una cobertura posterior asociada a una reparación.

![Microejemplo administrativo con cliente, equipo, orden, pago y repuesto.](figure:conceptual-admin-example)

Las relaciones podrían leerse así: un Cliente puede registrar varios Equipos; cada Equipo pertenece a un Cliente. Un Equipo puede tener varias Órdenes de reparación; cada Orden se asocia a un Equipo. Un Técnico puede registrar muchos Diagnósticos; cada Diagnóstico pertenece a una Orden. Una Orden puede usar varios Repuestos y un Repuesto puede aparecer en muchas Órdenes; por eso aparece DetalleRepuestoOrden con cantidad y precio aplicado. Una Orden puede tener varios Pagos si se aceptan abonos.

Este ejemplo permite descubrir decisiones importantes. Si el negocio no maneja abonos, la relación Orden-Pago cambia. Si no se registra técnico responsable, Tecnico podría quedar fuera al inicio. Si el negocio ofrece garantías, hay que decidir si Garantia se asocia a OrdenReparacion, a DetalleRepuesto, a Venta o a Producto.

## Casos especiales
La relación muchos a muchos es uno de los casos más importantes. Si una OrdenReparacion usa muchos Repuestos y un Repuesto puede usarse en muchas Órdenes, el modelo necesita resolver esa relación. Si la relación tiene datos propios como cantidad, precioUnitario, descuento u observación, se recomienda una entidad asociativa, por ejemplo DetalleRepuestoOrden.

Los atributos multivaluados también requieren criterio. telefonoCliente puede ser atributo si solo interesa un teléfono principal. Pero si el negocio necesita varios teléfonos, tipo de contacto, prioridad, autorización de WhatsApp y observaciones, TelefonoCliente puede ser entidad. Lo mismo puede ocurrir con direcciones, correos, contactos alternativos o documentos adjuntos.

Los atributos derivados se calculan desde otros datos. totalOrden puede depender de repuestos, mano de obra, descuentos e impuestos. saldoPendiente puede depender de total menos pagos registrados. El modelo puede nombrarlos porque son importantes para el negocio, pero debe indicar que son calculados para que luego el diccionario y el backend no los traten como datos manuales sin control.

Las entidades débiles dependen de otra entidad principal para tener sentido. DetallePago, DetalleRepuestoOrden o HistorialEstadoOrden no suelen existir de forma aislada. Tienen sentido porque pertenecen a una orden, un pago o un evento principal. Este tipo de entidad ayuda a conservar trazabilidad.

Los estados también merecen cuidado. EstadoOrden puede ser un atributo simple si solo se muestra el estado actual. Pero si se necesita historial, fecha de cambio, usuario responsable y motivo, conviene modelar HistorialEstadoOrden y luego complementar con UML Estados.

## Cuándo usarlo
Úsalo al inicio del análisis, cuando todavía estás entendiendo el negocio y necesitas separar conceptos antes de diseñar tecnología. También úsalo cuando una conversación con el cliente se vuelve confusa y aparecen muchas palabras mezcladas: clientes, usuarios, equipos, órdenes, pagos, garantías, reportes, ventas o inventario.

Es especialmente útil antes del diccionario de datos, antes del modelo lógico de base de datos, antes de UML Clases y antes de diseñar formularios complejos. También sirve para revisar sistemas ya existentes: si una aplicación tiene muchos parches, reconstruir su modelo conceptual puede revelar por qué se volvió confusa.

## Cuándo no usarlo
No lo uses para representar pasos temporales del negocio. Para eso conviene flujo operativo, BPMN o UML Actividad. No lo uses para representar navegación entre pantallas. Para eso conviene flujo de pantallas. No lo uses para representar cómo se comunican frontend, backend y base de datos. Para eso conviene UML Secuencia, C4 o despliegue técnico.

Tampoco lo uses como sustituto del diccionario de datos. El modelo conceptual dice que existe Cliente.telefono; el diccionario dirá si es obligatorio, qué formato tiene, si es sensible, dónde se usa y cómo se valida. Cada diagrama tiene su lugar.

## Errores comunes
![Errores comunes: pantalla, acción o tabla física confundidas con entidad conceptual.](figure:conceptual-common-errors)

El primer error es confundir entidad con pantalla. “Pantalla de clientes” no es una entidad; Cliente sí. El segundo es confundir entidad con acción. “Reparar” no suele ser entidad; OrdenReparacion o ServicioReparacion pueden serlo. El tercero es usar nombres vagos como Datos, Registro, Información o Gestión, que no dicen qué existe realmente.

Otro error común es diseñar el modelo como si ya fuera SQL físico. En una etapa conceptual no hace falta llenar todo con id, tipos físicos, índices o nombres de columnas definitivos. Eso puede venir después. También es un error omitir cardinalidades. Una línea sin cardinalidad deja ambigua la regla de negocio.

También se suele confundir Cliente con Usuario. Cliente es atendido por el negocio; Usuario es una cuenta que entra al sistema. A veces una persona puede ser ambas cosas, pero conceptualmente no son equivalentes. Algo parecido ocurre con Técnico y Usuario: un técnico puede tener cuenta de usuario, pero el rol operativo y la cuenta del sistema no son el mismo concepto.

Finalmente, es un error hacer el modelo demasiado grande en un solo lienzo. Si aparecen muchas entidades, conviene agrupar por módulos o crear vistas parciales: clientes y equipos, reparaciones, pagos, inventario, garantías, usuarios y permisos. Un modelo conceptual debe aclarar, no producir una maraña.

## Relación con otros diagramas
El diccionario de datos toma atributos del modelo y los precisa con descripción, tipo lógico, obligatoriedad, validaciones, valores permitidos, sensibilidad y uso. El mapa de módulos agrupa funcionalidades alrededor de conceptos como Clientes, Reparaciones, Pagos o Inventario. Roles y permisos define quién puede crear, consultar, editar, anular o exportar información asociada a esas entidades.

El flujo operativo y BPMN muestran cómo el trabajo real crea o modifica entidades. UML Estados explica ciclos de vida, por ejemplo OrdenReparacion pasando por Recibida, Diagnosticada, En reparacion, Lista, Entregada o Anulada. UML Clases puede transformar parte del modelo conceptual en clases de dominio, DTOs, servicios o enums. UML Secuencia puede mostrar cómo una operación concreta consulta o modifica esas entidades. El flujo de pantallas y los wireframes muestran cómo el usuario ve, edita o consulta esos datos.

## Qué pedirle a la IA después de entenderlo
Después de estudiar este capítulo, puedes pedirle a una IA tareas más precisas:

- A partir de esta entrevista con el cliente, identifica entidades principales, atributos y relaciones.
- Convierte esta descripción de una tienda de reparación de celulares en un modelo conceptual ER.
- Dame el modelo en dos vistas: Chen y pata de gallo, manteniendo el mismo significado.
- Detecta relaciones muchos a muchos y posibles entidades asociativas.
- Revisa si estoy confundiendo pantallas, acciones, reportes o tablas físicas con entidades del dominio.
- Propón cardinalidades y explícame qué preguntas debo hacer al cliente para validarlas.
- Identifica atributos derivados, multivaluados y entidades débiles.

## Ficha rápida
Pregunta que responde: qué conceptos importantes existen en el negocio, qué datos los describen y cómo se relacionan.

Símbolos principales: entidad, atributo, relación, cardinalidad, opcionalidad e identificador.

Úsalo cuando: estés levantando información, entendiendo el dominio o preparando diccionario de datos, módulos y base lógica.

Evítalo cuando: necesites modelar pantallas, pasos temporales, infraestructura, clases internas o SQL físico.

Checklist mínimo: entidades con nombres claros, atributos esenciales, relaciones con significado, cardinalidades validadas, opcionalidad explícita, relaciones muchos a muchos revisadas y confusiones con pantallas o acciones eliminadas.

## Auditoría de frontera teórica

El modelo conceptual pertenece a la familia de modelado de datos, pero no debe confundirse con el diccionario de datos ni con UML Clases. Su nivel correcto es semántico: define cosas del negocio, atributos importantes y relaciones con significado.

Una buena regla de lectura es esta: si la conversación todavía gira alrededor de qué significa Cliente, Equipo, Orden o Pago para el negocio, estás en modelo conceptual. Si ya estás definiendo longitud, formato, obligatoriedad, valores permitidos o trazabilidad campo por campo, pasaste al diccionario de datos. Si estás hablando de métodos, servicios, DTOs o responsabilidades de software, pasaste a UML Clases.

El modelo conceptual tampoco debe absorber pantallas ni flujos. Una entidad puede aparecer en una pantalla y puede cambiar durante un proceso, pero eso no convierte a la pantalla ni al proceso en parte del modelo conceptual.

## Checklist final de estudio
- Puedo explicar qué entidades, atributos y relaciones aparecen en el dominio.
- Puedo leer cardinalidades y opcionalidades como frases del negocio.
- Puedo distinguir este modelo de SQL, pantallas y UML Clases.
- Puedo detectar relaciones muchos a muchos, atributos derivados y entidades débiles cuando aparezcan.
- Puedo usarlo como entrada para diccionario de datos, módulos y reglas posteriores.

## Referencias / base teórica
La base conceptual proviene del modelo ER, entidad-relación, especialmente de la tradición iniciada por Peter Chen, y de notaciones ER usadas en análisis conceptual y diseño lógico de datos. En Domain Model Studio, Chen y pata de gallo deben entenderse como vistas útiles de un mismo esfuerzo: comprender entidades, atributos, relaciones y reglas del dominio antes de construir el sistema.
