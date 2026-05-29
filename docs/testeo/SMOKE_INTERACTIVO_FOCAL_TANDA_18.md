# Tanda 18 — Smoke interactivo focal de canvas especializado

## Objetivo

Validar manualmente el contrato runtime que los tests fuente no pueden cubrir todavía: selección, segundo arrastre, bendpoints, labels, contenedores visuales y paneles SideDock en los workspaces especializados.

Este smoke no reemplaza `scripts\02-ejecutar-tests.bat`. Complementa la suite automatizada con gestos JavaFX reales.

## Precondición

1. Ejecutar `scripts\00-verificar-entorno.bat`.
2. Ejecutar `scripts\02-ejecutar-tests.bat`.
3. Abrir la aplicación con `scripts\01-ejecutar-app.bat`.
4. Usar ejemplos oficiales UENS cuando estén disponibles.

## Escenario crítico: segundo arrastre sobre elemento seleccionado

Para cada workspace visual especializado:

1. Crear o abrir un ejemplo importable.
2. Hacer clic sobre un nodo/elemento arrastrable.
3. Soltar el mouse sin mover.
4. Volver a hacer clic sobre el mismo elemento ya seleccionado.
5. Arrastrarlo al menos 80 px.
6. Confirmar que el elemento se mueve en tiempo real y queda en su nueva posición al soltar.
7. Repetir seleccionando primero desde el panel de estructura y luego arrastrando desde el canvas.

## Matriz mínima por tipo

| Tipo | Elemento | Resultado esperado |
|---|---|---|
| Grafo libre | Nodo | Segundo drag mueve el nodo seleccionado. |
| Grafo lógico | Nodo lógico | Segundo drag mueve el nodo seleccionado. |
| Mapa de módulos | Módulo | Segundo drag mueve módulo y respeta contención. |
| Arquitectura C4 | Sistema/contenedor normal | Segundo drag mueve el nodo. |
| Despliegue técnico | ENVIRONMENT/NETWORK | Mueve solo desde banda de título/handle. |
| Flujo de pantallas | Pantalla | Segundo drag mueve la pantalla. |
| Wireframes | Pantalla/componente | Segundo drag mueve; esquina inferior puede activar resize. |
| UML Clases | Clase/módulo | Segundo drag mantiene vista filtrada y mueve. |
| BPMN/UML Actividad/Estado/Casos de uso | Nodo | Segundo drag mueve el nodo. |
| UML Secuencia | Lifeline/fragmento redimensionable | Drag y resize siguen diferenciados. |

## Bendpoints y relaciones

En tipos con bendpoints:

1. Seleccionar una relación.
2. Crear un bendpoint si la herramienta lo permite.
3. Confirmar feedback visual claro.
4. Arrastrar el bendpoint.
5. Confirmar que la ruta se actualiza durante el movimiento.
6. Soltar y confirmar que la ruta queda persistida.

## Selección rectangular

1. Dibujar un rectángulo de selección sobre nodos y relaciones.
2. Confirmar que los nodos dentro del área quedan seleccionados.
3. Confirmar que las relaciones visibles dentro del área se seleccionan cuando el tipo lo soporta.
4. Arrastrar un nodo de la selección múltiple y confirmar que el grupo se mueve.

## Paneles SideDock / estructura

1. Seleccionar un elemento en el canvas.
2. Confirmar que la lista/tabla del SideDock refleja la selección sin parpadeo fuerte.
3. Seleccionar otro elemento desde SideDock.
4. Confirmar que el canvas resalta el nuevo elemento.
5. Volver al canvas y repetir el segundo drag.

## Criterio de aprobación

El smoke queda aprobado si:

- el segundo drag funciona en todos los tipos visuales aplicables;
- no se pierde selección al pasar por SideDock;
- los contenedores handle-only de arquitectura se mueven solo desde el título;
- bendpoints y labels no bloquean el drag normal salvo cuando son el objetivo explícito;
- no aparecen errores en consola durante los gestos.

## Evidencia sugerida

Completar `docs\testeo\reportes\REPORTE_SMOKE_INTERACTIVO_TANDA_18.md` con fecha, commit/ZIP, sistema, JDK, Maven y observaciones por tipo.
