# Tests guardarraíl de arquitectura

## Propósito

Estas pruebas protegen la arquitectura. No buscan demostrar toda la funcionalidad del producto, sino detectar acoplamientos peligrosos antes de que el código crezca.

## Archivo principal

```txt
src/test/java/com/marcosmoreira/domainmodelstudio/architecture/ArchitectureBoundaryTest.java
```

## Enfoque

Se usan pruebas JUnit 5 y lectura directa de archivos `.java`. Esto evita meter dependencias adicionales como ArchUnit en el MVP de onboarding.

El test recorre:

```txt
src/main/java
```

y revisa imports y tamaños básicos.

## Reglas verificadas

### 1. `domain` no importa JavaFX

La capa de dominio debe mantenerse pura.

Ejemplo prohibido:

```java
import javafx.scene.paint.Color;
```

En vez de eso, usar un tipo propio futuro:

```java
RgbaColor
```

### 2. `application` no importa JavaFX

Los casos de uso no deben depender de controles visuales.

### 3. `domain` no depende de otras capas

Prohibido:

```java
import com.marcosmoreira.domainmodelstudio.presentation.*;
import com.marcosmoreira.domainmodelstudio.infrastructure.*;
import com.marcosmoreira.domainmodelstudio.application.*;
```

### 4. `application` no depende de `infrastructure` ni `presentation`

La capa de aplicación debe depender de abstracciones, no de adaptadores concretos ni vistas.

### 5. `infrastructure` no depende de `presentation`

Un parser o exportador no debe conocer Views ni ViewModels.

### 6. `presentation` no importa infraestructura directamente

El shell, canvas o inspector deben llamar casos de uso de application, no manipular directamente adaptadores de filesystem, parser o exportador.

### 7. Tamaño máximo inicial de archivos Java

Se establece un límite inicial prudente para detectar clases gigantes temprano.

Este límite puede ajustarse cuando aparezca una clase justificadamente más larga, pero debe documentarse.

## Cómo ejecutar

```bat
scripts	est-all.bat
```

o directamente:

```bat
mvn clean test
```

## Limitaciones

Estas pruebas son intencionalmente simples. Pueden detectar imports indebidos, pero no entienden toda la semántica del código.

No reemplazan revisión humana.

## Evolución futura

Más adelante se puede agregar:

- ArchUnit;
- métricas de longitud de métodos;
- reglas sobre nombres;
- validaciones de paquetes por feature;
- tests del parser;
- tests del formato `.dms`;
- tests del exportador SVG.

Por ahora, el objetivo es proteger las fronteras más importantes con el menor peso posible.
