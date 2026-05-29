---
dms_version: "1"
diagram_type: "uml-sequence"
name: "UENS — UML secuencia registrar calificación"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Secuencia temporal de registro de calificación desde usuario administrativo hasta servicios, repositorios y auditoría."
---
# Participantes

- Usuario administrativo
- Pantalla de calificaciones
- API CalificacionController
- CalificacionCommandService
- CalificacionRepository
- AuditoriaEventService

# Mensajes

1. Usuario administrativo -> Pantalla de calificaciones: selecciona clase y parcial
2. Pantalla de calificaciones -> API CalificacionController: solicita estudiantes y calificaciones existentes
3. API CalificacionController -> CalificacionRepository: consulta datos por clase/parcial
4. CalificacionRepository -> API CalificacionController: devuelve datos disponibles
5. API CalificacionController -> Pantalla de calificaciones: muestra lista de estudiantes
6. Usuario administrativo -> Pantalla de calificaciones: ingresa nota y observación
7. Pantalla de calificaciones -> Pantalla de calificaciones: valida formato local de nota
8. Pantalla de calificaciones -> API CalificacionController: envía calificación capturada
9. API CalificacionController -> CalificacionCommandService: validar y guardar calificación
10. CalificacionCommandService -> CalificacionCommandService: validar rango, parcial y unicidad
11. CalificacionCommandService -> CalificacionRepository: persistir calificación
12. CalificacionRepository -> CalificacionCommandService: confirma guardado
13. CalificacionCommandService -> AuditoriaEventService: registrar evento auditable
14. AuditoriaEventService -> CalificacionCommandService: confirma auditoría
15. CalificacionCommandService -> API CalificacionController: devuelve resultado
16. API CalificacionController -> Pantalla de calificaciones: confirma registro
17. Pantalla de calificaciones -> Usuario administrativo: muestra resultado guardado

# Fragmentos combinados

- fragmento: loop | id: loop-estudiantes-calificar | titulo: Registrar calificación por estudiante | guarda: [por cada estudiante de la clase] | rango: 6..17 | operandos: [siguiente estudiante] 6..17
- fragmento: alt | id: alt-nota-valida | titulo: Validación de nota | padre: loop-estudiantes-calificar | rango: 8..16 | operandos: [nota válida] 8..16; [nota inválida] 8..8
- fragmento: ref | id: ref-auditoria-notas | titulo: Registrar auditoría | padre: alt-nota-valida | referencia: Secuencia reutilizable de auditoría | rango: 13..14
