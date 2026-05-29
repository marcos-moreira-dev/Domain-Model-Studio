# UML Secuencia

## Introducción
UML Secuencia es un diagrama de interacción que muestra cómo varios participantes colaboran en el tiempo para completar un escenario concreto. Su eje principal no es la posición libre de las cajas, sino el orden temporal de los mensajes: lo que está arriba ocurre antes que lo que está abajo.

En aplicaciones administrativas resulta especialmente útil porque una operación casi nunca ocurre en una sola pieza. Registrar una matrícula, guardar una calificación, confirmar un pago o emitir un reporte puede involucrar actor humano, pantalla, servicio de aplicación, servicio de dominio, repositorio, base de datos, auditoría y sistemas externos. La secuencia permite explicar esa conversación sin mezclarla con estructura de clases, navegación de pantallas ni proceso BPMN completo.

Una buena secuencia responde con precisión: quién inicia, quién recibe, quién valida, quién consulta, quién modifica estado, quién registra evidencia y qué respuesta se devuelve.

![Anatomía de lifelines y mensajes en UML Secuencia.](figure:uml-sequence-anatomy)

## Pregunta que responde
UML Secuencia responde:

- ¿Qué participantes intervienen en una operación concreta?
- ¿Qué mensajes se envían entre ellos?
- ¿En qué orden temporal ocurren?
- ¿Qué respuestas o datos vuelven?
- ¿Qué validaciones condicionan el resultado?
- ¿Qué camino alternativo se toma si hay error?
- ¿Qué parte del sistema tiene cada responsabilidad?
- ¿Qué sistema externo participa y cómo se controla su respuesta?

Ejemplo administrativo:

- Secretaría solicita registrar matrícula.
- Pantalla de matrícula envía datos al backend.
- Servicio académico valida estudiante, representante, sección y cupo.
- Repositorio consulta o persiste datos.
- Servicio de auditoría registra el evento.
- Pantalla muestra confirmación o error.

## Idea central
La idea central es que una operación se ejecuta como conversación ordenada entre participantes. Cada mensaje debe tener propósito claro y cada participante debe representar una responsabilidad comprensible.

Secuencia mínima:

- Actor → Pantalla: solicita acción.
- Pantalla → Servicio: envía comando o consulta.
- Servicio → Repositorio: busca o guarda datos.
- Repositorio → Base de datos: ejecuta persistencia.
- Servicio → Pantalla: devuelve resultado.
- Pantalla → Actor: muestra confirmación o error.

Esta lectura evita que el sistema quede como una masa donde la pantalla decide reglas críticas, la base de datos parece actor principal o el servicio hace demasiadas cosas sin separar consulta, validación y persistencia.

## Qué es
UML Secuencia es un diagrama de interacción de UML. Representa participantes mediante lifelines y mensajes ordenados verticalmente por tiempo.

Un participante puede ser actor humano, pantalla, controlador, servicio, repositorio, base de datos, cola, job, servicio externo u objeto de dominio. La lifeline indica que el participante existe durante la interacción. Los mensajes muestran llamadas, solicitudes, eventos o retornos.

No es un mapa libre. No se lee por cercanía espacial, sino por tiempo. El eje vertical manda.

## Para qué sirve
Sirve para detallar cómo se ejecuta un caso de uso o una operación técnica significativa.

Usos frecuentes:

- pasar de caso de uso a diseño técnico;
- explicar comunicación frontend-backend;
- ubicar validaciones de permisos y reglas de negocio;
- mostrar consultas y comandos sobre repositorios;
- documentar integraciones con correo, facturación, WhatsApp, pagos o archivos;
- representar caminos de éxito, error y excepción de negocio;
- preparar pruebas de backend, integración o aceptación;
- detectar participantes con responsabilidad mal ubicada.

Ejemplo: si el caso de uso dice “Registrar pago”, la secuencia puede mostrar `PantallaPago → PagoService → OrdenRepository → PagoRepository → AuditoriaService`, incluyendo alternativa por monto inválido.

## Qué representa
Representa:

- participantes o lifelines;
- mensajes síncronos;
- mensajes asíncronos;
- retornos relevantes;
- activaciones;
- auto-mensajes;
- orden temporal;
- fragmentos combinados;
- guardas entre corchetes;
- alternativas, opciones, ciclos y paralelismo controlado;
- referencias a interacciones externas;
- caminos de error y respuestas de negocio.

![Secuencia típica entre usuario, pantalla, servicio, repositorio y base de datos.](figure:uml-sequence-frontend-backend-db)

## Qué no representa
No representa principalmente:

- estructura estática de clases;
- tablas físicas de base de datos;
- navegación general de pantallas;
- proceso de negocio largo entre áreas;
- wireframes o diseño visual final;
- despliegue físico;
- todos los escenarios del sistema en un solo dibujo.

Si se quiere mostrar estructura estática, conviene UML Clases. Si se quiere mostrar flujo de acciones sin participantes, conviene UML Actividad. Si se quiere mostrar proceso entre roles o áreas, conviene BPMN o flujo operativo. Si se quiere mostrar ciclo de vida de una matrícula u orden, conviene UML Estados.

## Elementos principales
Los elementos principales son participante, lifeline, mensaje, retorno, activación y fragmento combinado.

![Partes de un fragmento combinado: operador, guarda, operandos y rango temporal.](figure:uml-sequence-combined-fragment-parts)

Un participante aparece arriba. Su lifeline baja verticalmente. Los mensajes cruzan entre lifelines. Las activaciones muestran trabajo en ejecución. Los fragmentos combinados enmarcan zonas de la interacción para expresar condiciones, ciclos, paralelismo o referencias.

## Participante o lifeline
Un participante es cualquier elemento que interviene en el escenario.

Ejemplos:

- Secretaría;
- PantallaMatrícula;
- MatriculaController;
- MatriculaService;
- EstudianteRepository;
- BaseDatos;
- AuditoriaService;
- ServicioCorreoInstitucional.

No siempre debe ser una clase exacta. En un diagrama de alto nivel puede usarse “Backend/API”. En un diagrama técnico se puede separar controller, service y repository.

Error común: usar demasiadas lifelines. Si aparecen veinte columnas, probablemente el escenario debe dividirse o elevarse de nivel.

## Mensaje
Un mensaje representa comunicación entre participantes.

Ejemplos buenos:

- registrarMatricula(datos);
- validarCupo(seccion);
- buscarEstudiante(identificacion);
- guardarMatricula(matricula);
- registrarEventoAuditoria(evento);
- enviarConfirmacion(destinatario);

Los mensajes deben tener verbo y objeto. Evitar mensajes vagos como “proceso”, “datos”, “gestión” o “validar” sin indicar qué se valida.

## Retorno
El retorno muestra una respuesta relevante.

Ejemplo:

- MatriculaService → EstudianteRepository: buscarPorCedula(cedula)
- EstudianteRepository → MatriculaService: estudianteEncontrado

No todo retorno trivial debe dibujarse. Conviene mostrar retornos cuando aclaran datos devueltos, confirmación, error, estado o condición de decisión.

## Activación
La activación indica que un participante está ejecutando trabajo. Visualmente suele verse como una barra vertical sobre la lifeline.

Ejemplo: `MatriculaService` permanece activo mientras valida estudiante, representante, cupo, reglas de sección y auditoría.

Para una secuencia documental no hace falta obsesionarse con cada activación, pero sí conviene usarlas cuando aclaran quién controla la operación.

## Mensaje síncrono
Un mensaje síncrono representa una llamada donde el emisor espera una respuesta antes de continuar.

Ejemplo:

- PantallaMatrícula → MatriculaService: registrarMatricula(datos)
- MatriculaService → PantallaMatrícula: resultadoRegistro

En sistemas administrativos, muchas acciones de formulario se entienden como síncronas: el usuario espera confirmación o error antes de seguir.

## Mensaje asíncrono
Un mensaje asíncrono representa una comunicación que no bloquea de la misma manera el avance del emisor.

Ejemplos:

- publicar evento de matrícula registrada;
- enviar correo de confirmación;
- generar reporte pesado en segundo plano;
- disparar sincronización con sistema externo;
- registrar auditoría sin detener la respuesta principal.

![Diferencia entre mensaje síncrono y asíncrono.](figure:uml-sequence-sync-async)

No conviene usar asincronía solo para que el diagrama parezca más avanzado. Debe responder a una razón real: latencia, integración externa, trabajo en segundo plano o desacoplamiento.

## Fragmentos combinados
Un fragmento combinado agrupa una región temporal de la secuencia y declara cómo debe interpretarse.

Tiene cuatro ideas clave:

- operador: `alt`, `opt`, `loop`, `par`, `break`, `critical` o `ref`;
- guarda: condición escrita normalmente como `[condición]`;
- operandos: regiones internas del fragmento, por ejemplo ramas de un `alt`;
- rango temporal: mensajes que quedan dentro del marco.

El fragmento no es decoración. Debe explicar una estructura lógica del escenario.

Ejemplo conceptual:

```text
loop [por cada estudiante]
  consultarDatos()
  alt [datos completos]
    registrarMatricula()
  else [datos incompletos]
    solicitarCorrección()
```

## Operandos y guardas
Un operando es una región interna de un fragmento combinado. En `alt`, cada operando suele representar una rama. En `par`, cada operando puede representar una línea de trabajo paralela. En `opt`, normalmente hay un solo operando.

La guarda es la condición que activa un operando:

- `[estudiante existe]`
- `[no existe cupo]`
- `[usuario tiene permiso]`
- `[servicio externo responde OK]`
- `[por cada detalle]`

Las guardas deben ser legibles. No hace falta escribir lógica matemática compleja si una condición humana clara comunica mejor el escenario.

## Fragmento alt
`alt` representa alternativas excluyentes o caminos principales distintos.

Ejemplo:

- `[cupo disponible]`: registrar matrícula y confirmar.
- `[sin cupo]`: dejar solicitud pendiente o mostrar error.

![Fragmento alt para éxito y error de validación.](figure:uml-sequence-alt-error)

Regla práctica: `alt` debería tener al menos dos operandos. Si solo hay un bloque opcional, probablemente corresponde `opt`.

## Fragmento opt
`opt` representa comportamiento opcional: ocurre si la condición se cumple; si no, simplemente se omite.

Ejemplos:

- `[representante desea comprobante]`: emitir comprobante.
- `[notificar por correo]`: enviar confirmación.
- `[hay descuento autorizado]`: aplicar descuento.

![Fragmento opt como comportamiento opcional con una guarda.](figure:uml-sequence-opt-guard)

La diferencia con `alt` es que `opt` no necesita dibujar la rama contraria. Solo muestra el bloque que puede ocurrir.

## Fragmento loop
`loop` representa repetición.

Ejemplos:

- `[por cada estudiante importado]`;
- `[por cada calificación del parcial]`;
- `[mientras existan errores de validación]`;
- `[por cada documento adjunto]`.

![Fragmento loop para repetir mensajes sobre varios registros.](figure:uml-sequence-loop-guard)

Usar `loop` evita copiar mensajes repetidos. La guarda debe explicar el criterio de repetición.

## Fragmento par
`par` representa operandos que pueden avanzar de forma paralela o independiente.

Ejemplo:

- guardar operación principal;
- registrar auditoría;
- encolar notificación.

![Fragmento par con operandos paralelos controlados.](figure:uml-sequence-par-operands)

Debe usarse con prudencia. Si una acción depende de otra, no es realmente paralela. En aplicaciones administrativas pequeñas, muchas veces basta con mensajes secuenciales.

## Fragmento break
`break` representa interrupción del flujo normal cuando una condición corta el escenario.

Ejemplos:

- `[usuario sin permiso]`: devolver error de autorización y terminar.
- `[matrícula anulada]`: bloquear edición.
- `[servicio externo no disponible]`: marcar pendiente y salir del camino normal.

Es útil cuando un error no es solo una rama más, sino una salida anticipada del escenario.

## Fragmento critical
`critical` representa una región que debe ejecutarse sin interferencia problemática.

Ejemplos:

- asignar cupo evitando sobrecupo;
- reservar asiento o turno;
- confirmar pago contra saldo;
- actualizar inventario crítico.

![Fragmento critical para una región que protege consistencia.](figure:uml-sequence-critical-region)

En sistemas administrativos, `critical` se relaciona con invariantes: no sobreasignar cupos, no dejar saldo negativo, no duplicar una matrícula vigente.

## Fragmento ref
`ref` referencia otra interacción ya descrita en otro diagrama o escenario.

Ejemplos:

- `ref Validar identidad del representante`;
- `ref Emitir comprobante`;
- `ref Registrar auditoría`;
- `ref Autenticar usuario`.

![Fragmento ref para reutilizar una interacción definida aparte.](figure:uml-sequence-ref-interaction)

Sirve para no repetir subprocesos conocidos. Debe tener nombre claro de la interacción referenciada.

## Anidación de fragmentos
Un fragmento puede contener otro, pero la anidación debe mantenerse controlada.

Ejemplo razonable:

```text
loop [por cada estudiante]
  alt [datos válidos]
    registrar()
  else [datos incompletos]
    pedirCorrección()
```

![Anidación controlada de loop y alt.](figure:uml-sequence-nested-fragments)

Error común: anidar demasiados niveles hasta volver ilegible el escenario. Si hay más de dos o tres niveles, suele convenir dividir la secuencia.

## Secuencia frontend-backend-base de datos
Una plantilla frecuente en sistemas administrativos es:

- Usuario o actor;
- Pantalla;
- Controller/API;
- Service o caso de uso;
- Repository;
- Base de datos;
- Servicios externos si participan.

Esta plantilla ayuda a ubicar responsabilidades:

- la pantalla captura y muestra;
- el controller/API recibe y adapta;
- el service aplica reglas;
- el repository consulta o persiste;
- la base de datos almacena;
- el sistema externo responde fuera del límite propio.

## Secuencia con errores
Toda operación importante debe considerar errores de negocio.

Ejemplos:

- usuario sin permiso;
- registro no encontrado;
- estado no permite operación;
- cupo insuficiente;
- monto inválido;
- servicio externo caído;
- validación obligatoria fallida.

`alt` y `break` son útiles aquí. `alt` separa caminos; `break` corta el flujo cuando no debe continuar.

## Secuencia con permisos
Ejemplo:

- Usuario solicita anular matrícula.
- Pantalla envía solicitud.
- Servicio valida permiso `matriculas.anular`.
- `alt [tiene permiso]`: anula, registra auditoría y confirma.
- `break [sin permiso]`: devuelve error de autorización.

Este caso conecta directamente con Roles y permisos. La secuencia muestra dónde se verifica el permiso y qué respuesta recibe la interfaz.

## Secuencia con sistema externo
![Secuencia con sistema externo e integración controlada.](figure:uml-sequence-external-system)

Ejemplo:

- Sistema solicita emitir comprobante a un servicio externo.
- El servicio externo responde autorizado, rechazado o sin respuesta.
- `alt [autorizado]`: guardar número de comprobante.
- `alt [rechazado]`: registrar observación.
- `break [sin respuesta]`: marcar emisión pendiente.

Las integraciones externas deben tener caminos de error porque pueden fallar, responder tarde o rechazar la solicitud.

## Relación con casos de uso y pruebas
Un caso de uso dice qué objetivo quiere lograr un actor. La secuencia explica cómo colaboran las piezas para cumplir ese objetivo.

Cada mensaje importante puede convertirse en criterio de prueba:

- se envía comando con datos válidos;
- se consulta entidad existente;
- se rechaza estado inválido;
- se protege permiso requerido;
- se registra auditoría;
- se devuelve confirmación o error.

Las guardas de fragmentos suelen transformarse en casos de prueba: camino exitoso, error de validación, falta de permiso, sistema externo caído, repetición por varios registros.

## Relación con precondiciones, invariantes y postcondiciones
Una secuencia bien hecha puede leerse con lógica de corrección:

- precondición: lo que debe ser cierto antes de iniciar;
- mensajes: transformaciones y consultas;
- invariante: verdad que no debe romperse durante el proceso;
- postcondición: resultado verificable al terminar.

Ejemplo de matrícula:

- precondición: existe año lectivo abierto;
- invariante: una sección no supera su cupo máximo;
- postcondición: matrícula queda registrada o se informa motivo de rechazo;
- evidencia: evento de auditoría y comprobante interno.

Esto conecta UML Secuencia con el levantamiento lógico del negocio.

## Diferencia con UML Actividad
UML Actividad muestra flujo lógico de acciones. UML Secuencia muestra mensajes entre participantes.

Ejemplo:

- Actividad: validar datos → asignar cupo → guardar matrícula.
- Secuencia: PantallaMatrícula → MatriculaService → SeccionRepository → BaseDatos.

Ambos pueden describir la misma operación desde ángulos distintos.

## Diferencia con UML Clases
UML Clases muestra estructura estática: clases, atributos, operaciones y relaciones.

UML Secuencia muestra comportamiento temporal. Una clase puede aparecer como participante, pero la secuencia no reemplaza al diagrama de clases.

## Diferencia con C4 Contenedores
C4 Contenedores muestra piezas principales del sistema: desktop, API, base de datos, servicio externo.

UML Secuencia puede detallar una operación que atraviesa esos contenedores.

Ejemplo:

- C4: JavaFX Desktop → Backend/API → PostgreSQL.
- Secuencia: PantallaCalificaciones → CalificacionService → CalificacionRepository → BaseDatos.

## Relaciones y lectura
Un diagrama de secuencia se lee de arriba hacia abajo. Primero se identifica el escenario, luego el participante iniciador y después cada mensaje en orden.

Lectura recomendada:

- identificar el caso de uso;
- leer participantes de izquierda a derecha;
- seguir mensajes de arriba hacia abajo;
- revisar activaciones relevantes;
- leer fragmentos combinados por operador y guarda;
- comprobar que cada error importante esté modelado;
- confirmar que la responsabilidad de cada participante tenga sentido.

Si una flecha no se puede leer como acción clara, el mensaje debe corregirse.

## Cómo leer un diagrama de secuencia
Pasos de lectura:

- ubicar el escenario concreto;
- reconocer quién inicia;
- ubicar pantalla, servicio, repositorio y sistemas externos;
- seguir mensajes por orden vertical;
- interpretar retornos importantes;
- leer cada fragmento combinado desde su operador;
- revisar guardas entre corchetes;
- validar qué pasa en éxito, error, opción, ciclo o paralelismo;
- detectar responsabilidades mal ubicadas.

Pregunta de control:

- ¿El diagrama explica realmente cómo se completa la operación y cómo falla de forma controlada?

## Cómo construirlo paso a paso
Receta práctica:

- elegir un caso de uso concreto;
- definir actor iniciador;
- ubicar la pantalla o interfaz inicial;
- identificar el servicio que aplica reglas;
- identificar repositorios o datos consultados;
- agregar sistemas externos solo si afectan el resultado;
- dibujar primero el camino exitoso;
- agregar validaciones importantes;
- usar `alt` para caminos alternativos;
- usar `opt` para comportamiento opcional;
- usar `loop` para repetición;
- usar `break` para interrupción anticipada;
- usar `critical` si hay consistencia sensible;
- usar `ref` si se reutiliza una interacción ya descrita;
- revisar mensajes vagos y participantes sobrantes.

## Microejemplo administrativo
Caso: registrar calificación.

Participantes:

- Docente;
- PantallaCalificaciones;
- CalificacionService;
- EstudianteRepository;
- CalificacionRepository;
- AuditoriaService.

Secuencia resumida:

- Docente solicita guardar calificaciones.
- Pantalla envía lista de notas.
- Servicio valida permiso y periodo abierto.
- `loop [por cada estudiante]`: validar matrícula y nota.
- `alt [nota válida]`: guardar calificación.
- `alt [nota inválida]`: devolver observación.
- Servicio registra auditoría.
- Pantalla muestra resumen de guardado.

## Casos especiales
Un participante puede representar una capa completa si el diagrama es de alto nivel. Por ejemplo, “Backend/API” puede reemplazar controller, service y repository.

Un sistema externo aparece cuando su respuesta afecta el resultado. Facturación, correo, WhatsApp, pasarela de pago, almacenamiento de archivos o autenticación externa son ejemplos frecuentes.

Un retorno puede omitirse si no aporta claridad. Pero conviene mostrarlo cuando devuelve datos importantes, error, confirmación o estado.

Un `ref` evita repetir interacciones conocidas. Un `critical` protege consistencia. Un `break` corta camino inválido. Un `par` solo debe usarse cuando hay independencia real.

## Cuándo usarlo
Úsalo cuando importa el orden temporal y la colaboración entre partes.

Casos recomendados:

- registrar matrícula;
- registrar pago;
- guardar calificación;
- anular orden;
- iniciar sesión;
- emitir comprobante;
- integrar con facturación;
- generar reporte;
- importar datos masivos;
- registrar auditoría.

Es especialmente útil cuando una operación atraviesa interfaz, backend, base de datos y servicios externos.

## Cuándo no usarlo
No lo uses para representar toda la navegación de pantallas. Para eso existe Flujo de pantallas.

No lo uses para procesos largos entre áreas. Para eso puede servir BPMN o Flujo operativo.

No lo uses para estructura de clases. Para eso existe UML Clases.

No lo uses para ciclo de vida de entidades. Para eso existe UML Estados.

No intentes meter todo el sistema en una sola secuencia. Es mejor hacer varias secuencias concretas.

## Errores comunes
![Errores comunes al modelar secuencias sin orden ni responsabilidad clara.](figure:uml-sequence-common-errors)

Errores frecuentes:

- usar mensajes vagos como “procesar” o “gestionar”;
- ignorar que el tiempo va de arriba hacia abajo;
- poner demasiados participantes;
- no mostrar caminos de error relevantes;
- usar `alt` con una sola rama cuando corresponde `opt`;
- usar `loop` sin explicar condición de repetición;
- usar `par` aunque las acciones dependan una de otra;
- usar `ref` sin indicar la interacción referenciada;
- no poner guardas en decisiones importantes;
- hacer que la pantalla aplique reglas críticas de negocio;
- usar la base de datos como actor principal;
- mezclar navegación, clases, proceso y despliegue en una sola secuencia.

Consulta y comando no son iguales. Consultar datos no cambia estado. Registrar, anular, confirmar o asignar cupo sí cambia estado y requiere reglas más fuertes.

## Relación con otros diagramas
UML Casos de uso aporta el objetivo funcional que la secuencia detalla.

UML Actividad muestra el flujo lógico de acciones; UML Secuencia muestra quién se comunica con quién.

UML Clases define clases, servicios u objetos que pueden aparecer como participantes.

UML Estados define transiciones que una secuencia puede ejecutar.

Roles y permisos indican validaciones de autorización.

Diccionario de datos aporta campos, validaciones y reglas usadas en mensajes.

C4 Contenedores ayuda a ubicar si los participantes pertenecen al frontend, backend, base de datos o servicios externos.

Despliegue técnico explica dónde corren realmente esas piezas.

Flujo de pantallas indica desde qué pantalla se dispara la operación.

Wireframes muestran el botón, formulario o tabla que origina la interacción.

Levantamiento lógico aporta reglas, precondiciones, invariantes, postcondiciones y evidencia que pueden aparecer como guardas o mensajes críticos.

## Qué pedirle a la IA después de entenderlo
Prompts útiles:

- A partir de este caso de uso, genera un UML de secuencia con actor, pantalla, servicio, repositorio y base de datos.
- Modela la secuencia para registrar matrícula incluyendo validación de cupo, representante y auditoría.
- Incluye fragmentos `alt`, `opt`, `loop`, `break`, `critical` o `ref` solo cuando correspondan.
- Revisa esta secuencia y detecta mensajes vagos, participantes innecesarios o errores no modelados.
- A partir de este flujo operativo, identifica qué mensajes corresponden a pantalla, servicio, repositorio y sistema externo.
- Genera guardas entre corchetes para los caminos de éxito, error y repetición.
- Propón casos de prueba derivados de cada guarda del diagrama de secuencia.

## Ficha rápida
UML Secuencia representa interacciones ordenadas en el tiempo.

Se lee de arriba hacia abajo.

Elementos clave: participante, lifeline, mensaje, retorno, activación y fragmento combinado.

Fragmentos clave: `alt`, `opt`, `loop`, `par`, `break`, `critical` y `ref`.

Las guardas se escriben como `[condición]`.

Los operandos separan ramas internas de un fragmento.

Sirve para explicar cómo colaboran actor, pantalla, backend, repositorio, base de datos y sistemas externos.

No reemplaza UML Clases, UML Actividad, BPMN, Flujo de pantallas ni C4.

Pregunta clave: ¿quién llama a quién, en qué orden, bajo qué condición y con qué resultado?

## Auditoría de frontera teórica
UML Secuencia debe mantenerse como diagrama temporal de interacción. Su valor se pierde si se usa como mapa general de módulos, lista de pantallas, pseudocódigo largo o diagrama de clases disfrazado.

El criterio de frontera es simple:

- si necesitas estructura estática, usa UML Clases;
- si necesitas flujo lógico sin participantes, usa UML Actividad;
- si necesitas proceso de negocio entre áreas, usa BPMN o Flujo operativo;
- si necesitas navegación, usa Flujo de pantallas;
- si necesitas conversación temporal entre partes, usa UML Secuencia.

Un buen diagrama de secuencia está atado a un escenario y puede explicar éxito, error y reglas críticas sin perder legibilidad.

## Checklist final de estudio
- Puedo leer lifelines, mensajes, retornos, activaciones y fragmentos.
- Puedo distinguir `alt`, `opt`, `loop`, `par`, `break`, `critical` y `ref`.
- Puedo escribir guardas legibles entre corchetes.
- Puedo ubicar mensajes dentro de un rango temporal.
- Puedo detectar cuándo un fragmento necesita operandos.
- Puedo conectar una secuencia con casos de uso, permisos, estados, datos y pruebas.
- Puedo distinguir secuencia de clases, actividad, BPMN, C4 y flujo de pantallas.
- Puedo decidir cuándo dividir una secuencia demasiado grande.

## Referencias / base teórica
Este capítulo se apoya en la notación UML de interacción y en la práctica común de diseño de sistemas por capas. Para Domain Model Studio, el enfoque es académico-práctico: suficiente formalidad para construir secuencias útiles, pero orientado a aplicaciones administrativas, levantamiento lógico, pruebas y documentación importable/exportable.
