# Tanda 37 — Revalidación local completa

## Estado

Implementada como flujo automatizable + reporte manual.

## Objetivo

Ejecutar una validación integral antes de empaquetar.

## Alcance detallado

- Ejecutar scripts\02-ejecutar-tests.bat.
- Ejecutar scripts\08-validacion-local-completa.bat.
- Completar scripts\09-smoke-ui-minimo.bat.
- Completar scripts\11-smoke-levantamiento-logico.bat.
- Registrar resultados en reportes de testeo.

## Criterios de aceptación

- `BUILD SUCCESS`.
- `target\smoke-render` generado con reporte, CSV y contact sheet.
- Smoke UI completado.
- Smoke lógico completado.
- Reporte de revalidación local completado.
- No hay bloqueadores P0/P1 abiertos.

## Archivos o zonas probables

- `scripts/08-validacion-local-completa.bat`
- `scripts/13-revalidacion-local-completa.bat`
- `docs/testeo/REVALIDACION_LOCAL_COMPLETA_TANDA_37.md`
- `docs/testeo/reportes/REPORTE_REVALIDACION_LOCAL_COMPLETA.md`
- `docs/desarrollo/VALIDACION_LOCAL.md`

## Pruebas recomendadas

- Ejecutar `scripts\02-ejecutar-tests.bat` después de la implementación.
- Ejecutar `scripts\13-revalidacion-local-completa.bat`.
- Abrir `target\smoke-render\contact_sheet.html`.
- Completar los reportes manuales de UI y Levantamiento lógico.
- No avanzar a app-image si existe un hallazgo bloqueante.

## Zonas protegidas

- No tocar el canvas conceptual salvo que la tanda lo indique explícitamente.
- No romper el carril vertical del SideDock.
- No prometer capacidades sin test, smoke o aclaración documental.

## Dependencias previas

- La tanda anterior debe quedar con `BUILD SUCCESS` antes de avanzar.
- Si aparece un fallo nuevo de compilación o arquitectura, se corrige primero y no se mezclan cambios.
- Se debe conservar el principio anti-fachada: capacidad visible = implementación + prueba + documentación o aclaración de alcance.

## Riesgos específicos

- Mezclar demasiados objetivos en una sola tanda puede ocultar regresiones.
- Cambiar UI sin smoke manual puede dejar una promesa visible rota.
- Cambiar parsers/exportadores sin round-trip puede crear pérdida silenciosa de datos.
- Cambiar shell/canvas sin guardarraíles puede romper pestañas, dirty state o exportaciones.

## Entregables esperados

- ZIP del proyecto con la tanda aplicada.
- Patch de la tanda.
- Documento de desarrollo en `docs/desarrollo/` cuando la tanda cambie contrato, UI o arquitectura.
- Tests o guardarraíles nuevos cuando se agregue o cambie una promesa del producto.
- Lista de smoke manual recomendado si toca UI, canvas, exportaciones, batch o empaquetado.

## Criterio de no avance

No avanzar a la siguiente tanda si ocurre cualquiera de estos casos:

- `scripts\02-ejecutar-tests.bat` no termina en `BUILD SUCCESS`.
- Maven falla por compilación.
- Un guardarraíl arquitectónico queda rojo.
- La tanda cambia una promesa visible y no deja test, smoke o documentación.
- La tanda toca canvas conceptual sin una autorización explícita.

## Nota de continuidad

Este archivo existe para que el trabajo pueda retomarse aunque se pierda la conversación. La implementación real debe apoyarse siempre en el código actual, el log más reciente y los patches aplicados, no solo en este plan.
