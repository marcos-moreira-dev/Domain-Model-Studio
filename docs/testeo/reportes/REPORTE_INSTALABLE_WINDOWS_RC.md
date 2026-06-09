# Reporte instalable Windows — Release Candidate

## Datos

| Campo | Valor |
|---|---|
| Fecha | |
| Equipo | |
| JDK | |
| Maven | |
| Windows | |
| Resultado global | Pendiente |
| Hash SHA-256 app-image exe | |
| Hash SHA-256 MSI | |
| Carpeta logs RC | `dist\release\logs` |

## Evidencia automatizada

| Validación | Resultado | Observaciones |
|---|---|---|
| `scripts\02-ejecutar-tests.bat` | Pendiente | |
| `scripts\14-app-image-completa.bat` | Pendiente | |
| `scripts\15-msi-completo.bat` | Pendiente | |
| `scripts\16-release-candidate.bat` | Pendiente | |
| `scripts\31-generar-javadoc.bat` | Pendiente | |
| `dist\logs\maven-package.log` | Pendiente | |
| `dist\logs\jpackage-app-image.log` | Pendiente | |
| `dist\logs\jpackage-msi.log` | Pendiente | |
| `dist\release\RELEASE_CANDIDATE_MANIFEST.txt` con SHA-256 | Pendiente | |

## Smoke app-image

| Caso | Resultado | Observaciones |
|---|---|---|
| Ejecutable abre | Pendiente | |
| Pantalla de inicio carga | Pendiente | |
| Ejemplo oficial abre | Pendiente | |
| Grafo lógico UENS carga | Pendiente | |
| SideDock funciona | Pendiente | |
| Exportación básica funciona | Pendiente | |

## Smoke MSI

| Caso | Resultado | Observaciones |
|---|---|---|
| MSI instala | Pendiente | |
| Acceso directo / menú inicio funciona | Pendiente | |
| App instalada abre | Pendiente | |
| Guardar y reabrir `.dms` | Pendiente | |
| Exportar Markdown | Pendiente | |
| Exportar PNG | Pendiente | |
| Exportar SVG | Pendiente | |
| Desinstalar | Pendiente | |

## Decisión

```txt
No aprobado hasta completar smoke manual.
```
