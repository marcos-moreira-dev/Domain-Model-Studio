# Baseline de refactor integral — Tanda 25

Estado: **baseline vigente para refactor post-Levantamiento lógico**  
Alcance: toda la aplicación, sin cambiar comportamiento visible.  
Criterio rector: **refactorizar solo donde el beneficio sea claro y medible**.

## 1. Punto de partida confirmado

La línea base posterior a las Tandas 16–23 y hotfixes queda así:

| Indicador | Valor observado |
|---|---:|
| Archivos Java en `src/main/java` | 1192 |
| Líneas Java en `src/main/java` | 110142 |
| Tests Java en `src/test/java` | 550 |
| Markdown en `docs` | 916 |
| Scripts `.bat` en `scripts` | 45 |

La suite local fue reportada verde tras los hotfixes de Tanda 24A, 24A-2 y 24A-3. Esta tanda no introduce cambios funcionales; solo fija el mapa de refactor y la política documental para ejecutar las siguientes tandas con criterio.

## 2. Regla de decisión

No se refactoriza una clase por estar grande. Se refactoriza cuando cumple al menos una condición:

1. concentra responsabilidades de familias distintas;
2. obliga a tocar muchas zonas para un cambio pequeño;
3. mezcla UI, coordinación, persistencia o reglas de dominio;
4. dificulta tests o trazabilidad de errores;
5. reintroduce promesas falsas de producto;
6. bloquea limpieza de scripts/documentación/release.

Si una clase es grande pero estable, cubierta por tests y semánticamente clara, se deja en observación.

## 3. Hotspots principales

| Prioridad | Archivo | Líneas | Criterio |
|---:|---|---:|---|
| 1 | `presentation/shell/MainShellCommandHandler.java` | 947 | Coordinación transversal de shell; candidata a coordinadores especializados. |
| T29 | `presentation/shell/MainShellCommandHandler.java` | 829 | Post-refactor inicial: creación/apertura delegadas a coordinadores. |
| 2 | `application/ApplicationServices.java` | 773 | Fachada grande; conviene separar familias sin romper compatibilidad. |
| T30 | `infrastructure/resources/OfficialAiResourceDescriptors.java` | 26 | Agregador pequeño; delega a familias de recursos IA. |
| T30 | `application/catalog/DefaultDiagramTypeDefinitions.java` | 37 | Agregador pequeño; delega a familias de tipos oficiales. |
| 4 | `infrastructure/json/*` | variable | Persistencia `.dms`; tocar solo con roundtrip fuerte. |
| T32 | `infrastructure/markdown/*Parser.java` | variable | Parsers importables comparten `MarkdownImportDocument` para frontmatter/cuerpo; gramáticas intactas. |
| 6 | `presentation/umlclass/*` | variable | Módulo avanzado; tocar después de estabilizar shell/catálogos/persistencia. |
| 7 | `presentation/canvas/*` e `inspector/*` | variable | Modelo conceptual legacy; no es intocable, pero requiere diagnóstico específico. |

## 4. Zonas protegidas por riesgo, no por dogma

Estas zonas no están prohibidas para siempre. Solo requieren una tanda previa de diagnóstico, tests y criterio de retorno:

- modelo conceptual clásico: `presentation/canvas`, `presentation/inspector`, `domain/er`;
- importación de código fuente a UML Clases;
- persistencia `.dms`;
- parsers/exporters Markdown oficiales;
- recursos IA oficiales;
- instaladores y scripts de release.

La regla es: **si no hay beneficio claro, no se toca**.

## 5. Orden de implementación posterior

1. **Tanda 26 — Limpieza controlada de scripts.** Reducir scripts recomendados a la línea operativa real.
2. **Tanda 27 — Limpieza documental viva.** Conservar solo Markdown que explique capacidades vigentes; eliminar histórico que ya no aporte.
3. **Tanda 28 — `ApplicationServices` por familias.** Mantener fachada de compatibilidad.
4. **Tanda 29 — Shell/comandos.** Aplicada parcialmente: creación y apertura delegadas sin cambiar UX.
5. **Tanda 30 — Catálogos, capacidades y recursos oficiales.** Aplicada: agregadores pequeños y definiciones/recursos por familia.
6. **Tanda 31 — Persistencia `.dms`.** Aplicada: coordinadores principales delegan modelo conceptual legacy y payloads especializados sin cambiar formato.
7. **Tanda 32 — Parsers/exporters Markdown.** Aplicada: utilidad común para frontmatter/cuerpo sin cambiar gramáticas.
8. **Tanda 33 — ViewModels visuales comunes.** Aplicada parcialmente: `ProjectChangeSupport` centraliza listener/loading/notificación en ViewModels migrados sin tocar canvas conceptual legacy.
9. **Tanda 34 — UML Clases.** Refactor focalizado del módulo más avanzado.
10. **Tanda 35 — Modelo conceptual legacy.** Diagnóstico antes de extracción.
11. **Tanda 36 — Artefactos compatibles legacy.** Decidir conservar, renombrar o eliminar infraestructura interna.
12. **Tanda 37 — CSS/recursos UI.** Aplicada: eliminar placeholders CSS documentales, actualizar superficie viva de CSS y proteger iconos de toolbar.
13. **Tanda 38 — Smoke integral post-refactor.** Validación completa.
14. **Tanda 39 — RC post-refactor.** App-image, MSI y release candidate.

## 6. Criterio documental incorporado

La documentación no debe acumular historial por inercia.

Se conserva Markdown si explica una capacidad vigente, una frontera técnica vigente, una guía operativa vigente, un contrato vigente o una decisión todavía activa. Si solo describe una etapa pasada, una tanda antigua, una hipótesis abandonada o un registro histórico sin valor operativo, se elimina en la limpieza documental.

El README raíz deberá concentrar la explicación principal del producto después de la limpieza documental.

La política detallada queda en:

- `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`

## 7. Criterio de aceptación de esta tanda

Esta tanda queda correcta si:

- existe un baseline vigente de refactor;
- el plan distingue zonas a tocar de zonas de alto riesgo;
- la limpieza documental futura queda orientada a eliminar historia obsoleta, no a archivarla por defecto;
- no se modifica comportamiento funcional;
- los tests fuente protegen el contrato de refactor.

## Actualización documental — Tanda 27

La limpieza documental viva redujo `docs/` a 240 Markdown y el repositorio completo a 439 Markdown, incluyendo ejemplos, recursos IA y ayuda integrada. Esta reducción no cambia el baseline técnico de código de Tanda 25; solo elimina bitácora documental que ya no explica el producto vigente.


## Actualización técnica — Tanda 32

Los parsers Markdown importables comparten `MarkdownImportDocument` para separar frontmatter y cuerpo. Se retiraron copias locales de `readFrontMatter` y `stripFrontMatter` sin cambiar gramáticas ni ejemplos oficiales.


## Actualización técnica — Tanda 33

Los ViewModels visuales/documentales migrados comparten `ProjectChangeSupport` para registrar listeners, bloquear notificaciones durante carga/limpieza y publicar cambios del `DiagramProject` activo. No se introduce superclase visual ni se modifica el canvas conceptual legacy.

## Actualización técnica — Tanda 34

`SourceCodeUmlClassViewBuilder` baja de aproximadamente 364 líneas a 161 líneas al extraer `SourceCodeUmlSummarySelectionPolicy`. La política concentra ranking, límite y selección de la vista Resumen segura para importaciones código→UML. No cambia parsing Java/TypeScript, inferencia de relaciones ni UX visible.

11. **Tanda 35 — Modelo conceptual legacy.** Aplicada de forma focalizada: historial undo/redo y resolución de anclas salen de `DiagramCanvasViewModel`; el canvas visual y el inspector siguen protegidos.


## Actualización técnica — Tanda 36

Los artefactos compatibles legacy dejan de formar parte de la presentación del Levantamiento lógico. Se eliminan panel y CSS específicos, `DERIVATION` sale de la selección del expediente y `LogicalBusinessViewModel` deja de conocer `LogicalBusinessDerivationService`. La infraestructura de aplicación se conserva con `compatibleDrafts` y métodos legacy deprecated.


## Recordatorio documental post-limpieza

La documentación no debe acumular historial por inercia. Se conserva Markdown si explica una capacidad vigente; si solo registra una etapa pasada, se elimina en la limpieza documental. El README raíz concentra la descripción pública del producto.
