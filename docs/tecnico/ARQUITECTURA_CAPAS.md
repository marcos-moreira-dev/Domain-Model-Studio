# 52 - Reglas de capas y dependencias

## Capas oficiales

Domain Model Studio usa cuatro capas principales:

```txt
domain
application
infrastructure
presentation
```

Cada capa tiene una responsabilidad distinta y no debe absorber responsabilidades ajenas.

## 1. Capa `domain`

### Responsabilidad

Representar el modelo puro del problema:

- proyectos de diagrama;
- entidades;
- atributos;
- relaciones;
- cardinalidades;
- estilos como conceptos propios;
- layout como datos;
- reglas conceptuales;
- validaciones de modelo.

### Puede contener

```txt
DiagramProject
DiagramModel
EntityElement
AttributeElement
RelationshipElement
ConnectorElement
Cardinality
NodeLayout
ConnectorLayout
ElementStyle
TextStyle
RgbaColor
```

### No debe contener

```txt
Pane
Scene
Stage
Button
TreeView
ColorPicker
MouseEvent
Path
Rectangle
Ellipse
Text
```

### Prohibido

```java
import javafx.*;
import com.marcosmoreira.domainmodelstudio.presentation.*;
import com.marcosmoreira.domainmodelstudio.infrastructure.*;
import com.marcosmoreira.domainmodelstudio.application.*;
```

### Motivo

El dominio debe ser reutilizable y testeable sin abrir JavaFX. Debe poder servir para Markdown, `.dms`, SVG, PNG y futuras notaciones sin saber cómo se dibuja la UI.

## 2. Capa `application`

### Responsabilidad

Coordinar casos de uso:

- importar modelo;
- generar layout inicial;
- mover elementos;
- cambiar estilos;
- guardar proyecto;
- abrir proyecto;
- exportar SVG/PNG;
- validar acciones de edición.

### Puede depender de

```txt
domain
interfaces propias de application
```

### No debe depender de

```txt
presentation
infrastructure concreta
JavaFX
```

### Motivo

La capa `application` debe expresar operaciones del programa sin saber si el usuario las ejecutó desde un botón, un menú, un atajo de teclado o una prueba automática.

## 3. Capa `infrastructure`

### Responsabilidad

Resolver detalles técnicos:

- leer Markdown;
- escribir/leer `.dms`;
- generar SVG;
- generar PNG;
- acceder al sistema de archivos;
- cargar recursos internos;
- serializar JSON futuro.

### Puede depender de

```txt
domain
application
librerías técnicas justificadas
```

### No debe depender de

```txt
presentation
controles JavaFX de UI
ViewModels
Views
```

### Nota sobre PNG

Si en una fase futura la exportación PNG usa snapshot de JavaFX, debe definirse con cuidado. La opción preferida es que la exportación se base en modelo + layout + estilos. Si se requiere snapshot visual, se debe documentar la excepción para no contaminar todo infrastructure con presentación.

## 4. Capa `presentation`

### Responsabilidad

Construir y coordinar la interfaz JavaFX:

- shell;
- toolbar;
- sidebar;
- canvas;
- inspector;
- statusbar;
- diálogos;
- bindings;
- eventos visuales.

### Puede depender de

```txt
application
domain
JavaFX
```

### Debe evitar

```txt
parsear Markdown directamente
guardar archivos directamente
generar SVG directamente
contener reglas conceptuales profundas
contener clases gigantes
```

### Motivo

La UI debe ser reemplazable o testeable en piezas. La lógica conceptual debe vivir fuera de los controles visuales.

## Reglas de dependencia resumidas

```txt
domain -> nadie del proyecto
application -> domain
infrastructure -> domain/application
presentation -> domain/application
```

## Reglas por defecto para nuevas clases

Antes de crear una clase, responder:

1. ¿Representa una regla/concepto puro del modelo? -> `domain`.
2. ¿Coordina una acción del usuario o caso de uso? -> `application`.
3. ¿Lee/escribe/convierte archivos o recursos? -> `infrastructure`.
4. ¿Muestra algo o recibe eventos JavaFX? -> `presentation`.

## Excepciones

Toda excepción debe quedar documentada en `docs/10_registro_tandas.md` o en un documento específico de la tanda donde se introduzca.
