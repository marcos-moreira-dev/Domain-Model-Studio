---
id: colegio_chen_detallado_v1
title: Modelo conceptual - Gestión académica escolar
notation: chen
version: 1.0.0
status: draft
author: @programalobien
source: ejemplo_academico
domain: educacion
---

# Entidades

## Estudiante
id: estudiante
module: academico
description: Persona matriculada en la institución educativa y evaluada dentro de clases.
status: confirmed
confidence: high

- pk id
- nombres
- apellidos
- fecha_nacimiento
- edad [derived]
- estado

## RepresentanteLegal
id: representante_legal
module: academico
description: Persona responsable legal o familiar asociado a uno o varios estudiantes.
status: confirmed
confidence: high

- pk id
- nombres
- apellidos
- telefono [optional]
- correo_electronico [optional]
- estado

## Seccion
id: seccion
module: academico
description: Agrupación escolar por grado, paralelo y año lectivo.
status: confirmed
confidence: high

- pk id
- grado
- paralelo
- cupo_maximo
- anio_lectivo
- cantidad_estudiantes_registrados [derived]
- estado

## Docente
id: docente
module: academico
description: Persona encargada de impartir clases.
status: confirmed
confidence: high

- pk id
- nombres
- apellidos
- telefono [optional]
- correo_electronico [optional]
- estado

## Asignatura
id: asignatura
module: academico
description: Materia o área de conocimiento impartida dentro de una clase.
status: confirmed
confidence: high

- pk id
- nombre
- area
- descripcion [optional]
- grado
- estado

## Clase
id: clase
module: academico
description: Oferta concreta de una asignatura para una sección, con docente y franja horaria.
status: confirmed
confidence: high

- pk id
- franja_horaria
- estado

## Calificacion
id: calificacion
module: evaluacion
description: Registro de nota de un estudiante dentro de una clase y parcial específico.
status: confirmed
confidence: high

- pk id
- numero_parcial
- nota
- fecha_registro [optional]
- observacion [optional]

## UsuarioSistemaAdministrativo
id: usuario_sistema_administrativo
module: seguridad
description: Usuario con acceso al sistema administrativo.
status: confirmed
confidence: high

- pk id
- nombre_login [unique]
- credencial [sensitive]
- rol
- estado

# Relaciones

## Representa
id: representa
from: RepresentanteLegal
to: Estudiante
from_cardinality: 1
to_cardinality: 0..M
description: Un representante legal puede representar a varios estudiantes; cada estudiante debe tener un representante principal.
status: confirmed
confidence: high

## Agrupa
id: agrupa
from: Seccion
to: Estudiante
from_cardinality: 0..1
to_cardinality: 0..35
description: Una sección agrupa estudiantes hasta su cupo máximo; un estudiante pertenece como máximo a una sección activa.
status: confirmed
confidence: high

## Oferta
id: oferta
from: Seccion
to: Clase
from_cardinality: 1
to_cardinality: 0..M
description: Una sección puede ofertar varias clases; cada clase corresponde a una sección.
status: confirmed
confidence: high

## Imparte
id: imparte
from: Docente
to: Clase
from_cardinality: 0..M
to_cardinality: 0..1
description: Un docente puede impartir varias clases; una clase puede tener un docente asignado o quedar pendiente.
status: confirmed
confidence: medium

## SeDictaComo
id: se_dicta_como
from: Asignatura
to: Clase
from_cardinality: 1
to_cardinality: 0..M
description: Una asignatura puede dictarse como varias clases; cada clase corresponde a una asignatura.
status: confirmed
confidence: high

## Obtiene
id: obtiene
from: Estudiante
to: Calificacion
from_cardinality: 1
to_cardinality: 0..M
description: Un estudiante puede obtener muchas calificaciones; cada calificación pertenece a un estudiante.
status: confirmed
confidence: high

## CorrespondeA
id: corresponde_a
from: Clase
to: Calificacion
from_cardinality: 1
to_cardinality: 0..M
description: Una clase puede tener muchas calificaciones; cada calificación corresponde a una clase.
status: confirmed
confidence: high

# Reglas de negocio

- El estado general de entidades operativas se maneja como ACTIVO o INACTIVO.
- La edad del estudiante se deriva desde fecha_nacimiento y no debería almacenarse como dato principal.
- La cantidad de estudiantes registrados en una sección se deriva por conteo.
- Una calificación se identifica conceptualmente por estudiante, clase y número de parcial.
- El usuario administrativo queda separado del dominio académico.

# Dudas pendientes

- Confirmar si una clase puede tener más de un docente en fases futuras.
- Confirmar si el representante legal puede ser compartido por estudiantes de diferentes familias por error o por diseño.
- Confirmar si se requiere historial de cambios de calificaciones.
