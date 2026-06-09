# Smoke visual post-capas — Tanda 61

## Preparación

1. Ejecutar:

```bat
scripts\02-ejecutar-tests.bat
```

2. Abrir la aplicación:

```bat
scripts\01-ejecutar-app.bat
```

3. Usar ejemplos oficiales UENS o proyectos pequeños donde existan nodos, relaciones y superposición visual.

## Validaciones transversales

Para cada diagrama visual aplicable:

- Seleccionar una tarjeta/nodo.
- Usar **Traer al frente**, **Enviar al fondo**, **Subir** y **Bajar** desde el toolbar contextual.
- Confirmar que el orden visual cambia sin perder selección.
- Arrastrar la tarjeta y confirmar que las relaciones acompañan el movimiento en vivo.
- Confirmar que las relaciones no bloquean el drag del nodo.
- Guardar como `.dms`, cerrar y reabrir.
- Confirmar que el orden visual se conserva.
- Exportar PNG cuando el tipo lo soporte y verificar que respeta el mismo orden.
- Exportar SVG cuando el tipo lo soporte y verificar que no contradice la política documental del tipo.

## Flujo de pantallas

- Importar o crear un flujo con al menos dos pantallas conectadas.
- Colocar una transición cruzando cerca de una tarjeta.
- Confirmar que la tarjeta queda visualmente encima de la relación.
- Arrastrar la tarjeta y verificar ruta viva de la transición.
- Probar **Frente/Fondo/Subir/Bajar** con una pantalla seleccionada.

## Mapa de módulos

- Validar módulos contenedores y dependencias.
- Confirmar que dependencias no tapan módulos operables.
- Mover un módulo con relaciones y confirmar actualización en vivo.
- Probar orden visual entre módulos superpuestos.

## Arquitectura / C4 / despliegue

- Validar `BOUNDARY`, `ENVIRONMENT` y `NETWORK` como zonas de fondo.
- Confirmar que se arrastran solo desde el título/handle cuando corresponde.
- Confirmar que nodos normales quedan encima de zonas y relaciones.
- Exportar PNG/SVG y revisar que los fondos no tapen conectores ni nodos.

## UML Clases

- Validar módulos, clases y relaciones.
- Confirmar que las relaciones no bloquean el arrastre de clases.
- Probar orden visual entre módulos/clases cuando haya superposición.
- Validar que vistas filtradas no pierdan el orden visual esperado.

## Comportamiento / BPMN / UML visual

- Validar al menos BPMN básico, UML Actividad o UML Estados.
- Confirmar que nodos quedan encima de flujos cuando corresponde.
- Arrastrar nodos conectados y verificar rutas vivas.

## Wireframes administrativos

- Validar pantallas y componentes.
- Confirmar que resize handles siguen encima de nodos.
- Evitar confundir drag de nodo con resize desde la esquina.
- Probar orden visual entre componentes superpuestos.

## Grafo libre y Grafo lógico

- Validar nodos conectados por relaciones.
- Arrastrar nodos y verificar rutas vivas.
- Confirmar que la selección rectangular sigue funcionando con nodos y relaciones.
- Probar acciones de orden visual cuando estén disponibles.

## Criterios de bloqueo

Se considera bloqueador si:

- Un nodo visual no puede arrastrarse desde su zona operable.
- Una relación tapa o bloquea de forma persistente una tarjeta operable.
- El orden visual se pierde al guardar/reabrir `.dms`.
- PNG o SVG contradicen de forma clara el orden del canvas para el mismo tipo.
- Un contenedor transparente de arquitectura vuelve a capturar todo el rectángulo como drag target.
