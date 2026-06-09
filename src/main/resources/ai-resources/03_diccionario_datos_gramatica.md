# Gramática IA — Diccionario de datos

Estado: recurso de trabajo con IA y referencia documental productizada.  
Importable por la app: sí, mediante `diagram_type: "data-dictionary"`.  
Salida documental disponible: sí, como Markdown y PDF de diccionario de datos.  
Uso recomendado: generar un borrador documental de entidades, campos, tipos lógicos y reglas para revisar en Domain Model Studio.

## Advertencia

Domain Model Studio importa diccionarios de datos estructurados. Úsalo para pedirle a una IA que genere un diccionario consistente, revisable por humanos y útil para aplicaciones administrativas. Si el documento no respeta `diagram_type: "data-dictionary"` y la estructura de entidades/campos, trátalo como borrador que debe corregirse antes de importar.

## Estructura sugerida

```md
# Diccionario de datos: <nombre del sistema>

## Proyecto
- Cliente: <cliente>
- Sistema: <sistema>
- Versión del diccionario: <versión>
- Fecha: <aaaa-mm-dd>
- Estado: borrador | revisado | aprobado

## Entidad: <NombreEntidad>
- Descripción: <qué representa en el negocio>
- Módulo: <módulo funcional>
- Fuente: modelo conceptual | entrevista | migración | revisión manual

### Campo: <nombre_campo>
- Etiqueta visible: <texto para usuario final>
- Tipo lógico: texto corto | texto largo | entero | decimal | moneda | fecha | fecha-hora | booleano | estado | identificador | correo | teléfono
- Tipo físico sugerido: <opcional, ejemplo VARCHAR(120)>
- Obligatorio: sí | no
- Único: sí | no
- Clave primaria: sí | no
- Clave foránea: sí | no
- Referencia: <Entidad.campo si aplica>
- Validaciones: <reglas concretas>
- Regla de negocio: <significado operacional>
- Ejemplo: <valor ejemplo>
- Observaciones: <nota humana>
```

## Reglas

- No convertir este archivo en SQL PostgreSQL completo.
- Usar tipos lógicos primero y tipos físicos solo como sugerencia.
- Documentar significado de negocio, no solo estructura técnica.
- Si una normalización revela una entidad nueva, revisar también el modelo conceptual.
