# Onboarding de desarrollo — Domain Model Studio

## Objetivo

Levantar el proyecto de forma reproducible en Windows, ejecutar pruebas, abrir la aplicación JavaFX y preparar empaquetados sin depender de memoria o instrucciones dispersas.

## Requisitos base

| Herramienta | Versión recomendada | Uso |
|---|---:|---|
| JDK | 21 LTS, preferible Eclipse Temurin 21 | Compilar, ejecutar JavaFX y usar `jpackage` |
| Maven | 3.9+ | Compilar, probar y resolver dependencias |
| Git | versión reciente | Control de versiones |
| Python | 3.10+ opcional | Recalcular métricas de refactor |
| Windows | 10/11 | Entorno objetivo desktop |

## Primer arranque

Desde PowerShell o CMD en la raíz del proyecto:

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\01-ejecutar-app.bat
```

Si `00-verificar-entorno.bat` falla, resolver primero Java/Maven/PATH. No avanzar a correcciones funcionales con entorno roto.

## Ciclo diario recomendado

```bat
scripts\02-ejecutar-tests.bat
scripts\01-ejecutar-app.bat
```

Para tandas de refactor o limpieza arquitectónica:

```bat
scripts\06-medir-refactor.bat
scripts\02-ejecutar-tests.bat
```

Para cierre local más ordenado:

```bat
scripts\13-revalidacion-local-completa.bat
```


## Lectura del código y JavaDoc

Para estudiar el proyecto como material de ingeniería de software, usa también:

```bat
scripts\31-generar-javadoc.bat
```

Luego abre:

```txt
target\site\apidocs\index.html
```

Guía de lectura recomendada:

```txt
docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md
```

La ruta de estudio sugerida es dominio → aplicación → infraestructura → presentación. No conviene empezar por JavaFX si se quiere entender las reglas del negocio y las fronteras arquitectónicas.

Después de JD-2, la guía de código incluye una sección específica de aplicación para estudiar catálogos, casos de uso, validación, derivaciones, exportaciones, workspace y layout visual persistente.

Después de JD-3, la guía de código incluye infraestructura para estudiar Markdown, `.dms`, SVG, PDF, recursos IA y roundtrip de formatos.

Después de JD-4, la guía de código incluye una sección específica de presentación para estudiar shell, workspaces, SideDock, canvas transversal, adaptadores visuales y Grafo lógico visual sin tocar el modelo conceptual protegido.

Después de JD-5, existe una guía específica para usar el sitio JavaDoc generado: `docs/desarrollo/JAVADOC_SITIO_GUIA.md`. El sitio queda en `target\site\apidocs\index.html` y el guardarraíl gradual exige mantener al menos 95% de tipos públicos documentados.

Después de JD-6, existe una guía de ejemplos JavaDoc pedagógicos: `docs/desarrollo/JAVADOC_EJEMPLOS_PEDAGOGICOS.md`. Úsala para estudiar contratos concretos como grafo lógico, Markdown importable, `.dms`, canvas adaptado y SideDock transversal.

Después de JD-7, existe una guía viva de arquitectura y ruta de estudio: `docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md`. Úsala para recorrer dominio → aplicación → infraestructura → presentación con scripts y tests de respaldo.

## Estructura útil

| Ruta | Propósito |
|---|---|
| `src/main/java` | Código productivo Java |
| `src/test/java` | Pruebas unitarias, fuente y arquitectura |
| `src/main/resources` | CSS, ejemplos internos, ayuda, recursos IA |
| `examples/markdown` | Ejemplos oficiales importables |
| `scripts/` | Entrypoints operativos numerados |
| `scripts/internal/` | Implementación interna de scripts |
| `docs/desarrollo` | Guías vivas de desarrollo/validación |
| `docs/desarrollo/refactor` | Métricas y plan SOLID |
| `docs/producto` | Capacidades reales del producto |

## Regla del modelo conceptual

El modelo conceptual se conserva como zona protegida durante estas tandas. Se puede leer como referencia visual/arquitectónica, pero no modificar salvo corrección mínima inevitable y explícitamente documentada.

## Flujo para una nueva tanda

1. Partir del ZIP más reciente.
2. Aplicar una tanda acotada.
3. No mezclar hotfix funcional con refactor masivo salvo emergencia.
4. Documentar la tanda en `docs/desarrollo/TANDA_XXX_*.md`.
5. Ejecutar pruebas en Windows.
6. Generar ZIP completo del proyecto.

## Problemas frecuentes

### Maven no se reconoce

Ejecutar:

```bat
mvn -version
```

Si falla, instalar Maven o corregir `PATH`.

### Maven usa otro Java

Ejecutar:

```bat
java -version
javac -version
mvn -version
```

Los tres deben apuntar razonablemente a Java 21.

### JavaFX no abre

Primero verificar que los tests compilen. Luego ejecutar:

```bat
scripts\01-ejecutar-app.bat
```

Copiar el error completo si falla durante `javafx:run`.

### El instalador MSI falla

Primero validar app-image:

```bat
scripts\14-app-image-completa.bat
```

Luego validar MSI:

```bat
scripts\15-msi-completo.bat
```

Si app-image funciona pero MSI falla, revisar herramientas externas requeridas por `jpackage` en Windows y el log `dist\logs\jpackage-msi.log`.


## Ruta de lectura de infraestructura y formatos

Para estudiar cómo la aplicación se comunica con archivos y salidas documentales, usar:

```txt
docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/pdf
```

La pregunta guía de JD-3 es: ¿cómo se garantiza el roundtrip entre Markdown, `.dms`, SVG/PDF y recursos IA sin mezclar reglas de dominio con detalles de archivo?

Después de JD-8, existe una guía de lectura por casos de uso completos: `docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md`. Úsala para seguir funcionalidades de punta a punta, por ejemplo importar el ejemplo UENS del Grafo lógico, usar el Levantamiento lógico como fuente revisable, guardar/reabrir `.dms`, exportar Markdown/SVG/recursos IA y revisar canvas, ayuda y release.

## Después de JD-9 — decisiones de diseño y ADRs pedagógicos

Existe una guía para leer el **porqué** de las decisiones arquitectónicas principales:

```txt
docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md
```

Úsala después de leer JavaDoc y casos de uso completos. La pregunta guía ya no es solo qué clase hace algo, sino por qué se eligió esa frontera: SideDock transversal, Grafo lógico con dominio propio, ayuda académica separada de ayuda operativa, canvas por adaptadores, formatos Markdown/`.dms` y release con evidencia.
