# Gramática IA — UML Actividad

Estado: recurso de apoyo para IA y ejemplos oficiales.  
Importable por la app: sí.  
Editor visual implementado: sí, mediante el editor común de comportamiento.  
Uso recomendado: describir flujo de acciones de un caso de uso, operación o módulo.

## Advertencia

Domain Model Studio importa y renderiza una versión básica para levantamiento de información. Usar este formato como documentación guiada, no como motor de ejecución.


## Estructura sugerida

```md
# UML Actividad: <nombre del flujo>

## Contexto
- Módulo: <módulo>
- Disparador: <qué inicia el flujo>
- Resultado: <qué se obtiene>

## Flujo
1. Inicio
2. Acción: <verbo + objeto>
3. Decisión: <pregunta>
   - Sí: <acción siguiente>
   - No: <acción siguiente>
4. Acción: <verbo + objeto>
5. Fin

## Carriles opcionales
- carril: <Rol, área o sistema>
- carril: <Otro responsable>
```

## Reglas

- Usar acciones concretas, no nombres vagos.
- No confundir actividad con BPMN cuando se quiera modelar proceso organizacional completo.
- Usar decisiones solo cuando cambien el flujo.
- Si participan varios responsables, declarar carriles con `- carril: <responsable>` para que el editor los trate como nodos de responsabilidad, no como acciones.
