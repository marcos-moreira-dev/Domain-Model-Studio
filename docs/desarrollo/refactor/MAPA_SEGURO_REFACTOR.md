# Mapa seguro de refactor — Tanda 25

## Criterio general

El refactor de Domain Model Studio debe ser incremental, medible y reversible. No se toca una zona solo por simetría ni por deseo de homogeneizar: se toca cuando reduce riesgo, aclara responsabilidad o facilita mantenimiento real.

## Tocar primero

1. **Scripts**: aplicado en Tanda 26; la raíz conserva solo entry points vigentes.
2. **Documentación viva**: aplicada en Tanda 27; se elimina Markdown histórico que ya no explica el producto actual.
3. **`ApplicationServices`**: aplicada en Tanda 28; la fachada global delega en familias funcionales.
4. **Shell/comandos**: iniciado con `ProjectCreationCoordinator` y `ProjectOpenCoordinator`; seguir solo con beneficio medible.
5. **Catálogos y recursos oficiales**: aplicado en Tanda 30; mantener agregadores pequeños y preservar contratos UENS/IA.

## Tocar con cuidado medio

- `infrastructure/json/*`
- `infrastructure/markdown/*`
- `presentation/workbench/*`
- `presentation/sidedock/*`
- `presentation/toolbar/*`
- ViewModels visuales con duplicación real.

## Tocar solo con diagnóstico específico

- `presentation/canvas/*`
- `presentation/inspector/*`
- `domain/er/*`
- renderers Chen/Crow's Foot
- importación código→UML
- instaladores y scripts de release

Estas zonas no están prohibidas, pero requieren pruebas de no regresión y justificación explícita.

## Dependencias que deben mantenerse limpias

- `domain` no debe depender de JavaFX.
- `application` no debe depender de `presentation`.
- `presentation/interactivecanvas` no debe depender de UML Clases, Grafo libre ni Grafo lógico.
- Los catálogos de capacidades no deben prometer botones/exportaciones sin implementación real.
- Los recursos IA deben distinguir plantilla, ejemplo, gramática, prompt y referencia.

## Señales de alerta

- Una clase transversal empieza a importar un tipo de proyecto específico.
- Una corrección visual exige tocar persistencia sin necesidad.
- Un test fuente se actualiza para aceptar una promesa falsa en lugar de corregir el producto.
- Un Markdown histórico vuelve a aparecer como plan vigente.
- Un script antiguo se documenta como flujo recomendado o reaparece en la raíz sin uso vigente.

## Regla documental

La documentación histórica no se conserva por inercia. Si no explica una capacidad vigente, una frontera vigente o una decisión activa, se elimina en la limpieza documental.

La política detallada está en `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`.

## Punto de control

Antes de cada tanda de refactor debe responderse:

1. ¿Qué comportamiento se preserva?
2. ¿Qué tests lo protegen?
3. ¿Qué archivo grande o acoplamiento se reduce?
4. ¿Qué no se toca?
5. ¿Cómo se revierte si aparece regresión?


## Tanda 31 — Persistencia `.dms`

Aplicada con alcance controlado: `DmsProjectJsonReader` y `DmsProjectJsonWriter` delegan el modelo conceptual legacy y los payloads especializados opcionales sin cambiar formato JSON persistente. La persistencia sigue siendo zona sensible para futuras migraciones.


## Tanda 32 — Parsers/exporters Markdown

Aplicada con alcance controlado: se centraliza la separación de frontmatter y cuerpo en `MarkdownImportDocument` para parsers importables. No se modifican gramáticas, ejemplos oficiales ni exporters canónicos.


## Tanda 33 — ViewModels visuales/documentales

Seguro: centralizar `loading` + `projectChangeListener` en `ProjectChangeSupport`.

No seguro sin diagnóstico adicional: unificar edición, selección, movimiento o layout de todos los diagramas en una superclase común.

Fuera de alcance: canvas conceptual legacy e inspector.


## Tanda 34 — UML Clases

Seguro: extraer la política de selección de Resumen para importaciones código→UML, porque trabaja sobre `ParsedCodeProject` y no depende de JavaFX ni del layout visual.

No seguro sin diagnóstico adicional: tocar parser Java, parser TypeScript, `UmlClassCanvasAdapter`, navegación a código, protección PNG o layout visual.


## Tanda 35 — Canvas conceptual legacy

Aplicada con alcance conservador: `DiagramCanvasViewModel` delega historial de edición en `ConceptualCanvasEditHistory` y resolución de anclas en `ConceptualAnchorResolver`. No se migra el canvas conceptual a `interactivecanvas`, no se toca `DiagramCanvasView`, no se cambia persistencia, Markdown ni renderers Chen/Crow's Foot.


## Tanda 36 — Artefactos compatibles legacy

Aplicada con alcance limitado: se retira el panel/CSS/selección de derivaciones del Levantamiento lógico y se conserva el paquete de aplicación como infraestructura interna de borradores compatibles. No se eliminan writers ni se cambian importadores; no hay cambio visible salvo la ausencia de deuda UI que ya no se usaba.


## Tanda 37 — CSS y recursos UI

Aplicada con alcance bajo riesgo: se eliminan CSS documentales no importados, se actualiza el README de CSS y se agregan guardarraíles de recursos. No se cambia cascada funcional, layout ni geometría semántica de diagramas.
