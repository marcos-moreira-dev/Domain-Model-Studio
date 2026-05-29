# Tanda 061 — Smoke visual post-capas

## Propósito

Esta tanda no introduce cambios de código. Formaliza el smoke manual posterior a las Tandas 59 y 60, donde se agregó orden visual persistible (`zOrder`) y una política base de capas para el canvas especializado.

La validación busca confirmar que el nuevo orden visual es coherente en pantalla, PNG, SVG y persistencia `.dms`, sin reabrir problemas ya corregidos de drag, selección, bendpoints o contenedores transparentes.

## Alcance

Aplica a los workspaces visuales especializados:

- Mapa de módulos.
- UML Clases.
- Flujo de pantallas.
- Wireframes administrativos.
- Comportamiento / BPMN / UML visual.
- Arquitectura / C4 / despliegue.
- Grafo libre.
- Grafo lógico del negocio.

No aplica a:

- Pantalla de inicio.
- Modelo conceptual legacy congelado.
- Levantamiento lógico documental.
- Diccionario de datos documental.
- Roles/permisos como matriz estructurada.

## Contrato a validar

El orden esperado es:

1. Fondo y grilla.
2. Contenedores o zonas de fondo.
3. Conectores y relaciones.
4. Tarjetas, nodos y rectángulos operables.
5. Labels, bendpoints, resize handles, selección y overlays.
6. Preview vivo de interacción.

La Tanda 59 valida el orden relativo entre nodos seleccionables. La Tanda 60 valida la separación base entre familias de capas. Esta tanda valida ambos contratos mediante uso real de la interfaz.

## Evidencia esperada

El reporte manual debe registrar:

- Fecha, versión/carpeta evaluada y sistema operativo.
- Resultado de `scripts\\02-ejecutar-tests.bat`.
- Validación por tipo de diagrama.
- Validación de guardar/reabrir `.dms`.
- Validación de PNG/SVG cuando el tipo lo soporte.
- Incidencias visuales, capturas si existen y decisión de cierre.

## Criterio de aprobación

La Tanda 61 queda aprobada solo si:

- Los tests pasan localmente.
- El smoke manual no encuentra bloqueadores.
- Las relaciones no tapan tarjetas operables en los casos representativos.
- Los contenedores semánticos siguen detrás de nodos y conectores donde corresponde.
- Las acciones de capa de Tanda 59 se conservan al guardar/reabrir `.dms`.
