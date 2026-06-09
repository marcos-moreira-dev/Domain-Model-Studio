# Smoke instalable post-UX — Tanda 39

## 0. Preparación

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 0.1 | Confirmar parches 28–38 aplicados. | No hay conflictos pendientes. | Pendiente |
| 0.2 | Ejecutar `scripts\00-verificar-entorno.bat`. | Java/Maven/jpackage disponibles según corresponda. | Pendiente |
| 0.3 | Ejecutar `scripts\02-ejecutar-tests.bat`. | Suite verde. | Pendiente |
| 0.4 | Ejecutar `scripts\13-revalidacion-local-completa.bat`. | Revalidación completa sin bloqueadores. | Pendiente |
| 0.5 | Verificar reporte Tanda 38. | Smoke post-UX sin bloqueadores críticos. | Pendiente |

## 1. App-image

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 1.1 | Ejecutar `scripts\14-app-image-completa.bat`. | App-image generada. | Pendiente |
| 1.2 | Verificar `dist\staging\APP_IMAGE_MANIFEST.txt`. | Manifest existe. | Pendiente |
| 1.3 | Abrir `Domain Model Studio.exe` desde `dist\staging`. | La app abre. | Pendiente |
| 1.4 | Revisar recursos visuales. | CSS, iconos, ayudas y recursos IA cargan. | Pendiente |
| 1.5 | Probar F11. | Entra/sale de pantalla completa. | Pendiente |
| 1.6 | Abrir `Ayuda → Guía académica`. | Título y contenido correctos. | Pendiente |
| 1.7 | Abrir proyecto no conceptual. | SideDock y ayuda operativa cargan. | Pendiente |

## 2. Funciones críticas en app-image

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 2.1 | Importar Levantamiento lógico UENS gordito. | Se abre como documento estructurado. | Pendiente |
| 2.2 | Importar Grafo lógico UENS gordito. | Se abre como visual semántico. | Pendiente |
| 2.3 | Importar Despliegue técnico UENS. | Contenedores `ENVIRONMENT`/`NETWORK` visibles. | Pendiente |
| 2.4 | Mover contenedor con hijos. | Los hijos acompañan o se reajustan coherentemente. | Pendiente |
| 2.5 | Importar Diccionario UENS. | Se ve como documento técnico. | Pendiente |
| 2.6 | Guardar `.dms`. | Archivo se guarda sin error. | Pendiente |
| 2.7 | Reabrir `.dms`. | Datos/layout se conservan. | Pendiente |
| 2.8 | Exportar Markdown. | Archivo se genera. | Pendiente |
| 2.9 | Exportar SVG visual. | SVG con header documental. | Pendiente |
| 2.10 | Exportar PNG visual. | PNG con header sin solape. | Pendiente |
| 2.11 | Exportar PDF desde Diccionario. | PDF se genera. | Pendiente |
| 2.12 | Exportar Recursos IA. | Índice y recursos oficiales se copian. | Pendiente |

## 3. MSI

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 3.1 | Ejecutar `scripts\15-msi-completo.bat`. | MSI generado y verificación inicial lanzada. | Pendiente |
| 3.2 | Verificar `dist\installer\*.msi`. | MSI existe. | Pendiente |
| 3.3 | Verificar `dist\installer\MSI_MANIFEST.txt`. | Manifest existe. | Pendiente |
| 3.4 | Instalar MSI. | Instalación finaliza sin error. | Pendiente |
| 3.5 | Abrir app instalada desde acceso directo/menú. | La app abre. | Pendiente |
| 3.6 | Confirmar recursos. | CSS, ayuda, ejemplos y recursos IA disponibles. | Pendiente |
| 3.7 | Desinstalar MSI. | Desinstalación finaliza sin error. | Pendiente |

## 4. Funciones críticas en app instalada

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 4.1 | Importar ejemplo UENS documental. | Abre correctamente. | Pendiente |
| 4.2 | Importar ejemplo UENS visual. | Abre correctamente. | Pendiente |
| 4.3 | Guardar/reabrir `.dms`. | Persistencia funciona. | Pendiente |
| 4.4 | Exportar Markdown/SVG/PNG. | Exportaciones funcionan según capacidad real. | Pendiente |
| 4.5 | Exportar PDF de diccionario. | PDF funciona. | Pendiente |
| 4.6 | Revisar Guía académica y ayuda operativa. | Separación correcta. | Pendiente |
| 4.7 | Revisar SideDock. | Sin doble scroll crítico y mínimo visual presente. | Pendiente |

## 5. Release Candidate

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 5.1 | Ejecutar `scripts\16-release-candidate.bat`. | RC validado. | Pendiente |
| 5.2 | Verificar `dist\release\RELEASE_CANDIDATE_MANIFEST.txt`. | Manifest existe. | Pendiente |
| 5.3 | Revisar reportes exigidos por scripts históricos. | Reportes completos o justificación formal. | Pendiente |
| 5.4 | Revisar limitaciones conocidas. | No prometen capacidades falsas. | Pendiente |
| 5.5 | Marcar resultado de esta tanda. | Aprobado / Aprobado con observaciones / Rechazado. | Pendiente |

## 6. Resultado

Resultado final:

- [ ] Aprobado.
- [ ] Aprobado con observaciones no bloqueantes.
- [ ] Rechazado.

Observaciones:

```txt
Pendiente de completar durante el smoke local.
```
