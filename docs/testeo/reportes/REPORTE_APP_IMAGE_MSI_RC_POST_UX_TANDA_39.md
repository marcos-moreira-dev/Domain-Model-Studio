# Reporte — Tanda 39 App-image/MSI/RC post-UX

## Estado general

- Fecha:
- Responsable:
- Rama/commit:
- Sistema operativo:
- Java/JDK:
- Maven:
- Resultado final: Pendiente

## Parches aplicados

| Tanda | Estado | Observación |
|---|---|---|
| 28 | Pendiente | Ejemplos UENS corregidos. |
| 29–30 | Pendiente | Sincronización y tests UENS. |
| 31 | Pendiente | UX transversal. |
| 32 | Pendiente | Guía académica / ayuda operativa. |
| 33 | Pendiente | Contenedores arquitectura/despliegue. |
| 34 | Pendiente | Diccionario estilizado. |
| 35 | Pendiente | Figuras académicas reales. |
| 36 | Pendiente | Header exportable. |
| 37 | Pendiente | CSS/no-radius. |
| 38 | Pendiente | Smoke manual post-UX. |

## Validación automática

| Comando | Resultado | Evidencia / notas |
|---|---|---|
| `scripts\00-verificar-entorno.bat` | Pendiente |  |
| `scripts\02-ejecutar-tests.bat` | Pendiente |  |
| `scripts\13-revalidacion-local-completa.bat` | Pendiente |  |

## App-image

| Validación | Resultado | Evidencia / notas |
|---|---|---|
| `scripts\14-app-image-completa.bat` | Pendiente |  |
| `APP_IMAGE_MANIFEST.txt` existe | Pendiente |  |
| La app abre desde `dist\staging` | Pendiente |  |
| CSS/recursos/ayudas cargan | Pendiente |  |
| F11 funciona | Pendiente |  |
| Guía académica abre | Pendiente |  |
| Ayuda operativa SideDock funciona | Pendiente |  |
| Ejemplos UENS importan | Pendiente |  |
| `.dms` guarda/reabre | Pendiente |  |
| Exportaciones básicas funcionan | Pendiente |  |

## MSI

| Validación | Resultado | Evidencia / notas |
|---|---|---|
| `scripts\15-msi-completo.bat` | Pendiente |  |
| MSI existe | Pendiente |  |
| `MSI_MANIFEST.txt` existe | Pendiente |  |
| Instalación correcta | Pendiente |  |
| App instalada abre | Pendiente |  |
| Recursos disponibles en app instalada | Pendiente |  |
| `.dms` guarda/reabre desde app instalada | Pendiente |  |
| Exportaciones funcionan desde app instalada | Pendiente |  |
| Desinstalación correcta | Pendiente |  |

## Release Candidate

| Validación | Resultado | Evidencia / notas |
|---|---|---|
| `scripts\16-release-candidate.bat` | Pendiente |  |
| `RELEASE_CANDIDATE_MANIFEST.txt` existe | Pendiente |  |
| Reportes históricos requeridos están completos | Pendiente |  |
| Limitaciones conocidas revisadas | Pendiente |  |
| RC aprobado explícitamente | Pendiente |  |

## Bloqueadores

| ID | Descripción | Severidad | Estado |
|---|---|---|---|
| B-001 | Pendiente de ejecución local. | Alta | Abierto |

## Observaciones

```txt
Completar durante ejecución local en Windows.
```

## Resultado final

- [ ] Aprobado.
- [ ] Aprobado con observaciones no bloqueantes.
- [ ] Rechazado.
