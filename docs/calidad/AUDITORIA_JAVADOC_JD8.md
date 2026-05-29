# Auditoría JavaDoc JD-8 — Casos de uso completos

## Estado

JD-8 aplicada.

## Alcance

No modifica lógica funcional. Esta tanda mejora el valor pedagógico del proyecto mediante recorridos de lectura que atraviesan capas completas. Su objetivo es mejorar el valor pedagógico del proyecto mediante recorridos de lectura que atraviesan capas completas.

## Documentos agregados

```txt
docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md
```

## Código documentado

Se reforzó JavaDoc de `package-info.java` en las capas principales para indicar la lectura transversal por casos de uso completos:

```txt
domain
application
infrastructure
presentation
bootstrap
```

## Criterio de calidad

La guía debe permitir estudiar al menos estos recorridos:

```txt
- importar ejemplo UENS del Grafo lógico;
- derivar Grafo lógico desde Levantamiento lógico;
- guardar y reabrir `.dms`;
- exportar Markdown, SVG y recursos IA;
- interactuar con canvas y bendpoints;
- separar ayuda académica de ayuda operativa;
- entender release candidate y evidencia.
```

## Validación

Guardarraíl focalizado:

```bat
scripts\02-ejecutar-tests.bat
```

Test fuente:

```txt
Jd8CompleteUseCaseReadingGuideSourceTest
```

## Decisión

La documentación por capas de JD-1 a JD-7 ya permite estudiar arquitectura. JD-8 agrega la dimensión de ingeniería de software aplicada: seguir funcionalidades completas desde intención de usuario hasta tests y release.
