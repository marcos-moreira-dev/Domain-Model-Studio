# Scripts públicos vigentes

> Tanda 26: la superficie pública de `scripts/` queda reducida a scripts realmente operativos. Los flujos históricos de tandas pasadas se eliminaron de la raíz para no acumular deuda; su evidencia queda en documentación viva o en tests cuando aporta valor vigente.

## Uso diario

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\01-ejecutar-app.bat
```

| Script | Uso vigente |
|---|---|
| `00-verificar-entorno.bat` | Verifica Java 21, Maven, jpackage y herramientas base. |
| `01-ejecutar-app.bat` | Abre la aplicación JavaFX desde Maven. |
| `02-ejecutar-tests.bat` | Ejecuta `mvn clean test`. |
| `06-medir-refactor.bat` | Recalcula métricas estáticas del refactor seguro. |
| `13-revalidacion-local-completa.bat` | Ejecuta verificación, tests, smoke render automático y métricas. |
| `14-app-image-completa.bat` | Genera y valida la app-image con helper de entorno, log Maven, log jpackage y manifiesto con SHA-256. |
| `15-msi-completo.bat` | Genera/reusa app-image validada, genera MSI y abre verificación manual con manifiesto auditable. |
| `16-release-candidate.bat` | Orquesta revalidación, app-image, MSI y verificación RC; copia logs a `dist\release\logs`. |
| `31-generar-javadoc.bat` | Genera `target\site\apidocs\index.html` cuando se requiere revisar JavaDoc. |

## Flujo de cierre recomendado

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

## Evidencia de empaquetado

Los flujos de empaquetado dejan evidencia auditable en:

```txt
dist\logs\maven-package.log
dist\logs\jpackage-app-image.log
dist\logs\jpackage-msi.log
dist\staging\APP_IMAGE_MANIFEST.txt
dist\installer\MSI_MANIFEST.txt
dist\release\RELEASE_CANDIDATE_MANIFEST.txt
dist\release\logs\
```

Los manifiestos de app-image/MSI/RC incluyen tamaño, fecha UTC y hash SHA-256 de los artefactos principales. `scripts\16-release-candidate.bat` evita regenerar dos veces la app-image dentro del mismo flujo usando la señal interna `DMS_REUSE_APP_IMAGE`.

## Helpers internos

`scripts/internal/` contiene detalles de implementación: ejecución JavaFX, Maven, jpackage, smoke render automático y verificaciones de paquetes. No se ejecutan directamente salvo diagnóstico técnico.

## Regla de mantenimiento

- Un script nuevo en la raíz debe ser parte del flujo operativo vigente.
- Los scripts de una tanda pasada no se conservan por defecto.
- Si una validación histórica sigue siendo útil, debe integrarse en `13-revalidacion-local-completa.bat`, `16-release-candidate.bat`, un test automatizado o un documento vivo.
- La raíz de `scripts/` debe mantenerse pequeña, legible y orientada a usuario/desarrollador final.


## Compatibilidad de tests fuente post-limpieza

La expresión **scripts de tandas pasadas** se mantiene solo para documentar que esos wrappers ya no son superficie pública vigente. La raíz conserva únicamente entry points operativos.
