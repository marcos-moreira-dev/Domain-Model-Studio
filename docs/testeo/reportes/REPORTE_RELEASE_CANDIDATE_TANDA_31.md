# Reporte release candidate — Tanda 31

## Identificación

- Fecha: 2026-05-24
- Equipo: Domain Model Studio
- Usuario validador: Marcos Moreira / Omar Alvarado
- Rama/carpeta validada: `C:\Users\MARCOS MOREIRA\Downloads\model`
- ZIP base: paquete post Tanda 42 / cierre Grafo lógico productivo

## Resultado automatizado recibido

| Validación | Resultado | Evidencia |
|---|---|---|
| `scripts\27-validar-cierre-tests-post-grafo-logico-productivo.bat` | OK | 34 tests, 0 failures, 0 errors, `BUILD SUCCESS` |
| `scripts\02-ejecutar-tests.bat` | OK | 1218 tests, 0 failures, 0 errors, `BUILD SUCCESS` |
| `scripts\28-validar-tanda42-validacion-integral-grafo-logico.bat` | Pendiente de reconfirmar en este reporte | Ejecutar si se requiere evidencia focalizada nueva |
| `scripts\13-revalidacion-local-completa.bat` | Pendiente | Requiere smoke render/manual completo |
| App-image | Pendiente | `dist\staging` |
| MSI | Pendiente | `dist\installer` |
| Manifest RC | Pendiente | `dist\release\RELEASE_CANDIDATE_MANIFEST.txt` |

## Resultado manual

| Área | Resultado | Observaciones |
|---|---|---|
| App instalada abre | Pendiente | Validar con app-image/MSI. |
| Recursos IA se copian | Pendiente | Validar desde botón Recursos IA. |
| Importación Markdown | Pendiente | Validar con ejemplos oficiales UENS. |
| Guardar `.dms` | Pendiente | Validar al menos modelo conceptual, levantamiento lógico, grafo lógico y UML. |
| Reabrir `.dms` | Pendiente | Confirmar layout y documentos especializados. |
| Exportar Markdown | Pendiente | Validar salida activa por pestaña. |
| Exportar SVG vectorial documental | Pendiente | Confirmar SVG no vacío. |
| Exportar PNG | Pendiente | Confirmar PNG no vacío. |
| Exportar PDF diccionario | Pendiente | Confirmar PDF del diccionario. |
| Batch export | Pendiente | Confirmar si aplica. |
| Levantamiento lógico dirty/cierre | Pendiente | Confirmar edición, derivaciones y cierre. |
| Grafo lógico UENS | Pendiente | Confirmar 51 nodos, 56 relaciones y 0 hallazgos semánticos. |
| Canvas conceptual protegido | Pendiente | No debe usar SideDock transversal. |
| SideDock especializado | Pendiente | Ayuda operativa por proyecto. |
| Desinstalación MSI | Pendiente | Requiere instalación real. |

## Limitaciones aceptadas

- SVG es vectorial documental, no WYSIWYG universal.
- Wireframes son estructurales, no prototipos interactivos.
- BPMN/UML/C4 tienen alcance documental simplificado.
- PDF formal aplica al diccionario de datos.
- Artefactos compatibles desde levantamiento lógico son borradores revisables.
- Grafo lógico no reemplaza BPMN, UML, modelo conceptual ni diccionario.

## Fallos encontrados

| ID | Descripción | Severidad | Acción |
|---|---|---|---|
| RC31-001 | App-image/MSI aún no documentados en este reporte. | Pendiente | Ejecutar scripts 14, 15 y 16 si se requiere RC instalable aprobado. |

## Decisión

- [ ] Aprobado como release candidate local instalable.
- [x] Aprobado como base automatizada verde para preparar release candidate local.
- [ ] Aprobado con observaciones menores.
- [ ] No aprobado.

Firma/nota:

> Tests automatizados verdes. Pendiente completar validación app-image/MSI/smoke manual para declarar RC instalable aprobado.
