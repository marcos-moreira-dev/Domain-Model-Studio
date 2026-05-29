# IMP-13 — Roles/permisos y diccionario como editores estructurados

## Decisión técnica

No todo tipo de proyecto debe convertirse en `InteractiveDiagramCanvasView`.

| Tipo | Tratamiento correcto | Exportación real |
|---|---|---|
| Roles/permisos | Matriz estructurada rol × permiso | SVG vectorial, PNG de matriz, Markdown |
| Diccionario de datos | Documento/tabla técnica | PDF, Markdown |

## Roles/permisos

La matriz es una salida visual válida, pero no es canvas libre. Por eso la implementación queda separada:

```text
presentation/rolespermissions/RolesPermissionsStructurePanel.java
presentation/rolespermissions/RolesPermissionsEditorView.java
infrastructure/svg/rolespermissions/RolesPermissionsMatrixSvgExporter.java
```

### Responsabilidades

```text
RolesPermissionsStructurePanel
- navegación izquierda por roles y permisos;
- sincronización con selectedRole/selectedPermission;
- no contiene botones de creación ni exportación.

RolesPermissionsEditorView
- contenedor del workspace;
- render de la matriz JavaFX;
- snapshot PNG de la matriz;
- formularios de propiedades.

RolesPermissionsMatrixSvgExporter
- exporta matriz vectorial real;
- no usa JavaFX;
- no usa layout de nodos ni conectores;
- no incrusta imágenes raster.
```

## Protección contra salida engañosa

`MultiNotationSvgDiagramExporter` ahora detecta `ROLES_PERMISSIONS_MAP` y delega en `RolesPermissionsMatrixSvgExporter`.

`SpecializedSvgModelFactory` rechaza `ROLES_PERMISSIONS_MAP` para que el exportador visual genérico no lo represente accidentalmente como grafo de roles/permisos.

## Diccionario de datos

El diccionario conserva su criterio de producto:

```text
Salida principal: documento profesional.
Formatos: PDF + Markdown.
No canvas libre.
No SVG/PNG activado.
```

Esto evita transformar un documento técnico en una imagen que parezca diagrama sin aportar edición real.

## Smoke técnico ejecutado

```text
javac --release 21 -encoding UTF-8 \
  domain + application/export + application/visual + infrastructure/svg

SmokeRolesSvg:
- crea un proyecto roles-permissions-map;
- agrega rol, permiso y asignación;
- exporta mediante MultiNotationSvgDiagramExporter;
- confirma roles-permissions-matrix;
- confirma texto real;
- rechaza <image> y data:image.
```
