# Grafo lógico del negocio

## Introducción
El grafo lógico del negocio es una vista académica y semántica derivada del levantamiento lógico. Su propósito no es decorar información ni reemplazar el expediente fuente, sino mostrar cómo se conectan macroflujos, microflujos, casos de uso, acciones transformadoras, reglas, condiciones, entidades, estados, reportes, riesgos y preguntas pendientes.

Este artefacto ayuda a leer el negocio como una red de transformaciones. Una operación administrativa no solo mueve datos: parte de un estado inicial, exige precondiciones, ejecuta acciones, aplica reglas, protege invariantes y deja postcondiciones verificables. El grafo lógico vuelve visible esa cadena para que el analista pueda revisar si la documentación todavía tiene huecos.

A diferencia de un árbol rígido, el grafo permite reutilizar elementos. Una regla puede aplicar a varios casos de uso, una entidad puede ser consultada por varios flujos y una pregunta pendiente puede bloquear más de una derivación. Por eso se dice que el grafo lógico puede parecer un árbol en su columna vertebral, pero no debe reducirse a un árbol puro.

![Backbone semántico del grafo lógico: macroflujo, flujo, caso de uso y acción, con reglas y condiciones asociadas.](figure:logical-business-graph-backbone)

## Qué es
Un grafo lógico del negocio es un diagrama semántico de nodos tipados y relaciones tipadas. Cada nodo representa una pieza lógica reconocible del levantamiento: un macroflujo, un flujo, un caso de uso, una acción, una regla, una condición, una entidad candidata, un estado, un reporte, un riesgo o una pregunta pendiente.

La palabra “lógico” significa que cada vínculo debe tener una lectura defendible. Si un caso de uso ejecuta una acción, debe poder explicarse qué acción se ejecuta. Si una acción requiere una precondición, debe entenderse qué verdad debe cumplirse antes de iniciar. Si una acción protege una invariante, debe quedar claro qué consistencia se conserva mientras el negocio cambia de estado.

El grafo es una vista derivada y revisable. Puede nacer desde un Markdown producido por IA, desde una derivación del levantamiento lógico o desde una edición posterior dentro de la herramienta, pero su valor depende de que mantenga trazabilidad. Un nodo sin fuente, sin relación o sin sentido operativo debe considerarse una señal de revisión.

No es un grafo libre. En un grafo libre cualquier nodo puede representar una idea. En el grafo lógico, los nodos tienen tipos cerrados y relaciones con semántica esperada. Esa restricción existe para evitar diagramas bonitos pero ambiguos.

## Para qué sirve
Sirve para revisar la coherencia del levantamiento lógico antes de derivar o entregar documentación. Permite ver si los macroflujos tienen flujos concretos, si los flujos usan casos de uso, si los casos de uso ejecutan acciones y si esas acciones están justificadas por reglas, precondiciones, invariantes y postcondiciones.

También sirve para conversar con IA de forma controlada. En lugar de pedir “hazme un diagrama del negocio”, se puede pedir un grafo con nodos MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK y PEND. Eso reduce ambigüedad y facilita reimportar el resultado como Markdown compatible.

Sirve para detectar piezas aisladas. Una regla sin caso de uso, una entidad que nadie crea ni consulta, un reporte sin fuente de datos o una pregunta pendiente que no bloquea nada son señales de análisis incompleto. El grafo no resuelve automáticamente esos problemas, pero los hace visibles.

También ayuda a conectar teoría y práctica. Desde algoritmos, toma la idea de transformación de estados; desde análisis de negocio, toma reglas, actores, entidades, documentos y decisiones; desde documentación de software, produce una vista navegable para diseñar modelos posteriores.

## Elementos principales
MF significa macroflujo. Representa una gran operación o familia lógica del negocio, como matrícula, seguimiento académico, atención de soporte o cierre administrativo.

FL significa flujo o microflujo. Representa una variante concreta dentro de un macroflujo. Un macroflujo puede contener varios flujos porque el negocio rara vez funciona con una sola ruta perfecta.

CU significa caso de uso. Describe una funcionalidad observable para un actor o responsable. Es más concreto que un flujo, pero todavía no necesariamente describe cada paso interno.

ACC significa acción transformadora. Es el elemento que cambia estado: registra, valida, crea, modifica, consulta, genera, cierra, anula o bloquea algo. Las acciones son centrales porque permiten razonar sobre precondiciones, invariantes y postcondiciones.

RN, PRE, INV y POST representan regla de negocio, precondición, invariante y postcondición. Estas piezas permiten revisar corrección lógica: qué regla aplica, qué debe cumplirse antes, qué se mantiene verdadero durante la operación y qué queda verdadero al terminar.

ENT, EST y REP representan entidad candidata, estado y reporte. Permiten conectar la lógica con artefactos posteriores como modelo conceptual, diccionario de datos, UML Estados y salidas documentales.

RISK y PEND representan riesgo y pregunta pendiente. No deben ocultarse, porque muestran incertidumbre o amenaza para la validez del diseño.

## Relaciones y lectura
La relación contiene agrupa jerárquicamente, típicamente de MF hacia FL. Sirve para mostrar la columna vertebral del análisis, no para convertir todo el grafo en árbol rígido.

La relación usa conecta un flujo con un caso de uso o un caso de uso con una acción. La relación reutiliza indica que un flujo o caso vuelve a usar una pieza existente sin redefinirla.

La relación ejecuta muestra qué acción transformadora materializa un caso de uso o flujo. La relación aplica conecta reglas de negocio con los elementos sobre los que actúan.

La relación requiere apunta a precondiciones. La relación protege apunta a invariantes. La relación garantiza apunta a postcondiciones. Estas tres relaciones son la base para leer corrección lógica.

Las relaciones crea, modifica y consulta conectan acciones o casos con entidades o estados. La relación genera conecta con reportes. La relación alimenta indica que un elemento aporta datos o evidencia a otro.

La relación bloquea se usa para riesgos y preguntas pendientes que impiden validar o derivar. Habilita muestra que una condición satisfecha permite avanzar. Depende_de declara una dependencia general y deriva_en justifica un artefacto o elemento resultante.

![Trazabilidad del levantamiento lógico hacia grafo, entidades, reportes y diagramas derivados.](figure:logical-business-graph-traceability)

## Casos especiales
Un caso especial frecuente es la reutilización. Una acción como “validar datos obligatorios” puede participar en inscripción, actualización de estudiante, matrícula y generación de reportes. Si se fuerza un árbol, esa acción tendría que duplicarse; en un grafo se reutiliza.

Otro caso especial son las preguntas pendientes. Una pregunta no debe quedar al margen del dibujo. Si afecta una regla, una entidad, un estado o una derivación, debe aparecer como PEND y bloquear aquello que todavía no puede validarse.

También existen riesgos. Un riesgo no es una regla, pero puede afectar la confiabilidad del sistema. Por ejemplo, credenciales en archivos, respaldos sin prueba de restauración o reportes sin fuente auditable pueden representarse como RISK.

Las reglas inferidas requieren cuidado. Una IA puede proponer reglas plausibles, pero el grafo debe distinguir lo validado de lo supuesto. Una regla inferida puede aparecer, pero no debe presentarse como verdad definitiva si no tiene fuente.

Los reportes son otro caso especial. Un reporte no es solo una pantalla: puede depender de entidades, reglas, filtros y acciones previas. Si un reporte no se alimenta de nada, el grafo debe invitar a revisar su fuente.

## Cuándo usarlo
Úsalo cuando ya existe un levantamiento lógico con suficiente material y necesitas una vista visual para revisar coherencia. Es especialmente útil cuando el documento tiene muchas reglas, acciones, estados, entidades y preguntas que se cruzan.

Úsalo antes de derivar modelos posteriores si quieres comprobar que las piezas críticas no están aisladas. Por ejemplo, antes de pasar a modelo conceptual conviene revisar qué entidades nacen de acciones o reglas. Antes de pasar a BPMN o UML Actividad conviene revisar qué acciones transforman estado.

Úsalo cuando el proyecto se apoya en IA. El grafo lógico ofrece una gramática clara para que la IA produzca Markdown revisable en lugar de texto desordenado o diagramas sin trazabilidad.

Úsalo para explicar a otra persona por qué una regla, entidad o reporte existe. El grafo permite seguir la cadena desde macroflujo hasta acción, regla, entidad, estado o pregunta pendiente.

## Cuándo no usarlo
No lo uses como reemplazo del levantamiento lógico. El expediente fuente conserva el contexto, la evidencia, las notas, las preguntas y la madurez. El grafo es una vista, no toda la documentación.

No lo uses para modelar cualquier relación informal. Si lo que necesitas es un mapa mental, un grafo de conocimiento libre o una red conceptual sin tipos estrictos, conviene usar Grafo libre.

No lo uses como BPMN. El grafo lógico muestra dependencia y trazabilidad; BPMN muestra flujo de proceso con eventos, actividades, decisiones y secuencia operativa.

No lo uses como modelo conceptual o diccionario de datos. ENT puede sugerir entidades candidatas, pero el detalle de atributos, tipos y reglas de campos pertenece a otros módulos.

Tampoco conviene usarlo para simular cierre. Si hay PEND o RISK relevantes, deben permanecer visibles. Ocultar incertidumbre para que el diagrama parezca completo debilita el análisis.

## Errores comunes
Un error común es tratar todos los nodos como cajas libres. Si se ignora el tipo MF, FL, CU o ACC, el grafo pierde su valor y se convierte en dibujo genérico.

Otro error es conectar todo con todo usando depende_de. Esa relación es útil, pero si reemplaza a requiere, protege, garantiza, crea, modifica, consulta o bloquea, el grafo pierde precisión.

También es un error eliminar preguntas pendientes del dibujo. Las preguntas pendientes son parte de la verdad del análisis. Si una pregunta bloquea una decisión, debe verse.

Un error frecuente es confundir acción con entidad. “Matrícula” puede ser entidad, estado, proceso o acción según el contexto. El grafo obliga a precisar si se habla de registrar matrícula, matrícula vigente, flujo de matrícula o entidad matrícula.

Otro error es creer que el grafo debe quedar bonito antes de ser correcto. La legibilidad importa, pero primero debe ser semánticamente defendible: nodos tipados, relaciones válidas, leyenda visible y trazabilidad suficiente.

## Cierre académico
El grafo lógico del negocio conecta el levantamiento con la documentación canónica. Su valor está en hacer visible la lógica que sostiene los diagramas posteriores.

Un buen grafo lógico no pretende saberlo todo. Muestra lo validado, señala lo pendiente, permite revisar dependencias y conserva una lectura humana. Cuando se usa con disciplina, ayuda a que el proyecto avance desde conversación hacia diseño sin perder la razón de cada decisión.
