# Smoke manual post-UX — Tanda 38

## 0. Preparación

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 0.1 | Confirmar que los parches 28–37 están aplicados en orden. | No hay conflictos pendientes. | Pendiente |
| 0.2 | Ejecutar `scripts\00-verificar-entorno.bat`. | Entorno Java/Maven detectado correctamente. | Pendiente |
| 0.3 | Ejecutar `scripts\02-ejecutar-tests.bat`. | Suite verde. | Pendiente |
| 0.4 | Ejecutar `scripts\01-ejecutar-app.bat`. | La aplicación abre. | Pendiente |

## 1. Recursos IA y ejemplos UENS

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 1.1 | Exportar Recursos IA desde la aplicación. | Se genera índice `00_indice_recursos_ia.md`. | Pendiente |
| 1.2 | Revisar `logical_business_intake_uens_gordito.md`. | Figura como ejemplo UENS importable de Levantamiento lógico. | Pendiente |
| 1.3 | Importar ejemplos UENS gorditos. | Se abren sin error de parser. | Pendiente |
| 1.4 | Revisar roles/permisos UENS. | Roles implementados: `ADMIN` y `SECRETARIA`. | Pendiente |
| 1.5 | Revisar ejemplos con matrícula en nombre legacy. | El contenido aclara que no existe tabla/entidad persistente `matricula`. | Pendiente |
| 1.6 | Revisar reportes UENS. | Usa `reporte_solicitud_queue` y estados `PENDIENTE`, `EN_PROCESO`, `COMPLETADA`, `ERROR`. | Pendiente |

## 2. Shell y F11

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 2.1 | Abrir la aplicación sin proyecto activo. | Menú `Vista → Pantalla completa` visible. | Pendiente |
| 2.2 | Pulsar F11. | Entra en pantalla completa. | Pendiente |
| 2.3 | Pulsar F11 otra vez. | Sale de pantalla completa. | Pendiente |
| 2.4 | Repetir con un proyecto abierto. | No se pierde workspace, toolbar ni SideDock. | Pendiente |

## 3. Guía académica vs ayuda operativa

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 3.1 | Abrir `Ayuda → Guía académica` o F1. | Ventana titulada `Guía académica — Domain Model Studio`. | Pendiente |
| 3.2 | Revisar contenido de la guía. | Es teórico/académico, no tutorial operativo del SideDock. | Pendiente |
| 3.3 | Abrir un proyecto no conceptual y usar módulo `Ayuda` del SideDock. | La ayuda explica operación concreta de la herramienta activa. | Pendiente |
| 3.4 | Revisar que no diga `Guía operativa` en el Menú Bar. | El Menú Bar usa `Guía académica`. | Pendiente |

## 4. SideDock: scroll y mínimo visual

Probar al menos estos tipos: Grafo lógico, UML Clases, Comportamiento, Mapa de módulos, Flujo de pantallas, Wireframes, Diccionario, Roles/Permisos y Arquitectura.

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 4.1 | Abrir módulos de estructura/propiedades. | No aparece doble scroll innecesario. | Pendiente |
| 4.2 | Revisar listas con 1–3 elementos. | Tienen aire vertical; no se ven comprimidas. | Pendiente |
| 4.3 | Revisar tablas con pocos elementos. | Muestran mínimo visual aproximado de 8 filas sin registros falsos. | Pendiente |
| 4.4 | Revisar listas/tablas con muchos elementos. | El scroll funciona correctamente. | Pendiente |
| 4.5 | Cerrar/reabrir módulos del SideDock. | El módulo conserva comportamiento estable. | Pendiente |

## 5. Arquitectura/despliegue: contenedores reales

Usar ejemplo de despliegue técnico UENS o crear un diagrama con `ENVIRONMENT`, `NETWORK`, `SERVER`, `CLIENT`, `SERVICE` y `DATABASE`.

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 5.1 | Mover un `ENVIRONMENT` con nodos dentro. | Los hijos acompañan al contenedor. | Pendiente |
| 5.2 | Mover un `NETWORK` con servicios dentro. | Los hijos acompañan al contenedor. | Pendiente |
| 5.3 | Mover un hijo dentro/fuera de una zona. | El contenedor se ajusta coherentemente. | Pendiente |
| 5.4 | Exportar SVG. | Zonas quedan detrás de conectores/nodos normales. | Pendiente |
| 5.5 | Exportar PNG. | Zonas no tapan texto ni nodos principales. | Pendiente |
| 5.6 | Guardar/reabrir `.dms`. | Layout de contenedores e hijos se conserva. | Pendiente |

## 6. Diccionario de datos

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 6.1 | Abrir/importar diccionario UENS. | Se presenta como documento técnico. | Pendiente |
| 6.2 | Revisar panel izquierdo. | Dice `Índice documental` y muestra celdas enriquecidas. | Pendiente |
| 6.3 | Revisar vista central. | Explicita PDF formal, Markdown canónico y que no es ERD físico. | Pendiente |
| 6.4 | Revisar tabla de campos. | Incluye tipo físico, FK, regla de negocio y validación cuando existan. | Pendiente |
| 6.5 | Exportar PDF. | PDF se genera sin error. | Pendiente |
| 6.6 | Exportar Markdown. | Markdown se genera sin error. | Pendiente |

## 7. Guía académica: figuras reales

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 7.1 | Abrir temas de Grafo libre. | Figuras reales, no texto `Figura: <id>`. | Pendiente |
| 7.2 | Abrir Levantamiento lógico. | Figura ciclo estado/acción real. | Pendiente |
| 7.3 | Abrir Grafo lógico. | Figuras de backbone y trazabilidad reales. | Pendiente |
| 7.4 | Revisar legibilidad. | Figuras explican teoría sin filtrar detalles internos de implementación. | Pendiente |

## 8. Header exportable PNG/SVG

Probar: Grafo lógico, Arquitectura/despliegue, Mapa de módulos, Wireframes, UML Clases y Comportamiento.

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 8.1 | Exportar SVG. | Contiene `<g id="export-header">`. | Pendiente |
| 8.2 | Revisar SVG. | Header no usa `rx/ry` ornamental. | Pendiente |
| 8.3 | Exportar PNG. | Header superior visible. | Pendiente |
| 8.4 | Revisar PNG. | Header no solapa nodos ni queda recortado. | Pendiente |
| 8.5 | Exportar visual grande. | Header mantiene escala razonable. | Pendiente |

## 9. CSS/no-radius por alcance

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 9.1 | Revisar botones/workbench/SideDock. | Hover negro + texto blanco. | Pendiente |
| 9.2 | Revisar paneles/documentos no conceptuales. | Sin radius ornamental visible. | Pendiente |
| 9.3 | Revisar UML/BPMN/Chen. | No se rompieron formas semánticas como óvalos/eventos. | Pendiente |
| 9.4 | Revisar pantalla de inicio. | No se alteró si estaba congelada. | Pendiente |
| 9.5 | Revisar modelo conceptual legacy. | No se alteró si no se pidió. | Pendiente |

## 10. Persistencia y exportación por lote

| Paso | Acción | Resultado esperado | Estado |
|---|---|---|---|
| 10.1 | Guardar `.dms` de varios tipos. | Archivos se guardan sin error. | Pendiente |
| 10.2 | Reabrir `.dms`. | Layout, datos y vista se conservan. | Pendiente |
| 10.3 | Ejecutar exportación por lote con varios tabs. | Crea carpetas `input/`, `editable/`, `output/`. | Pendiente |
| 10.4 | Revisar `.dms` en `editable/`. | Conserva proyecto completo. | Pendiente |
| 10.5 | Revisar salidas en `output/`. | Markdown/SVG/PNG/PDF según capacidades reales. | Pendiente |

## 11. Resultado

La tanda queda aprobada si todos los pasos críticos están `OK` y no hay bloqueadores visuales o funcionales. Registrar observaciones en el reporte.
