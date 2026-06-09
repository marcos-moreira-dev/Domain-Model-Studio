# Tanda JD-7 — Onboarding vivo de arquitectura y ruta de estudio

## Tipo de tanda

Documentación técnica y JavaDoc de paquetes. No se modificó lógica funcional.

## Objetivo

Cerrar una ruta viva para estudiar Domain Model Studio como proyecto de ingeniería de software: capas, fronteras, flujos de lectura, scripts de validación y sitio JavaDoc.

## Cambios aplicados

```txt
- Se agregó `docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md`.
- Se reforzaron los package-info de domain, application, infrastructure, presentation y bootstrap.
- Se actualizó el onboarding general y el onboarding de código/JavaDoc.
- Se agregó auditoría JD-7.
- Se agregó guardarraíl fuente JD-7.
- La validación se integra a la suite vigente `scripts\02-ejecutar-tests.bat`.
```

## Ruta de estudio consolidada

```txt
Dominio → aplicación → infraestructura → presentación → scripts/tests → release
```

## Validación local sugerida

```bat
scripts\02-ejecutar-tests.bat
scripts\31-generar-javadoc.bat
scripts\02-ejecutar-tests.bat
```

## Criterio de cierre

JD-7 queda cerrada cuando el guardarraíl focalizado pase y el sitio JavaDoc permita navegar desde paquetes principales hacia rutas de estudio documentadas.
