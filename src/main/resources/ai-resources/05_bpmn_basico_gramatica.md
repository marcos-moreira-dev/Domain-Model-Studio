# Gramática IA — BPMN básico

Estado: recurso de apoyo para IA y ejemplos oficiales.  
Importable por la app: sí.  
Editor visual implementado: sí, mediante el editor común de comportamiento.  
Uso recomendado: describir procesos de negocio simples antes de diseñar pantallas o backend.

## Advertencia

Este recurso usa una versión básica de BPMN. No representa todo el estándar BPMN ni debe inventar símbolos fuera de los elementos listados.


## Estructura sugerida

```md
# BPMN básico: <nombre del proceso>

## Propósito
<qué proceso de negocio representa>

## Participantes
- Pool: <organización o sistema>
- Lane: <rol o área>

## Flujo
1. Evento de inicio: <condición que inicia>
2. Tarea: <acción concreta>
   - Responsable: <lane/rol>
   - Entrada: <dato/documento>
   - Salida: <dato/documento>
3. Gateway: <pregunta de decisión>
   - Opción sí: <siguiente paso>
   - Opción no: <siguiente paso>
4. Evento de fin: <resultado final>
```

## Elementos permitidos

- Evento de inicio
- Tarea
- Gateway exclusivo básico
- Evento de fin
- Pool
- Lane
- Flujo de secuencia

## Reglas

- Cada tarea debe tener verbo y objeto.
- Cada gateway debe formular una decisión clara.
- No usar BPMN para estructura de datos.
- No usar BPMN para clases, APIs ni deployment.
