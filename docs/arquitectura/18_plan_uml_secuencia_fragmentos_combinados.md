# Plan — UML Secuencia con fragmentos combinados y guía académica

## Propósito

Fortalecer el diagrama UML Secuencia para que deje de ser solo una línea temporal básica de participantes y mensajes, y pase a soportar elementos centrales de la notación UML de interacción:

- participantes / lifelines;
- mensajes síncronos, asíncronos, retornos y auto-mensajes;
- activaciones;
- fragmentos combinados `alt`, `opt`, `loop`, `par`, `break`, `critical` y `ref`;
- operandos con guardas como `[estudiante != null]` o `[sin permiso]`;
- anidación controlada de fragmentos;
- exportación SVG/PNG/Markdown coherente;
- explicación académica clara dentro de la guía.

## Frontera teórica

UML Secuencia no es un grafo libre. Aunque reutiliza zoom, paneo, selección y exportación del canvas transversal, conserva reglas temporales propias:

- el tiempo avanza de arriba hacia abajo;
- los participantes se ordenan horizontalmente;
- las lifelines son verticales;
- los mensajes tienen una posición temporal;
- los fragmentos combinados contienen regiones temporales, no nodos libres arbitrarios;
- los puntos intermedios libres no deben convertir una secuencia en un diagrama de flujo.

Por eso, las mejoras deben implementarse como extensión del motor temporal de secuencia, no como copia directa del comportamiento de grafo libre.

## Tanda 29 propuesta — UML Secuencia: fragmentos combinados

### Modelo de dominio/comportamiento

Agregar o consolidar soporte explícito para:

- `SequenceFragmentKind` con valores `ALT`, `OPT`, `LOOP`, `PAR`, `BREAK`, `CRITICAL`, `REF`;
- `SequenceFragmentOperand` con guardia textual y rango vertical;
- relación entre fragmento y mensajes contenidos;
- validación de fragmentos vacíos, guardas faltantes y anidación excesiva;
- persistencia en `.dms` y roundtrip JSON.

### Layout temporal

Extender `SequenceTimelineLayoutPolicy` para:

- reservar altura suficiente para encabezado del fragmento;
- dibujar compartimentos internos para operandos de `alt` y `par`;
- conservar mensajes dentro del rango vertical del fragmento;
- ajustar lifelines para cubrir todo el tramo temporal;
- evitar que un fragmento tape participantes o mensajes.

### Render visual

Extender `SequenceRenderKit` para:

- dibujar marco rectangular de fragmento combinado;
- dibujar pestaña superior izquierda con `alt`, `opt`, `loop`, `par`, etc.;
- mostrar guardas `[condición]` sobre cada operando;
- usar separadores horizontales para ramas de `alt` y `par`;
- mantener estilo académico sobrio y exportable.

### Interacción

Agregar acciones específicas:

- crear fragmento `alt`;
- crear fragmento `opt`;
- crear fragmento `loop`;
- crear fragmento `par`;
- agregar operando/rama;
- editar guarda;
- mover fragmento verticalmente de forma controlada;
- redimensionar altura del fragmento;
- validar si los mensajes quedan dentro del fragmento.

### SideDock

El módulo de estructura para UML Secuencia debe permitir revisar:

- participantes;
- mensajes;
- activaciones;
- fragmentos;
- operandos/guardas;
- advertencias de validación temporal.

### Exportación

Actualizar:

- `SpecializedSvgSequenceWriter` para SVG vectorial real de fragmentos;
- exportación PNG desde canvas;
- Markdown para describir fragmentos, guardas y mensajes contenidos.

## Tanda 30 propuesta — Guía académica UML Secuencia premium

Actualizar `src/main/resources/help/topics/uml-sequence.md` para explicar con mayor precisión:

- anatomía formal de lifeline, activación y mensaje;
- diferencia entre mensaje síncrono, asíncrono, retorno, creación y auto-mensaje;
- uso correcto de `alt`, `opt`, `loop`, `par`, `break`, `critical` y `ref`;
- guardas `[condición]` y operandos;
- errores comunes al usar fragmentos;
- cuándo no usar fragmentos;
- ejemplos administrativos UENS: matrícula, registro de calificación, autorización, auditoría y reporte.

Actualizar también las figuras didácticas en `ManualFigureUmlSequenceFigures` para incluir:

- fragmento `loop` con iteración;
- fragmento `alt` con dos ramas;
- fragmento `opt` para acción opcional;
- fragmento `par` para trabajo paralelo controlado;
- ejemplo de mala práctica: secuencia convertida en flujo de proceso.

## Guardarraíles recomendados

Agregar pruebas fuente/contrato para verificar:

- UML Secuencia conserva perfil `SEQUENCE` y no adopta bendpoints libres de grafo;
- `SequenceRenderKit` dibuja pestaña de fragmento y guardas;
- `SpecializedSvgSequenceWriter` exporta fragmentos vectoriales;
- la guía académica menciona `alt`, `opt`, `loop`, `par`, guardas y operandos;
- los ejemplos oficiales importables incluyen al menos un `alt` y un `loop`.

## Fuera de alcance de esta mejora

No incluir en esta tanda:

- motor de ejecución;
- simulación runtime;
- parser completo de PlantUML;
- inferencia automática de secuencias desde código fuente;
- transformación automática desde BPMN o actividad;
- mezcla con el modelo conceptual congelado.

## Estado posterior — Tanda 29 aplicada

La Tanda 29 implementó la primera versión productiva de fragmentos combinados sin cambiar la superficie común del canvas. La semántica se concentra en `SequenceFragmentKind`, `SequenceFragmentOperand` y `SequenceCombinedFragmentSpec`, manteniendo compatibilidad con nodos `FRAGMENT` existentes. Queda para una tanda posterior una UI de edición más cómoda para operandos/guardas, si se desea.

## Estado posterior — Tanda 30 aplicada

La Tanda 30 actualizó la guía académica de UML Secuencia para explicar con mayor profundidad los fragmentos combinados implementados en Tanda 29. La ayuda teórica ahora cubre `alt`, `opt`, `loop`, `par`, `break`, `critical`, `ref`, guardas, operandos, anidación controlada, relación con pruebas y relación con levantamiento lógico. También se agregaron figuras didácticas específicas y un guardarraíl de teoría.

