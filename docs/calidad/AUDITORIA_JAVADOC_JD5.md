# Auditoría JavaDoc JD-5 — Sitio generado y cobertura gradual

## Estado

Tanda JD-5 aplicada.

## Alcance

La tanda no agrega lógica funcional. Su objetivo es cerrar la parte operativa del JavaDoc:

```txt
- generación del sitio `target/site/apidocs`;
- guía de navegación del sitio;
- guardarraíl de cobertura de tipos públicos;
- regla gradual para no exigir 100% de miembros públicos todavía.
```

## Cobertura estática estimada

Auditoría sobre `src/main/java`:

```txt
Tipos públicos detectados: 750
Tipos públicos con JavaDoc: 741
Cobertura aproximada: 98.8%
Umbral JD-5: >= 95%
```

La cobertura de miembros públicos sigue siendo deliberadamente no bloqueante. En esta etapa el objetivo es documentar tipos, agregados, servicios, adaptadores y contratos arquitectónicos; no llenar getters, setters o records simples con comentarios obvios.

## Guardarraíl aplicado

Nuevo test fuente:

```txt
Jd5JavadocSiteCoverageSourceTest
```

Valida:

```txt
- `maven-javadoc-plugin` configurado en `pom.xml`;
- `scripts/31-generar-javadoc.bat` genera `target/site/apidocs/index.html`;
- `scripts\02-ejecutar-tests.bat` es la validación vigente tras la limpieza de scripts;
- `docs/desarrollo/JAVADOC_SITIO_GUIA.md` explica navegación, Search y paquetes;
- cobertura de tipos públicos >= 95% por análisis fuente gradual.
```

## Comandos

Generar JavaDoc:

```bat
scripts\31-generar-javadoc.bat
```

Validar JD-5 dentro de la suite vigente:

```bat
scripts\02-ejecutar-tests.bat
```

Suite global:

```bat
scripts\02-ejecutar-tests.bat
```

## Decisión de calidad

No se activa umbral de métodos públicos global todavía.

Motivo:

```txt
- el proyecto tiene muchos getters, setters, records y métodos de UI que no ganarían claridad con comentarios mecánicos;
- JavaDoc debe explicar contratos, no repetir nombres;
- las siguientes tandas pueden agregar ejemplos pedagógicos en puntos críticos sin inflar todo el código.
```

## Pendiente recomendado

La siguiente mejora útil es JD-6: ejemplos JavaDoc pedagógicos en clases críticas, especialmente validación del Grafo lógico, parsers/exporters y canvas transversal.
