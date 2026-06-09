# Checklist de cierre de base

Estado: **cierre final liviano previo a refactors o nuevos tipos**  
Tanda: **29**  
Tipo: **checklist operativo y documental**

## Objetivo

Este checklist define cuándo una base de Domain Model Studio puede considerarse lista para continuar con refactors pequeños, refactors grandes protegidos o incorporación de nuevos tipos/proyectos.

La regla principal es:

```txt
no avanzar con refactor grande si tests, smoke UI o documentación viva no tienen estado conocido
```

## CIERRE-001 — Identificar base usada

- [ ] ZIP o carpeta base identificado.
- [ ] Rama/commit anotado si aplica.
- [ ] Fecha de validación anotada.
- [ ] Cambios locales sin empaquetar revisados.

Evidencia mínima:

```txt
Base:
Fecha:
Responsable:
```

## CIERRE-002 — Entorno

Ejecutar:

```bat
scripts\00-verificar-entorno.bat
```

Validar:

- [ ] Java detectado.
- [ ] `javac` detectado.
- [ ] Maven detectado.
- [ ] `jpackage` detectado si se hará empaquetado.

## CIERRE-003 — Suite automatizada

Ejecutar:

```bat
scripts\02-ejecutar-tests.bat
```

Validar:

- [ ] `BUILD SUCCESS`.
- [ ] 0 failures.
- [ ] 0 errors.
- [ ] Guardar o copiar el resumen del log.

Criterio: si falla un test, la base no se considera cerrada hasta documentar si es bloqueo real o corrección menor.

## CIERRE-004 — Métricas de refactor

Ejecutar:

```bat
scripts\06-medir-refactor.bat
```

Validar:

- [ ] Métricas generadas.
- [ ] Hotspots conocidos siguen documentados.
- [ ] No se inicia refactor por tamaño sin analizar responsabilidad y pruebas.

## CIERRE-005 — Smoke UI mínimo

Ejecutar guía:

```bat
scripts\09-smoke-ui-minimo.bat
```

Completar o copiar:

```txt
docs\testeo\reportes\REPORTE_SMOKE_UI_MINIMO.md
```

Validar al menos estado conocido para:

- [ ] arranque/home;
- [ ] creación/importación y pestañas;
- [ ] toolbar contextual;
- [ ] SideDock contextual;
- [ ] canvas básico;
- [ ] guardar/reabrir `.dms`;
- [ ] exportaciones PNG/SVG/Markdown/PDF;
- [ ] UML desde código;
- [ ] ayuda/recursos IA;
- [ ] exportación por lote;
- [ ] levantamiento lógico documental si el ZIP incluye esta fase.

## CIERRE-006 — Empaquetado opcional

Si el objetivo es entregar una build de escritorio, ejecutar:

```bat
scripts\03-generar-app-image.bat
scripts\07-validar-app-image.bat
```

Solo si `app-image` funciona, evaluar:

```bat
scripts\04-generar-instalador-msi.bat
```

Validar:

- [ ] app-image abre.
- [ ] se puede cerrar sin excepción visible.
- [ ] MSI solo se genera si la app-image fue validada.

## CIERRE-007 — Documentación viva

Antes de continuar, revisar:

```txt
docs\documentacion\MAPA_DOCUMENTACION_VIVA.md
docs\diagnostico\MACRO_DIAGNOSTICO_TANDA_022.md
docs\testeo\MATRIZ_CASOS_USO_Y_TESTS.md
docs\testeo\MATRIZ_CASOS_USO_CATEGORIZADA.md
docs\testeo\UI_SMOKE_MINIMO_EJECUTABLE.md
docs\testeo\SMOKE_LEVANTAMIENTO_LOGICO_TANDA_14.md
docs\testeo\CHECKLIST_CIERRE_BASE.md
```

Validar:

- [ ] no usar `docs/estado/` como verdad vigente;
- [ ] las capacidades visibles coinciden con catálogo oficial;
- [ ] los hallazgos de smoke quedan registrados;
- [ ] la siguiente tanda queda anotada en `PLAN_TANDAS_ACTUAL.md`.

## CIERRE-008 — Decisión de avance

Marcar una opción:

- [ ] Base aprobada para refactor pequeño.
- [ ] Base aprobada para refactor grande con smoke UI registrado.
- [ ] Base aprobada para lectura/diagnóstico solamente.
- [ ] Base no aprobada: corregir hallazgos bloqueantes.

## CIERRE-009 — Zonas protegidas

No tocar sin decisión explícita:

```txt
presentation/canvas/DiagramCanvasView.java
presentation/canvas/DiagramCanvasViewModel.java
presentation/canvas/ChenDiagramRenderer.java
presentation/canvas/CrowsFootDiagramRenderer.java
presentation/inspector/InspectorViewModel.java
```

No tocar sin smoke UI o test equivalente:

```txt
MainShellCommandHandler
MainShellView
InteractiveCanvasSurfaceView
UmlClassDiagramViewModel
ModuleMapViewModel
WireframeViewModel
SpecializedVisualSvgWriter
ClientBatchExportCoordinator
```

## CIERRE-010 — Próxima tanda recomendada

Orden sugerido:

```txt
Tanda R1 — toolbar contributors
Tanda R2 — ActiveOutputResolver / outputs exportables
Tanda R3 — MainShellCommandHandler por familias
Tanda R4 — SpecializedVisualSvgWriter
Tanda R5 — ViewModels/adapters visuales
Tanda Roadmap — nuevo tipo/proyecto dentro del sistema
```

## Resultado final

```txt
Estado de cierre:
Hallazgos bloqueantes:
Hallazgos no bloqueantes:
Próxima tanda:
```
