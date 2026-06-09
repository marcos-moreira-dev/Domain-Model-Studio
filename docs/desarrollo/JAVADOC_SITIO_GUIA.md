# Guía del sitio JavaDoc

## Objetivo

Explicar cómo usar el sitio JavaDoc generado por Maven como material de estudio y mantenimiento del código. Esta guía complementa `ONBOARDING_CODIGO_JAVADOC.md`: el onboarding dice **qué leer** y esta guía dice **cómo navegar el sitio generado**.

## Generación del sitio

Desde la raíz del proyecto:

```bat
scripts\31-generar-javadoc.bat
```

Salida esperada:

```txt
target\site\apidocs\index.html
```

El script público vigente para JavaDoc es único. Los antiguos scripts focalizados JD-1..JD-5 fueron retirados durante la limpieza de scripts; la validación actual se hace con la suite completa:

```bat
scripts\02-ejecutar-tests.bat
```

Si solo se necesita revisar el sitio, usar `scripts\31-generar-javadoc.bat` y abrir `target\site\apidocs\index.html`.

## Cómo navegar

### 1. Empezar por paquetes

La lectura recomendada no empieza por una clase suelta. Empieza por los paquetes:

```txt
com.marcosmoreira.domainmodelstudio.domain
com.marcosmoreira.domainmodelstudio.application
com.marcosmoreira.domainmodelstudio.infrastructure
com.marcosmoreira.domainmodelstudio.presentation
```

Los `package-info.java` explican la frontera de cada capa y ayudan a evitar lecturas desordenadas.

### 2. Usar Search

El JavaDoc generado incluye búsqueda. Usa Search para ubicar contratos clave:

```txt
LogicalBusinessGraphDocument
LogicalBusinessGraphValidationPolicy
DefaultDiagramTypeDefinitions
DmsProjectJsonReader
InteractiveCanvasSurfaceView
WorkbenchSideDock
```

Después de abrir una clase, leer primero su descripción de tipo antes de saltar a métodos.

### 3. Leer por recorridos

Para estudiar el sistema como ingeniería de software, conviene recorrer por flujo:

```txt
Dominio → aplicación → infraestructura → presentación
```

Ejemplo de recorrido del Grafo lógico:

```txt
LogicalBusinessGraphDocument
→ LogicalBusinessGraphValidationPolicy
→ LogicalBusinessGraphMarkdownParser
→ DmsProjectJsonWriter
→ LogicalBusinessGraphViewModel
→ LogicalBusinessGraphCanvasAdapter
```

### 4. No usar JavaDoc como única fuente

JavaDoc explica contratos técnicos. Para decisiones de producto, validar también:

```txt
docs/README.md
docs/documentacion/MAPA_DOCUMENTACION_VIVA.md
docs/arquitectura/
docs/testeo/
docs/producto/
```

## Guardarraíl de cobertura

JD-5 fija un criterio gradual:

```txt
Tipos públicos documentados >= 95%.
Miembros públicos: no se exige 100% todavía.
Getters, setters, records simples y métodos triviales no bloquean la tanda.
```

Este criterio evita llenar el proyecto de comentarios obvios y mantiene el foco en contratos públicos útiles.

## Qué revisar después de generar

1. Existe `target\site\apidocs\index.html`.
2. El título del sitio muestra Domain Model Studio JavaDoc.
3. La navegación por paquetes carga correctamente.
4. Search encuentra clases críticas.
5. Los paquetes de dominio, aplicación, infraestructura y presentación tienen descripciones útiles.
6. No aparecen errores de generación por JavaDoc roto.

## Relación con las tandas JD

```txt
JD-1: dominio.
JD-2: aplicación.
JD-3: infraestructura.
JD-4: presentación.
JD-5: sitio generado y cobertura gradual.
```

Las tandas posteriores agregan ejemplos pedagógicos, rutas de lectura completas y decisiones arquitectónicas. La Tanda 38A revisa JavaDoc post-refactor solo donde cambiaron fronteras técnicas reales.
