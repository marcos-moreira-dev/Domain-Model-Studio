# 29 - Formato de proyecto `.dms`

## 1. Propósito

El archivo `.dms` es el formato editable propio de Domain Model Studio.

No reemplaza al Markdown de entrada.

```txt
Markdown (.md)
  Entrada semántica humana/IA

Domain Model Studio Project (.dms)
  Proyecto editable serializado

SVG/PNG
  Salidas documentales
```

El `.dms` debe guardar lo que el Markdown no guarda cómodamente:

- layout visual;
- posiciones corregidas manualmente;
- rutas de líneas;
- estilos personalizados;
- notación activa;
- metadatos de trazabilidad;
- configuraciones de exportación.

## 2. Principio central

El `.dms` guarda cuatro dimensiones separadas:

```txt
1. project    -> identidad del archivo
2. model      -> semántica del dominio
3. layouts    -> posiciones visuales por notación
4. styles     -> colores, bordes, fuentes y reglas visuales
5. metadata   -> trazabilidad humana
```

## 3. Estructura de alto nivel

```json
{
  "schemaVersion": "0.1.0",
  "project": {},
  "source": {},
  "notations": {},
  "model": {},
  "layouts": {},
  "styles": {},
  "exportProfiles": {},
  "metadata": {}
}
```

## 4. `schemaVersion`

Indica la versión del formato `.dms`, no la versión del proyecto del usuario.

Ejemplo:

```json
"schemaVersion": "0.1.0"
```

Debe existir desde el inicio para permitir migraciones futuras del propio formato de archivo.

## 5. `project`

Identidad del proyecto.

```json
"project": {
  "id": "supermercado_v1",
  "title": "Modelo conceptual - Supermercado",
  "description": "Modelo de dominio para levantamiento inicial de supermercado.",
  "version": "1.0.0",
  "status": "draft"
}
```

Campos sugeridos:

```txt
id           -> identificador estable
name/title   -> nombre visible
description  -> contexto humano
version      -> versión del modelo del usuario
status       -> draft, reviewed, approved, deprecated
```

## 6. `source`

Referencia al Markdown de entrada.

```json
"source": {
  "type": "markdown",
  "path": "examples/markdown/supermercado_multi_notacion.md",
  "importedAt": "2026-05-10T03:55:00Z",
  "contentHash": "pending-hash"
}
```

El `contentHash` permite saber si el Markdown original cambió desde la última importación.

No es obligatorio calcularlo en el MVP, pero el campo queda previsto.

## 7. `notations`

Configuración de notaciones.

```json
"notations": {
  "active": "CHEN",
  "available": ["CHEN", "CROWS_FOOT"],
  "default": "CHEN"
}
```

Regla:

```txt
active   -> notación actualmente visible
default  -> notación preferida al abrir el archivo
available -> notaciones que el proyecto reconoce
```

## 8. `model`

Modelo semántico neutral.

Debe representar el dominio sin depender de JavaFX ni de una notación visual concreta.

```json
"model": {
  "entities": [],
  "relationships": [],
  "notes": [],
  "rules": []
}
```

## 9. `layouts`

Layouts por notación.

```json
"layouts": {
  "CHEN": {},
  "CROWS_FOOT": {}
}
```

Cada notación puede necesitar posiciones diferentes.

Chen necesita posiciones para:

```txt
entidades
atributos
rombos de relación
etiquetas de cardinalidad
conectores
```

Crow's Foot necesita posiciones para:

```txt
cajas de entidad
compartimentos de atributos
líneas directas
marcadores de cardinalidad
```

## 10. `styles`

Estilos visuales.

```json
"styles": {
  "theme": "classic-light",
  "defaultElementStyles": {},
  "overrides": {}
}
```

Debe permitir:

- color de fondo;
- color de borde;
- color de texto;
- fuente;
- tamaño de fuente;
- grosor de línea;
- patrón de línea;
- visibilidad.

## 11. `exportProfiles`

Opciones de exportación.

```json
"exportProfiles": {
  "svg": {
    "background": "white",
    "includeMetadata": true,
    "margin": 40
  },
  "png": {
    "scale": 2.0,
    "background": "white",
    "margin": 40
  }
}
```

## 12. `metadata`

Trazas internas humana.

```json
"metadata": {
  "createdAt": "2026-05-10T03:55:00Z",
  "updatedAt": "2026-05-10T03:55:00Z",
  "createdBy": "@programalobien",
  "migrationRef": "V1__initial_schema",
  "meetingRef": "levantamiento_reunion_01",
  "notes": [
    "Modelo generado desde documentación de levantamiento.",
    "Crow's Foot queda previsto como vista futura."
  ]
}
```

## 13. Regla de compatibilidad

El parser debe tolerar campos desconocidos de versiones futuras, siempre que no rompan la estructura mínima.

Esto evita que un archivo `.dms` quede inutilizable por agregar metadatos nuevos.

## 14. No mezclar con SQL

El `.dms` no debe ser un archivo SQL ni un ORM.

Puede guardar referencias como:

```txt
migrationRef
schemaRef
physicalModelRef
```

Pero no debe intentar reemplazar el diseño físico de base de datos.

## 15. Criterio de aceptación

Un `.dms` válido debe permitir:

```txt
abrir proyecto;
mostrar nombre y estado;
reconstruir modelo semántico;
reconstruir layout de la notación activa;
reconstruir estilos;
exportar SVG/PNG;
conservar trazabilidad.
```
