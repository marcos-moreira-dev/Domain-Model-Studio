# PF07 — Centro de ayuda como micro-Wikipedia teórica

Estado: **planificación ejecutada**  
Tipo: **alineación funcional / contenido teórico / ayuda integrada**  
Alcance: **no implementa código; define el contrato de contenido, navegación y recursos gráficos**

## 1. Decisión principal

El centro de ayuda de Domain Model Studio no debe ser una lista breve de ideas ni un manual de botones. Debe funcionar como una **micro-Wikipedia interna de teoría de diagramas**, orientada a estudiar y consultar la base conceptual detrás de cada tipo de diagrama disponible en el producto.

Su rol principal es enseñar:

- qué representa cada tipo de diagrama;
- qué problema ayuda a analizar durante levantamiento de información, requerimientos, arquitectura o diseño;
- cuáles son sus elementos teóricos;
- cuáles son sus relaciones o conectores;
- qué reglas de lectura existen;
- qué errores conceptuales se cometen con frecuencia;
- qué casos especiales debe conocer el usuario;
- cómo se relaciona ese diagrama con otros diagramas del mismo proyecto.

La ayuda no debe convertirse en una explicación de cómo está programada la aplicación. Tampoco debe ser una pantalla de estado interno del producto. Debe hablar desde el dominio del usuario: diagramas, modelos, procesos, roles, pantallas, arquitectura, requerimientos y comunicación con clientes.

## 2. Regla de alcance

El centro de ayuda tendrá tres capas, pero la primera es la más importante:

| Capa | Rol | Prioridad |
|---|---|---|
| Teoría | Explica la ciencia, notación y uso conceptual de cada diagrama. | Alta |
| Lectura práctica | Explica cómo interpretar el diagrama en un proyecto real. | Media |
| Uso en la app | Explica cómo crear, editar, validar o exportar dentro de Domain Model Studio. | Baja |

La versión de productización debe priorizar la teoría. Las instrucciones de uso de botones pueden existir, pero no deben reemplazar el contenido conceptual.

## 3. Problema actual detectado

El archivo actual `ManualContent.java` contiene una ayuda demasiado pobre para lo que promete la aplicación. También mezcla tres cosas que deberían separarse:

1. estado de funciones disponibles;
2. teoría de los diagramas;
3. explicación visual mínima.

Además, todavía contiene restos de una etapa anterior donde la aplicación se asumía principalmente como herramienta de modelo conceptual. Ejemplos detectados:

- se habla de “Modelo conceptual validado” como ruta central;
- se sugiere que otros tipos quedan como guías de preparación;
- los dibujos son ASCII básicos y no alcanzan para explicar diagramas complejos;
- no existe profundidad teórica por cada tipo visible;
- no hay fichas con casos especiales, reglas de lectura, errores comunes ni relación entre diagramas.

Esto debe corregirse en implementación posterior sin inflar `ManualContent.java` ni crear una clase gigante.

## 4. Principio arquitectónico para implementar después

El contenido teórico debe migrar gradualmente a recursos editables, no quedar todo hardcodeado en Java.

Estructura recomendada:

```text
src/main/resources/help/
  index.yaml
  topics/
    inicio-rapido.md
    fundamentos-levantamiento.md
    modelo-conceptual.md
    diccionario-datos.md
    bpmn-basico.md
    flujo-operativo.md
    c4-contexto.md
    c4-contenedores.md
    despliegue-tecnico.md
    uml-casos-uso.md
    uml-clases.md
    uml-actividad.md
    uml-secuencia.md
    uml-estados.md
    mapa-modulos.md
    roles-permisos.md
    flujo-pantallas.md
    wireframes-administrativos.md
    glosario.md
  figures/
    conceptual-entidad.svg
    conceptual-relacion.svg
    uml-actor.svg
    uml-caso-uso.svg
    uml-clase.svg
    uml-secuencia-linea-vida.svg
    bpmn-evento-inicio.svg
    bpmn-tarea.svg
    bpmn-gateway.svg
    c4-persona.svg
    c4-sistema.svg
    wireframe-pantalla.svg
```

Java debería encargarse de cargar, navegar, buscar y renderizar contenido; no de contener toda la teoría en métodos enormes.

## 5. Componentes sugeridos para no violar SRP

No implementar todavía, pero planificar con esta separación:

| Componente sugerido | Responsabilidad única |
|---|---|
| `HelpTopic` | Representar una ficha de ayuda. |
| `HelpSection` | Representar una sección dentro de una ficha. |
| `HelpFigure` | Representar una imagen didáctica, su título y texto alternativo. |
| `HelpTopicRepository` | Cargar fichas desde recursos Markdown/YAML. |
| `HelpIndexRepository` | Cargar índice, categorías y orden de navegación. |
| `HelpSearchService` | Buscar por título, etiquetas y contenido resumido. |
| `HelpContentRenderer` | Convertir Markdown/figuras a nodos JavaFX. |
| `HelpCenterView` | Mostrar navegación, buscador y panel de lectura. |
| `HelpCenterViewModel` | Estado de selección, búsqueda y tema activo. |
| `HelpFigureCatalog` | Resolver figuras oficiales por tipo y concepto. |

Regla de trazabilidad humana: ninguna clase de ayuda debería saber a la vez de catálogo de diagramas, parseo de Markdown, carga de archivos, renderizado JavaFX, búsqueda y navegación. Si aparece esa mezcla, dividir.

## 6. Navegación deseada

El centro de ayuda debe sentirse como una mini enciclopedia:

```text
Centro de ayuda
├── Inicio
├── Fundamentos
│   ├── Levantamiento de información
│   ├── Requerimientos y modelos
│   ├── Diferencia entre diagrama, documento y maqueta
│   └── Cómo elegir un diagrama
├── Modelado de datos
│   ├── Modelo conceptual
│   └── Diccionario de datos
├── Procesos de negocio
│   ├── BPMN básico
│   └── Flujo operativo
├── UML
│   ├── Casos de uso
│   ├── Clases
│   ├── Actividad
│   ├── Secuencia
│   └── Estados
├── Arquitectura de software
│   ├── C4 Contexto
│   ├── C4 Contenedores
│   └── Despliegue técnico
├── Aplicaciones administrativas
│   ├── Mapa de módulos
│   ├── Roles y permisos
│   ├── Flujo de pantallas
│   └── Wireframes administrativos
├── Glosario
└── Fuentes y lecturas recomendadas
```

## 7. Plantilla obligatoria para cada ficha teórica

Cada tipo de diagrama debe tener una ficha con esta estructura mínima:

```text
# Nombre del diagrama

## Qué es
## Para qué sirve
## Cuándo usarlo
## Cuándo no usarlo
## Elementos principales
## Relaciones o conectores
## Reglas de lectura
## Casos especiales
## Errores comunes
## Relación con otros diagramas
## Mini ejemplo textual
## Figuras didácticas asociadas
## Fuentes teóricas recomendadas
```

Para algunos tipos, también aplica:

```text
## Niveles de abstracción
## Variantes de notación
## Diferencia con diagramas parecidos
## Qué simplifica Domain Model Studio
## Qué no debe prometer la herramienta
```

## 8. Política de imágenes didácticas

La ayuda debe acompañarse con figuras simples. No deben ser decorativas; deben enseñar la notación.

Reglas:

- usar estilo simple, limpio y vectorial cuando sea posible;
- preferir SVG para que se vea nítido al escalar;
- cada figura debe tener título, propósito y texto alternativo;
- no usar capturas confusas de la app como reemplazo de teoría;
- no usar imágenes con demasiado color ni detalle ornamental;
- una figura debe enseñar un concepto puntual: actor, entidad, relación, gateway, clase, estado, etc.;
- las imágenes generadas por IA deben revisarse y normalizarse antes de entrar al producto;
- no mezclar estilos visuales radicalmente distintos dentro de la misma ayuda.

Ejemplo de recurso correcto:

```text
Figura: uml-actor.svg
Uso: ficha UML Casos de uso
Propósito: mostrar que un actor representa un rol externo que interactúa con el sistema.
Texto alternativo: figura de palitos etiquetada como Actor.
```

## 9. Nivel de profundidad esperado

La ayuda no debe tener el tamaño de un libro completo, pero sí debe superar claramente una lluvia de ideas.

Nivel recomendado por ficha:

| Tipo de sección | Tamaño recomendado |
|---|---|
| Qué es | 1 a 2 párrafos claros. |
| Para qué sirve | 3 a 6 viñetas. |
| Elementos principales | tabla con símbolo, nombre y explicación. |
| Casos especiales | 3 a 10 casos según diagrama. |
| Errores comunes | lista concreta con corrección. |
| Relación con otros diagramas | 3 a 6 enlaces conceptuales. |
| Figuras | 3 a 8 por diagrama complejo; 1 a 3 por diagrama simple. |

## 10. Fuentes base recomendadas

Para mantener consistencia teórica, la implementación posterior debe usar fuentes primarias o reconocidas como base conceptual, sin copiar textos largos.

Fuentes base por familia:

| Familia | Fuente base recomendada |
|---|---|
| UML | OMG UML 2.5.1 Specification. |
| BPMN | OMG BPMN 2.0.2 Specification. |
| C4 | Documentación oficial del modelo C4 de Simon Brown. |
| Modelo conceptual / ER | Artículo original de Peter Chen sobre el modelo entidad-relación. |
| Wireframes | Material de prototipado de baja fidelidad y paper prototyping, más enfoque propio de levantamiento. |
| Requerimientos | ISO/IEC/IEEE 29148 como referencia de ingeniería de requerimientos, sin convertir la app en una herramienta formal SRS. |

Regla: si una fuente es estándar formal, la ayuda debe explicar de forma didáctica y resumida, no transcribir.

## 11. Criterio de cierre de PF07

Esta tanda de planificación queda cerrada cuando existan:

- contrato de micro-Wikipedia teórica;
- matriz de contenido por tipo de diagrama;
- política de figuras didácticas;
- ruta de implementación sin clase gigante;
- lista clara de fuentes teóricas base;
- decisión de que el centro de ayuda prioriza teoría sobre instrucciones de botones.

La implementación posterior debe crear el centro de ayuda real, pero esta tanda no implementa código.
