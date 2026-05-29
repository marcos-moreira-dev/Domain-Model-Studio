# Tanda 014 — Rebaseline del módulo de Levantamiento lógico

## Propósito

Esta tanda fija el alcance rector del tipo de proyecto `logical-business-intake` antes de rediseñar su SideDock, guía, gramática, ejemplo oficial y smoke. El objetivo es evitar que el módulo prometa generación automática, sincronización cruzada o derivación universal de otros proyectos cuando su responsabilidad real es conservar la lógica estable del negocio.

## Decisión central

El Levantamiento lógico es una fuente lógica canónica del negocio. Debe centralizar vocabulario, reglas, estados, acciones, precondiciones, invariantes, postcondiciones, casos de uso, entidades candidatas, relaciones candidatas, reportes, riesgos, supuestos y preguntas pendientes.

Su valor principal es dejar una estructura semántica duradera, legible y revisable, independiente de tecnologías pasajeras como lenguajes de programación, frameworks, bases de datos o infraestructura.

## Lo que el módulo promete

- Importar y exportar Markdown canónico del propio levantamiento.
- Registrar la lógica interna del negocio con IDs y nombres canónicos.
- Revisar coherencia interna del documento.
- Mostrar elementos, entidades, relaciones, preguntas y trazas internas del levantamiento activo.
- Servir como fuente para que el usuario y una IA preparen otros Markdown compatibles con Domain Model Studio.

## Lo que el módulo no promete

- No genera automáticamente todos los demás proyectos.
- No decide qué diagramas o documentos debe crear el usuario.
- No sincroniza automáticamente UML, C4, diccionario, grafo lógico, wireframes u otros artefactos.
- No valida la verdad absoluta del negocio ni sustituye la revisión humana.
- No convierte entidades candidatas en tablas físicas definitivas.

## Regla de alineación con IA y otros artefactos

El Levantamiento lógico define IDs, nombres, reglas y contratos semánticos canónicos. Cuando el usuario o una IA genere otros archivos Markdown compatibles con Domain Model Studio, debe reutilizar esos IDs y nombres para mantener consistencia entre artefactos.

Domain Model Studio puede importar, editar, validar y exportar cada proyecto por separado. La alineación semántica entre proyectos independientes queda a cargo del usuario y de la IA utilizada para generar esos Markdown, hasta que exista una feature explícita de validación cruzada.

## Decisión sobre “Artefactos compatibles”

`Artefactos compatibles` no debe permanecer como módulo principal del SideDock del Levantamiento lógico. El término sugiere una capacidad de generación o sincronización automática que no corresponde al alcance rector.

La idea se conserva solo como explicación de uso:

> Este levantamiento puede servir como fuente para preparar otros artefactos compatibles, pero no es obligatorio generarlos todos. El usuario decide qué necesita y debe revisar los Markdown producidos por IA antes de importarlos como proyectos independientes.

## SideDock objetivo

El SideDock del Levantamiento lógico debe tender a estos módulos:

1. Estructura
2. Elementos lógicos
3. Entidades y relaciones
4. Propiedades
5. Validación
6. Trazas internas
7. Ayuda y glosario

## Reglas de lenguaje visible

Evitar textos como:

- generar automáticamente todos los proyectos
- sincronizar artefactos
- alinear proyectos automáticamente
- validar negocio completo
- crear todos los diagramas derivados

Usar textos como:

- fuente lógica
- IDs canónicos
- nombres canónicos
- coherencia interna
- trazas internas
- borrador revisable
- uso con IA
- artefactos compatibles

## Corrección de guardarraíl pendiente

La tanda también actualiza el guardarraíl del Diccionario de datos para reconocer la política de scroll dominante externo del SideDock. El índice documental debe usar el espacio lateral completo y reservar mínimo visual de ocho filas sin crear registros falsos ni imponer un scroll interno innecesario.

## Plan de implementación posterior

Las siguientes tandas específicas del módulo lógico deben implementar, en orden:

1. Contrato de alcance, guía académica y textos sin falsas promesas.
2. Ayuda y glosario obligatorio.
3. SideDock lógico sin Artefactos compatibles y con módulos renombrados.
4. Elementos lógicos agrupados por familias.
5. Entidades, atributos y relaciones candidatas visibles.
6. Trazas internas del levantamiento.
7. Validación lógica interna y feedback de hallazgos.
8. Gramática Markdown canónica y plantilla IA.
9. Ejemplo oficial UENS actualizado.
10. Tests y smoke final del módulo lógico.

## Criterios de aceptación

- El alcance del Levantamiento lógico queda documentado como fuente lógica estable.
- La responsabilidad de alineación usuario/IA queda explícita.
- `Artefactos compatibles` queda fuera del SideDock objetivo.
- La validación queda acotada a coherencia interna.
- La gramática y la guía académica deberán alinearse con este contrato en tandas posteriores.
- El guardarraíl del Diccionario de datos queda alineado con la política actual de scroll del SideDock.
