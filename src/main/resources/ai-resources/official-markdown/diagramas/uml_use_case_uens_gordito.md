---
dms_version: "1"
diagram_type: "uml-use-case"
name: "UENS — UML casos de uso sistema escolar"
sample_kind: "uens-family"
domain: "unidad educativa"
status: "importable"
importable: true
intended_output: "diagrama visual"
description: "Casos de uso observables para roles implementados ADMIN y SECRETARIA."
---
# Sistema

Nombre: Sistema administrativo UENS
Límite: Operación académica y administrativa de fase inicial.

- sistema: Sistema administrativo UENS

# Actores

- actor: Administrador
- actor: Secretaría

# Casos de uso

- Iniciar sesión
- Gestionar estudiantes
- Validar datos obligatorios
- Gestionar representantes legales
- Asignar sección vigente
- Gestionar secciones
- Gestionar docentes
- Gestionar asignaturas
- Gestionar clases
- Registrar calificaciones
- Corregir calificación
- Solicitar reportes
- Descargar reportes
- Revisar auditoría
- Administrar usuarios

# Relaciones

- Administrador -> Iniciar sesión
- Secretaría -> Iniciar sesión
- Secretaría -> Gestionar estudiantes
- Secretaría -> Gestionar representantes legales
- Secretaría -> Asignar sección vigente
- Secretaría -> Gestionar secciones
- Secretaría -> Gestionar docentes
- Secretaría -> Gestionar asignaturas
- Secretaría -> Gestionar clases
- Secretaría -> Registrar calificaciones
- Secretaría -> Solicitar reportes
- Secretaría -> Descargar reportes
- Administrador -> Gestionar estudiantes
- Administrador -> Corregir calificación
- Administrador -> Solicitar reportes
- Administrador -> Descargar reportes
- Administrador -> Revisar auditoría
- Administrador -> Administrar usuarios
- Gestionar estudiantes -> Validar datos obligatorios: include
- Asignar sección vigente -> Validar datos obligatorios: include
- Registrar calificaciones -> Corregir calificación: extend
