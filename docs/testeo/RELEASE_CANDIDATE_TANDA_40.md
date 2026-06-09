# Smoke release candidate — Tanda 40

## Objetivo

Validar que Domain Model Studio queda como release candidate local, no como promesa abierta.

## Precondiciones

Antes de marcar esta tanda como aprobada deben existir evidencias de:

- `scripts\02-ejecutar-tests.bat` con `BUILD SUCCESS`.
- `scripts\13-revalidacion-local-completa.bat` ejecutado.
- `target\smoke-render\SMOKE_RENDER_AUTOMATICO.md` generado.
- `target\smoke-render\contact_sheet.html` generado.
- `dist\staging\APP_IMAGE_MANIFEST.txt` generado.
- `dist\installer\MSI_MANIFEST.txt` generado.
- MSI presente en `dist\installer\*.msi`.

## Smoke funcional mínimo

1. Abrir la app instalada desde el MSI.
2. Abrir la app-image desde `dist\staging`.
3. Verificar pantalla inicial y Recursos IA.
4. Importar un ejemplo oficial UENS.
5. Guardar `.dms`.
6. Cerrar y reabrir `.dms`.
7. Exportar Markdown.
8. Exportar SVG vectorial documental.
9. Exportar PNG en al menos un diagrama visual.
10. Exportar PDF documental desde diccionario de datos y levantamiento lógico.
11. Ejecutar batch export con varios proyectos abiertos.
12. Confirmar que el modelo conceptual mantiene su canvas protegido.
13. Confirmar que el SideDock especializado abre ayuda operativa concreta por tipo.
14. Confirmar que Levantamiento lógico marca dirty al editar y advierte al cerrar.

## Smoke visual mínimo

Revisar el `contact_sheet.html` generado por smoke render automático y confirmar:

- No hay SVG vacío.
- No hay raster embebido como salida principal.
- Diagramas largos son revisables.
- SVG se entiende como vectorial documental, no WYSIWYG universal.

## Criterio de aprobación

La Tanda 40 queda aprobada solo si:

- Tests completos están verdes.
- Revalidación local completa está registrada.
- App-image abre y funciona.
- MSI instala, abre, guarda/reabre, exporta y desinstala.
- Limitaciones conocidas están documentadas.
- No hay promesas visibles sin implementación, prueba o nota de alcance.

## Criterio de no aprobación

No aprobar release candidate si ocurre cualquiera de estos casos:

- Fallan tests.
- Falla app-image.
- Falla MSI.
- La app instalada no encuentra recursos.
- No se puede guardar/reabrir `.dms`.
- Exportaciones básicas fallan.
- El smoke visual detecta entregables vacíos, negros o ilegibles.
