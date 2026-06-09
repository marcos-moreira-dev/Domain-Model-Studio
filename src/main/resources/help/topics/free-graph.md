# Grafo libre

## Introducción
Un grafo libre permite dibujar nodos y relaciones sin imponer una notación especializada. Es útil cuando todavía se está pensando, comparando conceptos o conectando elementos que no pertenecen claramente a UML, BPMN, C4, módulos administrativos, roles o diccionario de datos.

La idea central es simple: cada nodo representa una cosa que quieres nombrar y cada relación representa una conexión entre dos nodos. Esa conexión puede ser dirigida, como una dependencia o flujo de influencia, o no dirigida, como una asociación general.

![Grafo libre con nodos y relaciones etiquetadas.](figure:free-graph-overview)

## Qué es
Un grafo libre es una red editable de nodos y relaciones. Cada nodo tiene título y contenido. Cada relación puede tener etiqueta y dirección.

A diferencia de un diagrama formal, no obliga a usar símbolos con significado rígido. El usuario decide qué representa cada nodo: idea, concepto, documento, persona, módulo, evidencia, decisión, riesgo o recurso.

![Nodo de grafo libre con título y contenido.](figure:free-graph-node-content)

## Para qué sirve
Sirve para explorar relaciones antes de escoger una notación más precisa. También sirve para mapas de conocimiento, dependencias informales, análisis de causa y efecto, lluvia de ideas ordenada, redes de conceptos y esquemas de explicación.

En un levantamiento de información puede ayudar a conectar conceptos sueltos que todavía no justifican crear un modelo conceptual, un proceso BPMN o un diagrama UML.

## Elementos principales
Los elementos principales son nodo, relación, etiqueta, dirección y observaciones generales.

Un nodo debe tener un título claro. El contenido del nodo debe explicar la idea sin convertir el grafo en un documento largo. Una relación debe conectar dos nodos existentes. La etiqueta de la relación debe decir por qué existe esa conexión.

![Relación dirigida y relación no dirigida en un grafo libre.](figure:free-graph-edge-types)

## Relaciones y lectura
Una relación dirigida se lee desde el nodo origen hacia el nodo destino. Por ejemplo, `Diagnóstico -> Repuesto: puede requerir` se interpreta como que el diagnóstico puede producir la necesidad de un repuesto.

Una relación no dirigida se lee como asociación. Por ejemplo, `Docente -- Sección: trabaja con` indica cercanía conceptual sin afirmar flujo ni dependencia estricta.

Una autorrelación conecta un nodo consigo mismo. Por ejemplo, `Auditoría -> Auditoría: revisa cambios` sirve para representar ciclos, recursión, retroalimentación o revisión interna.

La lectura correcta depende de la etiqueta. Una flecha sin etiqueta puede entenderse, pero en grafos de trabajo conviene nombrar la relación para no dejar ambigüedades.

## Casos especiales
Puede haber grafos dirigidos, no dirigidos o mixtos. En un grafo dirigido, todas las relaciones se normalizan como flechas. En un grafo no dirigido, todas se normalizan como asociaciones. En un grafo mixto, cada relación conserva su tipo.

Un grafo libre también puede representar relaciones temporales simples, pero si el orden del tiempo es lo principal conviene usar UML Secuencia o UML Actividad.

## Cuándo usarlo
Úsalo cuando necesitas libertad: conectar ideas, conceptos, riesgos, dependencias generales, argumentos, decisiones, rutas mentales o partes de un problema que aún no tienen forma definitiva.

También es útil para preparar material didáctico, explicar un tema a alto nivel o crear una vista previa antes de formalizar un modelo más especializado.

![Grafo libre usado como mapa de conocimiento.](figure:free-graph-knowledge-map)

## Cuándo no usarlo
No lo uses para sustituir diagramas con semántica fuerte. Si necesitas clases, usa UML Clases. Si necesitas procesos de negocio, usa BPMN básico o flujo operativo. Si necesitas arquitectura de software, usa C4 o despliegue técnico. Si necesitas roles y permisos, usa matriz de roles.

El grafo libre es flexible, pero esa flexibilidad también reduce precisión. Cuando el problema ya tiene una notación más clara, esa notación debe tener prioridad.

## Errores comunes
Un error común es llenar cada nodo con demasiado texto. El nodo debe resumir; el detalle completo puede quedar en documentos externos o en otra vista.

Otro error es crear relaciones sin etiqueta. Si muchas líneas no tienen nombre, el grafo se vuelve decorativo y pierde trazabilidad.

También es un error usar el grafo libre para esconder que falta análisis. Si las relaciones ya son procesos, clases o módulos, conviene migrarlas al diagrama correspondiente.

![Errores comunes: nodos enormes, relaciones sin etiqueta y mezcla de notaciones.](figure:free-graph-common-errors)
