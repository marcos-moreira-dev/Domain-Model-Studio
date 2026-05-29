# Reporte — Revalidación local completa

Fecha:  
Responsable:  
Versión/ZIP:  
Sistema operativo:  
Java / distribución:  
Maven:  
Ruta del proyecto:  

## Resultado automatizable

| Bloque | Comando | Estado | Evidencia / observaciones |
|---|---|---|---|
| Entorno | `scripts\00-verificar-entorno.bat` | Pendiente |  |
| Tests completos | `scripts\02-ejecutar-tests.bat` | Pendiente |  |
| Smoke render automático | `scripts\12-smoke-render-automatico.bat` | Pendiente |  |
| Métricas de refactor | `scripts\06-medir-refactor.bat` | Pendiente |  |
| App JavaFX | `scripts\01-ejecutar-app.bat` | Pendiente |  |

Estados permitidos: `Aprobado`, `Aprobado con observaciones`, `Bloqueado`, `Pendiente`, `No aplica justificado`.

## Evidencias generadas

```txt
Suite Maven:

Smoke render:
target\smoke-render\SMOKE_RENDER_AUTOMATICO.md
target\smoke-render\contact_sheet.html
target\smoke-render\smoke-render.csv

Métricas:
docs\desarrollo\refactor\METRICAS_PRE_REFACTOR.md
```

## Resultado de smoke manual

| Bloque | Reporte | Estado | Observaciones |
|---|---|---|---|
| UI mínimo | `docs\testeo\reportes\REPORTE_SMOKE_UI_MINIMO.md` | Pendiente |  |
| Levantamiento lógico | `docs\testeo\reportes\REPORTE_SMOKE_LEVANTAMIENTO_LOGICO.md` | Pendiente |  |
| Exportaciones visuales | `target\smoke-render\contact_sheet.html` + salidas manuales | Pendiente |  |
| Guardar/reabrir `.dms` | evidencia manual | Pendiente |  |

## Hallazgos

| ID | Bloque | Severidad | Descripción | Evidencia | Acción sugerida |
|---|---|---|---|---|---|
| RV-001 |  |  |  |  |  |

Severidades sugeridas: `Bloqueante`, `Alta`, `Media`, `Baja`, `Editorial`.

## Decisión

- [ ] Aprobado para app-image.
- [ ] Aprobado con observaciones menores.
- [ ] No aprobado; resolver hallazgos bloqueantes.

Notas finales:

```txt

```
