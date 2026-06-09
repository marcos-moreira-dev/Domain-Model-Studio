# Tanda 22 — Limpieza documental ligera y macro diagnóstico

## Objetivo

Dejar el proyecto entendible antes de salir de la ventana de contexto actual. Esta tanda no continúa Javadocs ni refactor. Se enfoca en:

- separar documentación viva de histórico;
- dejar un macro diagnóstico de pendientes reales;
- categorizar los casos de uso y niveles de prueba;
- agregar tests documentales que impidan perder esta trazabilidad.

## Archivos agregados

- `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md`
- `docs/testeo/MATRIZ_CASOS_USO_CATEGORIZADA.md`
- `docs/diagnostico/MACRO_DIAGNOSTICO_TANDA_022.md`
- `src/test/java/com/marcosmoreira/domainmodelstudio/productization/Tanda22DocumentationCleanupAndMacroDiagnosisSourceTest.java`

## Archivos actualizados

- `docs/README.md`
- `docs/desarrollo/PLAN_TANDAS_ACTUAL.md`

## Decisiones

- No se borra documentación histórica porque puede servir como trazabilidad y algunos tests pueden depender de evidencia documental.
- No se toca el `README.md` raíz.
- No se toca el modelo conceptual.
- No se agregan Javadocs en esta tanda.
- La fuente viva de continuidad queda en `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md` y `docs/diagnostico/MACRO_DIAGNOSTICO_TANDA_022.md`.

## Validación esperada

En Windows:

```bat
scripts\02-ejecutar-tests.bat
```

La prueba nueva valida que:

- exista el mapa de documentación viva;
- exista el macro diagnóstico;
- exista la matriz categorizada;
- todos los tipos visibles estén documentados en la matriz categorizada;
- las categorías transversales estén presentes;
- los Javadocs estén marcados como aplazados.

## Pendiente después de esta tanda

- Cierre final liviano o UI/E2E mínimo.
- Javadocs pedagógicos en otro chat.
