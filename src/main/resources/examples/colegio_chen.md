---
id: colegio_conceptual_v1
title: Modelo conceptual - Colegio
notation: chen
version: 0.1.0
status: draft
---

# Entidades

## Estudiante
id: estudiante
module: academico
description: Persona matriculada o registrada dentro del sistema académico.

- pk id
- nombres
- apellidos
- fecha_nacimiento
- edad [derived]
- estado

## RepresentanteLegal
id: representante_legal
module: academico
description: Persona responsable del estudiante.

- pk id
- nombres
- apellidos
- telefono [optional]
- correo_electronico [optional]

## Seccion
id: seccion
module: academico
description: Grupo académico definido por grado, paralelo y año lectivo.

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
description: Profesor que puede impartir clases.

- pk id
- nombres
- apellidos
- telefono [optional]
- correo_electronico [optional]
- estado

## Asignatura
id: asignatura
module: academico
description: Materia ofrecida por grado o área.

- pk id
- nombre
- area
- descripcion [optional]
- grado
- estado

## Clase
id: clase
module: academico
description: Oferta concreta de una asignatura para una sección, posiblemente con docente asignado.

- pk id
- franja_horaria
- estado

## Calificacion
id: calificacion
module: academico
description: Registro de nota obtenida por un estudiante en una clase y parcial.

- pk id
- numero_parcial
- nota
- fecha_registro [optional]
- observacion [optional]

# Relaciones

## Representa
id: representa
from: RepresentanteLegal
to: Estudiante
from_cardinality: 1
to_cardinality: 0..M
description: Un representante legal puede representar varios estudiantes; cada estudiante tiene un representante legal principal.

## Agrupa
id: agrupa
from: Seccion
to: Estudiante
from_cardinality: 0..1
to_cardinality: 0..35
description: Una sección agrupa estudiantes hasta un cupo máximo; un estudiante pertenece a una sección activa.

## Oferta
id: oferta
from: Seccion
to: Clase
from_cardinality: 1
to_cardinality: 0..M
description: Una sección puede ofrecer varias clases.

## Imparte
id: imparte
from: Docente
to: Clase
from_cardinality: 0..1
to_cardinality: 0..M
description: Un docente puede impartir varias clases; una clase puede no tener docente asignado todavía.

## SeDictaComo
id: se_dicta_como
from: Asignatura
to: Clase
from_cardinality: 1
to_cardinality: 0..M
description: Una asignatura se dicta como una o más clases.

## Obtiene
id: obtiene
from: Estudiante
to: Calificacion
from_cardinality: 1
to_cardinality: 0..M
description: Un estudiante obtiene varias calificaciones.

## CorrespondeA
id: corresponde_a
from: Calificacion
to: Clase
from_cardinality: 0..M
to_cardinality: 1
description: Cada calificación corresponde a una clase.
