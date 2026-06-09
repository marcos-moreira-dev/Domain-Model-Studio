# Tanda 40 — Release candidate IMPLEMENTADA

## Objetivo

Cerrar una versión candidata local con tests, smoke, documentación, recursos IA, exportaciones y empaquetado validados.

## Implementación aplicada

- `scripts\16-release-candidate.bat` orquesta revalidación, app-image, MSI y verificación RC.
- `scripts\internal\verify-release-candidate.bat` verifica manifests, MSI, smoke render y documentos de cierre.
- `docs\testeo\RELEASE_CANDIDATE_TANDA_40.md` define el smoke final.
- `docs\testeo\reportes\REPORTE_RELEASE_CANDIDATE_TANDA_40.md` registra la decisión final.
- `docs\release\RELEASE_CANDIDATE_0_0_1.md` resume el alcance del RC.
- `docs\release\LIMITACIONES_CONOCIDAS_0_0_1.md` registra límites aceptados.

## Flujo final recomendado

```bat
scripts\02-ejecutar-tests.bat
scripts\16-release-candidate.bat
```

El script de RC ejecuta internamente:

```bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\internal\verify-release-candidate.bat
```

## Criterios de aceptación

- Tests completos en `BUILD SUCCESS`.
- Smoke render automático generado.
- App-image generada, abierta y validada.
- MSI generado, instalado, probado y desinstalado.
- Guardar/reabrir `.dms` funciona.
- Exportaciones Markdown, SVG documental, PNG y PDF donde aplica funcionan.
- Recursos IA se copian correctamente.
- Reporte de release candidate completado.

## Criterio de no avance

No declarar RC si falla cualquier etapa automatizada o si el smoke manual detecta una promesa visible rota.

## Estado posterior

Después de esta tanda ya no quedan tandas de implementación planificadas. Queda ejecución local de validación, app-image, MSI y decisión final de release candidate.
