# Gramática IA — Flujo operativo

Estado: recurso de trabajo con IA.  
Importable por la app: sí, mediante `diagram_type: "operational-flow"`.  
Editor visual implementado: sí, como diagrama operativo de pasos, decisiones y responsables.  
Uso recomendado: convertir entrevistas de operación diaria en un flujo simple y revisable, menos formal que BPMN.

## Principio

Un flujo operativo documenta **qué ocurre en la operación**, quién interviene, qué decisiones aparecen y qué evidencia queda. No pretende ser BPMN industrial completo; sirve para levantar y comunicar procesos administrativos de forma clara.

## Estructura importable sugerida

```md
---
diagram_type: "operational-flow"
name: "Flujo operativo — ejemplo"
importable: true
---
# Flujo operativo

## Recepción de solicitud
id: recepcion_solicitud
tipo: paso
responsable: Secretaría
notas: registra datos mínimos y verifica documentos.

## Verificar información
id: verificar_informacion
tipo: decisión
responsable: Secretaría
notas: si falta información, se solicita corrección.

## Cerrar atención
id: cerrar_atencion
tipo: fin
responsable: Secretaría
notas: se deja evidencia de cierre.

# Relaciones
- recepcion_solicitud -> verificar_informacion: solicitud recibida
- verificar_informacion -> cerrar_atencion: información completa
```

## Reglas de escritura

- Mantener nombres cortos y verbos operativos: registrar, verificar, aprobar, notificar, cerrar.
- Usar `tipo: paso`, `tipo: decisión`, `tipo: inicio` o `tipo: fin` cuando ayude a leer el flujo.
- Incluir responsable cuando el proceso dependa de un rol o área.
- Usar relaciones explícitas con `origen -> destino: condición o etiqueta`.
- Si el proceso requiere notación BPMN estricta, usar el tipo `bpmn-basic` en lugar de `operational-flow`.

## Diferencia con BPMN básico

- BPMN básico prioriza eventos, actividades y decisiones con una notación más formal.
- Flujo operativo prioriza lectura administrativa y pasos cotidianos del negocio.
