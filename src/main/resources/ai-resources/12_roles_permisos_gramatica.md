# Gramática Markdown — Roles y permisos

Estado: importable por Domain Model Studio.  
Salida esperada: matriz visual rol × permiso, exportable como PNG y Markdown.

## Encabezado obligatorio

```yaml
---
dms_version: "1"
diagram_type: "roles-permissions-map"
name: "Roles y permisos — nombre del sistema"
status: "importable"
importable: true
intended_output: "matriz visual"
---
```

## Estructura importable

```md
# Roles

## Administrador
id: administrador
propósito: controla configuración, usuarios y reportes.

## Vendedor
id: vendedor
propósito: registra ventas y atiende clientes.

# Permisos

- clientes_leer: consultar clientes.
- clientes_editar: crear o actualizar clientes.
- ventas_crear: registrar ventas.

# Asignaciones

| Rol | Permiso | Decisión | Alcance | Observación |
|---|---|---|---|---|
| administrador | clientes_leer | Permitido | global | Acceso completo. |
| vendedor | ventas_crear | Permitido | ventas | No configura usuarios. |
```

## Reglas

- `diagram_type` debe ser `roles-permissions-map`.
- Cada rol se declara con `## Nombre del rol` dentro de `# Roles`.
- Cada rol debe tener `id` estable en minúsculas, sin espacios.
- Cada permiso se declara en lista con `permiso_id: descripción` dentro de `# Permisos`.
- Cada asignación debe usar IDs existentes de rol y permiso.
- La columna `Decisión` acepta `Permitido`, `Condicionado` o `Denegado`.
- La columna `Alcance` documenta ámbito operativo y no convierte la celda en condicionada.
- Para permisos realmente condicionados, usa `Decisión = Condicionado` y describe la restricción en la observación o en una columna `Condición`.
- La columna `Observación` se conserva como nota humana para revisión.
