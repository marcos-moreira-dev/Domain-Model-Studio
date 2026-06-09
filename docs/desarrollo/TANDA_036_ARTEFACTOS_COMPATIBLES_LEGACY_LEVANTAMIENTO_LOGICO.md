# Tanda 36 — Artefactos compatibles legacy del Levantamiento lógico

Estado: aplicada.

## Objetivo

Cerrar la deuda visible que quedaba alrededor de las antiguas derivaciones del Levantamiento lógico. La infraestructura de borradores Markdown compatibles se conserva solo como soporte interno/legacy, pero deja de tener representación en la UI del expediente lógico: los antiguos artefactos compatibles dejan de formar parte de la presentación principal del Levantamiento lógico.

## Cambios principales

- Se eliminó `LogicalBusinessDerivationsPanel` de `presentation/logicalbusiness`.
- Se eliminó `logical-business-derivations.css` y su importación desde `logical-business.css`.
- `LogicalBusinessViewModel` ya no instancia `LogicalBusinessDerivationService` ni expone `derivationDrafts`, `selectedDerivationDraft` o `selectDerivation`.
- `LogicalBusinessSelectionKind` ya no contiene `DERIVATION`.
- `LogicalBusinessSelection` ya no crea selecciones de derivación.
- La ayuda contextual y el inspector ya no tratan artefactos compatibles como foco seleccionable.
- `LogicalBusinessDerivationService` queda como fachada interna de borradores compatibles, con métodos nuevos `compatibleDrafts` y `compatibleDraft`.
- Los métodos `deriveAll` y `derive` se conservan deprecated como compatibilidad legacy.
- El frontmatter de los borradores internos pasa a `sample_kind: "compatible-draft"`, `status: "borrador compatible revisable"` y `source_mode: "levantamiento-logico-como-fuente"`.

## Contrato vigente

El Levantamiento lógico no genera proyectos, no sincroniza workspaces, no importa automáticamente y no conserva un módulo de artefactos compatibles en el SideDock. Los borradores internos son únicamente insumos revisables que reutilizan IDs, nombres y reglas de la fuente lógica canónica.

## Tests

Se agregan/actualizan guardarraíles para validar:

- ausencia de panel/CSS de derivaciones en UI;
- ausencia de `DERIVATION` en selección del expediente;
- ausencia de `LogicalBusinessDerivationService` en el ViewModel;
- existencia del contrato interno de `compatibleDrafts`;
- compatibilidad de importación de borradores internos;
- frontmatter nuevo de borradores compatibles;
- conservación de writers pequeños por destino.

## Fuera de alcance

No se elimina el paquete `application.logicalbusiness.derivation` porque aún sirve como infraestructura interna para pruebas/importabilidad y como compatibilidad técnica. Una eliminación física completa solo tendría sentido si se decide retirar definitivamente esos borradores internos.
