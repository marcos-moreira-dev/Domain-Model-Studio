# UML Actividad

## Introducción
UML Actividad es un diagrama de comportamiento para estudiar cómo avanza una actividad paso a paso. Ayuda a representar acciones, decisiones, validaciones, caminos alternativos, repeticiones y responsabilidades sin bajar todavía al nivel de código fuente.

En sistemas administrativos es útil porque muchas operaciones no son solamente “guardar algo”. Por ejemplo, registrar un pago puede implicar buscar una orden, validar permisos, verificar el estado de la orden, comprobar el monto, registrar el pago, actualizar saldo, generar historial y mostrar confirmación. Un diagrama de actividad permite ordenar esa lógica antes de implementarla.

Este diagrama se parece a un flujo operativo y a BPMN, pero no es exactamente lo mismo. El flujo operativo es más libre y cercano a la entrevista con el negocio. BPMN está más orientado a procesos de negocio formales entre áreas o participantes. UML Actividad se concentra en el flujo lógico de acciones que completan un comportamiento, caso de uso o regla del sistema.

![Símbolos básicos de UML Actividad: inicio, acción, decisión, fork, join y final.](figure:uml-activity-symbols)

## Pregunta que responde
UML Actividad responde:

- ¿Qué acciones ocurren dentro de una actividad?
- ¿En qué orden se ejecutan?
- ¿Qué decisiones dividen el flujo?
- ¿Qué condiciones permiten avanzar?
- ¿Qué errores o caminos alternativos existen?
- ¿Qué responsable ejecuta cada acción, si se usan carriles?
- ¿Cuándo termina la actividad?

Ejemplo:

- El usuario solicita registrar un pago.
- El sistema busca la orden.
- El sistema valida que la orden no esté anulada.
- El sistema verifica que el monto sea correcto.
- Si el monto es válido, registra el pago.
- Si el monto no es válido, muestra error.

## Idea central
La idea central es:

- UML Actividad muestra el flujo de acciones necesario para completar un comportamiento.

No muestra principalmente estructura de clases, arquitectura, pantallas o despliegue. Muestra comportamiento paso a paso.

Un buen diagrama de actividad debe poder leerse como una secuencia lógica:

- iniciar actividad;
- ejecutar acción;
- evaluar condición;
- tomar camino alternativo;
- repetir si hace falta;
- terminar con resultado claro.

## Qué es
UML Actividad es un diagrama UML de comportamiento que representa el flujo lógico de acciones dentro de una actividad, caso de uso, validación o regla del sistema. Muestra cómo se avanza desde un inicio hasta uno o varios finales, pasando por acciones, decisiones, condiciones y caminos alternativos.

## Para qué sirve
Sirve para convertir una descripción informal en una lógica revisable. Ayuda a encontrar validaciones faltantes, errores no considerados, decisiones ambiguas y responsabilidades mezcladas. También sirve como puente entre requerimientos funcionales, pruebas, pantallas y diseño backend.

## Qué representa
Representa:

- acciones;
- decisiones;
- condiciones;
- validaciones;
- caminos alternativos;
- errores controlados;
- repeticiones;
- paralelismo simple;
- responsables mediante carriles;
- inicio y fin de una actividad.

Ejemplo administrativo:

- Validar datos de una orden.
- Registrar diagnóstico técnico.
- Registrar pago.
- Cerrar caja.
- Aprobar una cotización.
- Anular una orden con motivo.

## Qué no representa
No representa principalmente:

- estructura estática de clases;
- tablas de base de datos;
- pantallas completas;
- navegación entre pantallas;
- infraestructura de despliegue;
- procesos BPMN formales con todos sus eventos y pools;
- mensajes temporales entre objetos, como en UML Secuencia.

Puede conectarse con todo eso, pero no lo reemplaza.

Ejemplo incorrecto:

- Pantalla de pagos → Pantalla de confirmación.

Eso es flujo de pantallas.

Ejemplo correcto para actividad:

- Ingresar monto → Validar monto → Registrar pago → Actualizar saldo → Mostrar confirmación.

## Elementos principales
Los elementos principales son nodo inicial, acción, flujo de control, decisión, merge, fork, join, nodo final y swimlane. Con esos elementos se puede representar la mayoría de flujos administrativos sin recurrir a una notación excesivamente pesada.

## Nodo inicial
El nodo inicial indica dónde comienza la actividad. Se dibuja como un círculo sólido.

No siempre significa el inicio del proceso completo del negocio. Significa el inicio del comportamiento que se está modelando.

Ejemplo:

- El usuario presiona “Registrar pago”.
- El técnico selecciona “Guardar diagnóstico”.
- El supervisor solicita “Anular orden”.

## Acción
Una acción es una unidad de trabajo dentro de la actividad.

Ejemplos:

- Buscar orden.
- Validar datos.
- Verificar permiso.
- Calcular saldo pendiente.
- Registrar pago.
- Actualizar estado.
- Mostrar confirmación.

Las acciones deben tener verbos claros. No conviene escribir acciones vagas como:

- Procesar.
- Gestionar.
- Hacer trámite.
- Validación.

Es mejor escribir:

- Validar monto del pago.
- Registrar diagnóstico técnico.
- Actualizar estado de la orden.

![Actividad lineal con inicio, acciones concretas y final.](figure:uml-activity-linear)

## Flujo de control
El flujo de control es la flecha que conecta las acciones y muestra el orden lógico de ejecución.

Ejemplo:

- Buscar orden → Validar estado → Registrar pago → Actualizar saldo.

La flecha no es decoración. Indica que una acción ocurre antes de otra. Si el orden no importa o hay acciones paralelas, debe representarse de otro modo.

## Decisión
Una decisión divide el flujo según una condición. Se representa con un rombo.

Ejemplos:

- ¿Monto válido?
- ¿Usuario tiene permiso?
- ¿Orden está activa?
- ¿Pago completa el saldo?
- ¿Hay errores de validación?

Cada salida de una decisión debe tener una condición clara.

Ejemplo:

- Sí → Registrar pago.
- No → Mostrar error.

![Actividad con decisión, camino exitoso y camino de error.](figure:uml-activity-decision)

## Merge o unión
El merge une caminos alternativos después de una decisión. No significa que varios caminos ocurren al mismo tiempo; solo vuelve a juntar rutas posibles.

Ejemplo:

- Si hay descuento, aplicar descuento.
- Si no hay descuento, continuar.
- Ambos caminos llegan a calcular total final.

## Fork y join
Fork y join sirven para representar paralelismo o acciones independientes que deben sincronizarse.

- Fork divide el flujo en ramas paralelas.
- Join espera que las ramas necesarias terminen para continuar.

Ejemplo:

- Al cerrar una reparación, el sistema puede registrar historial, generar comprobante y preparar notificación.
- Luego muestra el cierre completado.

![Fork y join para acciones paralelas o independientes.](figure:uml-activity-fork-join)

No conviene usar paralelismo solo para que el diagrama se vea más complejo. Si las acciones realmente ocurren una después de otra, deben modelarse como secuencia normal.

## Nodo final
El nodo final indica que la actividad terminó. Puede haber más de un final.

Ejemplos:

- Pago registrado.
- Diagnóstico guardado.
- Orden anulada.
- Error mostrado y operación cancelada.

En diagramas administrativos conviene mostrar finales alternativos si cambian el resultado del negocio.

## Swimlanes o carriles
Los carriles separan quién ejecuta cada acción. Pueden representar personas, roles, áreas o incluso el sistema.

Ejemplo:

- Usuario: ingresa datos.
- Sistema: valida información.
- Backend: registra cambios.
- Caja: recibe confirmación.

![Actividad con carriles para usuario y sistema.](figure:uml-activity-swimlanes)

Los carriles son útiles cuando el flujo mezcla acciones humanas y acciones automáticas. Si no se distinguen, el diagrama puede volverse ambiguo.

## Validaciones y errores
UML Actividad es muy útil para representar validaciones.

Ejemplo:

- Ingresar datos del cliente.
- Validar cédula.
- ¿Cédula válida?
- Si sí, guardar cliente.
- Si no, mostrar error y permitir corrección.

Las validaciones deben expresarse como acciones o decisiones claras. Esto ayuda después a diseñar backend, formularios y pruebas.

## Caminos alternativos y repetición
Una actividad no debe mostrar solo el camino ideal. Debe considerar alternativas importantes.

Ejemplo:

- Si la orden está anulada, no permite registrar pago.
- Si el monto supera el saldo, muestra error.
- Si faltan datos obligatorios, vuelve al formulario.

También puede haber repetición:

- Ingresar datos → Validar → ¿Hay errores? → Corregir datos → Validar otra vez.

## Diferencia con BPMN
BPMN se orienta a procesos de negocio. Suele mostrar eventos, tareas, gateways, pools y lanes con una semántica de proceso organizacional.

UML Actividad se orienta a flujo de acciones. Puede detallar la lógica interna de un caso de uso, una operación del sistema o una regla de negocio.

Ejemplo BPMN:

- Cliente solicita reparación → Recepción registra orden → Técnico diagnostica → Caja cobra.

Ejemplo UML Actividad:

- Buscar orden → Validar estado → Validar monto → Registrar pago → Actualizar saldo.

## Diferencia con flujo operativo
El flujo operativo es más libre y sirve para levantar cómo trabaja el negocio en una entrevista.

UML Actividad es más preciso. Sirve cuando ya se quiere ordenar la lógica y representar decisiones, validaciones y finales con mayor claridad.

## Diferencia con UML Secuencia
UML Actividad muestra el flujo lógico de acciones.

UML Secuencia muestra los mensajes entre participantes a lo largo del tiempo.

Ejemplo actividad:

- Validar pago → Guardar pago → Actualizar saldo.

Ejemplo secuencia:

- PantallaPago → PagoService → PagoRepository → BaseDatos.

![Comparación entre actividad y secuencia.](figure:uml-activity-vs-sequence)

## Relaciones y lectura
La lectura se hace siguiendo las flechas desde el inicio. Cada acción debe leerse como una frase con verbo; cada decisión debe leerse como una pregunta o condición; cada salida debe indicar cuándo se toma ese camino. Si hay carriles, también debe leerse quién ejecuta cada acción.

## Cómo leer un diagrama de actividad
Para leerlo correctamente:

- Empieza por el nodo inicial.
- Sigue las flechas de control.
- Lee cada acción como verbo.
- En cada rombo, lee la condición.
- Sigue cada camino alternativo.
- Observa si hay carriles y responsables.
- Identifica finales exitosos y finales alternativos.

Un buen diagrama puede leerse en voz alta:

- El usuario ingresa datos.
- El sistema valida el monto.
- Si el monto es válido, registra el pago.
- Si no es válido, muestra error.

## Cómo construirlo paso a paso
Receta práctica:

- Elige una actividad concreta.
- Define qué evento la inicia.
- Escribe las acciones principales con verbos claros.
- Agrega decisiones importantes.
- Etiqueta las salidas de cada decisión.
- Agrega errores controlados.
- Agrega repeticiones si el usuario puede corregir datos.
- Agrega carriles si participan varios responsables.
- Define uno o varios finales.
- Revisa que no estés modelando pantallas ni mensajes técnicos demasiado detallados.

## Microejemplo administrativo
Actividad: Registrar pago de una orden de reparación.

Flujo:

- Inicio: Caja solicita registrar pago.
- Buscar orden.
- Validar que la orden exista.
- Validar que la orden no esté anulada.
- Ingresar monto y método de pago.
- Validar monto.
- ¿Monto válido?
- Si no, mostrar error y corregir.
- Si sí, registrar pago.
- Actualizar saldo pendiente.
- ¿Saldo queda en cero?
- Si sí, marcar orden como pagada.
- Mostrar confirmación.
- Fin.

Este diagrama no explica todavía qué clase guarda el pago ni qué endpoint se llama. Eso puede ir en UML Secuencia o UML Clases.

## Casos especiales
Actividad con permisos

Antes de ejecutar una acción crítica puede haber una validación:

- Validar permiso.
- ¿Tiene permiso?
- Si sí, continuar.
- Si no, mostrar error de autorización.

Actividad con estado

Una acción puede depender del estado de un registro.

Ejemplo:

- Solo se puede entregar una orden si está lista para entrega.
- Solo se puede anular si no está entregada.

Actividad con datos obligatorios

Cuando faltan datos:

- Mostrar errores.
- Permitir corrección.
- Volver a validar.

Actividad con cierre irreversible

Algunas actividades terminan en estados difíciles de revertir:

- Cerrar caja.
- Anular pago.
- Entregar equipo.

Estas deben mostrar validaciones, permiso y confirmación.

## Cuándo usarlo
Úsalo cuando necesitas detallar la lógica de un caso de uso, una validación, una operación administrativa o una regla de negocio. Es especialmente útil para registrar pagos, anular órdenes, aprobar cotizaciones, validar formularios, cerrar caja o cambiar estados con condiciones.

## Cuándo no usarlo
No lo uses para dibujar pantallas completas, arquitectura técnica, clases estáticas, tablas físicas o despliegue. Si el objetivo principal es representar un proceso organizacional con eventos y participantes, BPMN puede ser mejor. Si el objetivo es representar llamadas entre objetos o servicios, UML Secuencia es más adecuado.

## Errores comunes
![Errores comunes: acciones vagas, decisiones sin condición y mezcla con pantallas.](figure:uml-activity-common-errors)

Errores frecuentes:

- Usar acciones vagas como “gestionar” o “procesar”.
- No etiquetar salidas de decisión.
- Confundir pantalla con acción.
- Dibujar BPMN cuando se quería lógica de una operación.
- Dibujar secuencia de servicios cuando se quería flujo lógico.
- No mostrar errores ni caminos alternativos.
- Usar fork y join sin paralelismo real.
- Hacer un diagrama gigante con varias actividades mezcladas.

## Relación con otros diagramas
- UML Casos de uso: una actividad puede detallar el flujo interno de un caso de uso.
- BPMN: puede mostrar el proceso general; actividad detalla lógica específica.
- Flujo operativo: puede ser el borrador informal antes de la actividad.
- UML Secuencia: muestra quién se comunica con quién para ejecutar las acciones.
- UML Estados: define si una acción es válida según el estado de una entidad.
- Diccionario de datos: aporta campos, obligatoriedad y validaciones.
- Roles y permisos: indica si una acción requiere autorización.
- Flujo de pantallas: muestra desde dónde se dispara la actividad.
- UML Clases: define las clases o servicios responsables de ejecutar comportamientos.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de este caso de uso, genera un UML de actividad con acciones, decisiones, errores y finales.
- Convierte este flujo operativo en un diagrama de actividad UML.
- Revisa este diagrama de actividad y detecta acciones vagas, decisiones sin condición o finales faltantes.
- Modela la actividad “Registrar pago” incluyendo validación de permisos, validación de monto y confirmación.
- A partir de este formulario, genera el flujo de validación y guardado como UML Actividad.
- Diferencia qué parte debería ir en BPMN y qué parte debería ir en UML Actividad.
- Relaciona esta actividad con pantallas, permisos, datos y clases responsables.

## Ficha rápida
UML Actividad representa:

- acciones;
- decisiones;
- condiciones;
- validaciones;
- caminos alternativos;
- errores controlados;
- inicio y final de una actividad.

No representa principalmente:

- pantallas completas;
- clases estáticas;
- tablas físicas;
- despliegue;
- mensajes temporales entre objetos.

Sirve para:

- detallar casos de uso;
- ordenar lógica de negocio;
- preparar validaciones;
- descubrir errores y caminos alternativos;
- conectar análisis funcional con implementación.

Error clásico:

- Confundir estado o pantalla con acción.

Pregunta clave:

- ¿Qué acciones se ejecutan, en qué orden y bajo qué condiciones?

## Auditoría de frontera teórica

UML Actividad modela flujo lógico de acciones, decisiones y validaciones. Se parece a BPMN, pero no tiene el mismo centro.

BPMN mira procesos de negocio con participantes y notación de proceso. UML Actividad puede detallar el comportamiento interno de un caso de uso, una operación o una regla. UML Secuencia, en cambio, muestra mensajes entre participantes técnicos u objetos a lo largo del tiempo.

Una señal práctica: si estás preguntando qué acciones ocurren y bajo qué condiciones, usa actividad. Si preguntas quién llama a quién, usa secuencia. Si preguntas por qué estados pasa una entidad, usa estados.

## Checklist final de estudio
- Puedo modelar acciones, decisiones, caminos alternativos y finales.
- Puedo distinguir actividad de BPMN, flujo operativo y secuencia.
- Puedo representar validaciones, errores, repeticiones y carriles.
- Puedo usarla para detallar un caso de uso o comportamiento.
- Puedo evitar acciones vagas como procesar o gestionar.

## Referencias / base teórica
Este capítulo se apoya en la semántica general de UML Actividad dentro de UML, pero está redactado con enfoque práctico para sistemas administrativos. El objetivo no es cubrir todos los detalles formales de UML, sino enseñar el subconjunto necesario para modelar flujos de acciones, decisiones, validaciones y responsabilidades de forma útil.
