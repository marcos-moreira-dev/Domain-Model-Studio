# Índice teórico de diagramas

## Propósito

Este índice reúne las fichas teóricas mínimas que Domain Model Studio debe respetar antes de implementar nuevos diagramas.

El objetivo no es reemplazar estándares completos, sino fijar reglas suficientes para que la app no invente símbolos ni mezcle conceptos.

## Estado real

El estado funcional actualizado ya no se mantiene en estas fichas históricas, sino en:

```txt
docs/estado/ESTADO_ACTUAL.md
src/main/resources/help/topics/
```

Estas fichas quedan como referencia teórica complementaria. El centro de ayuda integrado usa las fichas Markdown de `src/main/resources/help/topics/`.

La lectura viva actual para implementación y cierre por tipo queda canonizada en:

```txt
docs/planificacion/07_teoria_minima_por_tipo_diagrama.md
```

## Fichas incluidas

```txt
modelo_conceptual.md
diccionario_datos.md
bpmn_basico.md
c4_contexto_contenedores.md
uml_casos_uso.md
uml_clases.md
uml_actividad.md
uml_secuencia.md
uml_estados.md
wireframes_administrativos.md
roles_permisos.md
mapa_modulos.md
```

## Regla de lectura

Cada ficha distingue:

```txt
- propósito
- cuándo usar
- cuándo no usar
- elementos permitidos
- relaciones permitidas
- reglas visuales
- reglas semánticas
- errores comunes
- ejemplo mínimo
- relación con aplicaciones administrativas
```

## Regla de implementación futura

Ningún nuevo renderer debe implementarse sin revisar antes su ficha teórica.
