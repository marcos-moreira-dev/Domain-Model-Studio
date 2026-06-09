# Tanda 15 — Contrato Markdown canónico del Levantamiento lógico

## Objetivo

Estabilizar el formato Markdown que sostiene el módulo de Levantamiento lógico. Esta tanda reconoce que el Markdown canónico es el contrato principal: la IA o el usuario lo rellenan, Domain Model Studio lo importa, el usuario lo calibra mediante controles y luego se exporta nuevamente como fuente lógica.

## Cambios principales

- Se actualizó `logical_business_intake_template.md` con la plantilla maestra revisada.
- Se actualizó `examples/markdown/plantillas/logical_business_intake.md` para que la descarga pública use el mismo contrato.
- Se agregó el contrato `logical-business-master-v1` en frontmatter.
- Se documentó el contrato en `docs/tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md`.
- Se agregó `LogicalBusinessCanonicalMarkdownContract` como referencia técnica del orden, estados e IDs permitidos.
- Se amplió la detección de referencias para `SUP-...` y `CALC-...`.
- Se ajustó el exportador para conservar el orden canónico 0–22.
- Se mantuvieron los ejemplos oficiales fuera del negocio personal del usuario.

## Criterio de cierre

El módulo queda alineado con el flujo híbrido:

```text
plantilla escrita en piedra
→ GPT rellena
→ importación
→ calibrado humano en UI
→ exportación canónica
→ uso como fuente para otros artefactos compatibles
```

## Nota posterior

La Tanda 16 convirtió `SUP` y `CALC` en elementos de primera clase del contrato lógico, manteniendo el principio de que la plantilla enseña placeholders y los ejemplos oficiales demuestran IDs reales.


## Rebaseline posterior de alcance lógico

Desde la línea de planificación LB, el contrato se interpreta de forma estricta: el Levantamiento lógico fija la lógica canónica del negocio, pero no genera automáticamente todos los demás proyectos ni garantiza sincronización cruzada entre artefactos independientes.

Cuando el usuario o una IA preparen otros Markdown compatibles, deben reutilizar IDs, nombres y reglas canónicas del levantamiento. Cada tipo de proyecto conserva su propio alcance, parser, validación, edición y exportación.
