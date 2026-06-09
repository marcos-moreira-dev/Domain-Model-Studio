# Gramática Markdown — UML Clases

Estado: recurso oficial importable.  
Importable por la app: sí.  
Salida esperada: diagrama visual UML Clases con agrupadores por paquete/módulo, clases, atributos, métodos y relaciones.

## Encabezado obligatorio

```md
---
dms_version: "1"
diagram_type: "uml-class"
name: "UML Clases — <sistema>"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
```

## Paquetes o módulos

```md
# Paquetes

## dominio.ventas
propósito: reglas de pedidos y ventas.

## infraestructura.persistencia
propósito: acceso a datos y repositorios.
```

## Clases

```md
# Clases

## Pedido
paquete: dominio.ventas
tipo: Clase
visibilidad: public
responsabilidad: agrupa ítems solicitados por el cliente.
atributos:
- id: String
- estado: EstadoPedido
métodos:
- agregarItem(producto, cantidad): void
- cerrar(): void
```

Campos admitidos por clase:

```md
id: <id opcional>
paquete: <id, nombre o ruta de paquete>
tipo: Clase | Interfaz | Enum | Clase abstracta | Servicio | Controlador | Repositorio | DTO
visibilidad: public | protected | private | package | sin especificar
responsabilidad: <texto>
descripción: <texto>
notas: <texto>
atributos:
- <nombre>: <tipo>
métodos:
- <nombre>(<parámetros>): <retorno>
```

## Relaciones

```md
# Relaciones

- Pedido *-- ItemPedido: contiene
- ItemPedido --> Producto: referencia
- ControladorPedido --> PedidoService: usa
- ServicioBase <|-- PedidoService: hereda
- PedidoRepository <|.. RepositorioPedido: implementa
```

Conectores admitidos:

- `-->` o `->`: dependencia.
- `--`: asociación.
- `*--`: composición.
- `o--`: agregación.
- `<|--`: herencia.
- `<|..`: implementación.

## Reglas de uso con IA

- Generar primero paquetes/módulos y luego clases.
- Mantener una responsabilidad clara por clase.
- No convertir automáticamente todas las tablas en clases.
- No meter detalles de framework si no aportan al diseño.
- Usar nombres estables y legibles porque luego se exportan y se corrigen manualmente.
