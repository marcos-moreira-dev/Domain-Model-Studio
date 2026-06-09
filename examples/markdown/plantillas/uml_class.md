---
dms_version: "1"
diagram_type: "uml-class"
name: "Plantilla — UML Clases"
sample_kind: "template"
domain: "general"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# UML Clases

> Salida esperada: diagrama visual.

# Paquetes

## dominio
propósito: Reglas principales del negocio.

# Clases

## EntidadPrincipal
paquete: dominio
responsabilidad: Representa una entidad del negocio.
atributos:
- id: String
- nombre: String
métodos:
- activar(): void
- desactivar(): void

# Relaciones

- EntidadPrincipal --> OtraClase: usa o referencia.

