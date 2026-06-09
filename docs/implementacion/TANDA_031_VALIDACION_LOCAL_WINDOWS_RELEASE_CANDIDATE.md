# Tanda 31 — Validación local Windows / Release Candidate

## Estado

**Aplicada como cierre documental y operativo de Release Candidate local.**

Esta tanda no modifica lógica de ejecución. Consolida la evidencia local de Windows, deja el flujo de release candidate actualizado y fija qué debe considerarse aprobado, pendiente o no aprobado.

## Evidencia local recibida

La evidencia más reciente compartida por el usuario confirma:

```txt
scripts\27-validar-cierre-tests-post-grafo-logico-productivo.bat
Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

scripts\02-ejecutar-tests.bat
Tests run: 1218, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Esa evidencia desplaza los logs históricos con fallos del `logical-business-graph`. Si aparece un log posterior, el log posterior vuelve a mandar.

## Alcance validado en la evidencia automatizada

La corrida verde cubre, entre otras familias:

- catálogo, capacidades y workspace del `logical-business-graph`;
- teoría académica y figuras didácticas;
- CSS tokens;
- persistencia `.dms` especializada;
- importación/exportación Markdown;
- exportaciones SVG/PNG declaradas;
- validación integral del Grafo lógico;
- guardarraíles anti-fachada de documentación, toolbar, recursos IA y release;
- arquitectura y límites de tamaño de archivos;
- smoke/render automático cubierto por tests cuando se ejecute el flujo completo.

## Alcance pendiente de validación humana

La evidencia automatizada no reemplaza la validación visual/manual. Antes de declarar un RC instalable como aceptado, ejecutar o completar:

```bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

Y revisar manualmente:

- app-image abre desde `dist\staging`;
- MSI instala, abre y desinstala;
- ejemplos oficiales abren desde la app instalada;
- `logical-business-graph` UENS abre, valida, exporta y guarda/reabre `.dms`;
- el modelo conceptual y la pantalla de inicio siguen protegidos;
- recursos IA se copian y no prometen importabilidad falsa;
- limitaciones conocidas son aceptadas.

## Decisión de esta tanda

La línea actual queda en estado:

```txt
Tests automatizados: VERDE.
Grafo lógico: productivo a nivel técnico/documental.
Release Candidate instalable: pendiente de app-image/MSI/smoke humano si aún no se ejecutaron esos pasos.
Deuda SRP: no se activa salvo bloqueo real posterior.
```

## Script nuevo

Se agrega:

```bat
scripts\29-validar-tanda31-release-candidate-local.bat
```

Este script ejecuta el cierre automatizable de Tanda 31 y abre el reporte RC actual. El flujo de empaquetado instalable completo sigue quedando en:

```bat
scripts\16-release-candidate.bat
```

## No se tocó

- pantalla de inicio;
- modelo conceptual;
- canvas conceptual;
- sidebar legacy conceptual;
- semántica de diagramas ya estabilizados.

## Siguiente paso

Si el flujo de app-image/MSI/smoke manual pasa, cerrar como **RC local aprobado**. Si aparece una falla arquitectónica real, recién ahí activar **Tanda 9 — Deuda SRP focalizada**.
