# IMP-09 — UML Clases sobre canvas común

## Objetivo técnico

Conectar UML Clases al paquete `presentation.interactivecanvas` sin copiar la lógica de paneo, zoom, selección rectangular, drag ni puntos intermedios.

## Contrato aplicado

```text
UmlModuleGroup    -> InteractiveCanvasNode kind=uml-module
UmlClassNode      -> InteractiveCanvasNode kind=uml-class-<tipo>
UmlClassRelation  -> InteractiveCanvasConnector kind=uml-relation-<tipo>
```

IDs visuales persistentes:

```text
uml-module:<moduleId>
uml-class:<classId>
uml-relation:<relationId>
```

## Responsabilidades nuevas

### UmlClassCanvasAdapter

Responsable de traducir el documento UML al contrato del canvas común:

```text
- nodes()
- connectors()
- layoutForNode()
- layoutForConnector()
- selección de módulo/clase/relación
- movimiento de módulo/clase
- puntos intermedios de relaciones
```

No renderiza JavaFX y no conoce CSS.

### UmlClassRenderKit

Responsable únicamente de la presentación JavaFX de UML Clases dentro del canvas común:

```text
- módulo como caja agrupadora sobria
- clase/interfaz/enum como caja UML con miembros visibles
- relación como polyline con flecha/etiqueta sobria
```

No modifica el documento ni persiste layout.

### UmlClassDiagramViewModel

Sigue siendo el ViewModel del editor, pero ahora también expone operaciones de layout:

```text
layoutForModule()
layoutForClass()
layoutForConnector()
moveModuleTo()
moveClassTo()
addConnectorBendPoint()
moveConnectorBendPointTo()
removeConnectorBendPoint()
```

Estas operaciones usan `VisualLayoutService` y guardan en `DiagramLayouts`, no en `UmlClassNode` ni `UmlModuleGroup`.

## Anti-fachada

La migración no se limita a mostrar tarjetas. Ahora las clases y módulos son nodos del canvas común y su posición se persiste en layout visual. El PNG se toma del canvas común y el SVG sigue generándose con primitivas vectoriales desde el modelo especializado.

## Límite intencional

UML Clases no intenta ser un IDE UML completo. El alcance de esta tanda es diagrama editable, sobrio, movible y exportable para documentación/levantamiento, con panel derecho para detalles.
