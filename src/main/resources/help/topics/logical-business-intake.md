# Levantamiento lógico de negocio

## Introducción
El levantamiento lógico de negocio es la capa documental que ocurre antes de dibujar diagramas, diseñar tablas o programar pantallas. Su propósito es convertir entrevistas, observaciones, documentos y decisiones del cliente en una estructura estable que pueda revisarse durante años: reglas, estados, acciones, precondiciones, invariantes, postcondiciones, entidades candidatas, relaciones, reportes, riesgos y preguntas pendientes.

Este tipo de proyecto no es un canvas visual ni un generador automático de todos los demás artefactos. Es una fuente lógica canónica: fija IDs, nombres, reglas y contratos semánticos que luego pueden ser reutilizados por el usuario y por una IA para preparar otros Markdown compatibles con Domain Model Studio.

La idea central es tratar el negocio como un sistema que cambia de estado. Un estudiante se registra, una clase se crea, una calificación se confirma, una solicitud de reporte entra en cola, una auditoría registra una acción. Cada acción modifica información y deja consecuencias. Si esas consecuencias no se entienden, el software puede terminar guardando datos sin sentido, permitiendo estados imposibles o mezclando reglas que luego nadie sabe explicar.

La ayuda académica de este tema no busca enseñar una notación visual nueva. Busca explicar el fundamento intelectual del módulo: por qué conviene escribir primero estados, acciones, reglas, precondiciones, invariantes, postcondiciones, preguntas pendientes y trazas internas antes de pedirle a una IA o a una persona que prepare otros documentos o diagramas.

![Ciclo lógico de una acción de negocio: estado inicial, precondiciones, acción transformadora, reglas, invariantes, postcondiciones y evidencia.](figure:logical-business-state-action-cycle)

## Qué es
Un levantamiento lógico de negocio es un documento estructurado que describe cómo funciona o debería funcionar un negocio desde el punto de vista de su lógica operativa. No se limita a listar pantallas, entidades o campos. Intenta explicar qué cosas cambian, qué acciones producen esos cambios, qué reglas deben respetarse y qué condiciones hacen que una operación pueda considerarse válida.

En este módulo, la palabra “lógico” no significa que todo deba estar escrito con símbolos matemáticos. Significa que cada afirmación importante debe poder defenderse con una razón: de dónde salió, qué problema resuelve, qué estado protege, qué consecuencia evita y qué elementos del documento la respaldan.

Por ejemplo, decir “el sistema tendrá estudiantes” todavía es una afirmación pobre. Un levantamiento lógico pregunta por qué existe el concepto estudiante, qué estados puede tener, qué acciones lo crean o modifican, qué reglas se aplican sobre él, qué evidencias lo respaldan y qué reportes o procesos dependen de su existencia.

Tampoco es un bloc de notas libre. Puede contener prosa abundante, pero esa prosa debe organizarse alrededor de secciones estables: contexto, estados, vocabulario del dominio, predicados, reglas de negocio, precondiciones, invariantes, postcondiciones, acciones transformadoras, macroflujos, casos de uso, entidades candidatas, relaciones candidatas, reportes, riesgos y preguntas pendientes.

La frase clave es: el levantamiento lógico es una fuente lógica canónica; los demás artefactos son documentos independientes que pueden reutilizar sus IDs y nombres canónicos bajo revisión del usuario y la IA.

## Para qué sirve
Sirve para evitar que el diseño del sistema empiece demasiado pronto por tablas, pantallas o diagramas bonitos. En proyectos reales, es común que el analista escuche al cliente, imagine entidades, dibuje un modelo conceptual rápido y pase a implementar. Ese camino puede funcionar en sistemas simples, pero se vuelve frágil cuando aparecen excepciones, estados ambiguos, reglas no validadas, cobros parciales, anulaciones, reportes, permisos o flujos alternos.

El levantamiento lógico ayuda a convertir información cruda en conocimiento operativo. Una entrevista puede contener frases útiles, contradicciones, deseos, suposiciones, dolores, soluciones imaginadas y datos incompletos. El módulo ayuda a separar esas capas: qué es hecho observado, qué es regla validada, qué es inferencia del analista, qué es pregunta pendiente y qué es decisión todavía no tomada.

También sirve como base semántica para otros tipos de proyecto. De una regla pueden salir validaciones, restricciones y pruebas. De una acción transformadora pueden salir casos de uso, actividades UML o procesos BPMN. De conceptos bien definidos pueden salir modelo conceptual y diccionario de datos. De actores, permisos y operaciones pueden salir roles y permisos. Pero esa continuidad no es automática: el usuario y la IA deben reutilizar los IDs y nombres canónicos cuando preparen otros Markdown.

Su valor principal no es producir un documento largo. Su valor es producir un documento revisable, trazable internamente y defendible ante personas del negocio y del equipo técnico.

## Responsabilidad de alineación semántica
El Levantamiento lógico define IDs, nombres, reglas y contratos semánticos canónicos del negocio. Cuando el usuario o una IA genere otros archivos Markdown compatibles con Domain Model Studio, debe reutilizar esos IDs y nombres para mantener consistencia entre artefactos.

Domain Model Studio no garantiza automáticamente la alineación entre proyectos independientes. Cada tipo de proyecto mantiene su propio alcance, parser, validación, edición y exportación. El diccionario valida diccionario; UML valida UML; C4 valida arquitectura; el Levantamiento lógico valida la coherencia interna del documento lógico activo.

No es obligatorio generar todos los tipos de proyecto. El usuario decide qué artefactos necesita para el caso de negocio. La IA puede ayudar a preparar Markdown compatibles, pero debe recibir instrucciones claras y debe respetar los IDs canónicos del levantamiento.

## Fundamento algorítmico
En algoritmos, un procedimiento se estudia preguntando si produce un resultado correcto y cuánto cuesta ejecutarlo. Para demostrar corrección, se usan ideas como precondiciones, invariantes y postcondiciones. La precondición indica qué debe ser verdadero antes de empezar. La invariante indica qué verdad se conserva durante la ejecución. La postcondición indica qué debe ser verdadero cuando el procedimiento termina.

Esa estructura puede trasladarse a procesos de negocio. Registrar un pago, confirmar una venta, cerrar una reparación o aprobar una asignación no son algoritmos académicos como Insertion Sort, pero sí pueden analizarse como transformaciones de estado. Antes de registrar una operación debe existir una causa válida. Durante el registro no debe romperse la trazabilidad. Al finalizar debe quedar evidencia, estado actualizado y cierre verificable.

La conexión no debe forzarse de manera artificial. El objetivo no es decirle al cliente que todo negocio “es un algoritmo” en sentido estricto. En la ayuda académica sí podemos hablar de algoritmos porque explican la base conceptual. En la UI conviene hablar de acciones transformadoras, flujos operativos y casos de uso. La disciplina de fondo sigue siendo la misma: identificar entrada, estado, transformación, regla, conservación de verdades y salida verificable.

Un ejemplo académico sencillo es sumar un arreglo: la variable `suma` empieza en cero, se actualiza con cada elemento y al final contiene el total. En negocio, calcular el total de ventas del día, actualizar el saldo de una cuenta o acumular calificaciones sigue una lectura parecida: hay un estado inicial, una colección de registros, una operación repetitiva y un resultado que debe poder verificarse.

Esta base académica ayuda a que el levantamiento lógico no sea solo narrativo. La prosa explica, pero las secciones lógicas obligan a preguntar si cada proceso tiene inicio, transformación, regla y cierre.

## Precondiciones, invariantes y postcondiciones
Una precondición es una afirmación que debe ser verdadera antes de ejecutar una acción. Por ejemplo: antes de registrar una calificación, debe existir una clase activa y un estudiante asociado a esa clase. Si esa condición no se cumple, la operación no debería continuar sin corrección o decisión humana.

Una invariante es una verdad que debe conservarse mientras el negocio cambia de estado. No es una línea de código. Es una regla de sanidad del negocio. Por ejemplo: una calificación no debe quedar huérfana sin estudiante y clase; una solicitud de reporte no debe pasar a completada sin evidencia de salida; un evento de auditoría no debe perder el usuario responsable.

Una postcondición declara qué debe quedar verdadero cuando la acción termina correctamente. Después de registrar una calificación, debe existir un registro trazable, con valor válido, asociado a estudiante, clase y evidencia de auditoría si aplica.

Estas tres piezas permiten revisar si una acción transformadora está completa. Una acción sin precondiciones puede permitir operaciones fuera de orden. Una acción sin invariantes puede romper verdades esenciales. Una acción sin postcondiciones no tiene cierre verificable.

## Estados y acciones transformadoras
Un estado describe cómo está algo en un momento determinado. En un algoritmo, el estado puede ser el valor actual de variables, índices o estructuras de datos. En un negocio, el estado puede ser una solicitud pendiente, una clase activa, una calificación registrada, una orden en diagnóstico o una cuenta por cobrar parcialmente pagada.

Una acción transformadora es una operación que cambia uno o varios estados. No basta con escribir “registrar calificación”. Conviene describir qué situación existe antes, quién ejecuta la acción, qué datos se necesitan, qué pasos ocurren, qué reglas se aplican, qué invariantes deben protegerse, qué entidades se crean o modifican y qué evidencia queda al final.

La forma general de lectura es:

```text
estado inicial
→ precondiciones
→ transformación
→ reglas aplicadas
→ invariantes protegidas
→ postcondiciones
→ evidencia
```

Esta secuencia es importante porque obliga a cerrar el ciclo lógico. Una acción sin evidencia puede ser imposible de auditar. Una acción sin estado inicial puede empezar en una situación imposible. Una acción sin reglas aplicadas puede terminar aceptando datos inválidos.

En la interfaz del módulo, las acciones transformadoras deberían ser elementos navegables y revisables. Si una acción no tiene estado inicial, no tiene condiciones iniciales o no declara qué deja como cierre, el sistema debería marcarla como incompleta o riesgosa.

## Entidades y atributos candidatos
Un levantamiento lógico no puede quedarse solo en flujos, reglas e invariantes. Toda acción transformadora ocurre sobre algo: estudiantes, clases, docentes, calificaciones, documentos, reportes, cuentas por cobrar, tickets, matrículas, estados históricos o cualquier otra entidad de negocio.

La idea importante es el orden. El levantamiento lógico no pregunta primero “qué tablas tienes”. Pregunta qué ocurre, qué debe ser verdadero, qué evidencia queda y qué datos deben existir para que esa operación sea confiable. De ahí aparecen entidades candidatas, atributos candidatos y relaciones candidatas.

Una entidad candidata es un objeto lógico del negocio que merece seguimiento porque participa en acciones, reglas, estados, documentos, reportes o trazas internas. No es todavía una tabla definitiva. Por ejemplo, una calificación puede aparecer porque una acción la crea, varias reglas controlan sus valores, una postcondición exige cierre y un reporte necesita calcular rendimiento académico.

Un atributo candidato es un dato que debe recordarse, cambiarse, validarse, calcularse, mostrarse, auditarse o reportarse. El atributo `nota_final` no debería existir solo porque suena académico; existe porque hay reglas de rango, reportes, decisiones de promoción o evidencia de evaluación.

Una relación candidata explica por qué dos entidades del negocio deben conectarse. “Estudiante pertenece a Sección” no es solo una foreign key futura; es una afirmación de negocio que debe tener lectura humana y justificación lógica.

## Elementos principales
El documento trabaja con elementos identificables por prefijos. Estos prefijos permiten que el levantamiento sea estable y que otros Markdown puedan reutilizar IDs y nombres canónicos.

Elementos de operación del negocio:

- `ACT`: actor, persona, rol, área o sistema que participa en acciones.
- `MF`: macroflujo, agrupación grande de operación.
- `FL`: flujo, variante operativa concreta.
- `CU`: caso de uso, acción orientada a un objetivo de usuario o sistema.
- `ACC`: acción transformadora, operación que cambia estado.

Elementos de reglas y condiciones:

- `RN`: regla de negocio.
- `PRE`: precondición.
- `INV`: invariante.
- `POST`: postcondición.

Elementos de datos y evidencia:

- `ENT`: entidad candidata.
- `ATR`: atributo candidato.
- `REL`: relación candidata.
- `EVID`: evidencia.

Elementos de estados, salidas, riesgos y dudas:

- `EST`: estado.
- `REP`: reporte.
- `SUP`: supuesto.
- `RISK`: riesgo.
- `PEND`: pregunta pendiente.

Cada elemento importante debe tener ID, nombre, descripción humana, estado de revisión y referencias a otros elementos cuando corresponda.

## Relaciones y lectura
Las relaciones internas del levantamiento se expresan mediante IDs y verbos claros. No basta con decir que dos cosas “están relacionadas”. Conviene escribir cómo se conectan.

Ejemplos de lectura:

- `CU-006 requiere PRE-006`: el caso de uso Registrar calificación requiere que exista una clase activa.
- `CU-006 aplica RN-008`: el caso de uso debe cumplir la regla de rango permitido.
- `CU-006 protege INV-004`: el caso de uso protege que la calificación tenga estudiante y clase.
- `CU-006 crea ENT-007`: el caso de uso crea una calificación.
- `REP-001 depende_de ENT-007`: el reporte académico depende de calificaciones.
- `PEND-003 bloquea RN-009`: una pregunta pendiente impide cerrar la regla de redondeo.

Estas relaciones no sincronizan otros proyectos automáticamente. Sirven para que el levantamiento sea trazable internamente y para que el usuario o una IA puedan reutilizar los mismos IDs al preparar otros Markdown.

## Casos especiales
Un supuesto no debe tratarse como regla validada. Si el analista cree que una regla existe, pero el cliente no la confirmó, debe registrarla como `SUP` o como `PEND` según corresponda.

Una pregunta pendiente no es un adorno. Es deuda de análisis visible. Si una pregunta afecta reglas, entidades, flujos o reportes, debe quedar conectada a esos elementos.

Una entidad candidata puede ser fuerte, débil, catálogo, documento, evento, historial o movimiento. Esa clasificación sigue siendo lógica. No convierte automáticamente la entidad en tabla física.

Una fórmula puede ayudar, pero debe tener lectura humana. Una fórmula sin explicación no está lista para revisión con cliente ni para validación interna.

Un reporte puede revelar entidades y atributos necesarios, pero no todo reporte exige crear una entidad nueva. Debe explicarse qué pregunta responde, qué datos necesita y qué decisión permite tomar.

## Cuándo usarlo
Úsalo cuando el negocio todavía necesita clarificar reglas, estados, excepciones o preguntas importantes. Es especialmente útil antes de construir un conjunto grande de diagramas o documentos técnicos.

Úsalo cuando una IA va a generar Markdown compatibles con Domain Model Studio. En ese caso, el levantamiento debe actuar como fuente semántica: la IA debe reutilizar IDs, nombres y reglas canónicas.

Úsalo cuando hay riesgo de que distintas personas llamen de varias formas al mismo concepto. Si en el levantamiento `CU-006` se llama “Registrar calificación”, otros artefactos deberían reutilizar ese nombre salvo decisión explícita.

Úsalo cuando necesitas justificar por qué existe una entidad, atributo, relación, validación o reporte.

## Cuándo no usarlo
No lo uses para reemplazar todos los demás módulos. El levantamiento lógico es fuente lógica, no artefacto final único. Cuando necesitas explicar relaciones de datos, conviene modelo conceptual. Cuando necesitas detallar campos, conviene diccionario. Cuando necesitas proceso visual, conviene BPMN o flujo operativo. Cuando necesitas interacción de pantallas, conviene flujo de pantallas o wireframes.

Tampoco conviene usarlo para escribir notas personales sin estructura. Si el documento no usa secciones, IDs, estados, preguntas pendientes ni trazas internas, entonces no está funcionando como levantamiento lógico; solo está funcionando como texto acumulado.

No lo uses para simular certeza. Si una regla no está validada, debe quedar como borrador o pendiente. Si una entidad no tiene justificación, debe quedar como candidata. Si una pregunta afecta diseño, debe permanecer visible.

Y no conviene usarlo cuando el problema es puramente visual o de presentación. Para una maqueta de pantalla, usa wireframes. Para explicar navegación, usa flujo de pantallas. Para mostrar arquitectura, usa C4 o despliegue técnico.

## Errores comunes
Un error común es empezar por tablas. Las tablas son consecuencia de decisiones lógicas, no punto de partida obligatorio. Si se empieza por tablas, se corre el riesgo de crear entidades porque “suenan necesarias” y no porque estén justificadas por acciones, reglas o estados.

Otro error es escribir reglas sin lectura humana. Una fórmula puede verse elegante, pero si nadie puede explicar qué significa, qué protege y qué pasa si se rompe, no aporta al levantamiento.

También es común confundir deseo del cliente con regla del negocio. “Quiero ver un dashboard” no es todavía una regla. Puede ser necesidad de reporte, señal de dolor operativo o solución imaginada. El levantamiento debe preguntar qué decisión se tomará con ese dashboard y qué datos deben ser confiables para que sirva.

Otro error es ocultar preguntas pendientes. Las dudas importantes deben verse, porque condicionan diseño. Una pregunta pendiente de alta prioridad puede bloquear una entidad, un flujo o una validación.

Finalmente, es un error pedir todos los diagramas demasiado pronto. El usuario debe decidir qué artefactos necesita. El levantamiento aporta IDs, reglas y nombres canónicos; otros proyectos se preparan y revisan de forma independiente.

## Uso como fuente para otros artefactos
Cuando el levantamiento lógico alcanza suficiente madurez, puede servir como fuente para preparar otros Markdown compatibles con Domain Model Studio. No se trata de transformar texto en diagramas de manera ciega, sino de reutilizar IDs y nombres canónicos para producir artefactos revisables.

Del vocabulario y las entidades candidatas puede prepararse un modelo conceptual. De los atributos, validaciones y reglas puede prepararse un diccionario de datos. De las acciones transformadoras pueden prepararse casos de uso, actividades UML o procesos BPMN. De los estados válidos e inválidos puede prepararse UML Estados. De actores, permisos y acciones puede prepararse matriz de roles y permisos.

No es obligatorio producir todos esos artefactos. La selección depende del caso de negocio y del usuario. Cada archivo Markdown generado debe respetar su propio `diagram_type`, su propia gramática y su propia validación.

El principio de diseño es sencillo: el levantamiento lógico no reemplaza el criterio humano; lo organiza para que el criterio humano y la IA puedan revisar, corregir y preparar otros documentos con menos ambigüedad.

## Cierre documental y validación humana
El cierre documental no significa que el programa haya generado todos los artefactos posibles. Significa que el levantamiento puede defenderse: sus reglas tienen fuente, sus acciones tienen cierre, sus entidades candidatas tienen justificación, sus preguntas críticas están resueltas o explícitamente marcadas y su Markdown conserva referencias suficientes.

Antes de cerrar un levantamiento conviene revisar cuatro preguntas. La primera es si el documento todavía tiene bloqueos graves. La segunda es si las preguntas pendientes afectan decisiones importantes de diseño. La tercera es si las entidades, atributos y relaciones candidatas pueden explicarse a partir de acciones, reglas, evidencias o reportes. La cuarta es si el cliente o responsable del negocio validó las afirmaciones principales o si todavía son inferencias del analista.

Un levantamiento puede estar suficientemente maduro para usarse como fuente de otros artefactos y aun así no estar validado con el cliente. Esa diferencia debe conservarse. Usable como fuente significa que hay estructura suficiente para producir borradores revisables; validado significa que las afirmaciones principales fueron revisadas por una persona responsable del negocio.

El módulo de ayuda contextual del SideDock debe acompañar este cierre sin reemplazar el criterio humano. Su función es recordar qué mirar según el nodo seleccionado: una acción transformadora necesita precondiciones y postcondiciones; una entidad necesita fuente lógica; una pregunta pendiente puede bloquear diseño; un artefacto compatible preparado por IA debe revisarse antes de importarse.

Checklist mínimo de cierre:

- No hay acciones transformadoras críticas sin postcondición.
- No hay entidades candidatas importantes sin fuente lógica.
- Las preguntas críticas están resueltas, aplazadas con razón o marcadas como bloqueo.
- Las trazas internas permiten explicar de dónde viene cada regla, entidad o relación importante.
- El Markdown exportado conserva estructura, IDs y fuentes suficientes para reimportación o revisión posterior.
- Los demás Markdown generados a partir del levantamiento reutilizan IDs y nombres canónicos cuando corresponda.

## Cierre académico
El levantamiento lógico toma una idea académica y la vuelve operativa. Desde algoritmos, hereda la disciplina de razonar sobre estado, transformación, corrección, invariantes y cierre. Desde análisis de negocio, hereda la necesidad de escuchar, preguntar, distinguir fuentes, registrar dudas y documentar decisiones.

Por eso este módulo no debe implementarse como un editor de texto libre ni como un diagrama más. Debe implementarse como un expediente lógico estructurado: mucha prosa, sí, pero prosa con IDs, estados, trazas internas, validaciones y posibilidad de usarse como fuente semántica.

La meta no es que el documento sea bonito. La meta es que sea defendible. Si una persona, un cliente, un programador o una IA pregunta por qué existe una entidad, una regla o una validación, el levantamiento lógico debe permitir rastrear la respuesta.

En una frase: un buen levantamiento lógico convierte conversación en estructura, estructura en criterio, criterio en decisiones revisables y decisiones revisables en software más difícil de romper.
