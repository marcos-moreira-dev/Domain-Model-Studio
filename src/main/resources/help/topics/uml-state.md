# UML Estados

## Introducción
UML Estados permite estudiar el ciclo de vida de una entidad importante del sistema. Es especialmente útil cuando un registro cambia de situación y no todas las acciones son válidas en todo momento.

## Pregunta que responde
¿Qué estados puede tener una entidad, qué eventos provocan cambios, qué transiciones están permitidas y cuáles deberían bloquearse?

## Idea central
Un estado es una situación estable; una acción o evento es algo que ocurre y puede provocar una transición. Confundir estados con pasos es uno de los errores más comunes.

## Qué es
![Símbolos básicos de UML Estados: inicio, estado, transición, guarda y final.](figure:uml-state-symbols)
UML Estados es un diagrama de comportamiento que describe el ciclo de vida de una entidad, objeto, solicitud, trámite o proceso que puede pasar por situaciones estables. No muestra simplemente una lista de pasos. Muestra en qué estado puede estar algo importante, qué evento lo hace cambiar, qué condiciones deben cumplirse y qué estados cierran el ciclo.

En sistemas administrativos, este diagrama es especialmente valioso porque muchas cosas no solo existen: cambian de situación. Una orden de reparación puede estar recibida, diagnosticada, aprobada, en reparación, lista para entrega, entregada, rechazada o anulada. Un pago puede estar pendiente, parcial, confirmado, anulado o reembolsado. Una garantía puede estar solicitada, en revisión, aprobada, rechazada o cerrada.

La idea central es sencilla: un estado representa una situación estable; una transición representa un cambio permitido; un evento dispara ese cambio; una condición o guarda decide si el cambio puede ocurrir; y una acción asociada puede registrar efectos como auditoría, notificación o actualización de fechas.

## Para qué sirve
UML Estados sirve para convertir el campo “estado” en una regla de negocio entendible. En muchos sistemas administrativos se crea un campo llamado estadoOrden, estadoPago o estadoSolicitud, pero nadie define con claridad qué valores son válidos, quién puede cambiarlos, cuándo se puede pasar de uno a otro y qué transiciones deben estar prohibidas. El diagrama de estados evita esa improvisación.

Sirve para responder preguntas como estas: ¿una orden anulada puede volver a reparación?, ¿una orden entregada puede editarse?, ¿un pago confirmado puede eliminarse?, ¿quién puede rechazar una garantía?, ¿qué pasa si el cliente no aprueba la cotización?, ¿la entrega requiere pago completo?, ¿qué historial debe quedar cuando cambia un estado?

También ayuda a diseñar validaciones del backend, permisos, mensajes de error, auditoría y botones visibles en pantalla. Si una orden está “Entregada”, quizá el sistema debe ocultar el botón “Registrar diagnóstico”. Si está “Lista para entrega”, quizá el botón “Entregar” solo debe habilitarse cuando el saldo esté pagado. El diagrama no es código, pero prepara reglas implementables.

## Qué representa
Representa estados, eventos, transiciones, condiciones, acciones asociadas, estados iniciales, estados finales, estados terminales, estados compuestos e historial de cambios.

## Qué no representa
No representa pantallas, tareas de BPMN, pasos operativos sueltos ni mensajes entre servicios. Para esos casos convienen flujo de pantallas, BPMN, UML Actividad o UML Secuencia.

## Elementos principales
![Ciclo de vida de una orden de reparación desde recepción hasta entrega.](figure:uml-state-order-lifecycle)
Los elementos principales son:

- Estado inicial: indica el punto de entrada del ciclo de vida.
- Estado: situación estable y reconocible de la entidad.
- Transición: flecha que conecta un estado origen con un estado destino.
- Evento: hecho o acción que dispara la transición.
- Guarda o condición: regla que debe cumplirse para permitir la transición.
- Acción: efecto que se ejecuta durante la transición.
- Estado final: cierre normal o alternativo del ciclo.

Un estado debe nombrarse como situación, no como acción. “Diagnosticada” es estado; “Registrar diagnóstico” es evento o acción. “Pagado” es estado; “Registrar pago” es evento. “Entregada” es estado; “Entregar equipo” es evento.

Una transición puede leerse con esta forma:

estado actual + evento [condición] / acción = nuevo estado

Ejemplo:

ListaParaEntrega -- entregarEquipo [pagoCompleto] / registrarFechaEntrega --> Entregada

Esa línea dice mucho más que una flecha decorativa. Dice que la entrega solo procede si el pago está completo y que al realizarla se registra una fecha o evidencia.

## Estado
Un estado representa una situación estable durante un periodo. No tiene que durar mucho tiempo, pero debe ser una condición reconocible.

Ejemplos:

Recibida
En diagnóstico
Diagnosticada
Aprobada
En reparación
Lista para entrega
Entregada
Anulada
Rechazada
No reparable

Los nombres de estado deben ser claros y verificables. “Procesando” puede ser demasiado vago si nadie sabe qué significa. “En diagnóstico” es mejor si indica que el técnico está revisando el equipo. “Lista para entrega” es mejor que “Finalizada” si todavía falta entregar físicamente el equipo.

## Evento
Un evento es lo que ocurre para intentar cambiar de estado.

Ejemplos:

registrarDiagnostico
aprobarCotizacion
rechazarCotizacion
iniciarReparacion
marcarComoLista
registrarEntrega
anularOrden
reabrirOrden

El evento no siempre debe permitirse. Puede depender del estado actual, del rol del usuario, de datos obligatorios o de una condición externa.

Ejemplo:

Evento: registrarEntrega
Condición: pagoCompleto
Permiso: reparaciones.entregar
Destino: Entregada

## Transición
La transición es la flecha entre estados. Representa una regla del negocio, no un simple enlace visual.

Ejemplo:

Diagnosticada -- clienteApruebaCotizacion --> Aprobada
Diagnosticada -- clienteRechazaCotizacion --> Rechazada
Recibida -- anularOrden --> Anulada

Una buena transición debe tener origen, destino y evento. Si tiene condición, debe aparecer explícita. Si requiere permiso especial, debe documentarse aunque el dibujo no lo muestre por completo.

## Guarda o condición
La guarda es una condición que debe cumplirse para que la transición sea válida.

Ejemplos:

[pagoCompleto]
[usuarioEsSupervisor]
[hayRepuestosDisponibles]
[diagnosticoRegistrado]
[cotizacionAprobada]

Las guardas son muy útiles porque conectan el diagrama con validaciones reales del sistema. Una pantalla puede mostrar el botón, pero el backend debe validar la condición. No basta con ocultar botones en la interfaz.

## Acción asociada a una transición
Una transición puede producir un efecto adicional.

Ejemplos:

/ registrarFechaEntrega
/ enviarNotificacionCliente
/ crearEntradaHistorial
/ bloquearEdicion
/ generarComprobante

La acción no es el nuevo estado, sino algo que ocurre durante el cambio. Por ejemplo, al pasar una orden a “Entregada”, el sistema puede registrar la fecha de entrega y el usuario responsable.

## Estado inicial, final y terminal
![Estados terminales que cierran el ciclo de vida normal.](figure:uml-state-terminal-states)
El estado inicial indica dónde comienza el ciclo. En una orden de reparación puede ser “Recibida”. En una cotización puede ser “Borrador”. En una garantía puede ser “Solicitada”.

El estado final indica que el ciclo terminó. Puede haber varios finales válidos:

Entregada
Anulada
Rechazada
No reparable
Expirada
Cerrada

Un estado terminal normalmente no debería tener salidas comunes. Si se permite salir de un estado terminal, debe ser una excepción con permiso especial, motivo y auditoría.

Ejemplo:

Entregada -- reabrirOrden [usuarioSupervisor] / registrarMotivo --> Reabierta

Esa transición no debería ser invisible ni automática.

## Relaciones y lectura
![Transición con condición, acción y permiso requerido.](figure:uml-state-guard-condition)
Un diagrama de estados debe poder leerse como reglas del negocio. Por ejemplo:

Una orden empieza como Recibida.
Cuando el técnico registra diagnóstico, pasa a Diagnosticada.
Si el cliente aprueba la cotización, pasa a Aprobada.
Cuando inicia el trabajo técnico, pasa a En reparación.
Cuando se termina el trabajo, pasa a Lista para entrega.
Si el pago está completo y recepción entrega el equipo, pasa a Entregada.

También debe poder leerse lo que no está permitido. Si no existe una flecha de “Anulada” hacia “En reparación”, entonces esa transición no debería ocurrir normalmente. Si existe una transición excepcional para reabrir una orden, debe quedar etiquetada con evento, condición y permiso.

La relación con otros conceptos es directa. El evento suele corresponder a una acción del sistema o del usuario. La condición suele venir de una regla de negocio. El permiso define quién puede ejecutar esa transición. La acción puede generar auditoría, notificación, cambio de fecha, bloqueo o actualización de historial.

## Cómo leer el diagrama
Para leer un diagrama de estados, empieza por la entidad modelada. Luego identifica el estado inicial, sigue las flechas, lee los eventos y observa los estados finales.

Una lectura correcta no dice simplemente “hay una flecha”. Dice:

La orden empieza en Recibida.
Cuando se registra diagnóstico, pasa a Diagnosticada.
Si el cliente aprueba, pasa a Aprobada.
Si el cliente rechaza, pasa a Rechazada.
Si el pago está completo, puede pasar de ListaParaEntrega a Entregada.

También debes preguntar qué transiciones no aparecen. Si no existe transición de “Anulada” a “En reparación”, esa operación debería bloquearse o requerir una regla excepcional.

## Cómo construirlo paso a paso
Una receta práctica:

1. Escoge una sola entidad o proceso con ciclo de vida.
2. Escribe sus estados posibles en lenguaje del negocio.
3. Identifica el estado inicial.
4. Identifica los estados finales o terminales.
5. Dibuja transiciones normales.
6. Nombra cada transición con evento claro.
7. Agrega guardas para condiciones importantes.
8. Agrega acciones asociadas cuando haya auditoría, notificación o actualización.
9. Revisa caminos alternativos: rechazo, anulación, expiración, error.
10. Elimina estados vagos o duplicados.
11. Valida qué rol puede ejecutar cada transición crítica.
12. Convierte el diagrama en reglas de backend y mensajes de error.

## Microejemplo administrativo
Entidad: OrdenReparacion.

Estados posibles:

Recibida
EnDiagnostico
Diagnosticada
Aprobada
EnReparacion
ListaParaEntrega
Entregada
Rechazada
Anulada
NoReparable

Transiciones:

Recibida -- asignarTecnico --> EnDiagnostico
EnDiagnostico -- registrarDiagnostico --> Diagnosticada
Diagnosticada -- aprobarCotizacion --> Aprobada
Diagnosticada -- rechazarCotizacion --> Rechazada
Aprobada -- iniciarReparacion [hayRepuestos] --> EnReparacion
EnReparacion -- finalizarReparacion --> ListaParaEntrega
ListaParaEntrega -- entregarEquipo [pagoCompleto] / registrarFechaEntrega --> Entregada
Recibida -- anularOrden [usuarioSupervisor] / registrarMotivo --> Anulada

Este ejemplo revela reglas importantes: la entrega requiere pago completo; la anulación requiere permiso; el rechazo es un final alternativo; y no se debería pasar de “Recibida” directamente a “Entregada” sin diagnóstico ni reparación.

## Casos especiales
![Caminos alternativos como rechazo, anulación o no reparable.](figure:uml-state-alternative-paths)
Un caso especial frecuente es la anulación. En sistemas administrativos, borrar registros críticos suele ser mala práctica. Es preferible pasar a un estado “Anulada”, registrar el motivo, el usuario y la fecha.

Otro caso es el rechazo. Una reparación diagnosticada puede no avanzar si el cliente rechaza la cotización. Ese camino alternativo debe estar modelado. Si solo dibujas el camino feliz, el sistema terminará improvisando qué hacer con casos reales.

También existen estados compuestos. Por ejemplo, “En reparación” puede contener subestados:

Esperando repuesto
Reparando placa
En prueba
Pendiente de aprobación técnica

No siempre necesitas modelarlos. Solo conviene hacerlo si el negocio necesita distinguirlos, reportarlos o asignar permisos diferentes.

Otro caso especial es el historial de estados.

![Historial de estados para auditoría y trazabilidad humana.](figure:uml-state-history)

Para una orden real puede ser más importante saber no solo su estado actual, sino cómo llegó ahí:

fecha
usuario
estado anterior
estado nuevo
evento
motivo
observación

Ese historial conecta UML Estados con auditoría, trazabilidad humana, reportes y soporte al cliente.

## Cuándo usarlo
Usa UML Estados cuando una entidad tiene ciclo de vida importante y las acciones permitidas dependen de ese ciclo.

Buenos candidatos:

Orden de reparación
Pago
Cotización
Garantía
Ticket de soporte
Solicitud
Pedido
Factura
Usuario
Reserva

También conviene usarlo cuando hay estados terminales, anulaciones, aprobaciones, rechazos, permisos por estado o reglas de transición difíciles de explicar solo con texto.

Ejemplo: si una orden “Entregada” no debe permitir editar diagnóstico, registrar nuevos repuestos o cambiar técnico sin permiso, necesitas una regla de estado clara.

## Cuándo no usarlo
No lo uses para entidades que no tienen ciclo de vida relevante. Si una categoría solo está activa o inactiva, quizá basta con una regla simple. Tampoco lo uses para reemplazar un flujo operativo completo. Si lo que quieres mostrar son pasos humanos de un procedimiento, puede convenir flujo operativo o BPMN. Si quieres mostrar validaciones internas de una operación, puede convenir UML Actividad. Si quieres mostrar llamadas entre pantalla, servicio y base de datos, usa UML Secuencia.

Tampoco lo uses como flujo de pantallas. “Pantalla de diagnóstico” no es un estado de la orden. La orden puede estar “Diagnosticada”; la pantalla es solo una interfaz para registrar o consultar esa información.

## Errores comunes
![Errores comunes al confundir estados con acciones o pantallas.](figure:uml-state-common-errors)
El error más común es confundir estado con acción. “Registrar pago” no es estado; “Pagado” o “Pago confirmado” sí pueden ser estados. “Revisar equipo” no es estado; “En diagnóstico” sí puede serlo.

Otro error es dibujar flechas sin evento. Si una orden pasa de “Diagnosticada” a “En reparación”, debe saberse qué ocurrió: cliente aprobó, técnico inició reparación o supervisor autorizó.

También es común permitir cualquier transición. Eso vuelve inútil el diagrama. Si todo puede pasar a todo, no hay regla de negocio. Deben existir transiciones prohibidas.

Errores frecuentes:

- Estados vagos como “Procesando” o “Gestionando”.
- Mezclar ciclo de vida de orden, pago y garantía en el mismo diagrama.
- No representar estados terminales.
- No modelar anulaciones, rechazos o excepciones.
- No asociar transiciones críticas con permisos.
- No dejar historial cuando el negocio lo necesita.
- Confundir pantalla con estado.
- Confundir acción con estado.

## Relación con otros diagramas
UML Estados se conecta con casi todo el sistema de modelado.

Con el modelo conceptual, identifica entidades que tienen ciclo de vida. Con el diccionario de datos, define campos como estadoOrden, fechaCambioEstado, motivoAnulacion o usuarioResponsable. Con roles y permisos, decide quién puede ejecutar transiciones críticas. Con flujo operativo y BPMN, muestra qué pasos o tareas provocan cambios de estado. Con UML Actividad, detalla las acciones internas durante una transición. Con UML Secuencia, muestra qué servicios, repositorios y objetos participan cuando se ejecuta el cambio. Con UML Clases, define enums, métodos y reglas en clases del dominio. Con flujo de pantallas y wireframes, determina qué botones aparecen o se bloquean según el estado.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

A partir de esta descripción del negocio, identifica qué entidades necesitan diagrama de estados.

Genera un UML de estados para una orden de reparación desde recibida hasta entregada, rechazada o anulada.

Revisa este diagrama y detecta estados vagos, transiciones inválidas, eventos faltantes y finales no definidos.

Convierte este flujo operativo en estados reales de la entidad OrdenReparacion.

Dame una tabla de transiciones con estado origen, evento, condición, destino, acción y permiso requerido.

Relaciona estos estados con botones visibles en pantalla y permisos de usuario.

Convierte este diagrama de estados en reglas de validación para backend.

## Ficha rápida
UML Estados

Representa:
estados válidos, eventos, transiciones, condiciones, acciones y finales.

No representa:
pantallas, botones, tablas, flujo completo del negocio ni llamadas técnicas entre servicios.

Sirve para:
definir ciclos de vida y evitar cambios incoherentes.

Pregunta clave:
¿En qué estados puede estar esta entidad y qué eventos permiten pasar de uno a otro?

Error clásico:
confundir acciones como “registrar pago” con estados como “Pagado”.

Conecta con:
backend, permisos, auditoría, pantallas, diccionario de datos y reglas de negocio.

## Auditoría de frontera teórica

UML Estados modela ciclos de vida. Su unidad central no es la acción, sino el estado estable de una entidad importante y las transiciones permitidas.

Si escribes registrar diagnóstico, aprobar cotización o entregar equipo, probablemente son eventos o acciones. Si escribes Diagnosticada, Aprobada, Lista para entrega o Entregada, estás hablando de estados. UML Actividad puede mostrar los pasos para registrar diagnóstico; UML Estados muestra que después de ese evento la orden pasa a Diagnosticada.

Este diagrama debe conectarse con permisos y wireframes: el estado actual puede habilitar o bloquear botones, exigir confirmaciones o impedir transiciones peligrosas.

## Checklist final de estudio
- Puedo distinguir estado, evento, transición, guarda y acción.
- Puedo modelar ciclos de vida de entidades importantes.
- Puedo detectar estados terminales y transiciones prohibidas.
- Puedo relacionar estados con permisos, pantallas, backend e historial.
- Puedo convertir estados en reglas de negocio implementables.

## Referencias / base teórica
UML Estados pertenece a UML como diagrama de comportamiento. En esta guía se usa con enfoque práctico para sistemas administrativos: órdenes, pagos, garantías, solicitudes y tickets. La intención no es memorizar toda la especificación formal, sino aprender a expresar ciclos de vida que luego puedan convertirse en reglas claras de negocio, validaciones de backend, permisos y trazabilidad.
