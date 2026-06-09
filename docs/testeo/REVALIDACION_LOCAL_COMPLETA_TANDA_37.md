# Revalidación local completa — Tanda 37

Estado: **manual + automatizable**  
Propósito: confirmar que la base es apta para pasar de implementación/refactor a empaquetado.

## Orden recomendado

Ejecutar desde la raíz del proyecto:

```bat
scripts\13-revalidacion-local-completa.bat
```

Ese script encadena:

```txt
00-verificar-entorno
02-ejecutar-tests
12-smoke-render-automatico
06-medir-refactor
09-smoke-ui-minimo
11-smoke-levantamiento-logico
```

## Criterios de aceptación automatizables

- `scripts\02-ejecutar-tests.bat` termina en `BUILD SUCCESS`.
- `scripts\12-smoke-render-automatico.bat` genera:
  - `target\smoke-render\SMOKE_RENDER_AUTOMATICO.md`
  - `target\smoke-render\contact_sheet.html`
  - `target\smoke-render\smoke-render.csv`
- `scripts\06-medir-refactor.bat` termina sin error.
- No hay fallos de compilación, arquitectura, recursos, documentación viva o productización.

## Criterios de aceptación manuales

Completar estos reportes:

```txt
docs\testeo\reportes\REPORTE_REVALIDACION_LOCAL_COMPLETA.md
docs\testeo\reportes\REPORTE_SMOKE_UI_MINIMO.md
docs\testeo\reportes\REPORTE_SMOKE_LEVANTAMIENTO_LOGICO.md
```

## Smoke manual obligatorio

1. Abrir aplicación con `scripts\01-ejecutar-app.bat`.
2. Importar al menos un ejemplo visual, uno documental y uno lógico.
3. Verificar guardado/reapertura `.dms`.
4. Verificar exportaciones principales: Markdown, SVG documental, PNG y PDF documental por tipo.
5. Revisar `target\smoke-render\contact_sheet.html`.
6. Confirmar que el modelo conceptual mantiene su comportamiento protegido.
7. Confirmar que los workspaces especializados siguen respondiendo después de los refactors.

## Criterio de no avance

No pasar a Tanda 38 si ocurre cualquiera de estos casos:

- no hay `BUILD SUCCESS`;
- no se genera `target\smoke-render`;
- JavaFX no abre;
- un smoke manual queda `Bloqueado`;
- hay pérdida de cambios, error al guardar/reabrir o exportación vacía;
- se detecta regresión en el canvas conceptual.
