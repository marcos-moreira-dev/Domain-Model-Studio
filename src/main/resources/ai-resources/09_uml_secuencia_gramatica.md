# Gramática IA — UML Secuencia

Estado: recurso de apoyo para IA y ejemplos oficiales.  
Importable por la app: sí.  
Editor visual implementado: sí, mediante el editor común de comportamiento.  
Uso recomendado: describir interacción temporal entre usuario, UI, backend, base de datos o servicios.

## Advertencia

Domain Model Studio importa y renderiza una versión básica de secuencia. Este formato no debe reemplazar pruebas ni contratos API.


## Estructura sugerida

```md
# UML Secuencia: <escenario>

## Participantes
- Actor: <usuario o sistema externo>
- UI: <pantalla o cliente>
- Servicio: <servicio/backend>
- Repositorio: <repositorio si aplica>
- Base de datos: <base>

## Mensajes
1. <Actor> -> <UI>: <acción>
2. <UI> -> <Servicio>: <solicitud>
3. <Servicio> -> <Repositorio>: <consulta/comando>
4. <Repositorio> -> <Base de datos>: <operación>
5. <Base de datos> --> <Repositorio>: <resultado>
6. <Servicio> --> <UI>: <respuesta>
7. <UI> --> <Actor>: <resultado visible>

## Fragmentos combinados
- fragmento: alt | id: alt-validacion | titulo: Alternativa de validación | guarda: [dato válido] | rango: 2..5 | operandos: [dato válido] 2..4; [dato inválido] 5..5
- fragmento: opt | id: opt-notificacion | titulo: Notificación opcional | guarda: [usuario acepta notificación] | rango: 6..6 | operandos: [usuario acepta notificación] 6..6
- fragmento: loop | id: loop-items | titulo: Repetir por cada ítem | guarda: [quedan ítems] | rango: 3..7 | operandos: [siguiente ítem] 3..7
- fragmento: par | id: par-tareas | titulo: Tareas paralelas | rango: 4..8 | operandos: [tarea A] 4..5; [tarea B] 6..8
- fragmento: break | id: break-error | titulo: Corte por error crítico | guarda: [error crítico] | rango: 7..7
- fragmento: critical | id: critical-guardar | titulo: Guardado atómico | rango: 8..9
- fragmento: ref | id: ref-auditoria | titulo: Registrar auditoría | referencia: Secuencia registrar auditoría | rango: 10..10

## Alternativas
- Si <condición>: <mensajes alternos>
```

## Reglas

- Ordenar mensajes según el tiempo.
- Distinguir solicitud de respuesta.
- Usar fragmentos `alt`, `opt`, `loop`, `par`, `break`, `critical` y `ref` solo cuando ayuden a leer el escenario.
- Declarar guardas como `[condición]` y rangos como `rango: 2..5` cuando el fragmento cubra mensajes concretos.
- No meter todos los detalles internos si no aportan a la comprensión.
