# Validación local

## Objetivo

Definir un camino simple para comprobar que el proyecto compila, sus pruebas pasan, genera evidencia visual mínima y la aplicación puede revisarse manualmente.

## Validación mínima obligatoria

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\01-ejecutar-app.bat
```

## Revalidación local completa

```bat
scripts\13-revalidacion-local-completa.bat
```

Este flujo ejecuta:

1. Verificación de entorno.
2. Tests completos.
3. Smoke render automático SVG/documental.
4. Métricas de refactor.
5. Apertura del reporte de revalidación.

Guías relacionadas:

```txt
docs\testeo\UI_SMOKE_MINIMO_EJECUTABLE.md
docs\testeo\SMOKE_LEVANTAMIENTO_LOGICO_TANDA_14.md
docs\testeo\reportes\REPORTE_REVALIDACION_LOCAL_COMPLETA.md
```

## Smoke render automático

El smoke render se ejecuta desde `scripts\13-revalidacion-local-completa.bat` mediante el helper interno correspondiente.

Genera evidencias en:

```txt
target\smoke-render\SMOKE_RENDER_AUTOMATICO.md
target\smoke-render\contact_sheet.html
target\smoke-render\smoke-render.csv
```

## Smoke visual humano

Después de abrir la app:

1. Crear o abrir un proyecto.
2. Abrir ejemplo oficial de Grafo libre.
3. Confirmar etiquetas de relaciones.
4. Abrir/importar UML Clases.
5. Confirmar jerarquías visuales de relaciones.
6. Seleccionar y arrastrar una clase.
7. Guardar `.dms`.
8. Reabrir `.dms`.
9. Exportar Markdown.
10. Exportar SVG documental.
11. Exportar PNG.
12. Revisar que el modelo conceptual siga estable.

## Validación de empaquetado

```bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

## Cómo reportar fallos

Copiar siempre:

- comando ejecutado;
- carpeta desde donde se ejecutó;
- error completo de Maven, JavaFX o `jpackage`;
- captura si es visual;
- ZIP/tanda usada;
- `target\smoke-render\contact_sheet.html` cuando el fallo sea visual/exportable;
- reporte `docs\testeo\reportes\REPORTE_REVALIDACION_LOCAL_COMPLETA.md` cuando sea cierre local.

## Criterio de no avance

No pasar a refactor, app-image o documentación final si ocurre cualquiera de estos casos:

- el proyecto no compila;
- los tests fallan;
- JavaFX no abre;
- no se genera `target\smoke-render\contact_sheet.html`;
- un diagrama visual promete PNG/SVG/Markdown y no exporta;
- UML Clases no permite selección/arrastre básico;
- Grafo libre no muestra etiquetas de relación;
- el modelo conceptual presenta regresión visual o de interacción.
