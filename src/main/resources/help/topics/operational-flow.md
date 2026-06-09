# Flujo operativo

## Introducción
El flujo operativo es una herramienta para entender cómo trabaja realmente una persona, un área o un negocio antes de convertir ese trabajo en pantallas, módulos, reglas técnicas o procesos formales. Su valor está en capturar el procedimiento con claridad humana: qué se hace primero, quién lo hace, qué información se necesita, qué decisión cambia el camino y qué resultado queda al final.

En muchos negocios pequeños o medianos, el trabajo no empieza ordenado en una notación formal. Empieza como una narración: “cuando llega el cliente, primero revisamos el equipo; si el daño parece sencillo, lo pasamos al técnico; si necesita repuesto, preguntamos al proveedor; si el cliente acepta, seguimos; si no acepta, se devuelve”. El flujo operativo convierte esa conversación en una secuencia visible sin obligar todavía a usar BPMN, UML o una arquitectura técnica.

Por eso es especialmente útil durante levantamientos de información. Permite conversar con el cliente usando su propio lenguaje, detectar pasos ocultos, descubrir responsables reales y separar lo que el negocio hace hoy de lo que debería hacer cuando tenga un sistema.

## Pregunta que responde
El flujo operativo responde:

¿Qué pasos sigue el negocio para realizar una tarea,
quién participa,
qué información se usa,
qué decisiones aparecen
y qué resultado queda al final?

Ejemplo en una tienda de reparación de celulares:

Cliente entrega equipo
Recepción registra datos
Técnico diagnostica
Cliente aprueba o rechaza
Técnico repara
Caja cobra
Recepción entrega equipo

Eso todavía no es BPMN, no es flujo de pantallas y no es diseño de base de datos. Es el trabajo real contado de forma ordenada.

## Idea central
La idea central es:

El flujo operativo representa el procedimiento real del negocio en lenguaje claro,
sin exigir todavía una notación formal estricta.

Sirve para escuchar, ordenar y validar. Si el cliente dice que “normalmente lo hacen por WhatsApp”, “a veces anotan en un cuaderno” o “el técnico avisa cuando puede”, todo eso puede aparecer en el flujo operativo como pasos, esperas, decisiones u observaciones.

El objetivo no es dibujar algo elegante. El objetivo es que el procedimiento sea comprensible y verificable.

![Flujo operativo lineal con pasos claros de trabajo.](figure:operational-flow-linear)

## Qué es
Un flujo operativo es una representación clara de cómo se realiza un procedimiento real del negocio, paso a paso.

## Para qué sirve
Sirve para levantar información con el cliente, detectar responsables, decisiones, esperas, reprocesos y datos generados.

## Qué representa
Representa:

pasos de trabajo
responsables
entradas
salidas
decisiones
esperas
reprocesos
excepciones
observaciones operativas

Un paso puede ser humano:

Recepción registra el equipo.
Técnico revisa la falla.
Caja registra el pago.

También puede ser del sistema:

El sistema genera número de orden.
El sistema calcula saldo pendiente.
El sistema notifica al cliente.

La clave es distinguir quién hace cada cosa.

## Qué no representa
No representa directamente:

pantallas del sistema
botones
clases de programación
tablas SQL
endpoints
servidores
arquitectura interna

Puede ayudar a descubrir todas esas cosas, pero no las reemplaza.

Por ejemplo:

Recepción registra orden

puede implicar después una pantalla, un caso de uso, una tabla, una clase y permisos. Pero en el flujo operativo todavía se lee como paso de trabajo.

## Elementos principales
Sus elementos principales son paso operativo, responsable, entrada, salida, decisión, espera, excepción y reproceso.

## Paso operativo
Un paso operativo es una acción concreta dentro del procedimiento.

Buenos ejemplos:

Recibir equipo
Registrar datos del cliente
Crear orden de revisión
Diagnosticar falla
Solicitar aprobación del cliente
Registrar pago
Entregar equipo

Malos ejemplos:

Gestionar
Procesar
Hacer trámite
Ver cosas
Administrar reparación

Un paso debe tener suficiente detalle para que alguien entienda qué ocurre. Si el paso es demasiado grande, conviene dividirlo. Si es demasiado pequeño, quizá pertenece a una pantalla o formulario, no al flujo del negocio.

## Responsable
El responsable indica quién ejecuta el paso o toma la decisión.

Ejemplos:

Cliente
Recepción
Técnico
Caja
Supervisor
Sistema
Proveedor
Servicio externo

![Flujo operativo organizado por responsables mediante carriles.](figure:operational-flow-swimlane)

Los responsables son importantes porque ayudan a descubrir roles, permisos y pantallas. Si un técnico registra diagnóstico, quizá el sistema necesita un rol Técnico, un permiso para registrar diagnóstico y una pantalla para órdenes asignadas.

## Entradas, salidas y datos generados
Un flujo operativo no solo dice pasos. También puede decir qué entra y qué sale de cada paso.

Ejemplo:

Entrada:
cliente entrega equipo y describe problema.

Paso:
recepción registra la orden.

Salida:
orden creada con número, cliente, equipo y problema reportado.

Datos generados durante el flujo:

ficha de cliente
orden de reparación
diagnóstico técnico
cotización
comprobante de pago
historial de estado
acta de entrega

Estos datos se conectan después con modelo conceptual y diccionario de datos.

## Decisiones
Una decisión divide el camino según una condición.

Ejemplos:

¿El cliente aprueba la reparación?
¿Hay repuesto disponible?
¿El pago está completo?
¿El equipo todavía tiene garantía?
¿La reparación fue exitosa?

Una buena decisión se escribe como pregunta clara. No basta con escribir “aprobación” o “validación”.

![Flujo operativo con decisión y caminos alternativos.](figure:operational-flow-decision)

Las salidas también deben estar nombradas:

Sí / No
Aprobado / Rechazado
Disponible / No disponible
Completo / Pendiente

## Esperas
Muchos procesos administrativos tienen esperas. No todo ocurre inmediatamente.

Ejemplos:

Esperar aprobación del cliente.
Esperar llegada de repuesto.
Esperar confirmación de transferencia.
Esperar revisión del supervisor.

Una espera debe aparecer cuando afecta el trabajo real, los estados del registro o la responsabilidad de seguimiento. En una reparación, “esperar repuesto” puede cambiar el estado de la orden, afectar el plazo de entrega y generar una notificación al cliente.

## Reprocesos
Un reproceso ocurre cuando el flujo vuelve a un paso anterior.

Ejemplo:

Reparar equipo
Probar equipo
¿Funciona?
Sí → entregar
No → volver a revisar

El reproceso no es un error de dibujo; es parte normal de muchos negocios. Lo importante es representarlo con claridad para no vender un flujo falso donde todo sale bien a la primera.

## Excepciones
Una excepción es un camino no ideal pero posible.

Ejemplos:

El cliente no responde.
No hay repuesto.
El equipo no tiene reparación.
El pago está incompleto.
El técnico detecta un daño adicional.
El cliente retira el equipo sin reparar.

Las excepciones son importantes porque suelen convertirse en reglas de negocio, estados, permisos o mensajes del sistema.

## Flujo actual y flujo propuesto
El flujo operativo puede documentar dos versiones.

AS-IS:
cómo trabaja el negocio hoy.

TO-BE:
cómo debería trabajar con el sistema.

Ejemplo:

AS-IS:
recepción anota la orden en cuaderno y avisa al técnico por WhatsApp.

TO-BE:
recepción crea orden en el sistema, el técnico ve órdenes asignadas y el cliente recibe notificación.

![Comparación entre flujo actual y flujo propuesto.](figure:operational-flow-current-vs-proposed)

Esta comparación ayuda a vender el valor del sistema sin hablar todavía de código.

## Diferencia con BPMN
BPMN es más formal. Usa eventos, tareas, gateways, pools y lanes. El flujo operativo es más libre y sirve para levantar información cuando el proceso todavía está desordenado o se está conversando con el cliente.

Flujo operativo:
“Recepción registra equipo, técnico diagnostica, cliente aprueba”.

BPMN:
evento de inicio, tarea, gateway, lane, evento final.

El flujo operativo puede ser el borrador previo de un BPMN.

## Diferencia con UML Actividad
UML Actividad modela flujos de acciones más estructurados, muchas veces dentro de un caso de uso, una operación o una lógica del sistema.

El flujo operativo está más cerca del negocio real. UML Actividad está más cerca de la precisión lógica.

Ejemplo:

Flujo operativo:
Caja cobra al cliente.

UML Actividad:
buscar orden → validar saldo → ingresar monto → validar monto → registrar pago.

## Diferencia con flujo de pantallas
El flujo operativo describe trabajo. El flujo de pantallas describe navegación.

Flujo operativo:
Técnico registra diagnóstico.

Flujo de pantallas:
Login → Dashboard → Órdenes asignadas → Detalle de orden → Registrar diagnóstico.

Confundirlos produce errores. Un paso del negocio puede requerir varias pantallas, y una pantalla puede servir para varios pasos.

![Comparación entre flujo operativo, BPMN y flujo de pantallas.](figure:operational-flow-vs-formal)

## Relaciones y lectura
Se lee como una historia de trabajo: Cliente entrega equipo, Recepción registra orden, Técnico diagnostica, Caja cobra y Recepción entrega.

## Cómo leer un flujo operativo
Debe leerse como una historia ordenada del trabajo.

Ejemplo:

El cliente entrega el equipo.
Recepción registra la orden.
El técnico diagnostica.
Si el cliente aprueba, se repara.
Si no aprueba, se cierra como rechazada.
Caja registra el pago.
Recepción entrega el equipo.

Si el lector no puede narrar el flujo con frases simples, probablemente el diagrama tiene pasos vagos, responsables ausentes o decisiones mal nombradas.

## Cómo construirlo paso a paso
Una receta práctica:

1. Escuchar cómo el negocio cuenta el procedimiento.
2. Identificar el inicio real.
3. Anotar los pasos en orden.
4. Asignar responsable a cada paso.
5. Marcar entradas y salidas importantes.
6. Detectar decisiones.
7. Nombrar las salidas de cada decisión.
8. Agregar esperas, excepciones y reprocesos.
9. Separar flujo actual y flujo propuesto si aplica.
10. Validar el flujo con un caso real.

Caso de validación:

Juan trae un celular con pantalla rota.
Recepción crea orden.
Técnico diagnostica cambio de pantalla.
Cliente aprueba.
Caja cobra anticipo.
Técnico repara.
Caja cobra saldo.
Recepción entrega.

El flujo debería poder explicar ese caso sin inventar pasos ocultos.

## Microejemplo administrativo
Sistema: tienda de reparación de celulares.

Flujo operativo de atención de reparación:

Cliente entrega equipo.
Recepción registra cliente y equipo.
Recepción crea orden de revisión.
Técnico diagnostica la falla.
Recepción comunica diagnóstico al cliente.
Cliente decide si aprueba.
Si aprueba, técnico repara.
Si no aprueba, recepción registra rechazo.
Caja registra pago si corresponde.
Recepción entrega equipo.

Decisiones:

¿Cliente aprueba reparación?
¿Hay repuesto disponible?
¿Pago completo antes de entrega?
¿Reparación fue exitosa?

Datos generados:

cliente
equipo
orden de reparación
diagnóstico
cotización
pago
estado de orden
historial

Ese flujo puede alimentar después BPMN, casos de uso, pantallas, permisos y estados.

## Casos especiales
Paso manual que luego se automatiza

Hoy el negocio puede llamar al cliente por WhatsApp manualmente. En el flujo propuesto, el sistema podría generar una notificación. El flujo operativo permite mostrar ambos escenarios sin decidir todavía la tecnología exacta.

Actor externo

Un proveedor puede intervenir si se necesita repuesto. Aunque no use el sistema, influye en el procedimiento.

Flujo con varias sucursales

Si una sucursal recibe el equipo y otra lo repara, el flujo debe mostrar transferencia, responsable, seguimiento y estado. Eso afecta permisos, despliegue e inventario.

Proceso demasiado grande

“Gestionar reparaciones” puede ser demasiado amplio. Conviene dividir en recepción, diagnóstico, aprobación, reparación, pago y entrega.

## Cuándo usarlo
Úsalo cuando todavía estás entendiendo cómo trabaja el negocio y necesitas una representación simple antes de formalizar.

## Cuándo no usarlo
No lo uses para dibujar pantallas, clases, tablas físicas ni arquitectura técnica.

## Errores comunes
Escribir pasos vagos

Mal:

Gestionar reparación

Mejor:

Registrar orden
Diagnosticar equipo
Solicitar aprobación
Reparar equipo
Entregar equipo

No indicar responsable

Si nadie sabe quién hace el paso, el flujo no sirve para diseñar roles ni permisos.

Omitir decisiones

Un flujo donde todo sale bien puede ocultar la mitad del negocio. Hay que modelar rechazos, esperas, pagos incompletos, anulaciones y errores reales.

Confundir proceso con pantalla

“Dashboard” no es paso operativo. “Técnico consulta órdenes asignadas” sí es paso operativo.

Confundir proceso con base de datos

“Guardar en tabla ordenes” no es paso del negocio. “Registrar orden de reparación” sí lo es.

![Errores comunes al dibujar flujos operativos.](figure:operational-flow-common-errors)

## Relación con otros diagramas
Modelo conceptual:
el flujo revela entidades como Orden, Diagnóstico, Pago o Garantía.

Diccionario de datos:
los pasos generan campos, reglas y validaciones.

Mapa de módulos:
los pasos se agrupan en módulos como Reparaciones, Pagos o Inventario.

Roles y permisos:
los responsables del flujo sugieren roles y autorizaciones.

BPMN:
el flujo operativo puede formalizarse como proceso de negocio.

Flujo de pantallas:
los pasos humanos pueden convertirse en recorridos de interfaz.

UML Casos de uso:
los pasos importantes pueden convertirse en funcionalidades.

UML Estados:
el flujo cambia estados como Recibida, Diagnosticada, En reparación o Entregada.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

A partir de esta entrevista con el cliente, extrae el flujo operativo actual paso a paso.

Convierte esta descripción informal en un flujo operativo con responsables, decisiones, esperas y caminos alternativos.

Detecta qué pasos del flujo generan datos, documentos o cambios de estado.

Separa este flujo en AS-IS y TO-BE.

Revisa este flujo operativo y dime si hay pasos vagos, responsables faltantes o decisiones mal formuladas.

A partir de este flujo operativo, sugiere módulos, pantallas, permisos y entidades necesarias.

Convierte este flujo operativo en un BPMN básico sin hacerlo innecesariamente complejo.

## Ficha rápida
Flujo operativo

Representa:
pasos reales de trabajo, responsables, decisiones, esperas y salidas.

No representa:
pantallas, clases, tablas, endpoints ni arquitectura técnica.

Sirve para:
levantamiento de información, conversación con clientes y análisis inicial.

Diferencia clave:
es más informal que BPMN y más cercano al trabajo real.

Error clásico:
dibujar pasos vagos como “gestionar” o confundir navegación con operación.

Pregunta clave:
¿Qué hace el negocio, quién lo hace y qué resultado deja cada paso?

## Auditoría de frontera teórica

El flujo operativo captura cómo trabaja el negocio en lenguaje práctico. Es anterior o paralelo a BPMN, pero no exige notación formal.

Si estás entrevistando a un cliente y escribiendo pasos como recibir equipo, revisar, diagnosticar, cobrar y entregar, estás en flujo operativo. Si conviertes esos pasos en eventos, tareas, compuertas, pools y lanes, estás en BPMN. Si modelas acciones internas de un caso de uso con decisiones técnicas o validaciones del sistema, estás entrando a UML Actividad.

El flujo operativo tampoco es flujo de pantallas. Puede decir que recepción registra una orden; el flujo de pantallas dirá por qué pantallas navega el usuario para hacerlo.

## Checklist final de estudio
- Puedo describir el trabajo real con pasos, responsables, entradas y salidas.
- Puedo distinguir flujo operativo de BPMN, UML Actividad y flujo de pantallas.
- Puedo identificar decisiones, esperas, reprocesos y excepciones.
- Puedo separar flujo actual AS-IS y flujo propuesto TO-BE.
- Puedo usarlo como base para pantallas, casos de uso y automatización posterior.

## Referencias / base teórica
Este capítulo se apoya en prácticas de análisis de procesos, levantamiento de información y modelado progresivo. Dentro de Domain Model Studio, el flujo operativo debe entenderse como una herramienta práctica previa a notaciones más formales como BPMN o UML Actividad.
