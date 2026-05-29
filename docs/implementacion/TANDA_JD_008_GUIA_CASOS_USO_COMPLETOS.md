# Tanda JD-8 — Guía de lectura por casos de uso completos

## Objetivo

Crear una guía de lectura que atraviese las capas del proyecto por funcionalidades completas. Esta tanda complementa el JavaDoc por paquetes y el onboarding vivo de arquitectura. No modifica lógica funcional.

## Cambios aplicados

```txt
- docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md
- docs/calidad/AUDITORIA_JAVADOC_JD8.md
- src/test/java/com/marcosmoreira/domainmodelstudio/productization/Jd8CompleteUseCaseReadingGuideSourceTest.java
- scripts\02-ejecutar-tests.bat
```

También se actualizaron:

```txt
- docs/desarrollo/ONBOARDING.md
- docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md
- docs/calidad/PLAN_TANDAS_JAVADOC.md
- docs/documentacion/MAPA_DOCUMENTACION_VIVA.md
- docs/raiz/PLAN_TANDAS_RESTANTES.md
- docs/implementacion/00_indice_implementacion.md
- scripts/README.md
- package-info.java de las capas principales
```

## Recorridos incluidos

```txt
1. Importar ejemplo UENS del Grafo lógico.
2. Levantamiento lógico → Grafo lógico derivado.
3. Guardar y reabrir `.dms`.
4. Exportar Markdown, SVG y recursos IA.
5. Interacción visual: selección, movimiento y bendpoints.
6. Ayuda académica vs ayuda operativa.
7. Release candidate y evidencia.
```

## Validación local

```bat
scripts\02-ejecutar-tests.bat
```

El script ejecuta el guardarraíl fuente JD-8 y genera el sitio JavaDoc para comprobar que la documentación sigue navegable desde `target\site\apidocs\index.html`.

## Criterio de cierre

JD-8 queda cerrada si el guardarraíl pasa y la guía permite estudiar funcionalidades completas sin mezclar responsabilidades entre dominio, aplicación, infraestructura y presentación.
