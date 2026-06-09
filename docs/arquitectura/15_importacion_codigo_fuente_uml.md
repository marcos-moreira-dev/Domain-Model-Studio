# Arquitectura — Importación de código fuente hacia UML Clases

## Propósito

Esta línea arquitectónica permite generar diagramas UML de clases a partir de un directorio de código fuente sin reemplazar la importación Markdown generada o asistida por IA.

La capacidad queda planteada como una segunda entrada paralela:

```txt
Entrada A — Markdown IA
Markdown UML Clases
→ UmlClassMarkdownParser
→ UmlClassDiagramDocument

Entrada B — Código fuente
Directorio de proyecto
→ SourceRootDetectorPort
→ SourceDirectoryScannerPort
→ SourceCodeParserPort
→ ParsedCodeProject
→ mapper futuro hacia UmlClassDiagramDocument
```

## Decisión principal

Los parsers de Java, TypeScript u otros lenguajes no pertenecen al dominio UML ni a la presentación. Deben implementarse como adaptadores conectados mediante puertos.

Esto evita acoplar el producto a:

- Java 21 como única versión válida;
- TypeScript moderno como único dialecto esperado;
- una estructura específica de backend/frontend;
- parsers frágiles incrustados dentro del editor JavaFX.

## Paquete base agregado

```txt
application/sourcecode
```

Contiene:

```txt
SourceCodeImportRequest
SourceRootDetectorPort
SourceRootDetectionResult
SourceDirectoryScannerPort
SourceScanResult
SourceRoot
SourceRootKind
SourceFileCandidate
SourceLanguage
SourceLanguageVersion
SourceCodeParserPort
SourceCodeParserRegistry
SourceCodeParseRequest
ParsedCodeProject
ParsedCodeSourceRoot
ParsedCodeModule
ParsedCodeType
ParsedCodeMember
ParsedCodeRelation
```

## Modelo intermedio neutral

`ParsedCodeProject` representa el resultado normalizado de leer código fuente. Es independiente de Java, TypeScript y UML final.

Debe permitir representar proyectos como:

```txt
Cedro Damasco
├─ backend Java / Spring Boot
└─ frontend TypeScript / Angular
```

sin mezclar todo en un único grafo plano.

## Convivencia con Markdown

La importación Markdown sigue siendo necesaria porque:

- una IA puede generar una vista arquitectónica que no sale directamente del AST;
- el usuario puede describir clases futuras todavía no implementadas;
- las relaciones de negocio pueden ser más claras en Markdown que en código;
- los parsers de código no deben intentar adivinar todo.

Por eso la importación desde código fuente complementa, pero no sustituye, `UmlClassMarkdownParser`.


## Tanda 2 — Detección de raíces y subproyectos

Se agregó un detector inicial de estructura de proyecto para reconocer casos full stack como:

```txt
Cedro Damasco
├─ backend / Java / Spring Boot
└─ frontend / TypeScript / Angular
```

La detección se conecta mediante `SourceRootDetectorPort` y queda implementada en infraestructura con `FileSystemSourceRootDetector`.

El detector no parsea clases todavía. Solo identifica raíces lógicas por señales como:

- `pom.xml`, `build.gradle`, `src/main/java` para Java;
- `package.json`, `angular.json`, `tsconfig.json`, `src/app` para TypeScript;
- nombres de carpeta como `backend`, `frontend`, `api`, `web`, `shared` o `lib` para sugerir rol arquitectónico.

Esto permite crear futuras vistas internas como `Backend Java`, `Frontend TypeScript`, `Integración API` y `Todo` sin mezclar todos los tipos en una sola nube.

## Siguiente evolución

Las siguientes tandas deben agregar:

1. detector de raíces y subproyectos; ✅ Tanda 2
2. escáner real de directorios con carpetas ignoradas;
3. parser mínimo Java;
4. parser mínimo TypeScript;
5. conversión de `ParsedCodeProject` hacia `UmlClassDiagramDocument`;
6. vistas internas por backend/frontend/integración/todo;
7. layout agrupado por package/carpeta/módulo;
8. lienzo expandible para diagramas grandes.

## Tanda 3 — Escáner de archivos con filtros profesionales

Se agregó un escáner de archivos fuente conectado mediante `SourceDirectoryScannerPort` e implementado en infraestructura con `FileSystemSourceDirectoryScanner`.

El flujo ahora queda así:

```txt
SourceCodeImportRequest
→ SourceRootDetectorPort
→ SourceRootDetectionResult
→ FileSystemSourceDirectoryScanner
→ SourceScanResult
```

El escáner recorre cada `SourceRoot` detectada y produce `SourceFileCandidate` para archivos soportados:

- `.java` para Java;
- `.ts` y `.tsx` para TypeScript.

También evita leer carpetas pesadas o ruido operativo:

```txt
.git
.idea
.vscode
target
build
dist
out
node_modules
coverage
.gradle
```

Por defecto excluye archivos de prueba, como `src/test`, `__tests__`, `*.spec.ts`, `*.test.ts`, `*Test.java`, `*Tests.java` o `*IT.java`. La bandera `includeTests` de `SourceCodeImportRequest` permite incluirlos cuando el usuario lo decida en una vista previa posterior.

Además, si una carpeta raíz de workspace y una subcarpeta específica detectan el mismo archivo, el escáner conserva la raíz más específica para evitar duplicar clases en proyectos full stack o monorepo.

## Siguiente evolución actualizada

Las siguientes tandas deben agregar:

1. detector de raíces y subproyectos; ✅ Tanda 2
2. escáner real de directorios con carpetas ignoradas; ✅ Tanda 3
3. parser mínimo Java;
4. parser mínimo TypeScript;
5. modelo neutral y normalización avanzada; ✅ Tanda 6
6. conversión de `ParsedCodeProject` hacia `UmlClassDiagramDocument`; ✅ Tanda 7
7. vistas internas por backend/frontend/integración/todo; ✅ Tanda 8
8. layout agrupado por package/carpeta/módulo;
9. contenedores visuales por módulo/carpeta;
10. lienzo expandible para diagramas grandes;
11. UI de importación desde código fuente;
12. vista previa antes de generar;
13. truncamiento de nombres largos y metadatos;
14. relaciones internas por lenguaje;
15. relaciones API frontend-backend;
16. persistencia `.dms`;
17. exportación Markdown/SVG/PNG;
18. filtros, búsqueda y navegación;
19. tests fuertes y cierre.

## Tanda 4 — Parser Java mínimo desacoplado

Se agregó el primer adaptador real de parsing de código fuente:

```txt
infrastructure/sourcecode/java/JavaSourceCodeParserAdapter
```

El adaptador implementa `SourceCodeParserPort`, por lo que el resto del sistema no depende directamente de Java ni de una versión concreta como Java 21. Las versiones siguen siendo pistas flexibles mediante `SourceLanguageVersion`.

La implementación usa el AST del JDK (`JavacTask`, `CompilationUnitTree`, `ClassTree`, etc.) en vez de convertir el parser en una colección frágil de expresiones regulares. En esta primera versión mínima extrae:

- `package`;
- clases;
- interfaces;
- enums;
- records;
- herencia `extends`;
- realización `implements`;
- campos;
- métodos;
- constructores;
- visibilidad;
- anotaciones como metadato;
- asociaciones tentativas desde campos tipados.

El resultado se expresa únicamente como modelo neutral:

```txt
ParsedCodeProject
├─ ParsedCodeSourceRoot
├─ ParsedCodeModule
├─ ParsedCodeType
├─ ParsedCodeMember
└─ ParsedCodeRelation
```

Todavía no se genera `UmlClassDiagramDocument`; eso queda para la tanda de mapeo posterior. Esta separación mantiene dos entradas paralelas sanas: Markdown generado por IA y código fuente parseado.

## Siguiente evolución actualizada

Las siguientes tandas deben agregar:

1. detector de raíces y subproyectos; ✅ Tanda 2
2. escáner real de directorios con carpetas ignoradas; ✅ Tanda 3
3. parser mínimo Java; ✅ Tanda 4
4. parser mínimo TypeScript;
5. modelo neutral y normalización avanzada; ✅ Tanda 6
6. conversión de `ParsedCodeProject` hacia `UmlClassDiagramDocument`; ✅ Tanda 7
7. vistas internas por backend/frontend/integración/todo; ✅ Tanda 8
8. layout agrupado por package/carpeta/módulo;
9. contenedores visuales por módulo/carpeta;
10. lienzo expandible para diagramas grandes;
11. UI de importación desde código fuente;
12. vista previa antes de generar;
13. truncamiento de nombres largos y metadatos;
14. relaciones internas por lenguaje;
15. relaciones API frontend-backend;
16. persistencia `.dms`;
17. exportación Markdown/SVG/PNG;
18. filtros, búsqueda y navegación;
19. tests fuertes y cierre.

## Tanda 5 — Parser TypeScript mínimo desacoplado

Se agregó el adaptador inicial para TypeScript:

```txt
infrastructure/sourcecode/typescript/TypeScriptSourceCodeParserAdapter
```

El adaptador implementa `SourceCodeParserPort`, por lo que sigue el mismo contrato que Java y se mantiene reemplazable por un adaptador más fuerte basado en el compilador oficial de TypeScript en una etapa futura.

Esta primera versión mínima extrae hacia el modelo neutral:

- clases;
- interfaces;
- enums;
- `type` aliases;
- `extends`;
- `implements`;
- propiedades/campos;
- métodos;
- constructores;
- visibilidad `public`, `protected`, `private`;
- decoradores como metadato, por ejemplo `@Component` o `@Injectable`;
- asociaciones tentativas desde tipos de propiedades y aliases.

La implementación no se acopla a TypeScript moderno como única versión válida. La versión de lenguaje sigue entrando solo como pista flexible mediante `SourceLanguageVersion`.

## Siguiente evolución actualizada

Las siguientes tandas deben agregar:

1. detector de raíces y subproyectos; ✅ Tanda 2
2. escáner real de directorios con carpetas ignoradas; ✅ Tanda 3
3. parser mínimo Java; ✅ Tanda 4
4. parser mínimo TypeScript; ✅ Tanda 5
5. modelo neutral y normalización avanzada; ✅ Tanda 6
6. conversión de `ParsedCodeProject` hacia `UmlClassDiagramDocument`; ✅ Tanda 7
7. vistas internas por backend/frontend/integración/todo; ✅ Tanda 8
8. layout agrupado por package/carpeta/módulo;
9. contenedores visuales por módulo/carpeta;
10. lienzo expandible para diagramas grandes;
11. UI de importación desde código fuente;
12. vista previa antes de generar;
13. truncamiento de nombres largos y metadatos;
14. relaciones internas por lenguaje;
15. relaciones API frontend-backend;
16. persistencia `.dms`;
17. exportación Markdown/SVG/PNG;
18. filtros, búsqueda y navegación;
19. tests fuertes y cierre.


## Tanda 6 — modelo neutral y normalización

La importación desde código ahora cuenta con una etapa de normalización neutral antes de generar UML.

Responsabilidades agregadas:

- `SourceCodeProjectParserUseCase`: orquesta escaneo, selección de parser y fusión de resultados por raíz/lenguaje.
- `ParsedCodeProjectNormalizer`: completa módulos, deduplica tipos/relaciones, resuelve destinos internos y enriquece metadatos.
- `ParsedCodeTypeRoleClassifier`: clasifica roles tentativos para futuras vistas y agrupaciones visuales.

Esta etapa mantiene el desacoplamiento: los parsers Java/TypeScript producen `ParsedCodeProject`, y el dominio UML todavía no depende de esos lenguajes.

## Tanda 7 — Conversión del modelo neutral hacia UML Clases

Se agregó el mapeo desde `ParsedCodeProject` hacia `UmlClassDiagramDocument` mediante:

```txt
SourceCodeToUmlClassDiagramMapper
GenerateUmlClassDiagramFromSourceCodeUseCase
```

El flujo de código fuente ahora puede llegar al documento semántico de UML Clases:

```txt
Directorio de proyecto
→ SourceRootDetectorPort
→ SourceDirectoryScannerPort
→ SourceCodeParserPort
→ ParsedCodeProjectNormalizer
→ SourceCodeToUmlClassDiagramMapper
→ UmlClassDiagramDocument
```

La conversión mantiene el camino Markdown en paralelo:

```txt
Markdown IA
→ UmlClassMarkdownParser
→ UmlClassDiagramDocument
```

Por ahora los metadatos de origen se conservan en campos documentales (`description`, `notes`) porque el dominio UML todavía no tiene una estructura formal de metadatos por clase/miembro/relación. Esa formalización queda para las tandas de vistas internas, truncamiento, panel de propiedades y persistencia.

## Tanda 8 — Vistas internas por backend/frontend/integración/todo

El documento UML Clases ahora puede declarar vistas internas mediante:

```txt
UmlClassDiagramView
UmlClassDiagramViewKind
SourceCodeUmlClassViewBuilder
```

Esto permite que un sistema multi-raíz, por ejemplo backend Java + frontend TypeScript, termine en un único documento UML con vistas filtrables:

```txt
[Resumen] [Backend Java] [Frontend TypeScript] [Integración API] [Mega vista]
```

La vista no duplica proyectos ni sustituye el parser Markdown. Solo filtra módulos, clases y relaciones dentro del mismo documento semántico.

## Siguiente evolución actualizada tras Tanda 8

Las siguientes tandas deben agregar:

1. layout agrupado por package/carpeta/módulo;
2. contenedores visuales por módulo/carpeta;
3. lienzo expandible para diagramas grandes;
4. UI de importación desde código fuente;
5. vista previa antes de generar;
6. truncamiento de nombres largos y metadatos;
7. relaciones internas por lenguaje;
8. relaciones API frontend-backend;
9. persistencia `.dms`;
10. exportación Markdown/SVG/PNG;
11. filtros, búsqueda y navegación;
12. tests fuertes y cierre.

## Tanda 11 — Lienzo expandible para diagramas grandes

La superficie visual común ahora puede expandir su área navegable según los bounds del contenido renderizado o semántico.

Piezas agregadas o ajustadas:

```txt
DynamicWorkspaceBoundsPolicy
DiagramSurfaceWorkspaceSize
ZoomableDiagramSurface.ensureWorkspaceContains(...)
DiagramSurfaceViewportController con tamaño dinámico
DiagramSurfaceBackground con grilla dinámica
```

Esta decisión es transversal: no pertenece solo a UML Clases. Cualquier diagrama visual montado sobre la superficie común puede beneficiarse cuando el contenido supera el tamaño inicial del workspace.

La regla de arquitectura queda así:

```txt
El contenido visual manda el tamaño navegable.
El canvas mantiene un tamaño base, pero se expande con margen cuando el diagrama crece.
```

No aplica igual a vistas documentales o tabulares, como diccionario de datos o matriz de roles/permisos.

## Tanda 17 — Persistencia `.dms` de vistas internas y metadatos

Se amplió el formato `.dms` para que `umlClassDiagram` conserve el arreglo `views[]`.

Esto permite guardar y reabrir diagramas generados desde código fuente sin perder vistas internas como:

```txt
Resumen
Backend Java
Frontend TypeScript
Integración API
Mega vista
```

La persistencia conserva filtros por source root, módulo, clase y relación. También mantiene metadatos de origen en los campos existentes de módulos, clases y relaciones (`description` y `notes`).

La compatibilidad con proyectos antiguos se mantiene: cuando un `.dms` no trae `views[]`, el lector crea el documento UML con lista de vistas vacía.

## Tanda 18 — Exportación Markdown/SVG/PNG

La exportación Markdown de UML Clases conserva las vistas internas mediante una sección dedicada. Esto permite que un diagrama generado desde código fuente pueda reabrirse desde Markdown sin perder la separación entre resumen, backend, frontend, integración y vista completa.

El SVG especializado incluye información de vistas internas en el encabezado cuando existen. La exportación PNG continúa actuando como snapshot de la vista visual activa.

## Tanda 19 — Filtros, búsqueda y navegación

El editor UML Clases incorpora filtrado por vista interna, texto, tipo de clase y tipo de relación. El buscador revisa nombres, rutas, packages, módulos, miembros, notas y metadatos de relaciones API. Cuando una relación coincide, conserva visibles sus extremos para evitar conectores sin contexto.

## Tanda 20 — Tests fuertes y cierre

La fase queda protegida con pruebas de regresión del flujo completo:

```txt
carpeta Java/TypeScript realista
→ detector/escáner
→ parsers Java y TypeScript
→ normalizador
→ mapper UML
→ vistas internas
→ relación API frontend-backend
```

También se agregan guardarraíles de fuente para verificar que la composición registre ambos parsers y que el comando de importación desde código conviva con la importación Markdown generada por IA.

Quedan fuera de esta fase los parsers exhaustivos de nivel compilador completo, la integración formal con OpenAPI y optimizaciones de rendimiento específicas para repositorios enormes. La arquitectura, sin embargo, queda preparada para extender esos puntos sin contaminar el dominio UML.
