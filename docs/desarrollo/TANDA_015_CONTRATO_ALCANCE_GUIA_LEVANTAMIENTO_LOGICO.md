# Tanda 015 — Contrato de alcance, guía académica y textos sin falsas promesas

## Objetivo

Alinear el Levantamiento lógico con su alcance real: fuente lógica canónica del negocio, no generador automático universal de artefactos.

## Decisión rectora

El Levantamiento lógico fija IDs, nombres, reglas y contratos semánticos canónicos. Puede servir como fuente para que el usuario y una IA preparen otros Markdown compatibles con Domain Model Studio, pero no decide qué proyectos crear, no genera todos los proyectos y no mantiene sincronización automática entre artefactos independientes.

## Cambios de lenguaje

- `Artefactos compatibles` deja de ser el lenguaje recomendado para la experiencia visible.
- Se usa `Artefactos compatibles`, `uso como fuente`, `borrador revisable`, `IDs canónicos` y `trazas internas`.
- Validación se describe como coherencia interna, no como verdad absoluta del negocio.
- Trazas internas se acota a trazas internas del levantamiento activo.

## Guía académica

La guía académica del tema `logical-business-intake` fue reescrita para enfatizar:

- fuente lógica canónica;
- responsabilidad de alineación semántica usuario/IA;
- límites del módulo;
- precondiciones, invariantes y postcondiciones;
- entidades, atributos y relaciones candidatas;
- uso como fuente para otros artefactos sin promesa de generación automática.

## Contrato Markdown

El contrato técnico del Markdown incorpora una sección de responsabilidad de alineación semántica. La sección 19 pasa a leerse como `Uso como fuente para otros artefactos`, manteniendo el identificador de contrato `logical-business-master-v1` y sin romper el orden canónico 0–22.

## Criterio de aceptación

- La ayuda académica no promete generación automática.
- Los textos visibles no prometen sincronización cruzada entre proyectos.
- El usuario y la IA quedan declarados como responsables de reutilizar IDs y nombres canónicos al preparar otros Markdown.
- Cada tipo de proyecto conserva su propio alcance, parser, validación, edición y exportación.


## Nota de ayuda contextual viva

El SideDock de Ayuda y glosario debe acompañar el Cierre documental y la validación humana del levantamiento lógico sin prometer generación automática.
