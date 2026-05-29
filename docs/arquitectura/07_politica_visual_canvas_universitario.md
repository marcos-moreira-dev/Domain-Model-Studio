# Tanda 7 — Política visual de canvas universitario

## Objetivo

La Tanda 7 aplica una política visual sobria para que los diagramas teóricos se lean como notación de modelado, no como tarjetas decorativas. El objetivo no es embellecer el lienzo, sino hacerlo más fiel a la teoría de cada herramienta.

## Regla principal

En el canvas manda la notación del diagrama:

- UML Casos de uso usa actor, elipse, límite de sistema y relaciones.
- UML Clases usa cajas con compartimentos y relaciones UML.
- UML Actividad usa inicio, fin, acción, decisión, fork/join y flujos.
- UML Estados usa estados, inicio/final y transiciones.
- BPMN básico usa eventos, tareas, compuertas y flujos.
- UML Secuencia usa participantes, líneas de vida, activaciones y mensajes.

La interfaz general de la aplicación puede tener estética de escritorio; el lienzo teórico debe ser primitivo, claro y legible.

## Cambios aplicados

### UML/BPMN de comportamiento

`BehaviorRenderKit` ahora distingue nodos que deben usar notación primitiva. Para esos nodos crea figuras principales dinámicas según los límites del layout:

- actor UML como figura de palitos con etiqueta inferior;
- caso de uso como elipse real con texto centrado;
- límite de sistema como rectángulo contenedor simple;
- acción/estado/tarea como rectángulo simple o redondeado según notación;
- decisión/gateway como rombo;
- inicio/final como círculos propios;
- fork/join como barra.

Estos nodos siguen usando `CanvasNodeViewFactory`, de modo que el símbolo compuesto se selecciona y arrastra desde una raíz interactiva única.

### UML Clases

Se añadieron overrides CSS para quitar sombras, radios decorativos y fondos suaves. Las clases se presentan como cajas UML más directas, con separadores y texto de alto contraste.

### UML Secuencia

Se ajustó el CSS para participantes, mensajes, líneas de vida, activaciones, fragmentos y notas. La prioridad es legibilidad temporal y texto negro, no decoración visual.

### C4, despliegue y mapa de módulos

Estos tipos sí aceptan cajas, porque su teoría y uso administrativo lo permiten. La tanda no elimina las cajas; solo reduce degradados, sombras y decoración excesiva.

## Qué no se tocó

- No se cambió la estructura del sidebar.
- No se modificó el PDF del diccionario.
- No se cambió roles/permisos.
- No se reescribieron exportadores.
- No se migró el modelo conceptual.

## Criterios de mantenimiento

1. No introducir degradados ni sombras en nodos UML/BPMN teóricos.
2. Si una notación espera una figura, esa figura debe ser el elemento principal, no un icono dentro de una tarjeta.
3. Si un tipo acepta cajas por naturaleza, mantenerlas sobrias.
4. Todo texto del canvas debe tener contraste suficiente.
5. Los símbolos compuestos deben seguir comportándose como una sola unidad interactiva.
