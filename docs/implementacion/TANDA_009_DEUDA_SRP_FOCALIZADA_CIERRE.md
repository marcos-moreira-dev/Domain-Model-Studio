# Tanda 9 — Deuda SRP focalizada: cierre sin refactor funcional

Estado: **aplicada como auditoría focalizada y cierre de no activación**  
Alcance: **arquitectura, documentación viva y guardarraíles fuente**  
Tipo de cambio: **sin cambios de lógica de negocio, sin cambios visuales y sin tocar zonas protegidas**

## 1. Motivo

La Tanda 9 estaba reservada como deuda SRP focalizada solo si aparecía un bloqueo real después de estabilizar el Grafo lógico, validar la base automatizada y preparar el Release Candidate local.

La evidencia local Windows recibida antes de esta tanda confirma una base automatizada verde:

```txt
scripts\27-validar-cierre-tests-post-grafo-logico-productivo.bat
Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

scripts\02-ejecutar-tests.bat
Tests run: 1218, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Además, los guardarraíles de arquitectura relevantes quedaron en verde:

```txt
ArchitectureBoundaryTest
ArchitectureStrongAuditTest
Tanda14PreRefactorArchitectureGuardSourceTest
```

Por tanto, no corresponde abrir un refactor SRP amplio por estética o ansiedad de cierre. La decisión correcta es cerrar la Tanda 9 como auditoría focalizada, dejar trazabilidad de la no activación y conservar la estabilidad de la Release Candidate local.

## 2. Decisión

```txt
No se activa refactor funcional.
No se divide código por estética.
No se toca pantalla de inicio.
No se toca modelo conceptual.
No se toca canvas conceptual.
No se toca SideDock/canvas transversal salvo bug comprobable.
No se alteran contratos productivos del Grafo lógico.
```

La deuda SRP queda tratada así:

```txt
Estado: sin bloqueo real detectado.
Acción: cierre documental y guardarraíl fuente.
Refactor futuro: solo si un test de arquitectura, packaging, performance o mantenimiento humano lo justifica con evidencia concreta.
```

## 3. Criterios de activación futura

Una deuda SRP futura solo debe abrirse si aparece al menos una de estas señales:

```txt
- ArchitectureBoundaryTest vuelve a fallar por tamaño o dependencia indebida.
- ArchitectureStrongAuditTest detecta acoplamiento transversal real.
- Un archivo supera límites de revisión humana y el exceso no se justifica.
- App-image/MSI/smoke manual detectan comportamiento roto por responsabilidades mezcladas.
- Un ViewModel o servicio concentra lógica de dominio, infraestructura y UI en el mismo punto.
- Una corrección futura exige tocar repetidamente el mismo archivo gigante.
```

Si no aparece una señal de ese tipo, no se debe crear una tanda SRP.

## 4. Auditoría aplicada

Se cerró la continuidad documental para que el estado vivo ya no diga que Tanda 9 está pendiente. La línea queda así:

```txt
Grafo lógico productivo mínimo: cerrado.
Validación integral: cerrada.
Validación local Windows / RC local automatizado: cerrada.
Deuda SRP focalizada: auditada, no activada por ausencia de bloqueo real.
Pendiente de release instalable: app-image, MSI y smoke manual.
```

## 5. Prueba focalizada

La tanda agrega un guardarraíl de fuente:

```txt
Tanda9FocusedSrpDebtClosureSourceTest
```

Ese test verifica que:

```txt
- existe este documento de cierre;
- el plan vivo ya no presenta Tanda 9 como pendiente funcional;
- el estado de auditoría declara la decisión de no activar refactor SRP;
- el mapa de documentación viva apunta a esta tanda;
- existe script focalizado de validación;
- scripts/README.md documenta el cierre.
```

Script asociado:

```bat
scripts\30-validar-tanda09-deuda-srp-focalizada.bat
```

## 6. Comandos recomendados

Validación focalizada de esta tanda:

```bat
scripts\30-validar-tanda09-deuda-srp-focalizada.bat
```

Validación global:

```bat
scripts\02-ejecutar-tests.bat
```

Cierre instalable, si se quiere aprobar release candidate de instalación:

```bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

## 7. Resultado esperado

La Tanda 9 debe cerrar sin cambios funcionales. Su valor es evitar un refactor innecesario cuando la base ya está verde y el riesgo mayor es introducir regresiones por exceso de intervención.

Conclusión:

```txt
Tanda 9 aplicada.
Deuda SRP auditada.
Sin bloqueo real.
Sin refactor funcional.
Sin tandas técnicas pendientes salvo empaquetado/smoke manual de RC instalable.
```
