# Gramática IA — Flujo de pantallas

Estado: recurso oficial para generar Markdown importable.  
Importable por la app: sí.  
Salida esperada: diagrama visual de pantallas conectadas por navegación.

## Encabezado obligatorio

```md
---
dms_version: "1"
diagram_type: "screen-flow"
name: "Flujo de pantallas — <sistema o módulo>"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
```

## Estructura importable

```md
# Pantallas

## Inicio
id: inicio
tipo: Panel
módulo: General
ruta: /inicio
propósito: Punto de entrada del usuario.

## Lista de ventas
id: lista_ventas
tipo: Listado
módulo: Ventas
ruta: /ventas
propósito: Buscar y revisar ventas registradas.

# Navegación

- inicio -> lista_ventas: abrir módulo ventas.
- lista_ventas -> inicio: volver al inicio.
```

## Reglas

- Cada pantalla se declara con `## Nombre de pantalla` dentro de `# Pantallas`.
- `id` es obligatorio si quieres nombres estables; si se omite, la app genera uno desde el nombre.
- `tipo`, `módulo`, `ruta`, `propósito` y `notas` son opcionales.
- La sección `# Navegación` usa líneas con `origen -> destino: acción`.
- Origen y destino pueden referirse al `id` o al nombre visible de la pantalla.
- No mezclar navegación visible con lógica interna de backend.
