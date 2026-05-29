# Contexto largo — UML desde código fuente, modo light y render seguro

Este documento existe para recuperar el contexto si se quiebra la ventana de chat.

## Estado del producto antes de esta fase

Domain Model Studio ya tiene una primera versión amplia de importación UML desde código fuente. La herramienta puede detectar proyectos Java y TypeScript, escanear directorios, parsear clases/interfaces/enums, normalizar un modelo neutral, convertirlo a UML Clases, crear vistas internas por backend/frontend/integración/mega vista, agrupar layout por módulos, persistir en `.dms`, exportar Markdown/SVG/PNG y navegar con filtros.

La importación Markdown generada por IA sigue siendo una entrada paralela y no debe ser reemplazada por la importación desde código. Ambas deben convivir:

```text
Markdown IA → UmlClassMarkdownParser → UmlClassDiagramDocument
Código fuente → Scanner/Parsers → ParsedCodeProject → Mapper → UmlClassDiagramDocument
```

## Problema observado en pruebas reales

Al seleccionar una carpeta concreta con archivos Java, la vista previa detecta archivos, raíces y lenguajes correctamente. Sin embargo, al generar UML, aparece progreso, se cierra la ventana de carga y después la aplicación puede quedar en `No responde`.

Esto indica que el bloqueo probablemente ocurre después del parseo, durante una de estas fases:

1. generación del documento UML detallado;
2. cálculo de layout para demasiados nodos/conectores;
3. montaje de muchos nodos JavaFX;
4. renderizado de demasiados textos de atributos/métodos;
5. fit/center inicial del lienzo;
6. export/snapshot potencialmente enorme.

## Regla nueva de producto

El canvas no debe ser un dump completo del código fuente. Debe ser un mapa arquitectónico navegable.

Por defecto se debe usar una vista light:

- nombre de clase/archivo;
- tipo: clase, interfaz, enum, record;
- package/ruta resumida;
- rol inferido: controller, service, repository, component, dto, etc.;
- relaciones principales;
- máximo configurable de atributos/métodos visibles;
- `...` o `+ N más` cuando existan más miembros.

El detalle completo debe quedar bajo demanda en panel de propiedades, tooltip, menú contextual o abriendo el archivo real en el explorador/editor del sistema.

## Decisión técnica general

La solución no es solamente poner todo en un hilo. El hilo evita congelar durante parseo, pero si al terminar se manda a JavaFX a renderizar cientos de clases con miles de textos de golpe, la interfaz puede congelarse igual.

La solución debe ser progresiva:

```text
Escanear
→ Parsear
→ Normalizar
→ Estimar costo
→ Generar vista light
→ Abrir editor seguro
→ Renderizar solo vista activa
→ Cargar detalle bajo demanda
```

## Tandas de esta fase

1. Auditoría del cuello de botella y puntos de congelamiento.
2. Perfil de importación UML light/detail.
3. Modelo UML con miembros completos, pero render limitado.
4. Estimador de costo antes de renderizar.
5. Medidor de memoria real.
6. Importación por etapas con modelo intermedio.
7. Render diferido por vista interna.
8. Apertura segura del editor UML.
9. Render por lotes en JavaFX.
10. Clic derecho: abrir archivo y acciones de navegación.
11. Panel derecho con detalle completo bajo demanda.
12. Protección de exportación PNG/SVG.
13. Manejo de OutOfMemoryError y fallos grandes.
14. Vista previa avanzada antes de generar.
15. Tests y cierre de estabilidad.
