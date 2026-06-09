# Tanda VIS-COPY-002 — Transferencia de selección entre proyectos abiertos compatibles

## Objetivo

Completar el flujo iniciado por VIS-COPY-001 para permitir que una selección visual de canvas se copie hacia otro proyecto abierto del mismo tipo desde la toolbar contextual.

## Alcance implementado

- Acción contextual **Transferir** en la toolbar de:
  - Grafo libre.
  - Grafo lógico del negocio.
- Diálogo de selección de destino con proyectos abiertos compatibles del mismo tipo.
- Validación de compatibilidad por `DiagramTypeId`.
- Reutilización del portapapeles visual semántico de VIS-COPY-001.
- Transferencia de nodos, relaciones y layout al proyecto destino.
- El proyecto destino queda activo tras aceptar la transferencia y se marca como modificado por el mecanismo normal de sincronización del workspace.

## Reglas de producto

- No se permite transferir entre tipos distintos.
- No se transfiere a placeholders ni a la pestaña origen.
- La selección debe contener elementos visuales válidos.
- Las relaciones seleccionadas conservan tipo, etiqueta, dirección, propiedades de layout, bendpoints y marcadores según el soporte del payload.
- El destino genera IDs/códigos nuevos para evitar colisiones.

## Límites explícitos

- Esta tanda no extiende el flujo a UML Clases, Mapa de módulos, Wireframes, Flujo de pantallas, Arquitectura ni Comportamiento.
- Esta tanda no usa el portapapeles del sistema operativo.
- Esta tanda deja activo el proyecto destino después de pegar, para que el usuario revise inmediatamente el resultado.

## Validación local recomendada

1. Abrir dos proyectos de Grafo libre.
2. Seleccionar varios nodos y relaciones con rectángulo.
3. Usar **Transferir** y elegir el otro Grafo libre.
4. Confirmar que aparecen nodos, relaciones, bendpoints y layout desplazados.
5. Repetir con dos proyectos de Grafo lógico.
6. Intentar transferir cuando no exista destino compatible y confirmar mensaje informativo.
