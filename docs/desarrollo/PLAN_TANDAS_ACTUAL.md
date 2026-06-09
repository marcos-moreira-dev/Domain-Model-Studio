# Plan vigente de refactor y cierre

Estado: **vigente después de Tanda 38A**

Este documento reemplaza la bitácora larga de tandas anteriores. No pretende conservar todo el pasado; solo indica el camino actual para continuar el refactor de Domain Model Studio con criterio.

## Regla de trabajo

No se refactoriza por estética ni por conteo de líneas aislado. Se refactoriza cuando existe beneficio medible:

- reduce acoplamiento real;
- elimina duplicación activa;
- protege una frontera arquitectónica;
- simplifica comandos, servicios o persistencia;
- mejora mantenibilidad sin cambiar comportamiento visible;
- viene acompañado de tests o guardarraíles fuente.

## Estado cerrado antes de esta línea

- Levantamiento lógico corregido como fuente lógica canónica.
- SideDock lógico sin Artefactos compatibles como módulo principal.
- Ejemplo oficial UENS actualizado.
- Scripts públicos reducidos a entry points vigentes.
- Política documental definida: conservar solo Markdown vivo.

## Tandas vigentes

1. **Tanda 25 — Baseline técnico de refactor integral.** Aplicada.
2. **Tanda 26 — Limpieza controlada de scripts.** Aplicada.
3. **Tanda 27 — Limpieza documental viva.** Aplicada en esta línea.
4. **Tanda 28 — Refactor de `ApplicationServices`.** Aplicada.
5. **Tanda 29 — Refactor shell/comandos.** Aplicada.
6. **Tanda 30 — Catálogos, capacidades y recursos oficiales.** Aplicada.
7. **Tanda 31 — Persistencia `.dms`.** Aplicada: reader/writer principales quedan como coordinadores y delegan modelo conceptual/payloads especializados.
8. **Tanda 32 — Parsers/exporters Markdown.** Aplicada: frontmatter/cuerpo se separan con utilidad común sin cambiar gramáticas.
9. **Tanda 33 — ViewModels visuales comunes.** Aplicada parcialmente: `ProjectChangeSupport` centraliza listener/loading/notificación sin forzar superclase visual.
10. **Tanda 34 — UML Clases.** Aplicada parcialmente: selección segura de vista Resumen extraída a política de aplicación, sin cambiar parsing ni UX.
11. **Tanda 35 — Modelo conceptual legacy.** Aplicada de forma focalizada.
12. **Tanda 36 — Artefactos compatibles legacy del Levantamiento lógico.** Aplicada: UI/selección/CSS retirados; infraestructura interna queda como borradores compatibles revisables.
13. **Tanda 37 — CSS y recursos UI.** Aplicada: se eliminan placeholders CSS documentales, se actualiza README de CSS y se agregan guardarraíles de recursos UI.
14. **Tanda 38A — Verificación JavaDoc post-refactor.** Aplicada: se revisan JavaDoc y guías de generación sin cambiar comportamiento productivo.
15. **Tanda 38 — Smoke integral post-refactor.** Obligatoria antes de RC.
16. **Tanda 39 — Release candidate post-refactor.** Empaquetado final.

## Documentos que mandan

- `docs/desarrollo/refactor/PLAN_REFACTOR_SOLID.md`
- `docs/desarrollo/refactor/MAPA_SEGURO_REFACTOR.md`
- `docs/desarrollo/refactor/BASELINE_REFACTOR_TANDA_025.md`
- `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md`
- `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`
- `scripts/README.md`


## Tanda 35

Aplicada: refactor focalizado del canvas conceptual legacy. `DiagramCanvasViewModel` delega undo/redo y resolución de anclas sin cambiar UX visible ni formato `.dms`.


## Tanda 36

Aplicada: las antiguas derivaciones dejan de existir como foco de UI. `LogicalBusinessDerivationService` queda como infraestructura interna de `compatibleDrafts` y conserva métodos legacy deprecated solo por compatibilidad.


## Tanda 37

Aplicada: limpieza de superficie CSS y recursos UI. Se eliminan `identity-polish.css` y `welcome-start.css` porque eran placeholders documentales, no hojas vivas; `ToolbarIcon` queda protegido por guardarraíl de iconos PNG existentes.


## Compatibilidad con guardarraíles históricos Tanda 22

Tanda 22 — Cierre final liviano: Javadocs pedagógicos en otro chat. Esta línea queda como antecedente aplicado; la continuidad actual está en el refactor post-Levantamiento lógico y su cierre.


## Hotfix post Tanda 37

- **Tanda 37B — Rebaseline final de tests post-refactor.** Corrige los últimos guardarraíles documentales/fuente sin cambiar comportamiento productivo.


## Tanda 38A

Aplicada: verificación JavaDoc post-refactor. `scripts\31-generar-javadoc.bat` queda como único entry point público para generar el sitio y la suite completa valida los guardarraíles fuente.
