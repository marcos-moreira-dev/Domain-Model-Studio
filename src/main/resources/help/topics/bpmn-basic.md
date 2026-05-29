# BPMN básico

## Introducción
BPMN significa Business Process Model and Notation. En español puede entenderse como una notación para modelar procesos de negocio. Su valor principal es que permite dibujar, con símbolos relativamente estandarizados, cómo empieza un proceso, qué tareas ocurren, quién participa, qué decisiones dividen el camino y cómo termina.

En Domain Model Studio este capítulo debe estudiarse como una guía práctica para procesos administrativos, no como un tratado completo de BPMN avanzado. El objetivo no es memorizar todos los símbolos posibles, sino aprender a representar procesos reales de negocio con suficiente claridad para analizarlos, explicarlos y después convertirlos en módulos, pantallas, permisos, reglas y automatizaciones.

Un flujo operativo puede nacer de una conversación informal con el cliente. BPMN básico aparece cuando ese flujo ya necesita más precisión: eventos de inicio, tareas concretas, compuertas de decisión, responsables, caminos alternativos y finales claros.

![Símbolos básicos para leer un flujo BPMN básico.](figure:bpmn-basic-symbols)

## Pregunta que responde
BPMN básico responde:

- ¿Cómo ocurre un proceso de negocio de inicio a fin?
- ¿Qué evento dispara el proceso?
- ¿Qué tareas se ejecutan?
- ¿Qué decisiones cambian el camino?
- ¿Quién realiza cada tarea?
- ¿Qué salidas o finales puede tener el proceso?

Ejemplo en una tienda de reparación de celulares:

- Cliente solicita revisión de equipo.
- Recepción registra la orden.
- Técnico diagnostica el equipo.
- Cliente aprueba o rechaza la reparación.
- Técnico repara si fue aprobada.
- Caja registra el pago.
- Recepción entrega el equipo.

Ese relato puede dibujarse como un proceso BPMN básico.

## Idea central
BPMN básico formaliza un proceso de negocio usando eventos, tareas, compuertas, responsables y flujos de secuencia.

La idea no es dibujar pantallas ni tablas. La idea es representar trabajo organizado. Un proceso BPMN debe poder leerse como una historia operativa completa.

Mal enfoque:

- Pantalla de clientes.
- Botón guardar.
- Tabla de pagos.

Buen enfoque:

- Cliente entrega equipo.
- Recepción registra orden.
- Técnico diagnostica.
- Cliente aprueba reparación.
- Caja registra pago.

## Qué es
BPMN básico es una notación para representar procesos de negocio mediante eventos, tareas, compuertas, flujos y responsables.

## Para qué sirve
Sirve para formalizar procesos administrativos de manera más estándar que un flujo operativo informal.

## Qué representa
Representa:

- procesos de negocio;
- eventos que inician o terminan procesos;
- tareas humanas o apoyadas por sistema;
- decisiones;
- caminos alternativos;
- responsables;
- esperas;
- excepciones simples;
- transferencias de trabajo entre áreas.

Ejemplos de procesos administrativos:

- atender una reparación;
- registrar una venta;
- cerrar caja;
- gestionar una garantía;
- aprobar un descuento;
- comprar repuestos;
- entregar un equipo reparado.

## Qué no representa
BPMN básico no representa principalmente:

- clases de programación;
- base de datos;
- pantallas internas;
- wireframes;
- endpoints;
- paquetes de código;
- servidores;
- estructura interna de backend.

Puede relacionarse con todo eso después, pero BPMN primero explica el proceso del negocio.

## Elementos principales
Sus elementos principales son evento de inicio, tarea, gateway, evento final, flujo de secuencia, pool y lane.

## Proceso de negocio
Un proceso de negocio es una secuencia organizada de actividades que produce un resultado útil para el negocio o para un cliente.

Ejemplo:

Atender reparación de celular.

Ese proceso puede incluir:

- recibir equipo;
- registrar datos;
- diagnosticar;
- cotizar;
- pedir aprobación;
- reparar;
- cobrar;
- entregar;
- cerrar la orden.

Un buen proceso tiene un inicio reconocible y al menos un final reconocible. Si el diagrama no tiene inicio ni fin, probablemente es una lista de tareas, no un proceso bien modelado.

![Proceso BPMN lineal con inicio, tareas y fin.](figure:bpmn-basic-linear-process)

## Evento de inicio
El evento de inicio indica qué dispara el proceso. Se representa como un círculo.

Ejemplos:

- cliente entrega equipo;
- llega solicitud de garantía;
- se recibe pedido de compra;
- se detecta stock bajo;
- cliente solicita devolución;
- cajero inicia cierre de caja.

Un error común es escribir simplemente “Inicio”. Eso no explica nada. Es mejor decir qué evento real inicia el proceso.

Mal:

- Inicio.

Mejor:

- Cliente entrega equipo para revisión.

## Tarea
La tarea representa trabajo concreto. Normalmente se dibuja como un rectángulo redondeado.

Ejemplos:

- Registrar orden.
- Revisar equipo.
- Emitir diagnóstico.
- Solicitar aprobación.
- Registrar pago.
- Entregar equipo.

Conviene nombrar las tareas con verbo y objeto. Una tarea debe expresar una acción comprensible.

Mal:

- Gestión.
- Proceso.
- Validación.

Mejor:

- Validar datos del cliente.
- Registrar diagnóstico técnico.
- Confirmar pago recibido.

## Gateway o compuerta
El gateway representa una decisión o división del flujo. Se dibuja como un rombo.

En una guía básica, el gateway más importante es el exclusivo: solo una salida continúa según la condición.

Ejemplo:

¿Cliente aprueba reparación?

- Sí: reparar equipo.
- No: registrar rechazo y devolver equipo.

![Gateway BPMN con caminos alternativos nombrados.](figure:bpmn-basic-gateway)

La compuerta debe tener una pregunta o condición clara. Además, las salidas deben estar etiquetadas.

Mal:

- Aprobación.

Mejor:

- ¿Cliente aprueba reparación?
- Salida Sí.
- Salida No.

## Evento final
El evento final indica cómo termina el proceso. Se representa como un círculo final.

Un proceso puede tener más de un final.

Ejemplos:

- Equipo entregado.
- Orden cancelada.
- Reparación rechazada.
- Garantía aprobada.
- Garantía rechazada.
- Caja cerrada.

Esto es importante porque no todos los procesos terminan en éxito. Un cliente puede rechazar la reparación, no haber repuesto, el pago puede quedar pendiente o el equipo puede ser no reparable.

## Flujo de secuencia
El flujo de secuencia es la flecha que indica el orden del proceso. No es decoración. Dice qué ocurre después de qué.

Ejemplo:

Cliente entrega equipo → Recepción registra orden → Técnico diagnostica → ¿Cliente aprueba? → Reparar → Cobrar → Entregar.

Las flechas deben permitir leer el proceso sin adivinar.

Si hay muchas flechas cruzadas, tareas sueltas o caminos sin final, el proceso necesita limpieza.

## Pool y lane
Un pool representa un participante grande del proceso, por ejemplo una organización, cliente externo o sistema externo.

Una lane representa una división de responsabilidad dentro de un participante. En sistemas administrativos, las lanes más útiles suelen ser áreas o roles operativos:

- Cliente;
- Recepción;
- Técnico;
- Caja;
- Supervisor;
- Sistema.

![Proceso BPMN con lanes para mostrar responsables.](figure:bpmn-basic-lanes)

Las lanes ayudan a responder:

- ¿Quién hace esta tarea?
- ¿Dónde cambia de responsable el proceso?
- ¿Qué área espera a otra?
- ¿Qué parte podría automatizar el sistema?

## Caminos alternativos
Un proceso real casi nunca tiene solo el camino ideal.

Ejemplo de camino ideal:

Cliente aprueba → técnico repara → caja cobra → recepción entrega.

Caminos alternativos:

- cliente rechaza reparación;
- no hay repuesto;
- el equipo no tiene reparación;
- el cliente no responde;
- el pago queda incompleto.

BPMN básico debe mostrar los caminos alternativos más importantes, no todos los casos imaginables. El criterio es: si el camino cambia el trabajo, el estado, el permiso, el pago o la responsabilidad, probablemente debe aparecer.

## Excepciones, esperas y reprocesos
Una excepción ocurre cuando el proceso no avanza normalmente.

Ejemplos:

- no hay repuesto;
- el cliente no responde;
- falla la prueba del equipo;
- el pago no se confirma;
- el técnico detecta daño adicional.

Una espera representa tiempo muerto o dependencia externa.

Ejemplos:

- esperar aprobación del cliente;
- esperar llegada de repuesto;
- esperar confirmación de transferencia;
- esperar revisión de supervisor.

Un reproceso ocurre cuando una parte del proceso debe repetirse.

Ejemplo:

Reparar → Probar equipo → ¿Funciona? → No → Volver a reparar.

Ese ciclo debe dibujarse de forma controlada, no como una flecha desordenada.

## Proceso actual y proceso propuesto
BPMN también puede servir para comparar cómo trabaja hoy el negocio y cómo debería trabajar después del sistema.

AS-IS significa proceso actual.

TO-BE significa proceso propuesto.

Ejemplo:

AS-IS:

- recepción registra en cuaderno;
- técnico avisa por WhatsApp;
- pagos se recuerdan manualmente;
- reportes se arman a mano.

TO-BE:

- recepción registra orden en sistema;
- técnico actualiza diagnóstico;
- sistema guarda estado;
- caja registra pago;
- sistema genera reporte.

![Comparación entre proceso actual y proceso propuesto.](figure:bpmn-basic-as-is-to-be)

Esta comparación ayuda a no digitalizar desorden. Primero se entiende el proceso actual; luego se diseña un proceso más limpio.

## Diferencia con flujo operativo
El flujo operativo es más libre y cercano a la entrevista con el cliente.

BPMN básico es más formal y usa símbolos más estables: eventos, tareas, gateways, lanes y eventos finales.

Una forma práctica de verlo:

- flujo operativo: “así me contó el cliente que trabajan”;
- BPMN básico: “así documento el proceso con una notación más precisa”.

Si todavía no entiendes el negocio, empieza con flujo operativo. Si ya quieres ordenar un proceso para validarlo o automatizarlo, pásalo a BPMN básico.

## Diferencia con UML Actividad
UML Actividad también muestra acciones y decisiones, pero suele enfocarse más en la lógica de una actividad, caso de uso o comportamiento del sistema.

BPMN está más orientado a proceso de negocio, responsables, áreas y eventos.

Ejemplo:

BPMN:

- cliente solicita reparación;
- recepción registra;
- técnico diagnostica;
- caja cobra.

UML Actividad:

- validar datos;
- calcular saldo;
- registrar pago;
- actualizar estado;
- mostrar confirmación.

## Diferencia con flujo de pantallas
BPMN no modela navegación de interfaz.

BPMN:

- Recepción registra orden.
- Técnico diagnostica.
- Caja registra pago.

Flujo de pantallas:

- Login → Dashboard → Reparaciones → Crear orden → Detalle de orden → Registrar pago.

Ambos se relacionan, pero no son iguales. Una tarea BPMN puede requerir una o varias pantallas, pero la pantalla no es el proceso.

## Relaciones y lectura
Se lee siguiendo el flujo desde el evento de inicio hasta uno o más finales, interpretando decisiones y responsables.

## Cómo leer un BPMN básico
Para leerlo, sigue este orden:

1. Encuentra el evento de inicio.
2. Sigue las flechas de secuencia.
3. Lee cada tarea como una acción concreta.
4. Revisa las lanes para saber quién hace cada tarea.
5. En cada gateway, lee la pregunta y las salidas.
6. Sigue caminos alternativos.
7. Identifica finales posibles.
8. Pregunta si falta algún error, espera o rechazo importante.

Un BPMN bien hecho se puede narrar en voz alta como una historia del negocio.

## Cómo construirlo paso a paso
Una receta práctica:

1. Define el proceso que quieres modelar.
2. Escribe qué evento lo inicia.
3. Lista las tareas principales en orden.
4. Marca quién realiza cada tarea.
5. Identifica decisiones importantes.
6. Etiqueta las salidas de cada decisión.
7. Agrega caminos alternativos relevantes.
8. Agrega esperas o excepciones importantes.
9. Define finales posibles.
10. Revisa si el proceso mezcla demasiadas cosas.
11. Valida el diagrama con un ejemplo real.
12. Simplifica símbolos avanzados si el cliente no los necesita.

## Microejemplo administrativo
Proceso: atención de reparación de celular.

Evento de inicio:

- Cliente entrega equipo para revisión.

Tareas:

- Recepción registra cliente y equipo.
- Recepción crea orden de reparación.
- Técnico revisa equipo.
- Técnico registra diagnóstico.
- Recepción informa cotización al cliente.
- Cliente aprueba o rechaza.
- Técnico repara si fue aprobado.
- Caja registra pago.
- Recepción entrega equipo.

Gateways:

- ¿Cliente aprueba reparación?
- ¿Hay repuesto disponible?
- ¿Pago completo?
- ¿Equipo funciona después de prueba?

Finales:

- Equipo entregado.
- Reparación rechazada.
- Orden anulada.
- Equipo no reparable.

Este proceso revela módulos, datos, permisos, pantallas y estados. No se queda solo en dibujo.

## Casos especiales
Cliente no responde

Puede requerir una espera o camino alternativo. El negocio debe decidir qué pasa después de cierto tiempo.

No hay repuesto

Puede llevar a esperar compra, cancelar reparación o proponer alternativa.

Pago incompleto

Puede impedir entrega o generar estado pendiente.

Daño adicional detectado

Puede devolver el proceso a cotización y aprobación.

Varias áreas participan

Si recepción, técnico, caja y supervisor intervienen, las lanes ayudan a visualizar cambios de responsabilidad.

## Cuándo usarlo
Úsalo cuando el procedimiento ya está suficientemente entendido y conviene documentarlo con una notación más formal.

## Cuándo no usarlo
No lo uses para pantallas, clases, base de datos, arquitectura interna o lógica detallada de servicios.

## Errores comunes
![Errores comunes al modelar BPMN básico.](figure:bpmn-basic-common-errors)

1. Usar BPMN para dibujar pantallas.

Pantalla de clientes no es tarea BPMN. Registrar cliente sí puede ser tarea.

2. Tareas demasiado vagas.

“Gestionar reparación” no explica el trabajo. Mejor separar registrar orden, diagnosticar, cotizar, reparar, cobrar y entregar.

3. Gateways sin pregunta.

Un rombo sin condición obliga a adivinar.

4. Salidas sin etiqueta.

Una compuerta debe decir Sí/No, aprobado/rechazado, disponible/no disponible.

5. No indicar responsables.

Si no se sabe quién hace la tarea, el problema operativo sigue oculto.

6. Solo modelar el camino feliz.

Un proceso real tiene rechazos, cancelaciones, errores y esperas.

7. Mezclar varios procesos en uno.

Ventas, reparación, garantía y compras pueden necesitar diagramas separados.

## Relación con otros diagramas
Flujo operativo:

- puede ser el borrador informal antes de BPMN.

Modelo conceptual:

- BPMN revela entidades como Orden, Pago, Diagnóstico, Repuesto o Garantía.

Diccionario de datos:

- cada tarea puede crear, consultar, validar o modificar datos.

Mapa de módulos:

- el proceso atraviesa módulos como Clientes, Reparaciones, Inventario y Pagos.

Roles y permisos:

- las lanes ayudan a descubrir roles y acciones autorizadas.

Flujo de pantallas:

- cada tarea humana puede requerir una pantalla.

UML Actividad:

- puede detallar lógica interna de una tarea concreta.

UML Secuencia:

- puede mostrar cómo una tarea se ejecuta técnicamente entre pantalla, backend y base de datos.

UML Estados:

- el proceso suele cambiar estados de entidades como OrdenReparacion.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de este flujo operativo, conviértelo en un BPMN básico con inicio, tareas, gateways, lanes y finales.
- Revisa este BPMN y detecta tareas vagas, caminos alternativos faltantes y responsables no definidos.
- Modela el proceso de atención de reparación de celulares desde recepción hasta entrega.
- Separa este proceso en lanes para Cliente, Recepción, Técnico y Caja.
- Dame una versión AS-IS y TO-BE del proceso.
- Simplifica este BPMN para que sea entendible por un cliente no técnico.
- A partir de este BPMN, identifica módulos, pantallas, permisos, datos y estados necesarios.

## Ficha rápida
BPMN básico

Representa:

- procesos de negocio, tareas, decisiones, responsables y finales.

No representa:

- pantallas, clases, tablas, endpoints ni servidores.

Símbolos básicos:

- evento de inicio;
- tarea;
- gateway;
- evento final;
- flujo de secuencia;
- pool y lane.

Sirve para:

- documentar procesos administrativos y prepararlos para automatización.

Error clásico:

- confundir proceso de negocio con navegación de pantallas.

Pregunta clave:

- ¿Qué ocurre en el negocio desde que inicia el proceso hasta que termina?

## Auditoría de frontera teórica

BPMN básico formaliza procesos de negocio. Su valor está en representar tareas, eventos, decisiones y responsables con una notación más estándar que el flujo operativo.

Si el objetivo es conversar rápido con el cliente sin cuidar notación, probablemente conviene flujo operativo. Si el objetivo es documentar un proceso con inicio, tareas, compuertas, lanes y finales claros, conviene BPMN. Si el objetivo es detallar cómo una operación se ejecuta dentro del sistema, con validaciones específicas, conviene UML Actividad o UML Secuencia.

BPMN tampoco debe convertirse en un mapa de pantallas. Una tarea humana puede necesitar una pantalla, pero el diagrama BPMN describe trabajo y responsabilidad, no diseño de interfaz.

## Checklist final de estudio
- Puedo leer inicio, tarea, gateway, flujo, pool, lane y final.
- Puedo distinguir BPMN de flujo operativo y UML Actividad.
- Puedo representar caminos alternativos, esperas y reprocesos básicos.
- Puedo ubicar responsables sin convertir el diagrama en pantallas.
- Puedo decidir cuándo BPMN aporta formalidad y cuándo basta un flujo simple.

## Referencias / base teórica
BPMN básico se apoya en la familia BPMN mantenida por OMG. Para el uso de Domain Model Studio no se pretende cubrir toda la especificación, sino usar un subconjunto pedagógico y práctico para procesos administrativos: inicio, tareas, decisiones, responsables, flujos y finales.
