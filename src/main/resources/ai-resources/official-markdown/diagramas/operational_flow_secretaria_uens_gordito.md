---
dms_version: "1"
diagram_type: "operational-flow"
name: "UENS — flujo operativo de secretaría"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Flujo operativo administrativo, menos formal que BPMN, para atención de Secretaría con trazabilidad."
---
# Flujo operativo

Objetivo: atender una solicitud administrativa de estudiante desde recepción hasta cierre trazable.

## Pasos

1. Secretaría recibe solicitud del representante legal.
2. Secretaría verifica identidad del estudiante o busca coincidencias por nombres, apellidos y fecha de nacimiento.
3. Secretaría revisa o registra datos del representante legal.
4. Secretaría identifica el trámite: alta de estudiante, actualización, asignación de sección, consulta o reporte.
5. Sistema administrativo muestra estado del estudiante y sección vigente si existe.
6. Secretaría completa datos faltantes o corrige información permitida.
7. Sistema administrativo valida campos obligatorios, estado de sección y cupo disponible.
8. Secretaría confirma la operación.
9. Sistema administrativo guarda cambios y registra evento de auditoría.
10. Secretaría entrega confirmación o indica observación pendiente.

## Puntos de atención

- Evitar duplicidad de estudiantes por nombres similares.
- No asignar sección si el cupo está completo.
- Confirmar teléfono o correo del representante cuando sea necesario.
- Registrar observación cuando exista corrección manual relevante.
- Matrícula se entiende como trámite operativo; la persistencia V2 usa estudiante.seccion_id.
