# Tanda 43 — Instalable Windows y auditoría JavaDoc

## Propósito

Esta tanda no cambia lógica funcional. Su objetivo es dejar lista la siguiente fase de cierre: generar el instalable Windows, conservar evidencia de Release Candidate y auditar el estado real de JavaDoc para decidir si conviene abrir tandas de documentación de código.

## Punto de partida

La base automatizada ya fue reportada en verde por el usuario:

```txt
scripts\27-validar-cierre-tests-post-grafo-logico-productivo.bat
Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

scripts\02-ejecutar-tests.bat
Tests run: 1218, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Por tanto, el siguiente paso razonable no es seguir tocando el producto, sino cerrar el empaquetado instalable y dejar claro qué documentación de código falta.

## Alcance aplicado

- Se mantiene el flujo existente de empaquetado: app-image, MSI y Release Candidate.
- Se agrega una guía específica para ejecutar el instalable Windows sin confundir base automatizada verde con instalable aprobado.
- Se agrega auditoría JavaDoc con métricas de cobertura a nivel de tipos públicos y miembros públicos.
- Se agrega plan de tandas JavaDoc por capas, sin ejecutar refactor funcional.
- Se agrega script focalizado para validar documentación de instalable y generar JavaDoc.

## Decisión sobre JavaDoc

El proyecto sí tiene bastante JavaDoc a nivel de tipos públicos, paquetes y contratos generales. El escaneo fuente muestra una cobertura alta de JavaDoc en tipos públicos.

La deuda real está en los métodos públicos: hay pocos JavaDoc por método. Esto no bloquea el instalable, pero sí conviene planificar tandas de documentación de código si el proyecto se usará para estudiar ingeniería de software y desarrollo de software.

## Qué no se toca

- No se toca pantalla de inicio.
- No se toca modelo conceptual.
- No se toca canvas conceptual.
- No se toca lógica de ejecución.
- No se declara MSI aprobado sin smoke manual real.

## Archivos agregados

```txt
docs\testeo\INSTALABLE_WINDOWS_RC_GUIA.md
docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md
docs\calidad\AUDITORIA_JAVADOC_2026_05_24.md
docs\calidad\PLAN_TANDAS_JAVADOC.md
scripts\31-generar-javadoc.bat
scripts\32-validar-instalable-y-javadoc.bat
src\test\java\com\marcosmoreira\domainmodelstudio\productization\Tanda43InstallerJavadocAuditSourceTest.java
```

## Validación focalizada

```bat
scripts\32-validar-instalable-y-javadoc.bat
```

Este script valida el guardarraíl documental de Tanda 43 y luego intenta generar JavaDoc en `target\site\apidocs`.

## Validación instalable completa

```bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

La aprobación final requiere completar manualmente:

```txt
docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md
docs\testeo\reportes\REPORTE_RELEASE_CANDIDATE_TANDA_40.md
```

## Frase de control

El cierre final de instalable depende de app-image/MSI/smoke manual, no solo de tests automatizados.
