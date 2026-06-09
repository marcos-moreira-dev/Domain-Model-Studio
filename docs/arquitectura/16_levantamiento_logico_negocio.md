# Arquitectura — Levantamiento lógico de negocio

Estado: **vigente después de Tanda 27**

Este documento resume la arquitectura actual del tipo `logical-business-intake`.

## Identidad

**Levantamiento lógico no es un diagrama.** Es un proyecto documental estructurado. Su salida principal es un expediente navegable, validable y exportable en Markdown.

El módulo actúa como **fuente lógica canónica** del negocio: conserva IDs, nombres, reglas, precondiciones, invariantes, postcondiciones, acciones, estados, entidades candidatas, reportes, riesgos, supuestos y preguntas pendientes.

## Capas

- `domain/logicalbusiness`: dominio puro e inmutable, sin JavaFX.
- `application/logicalbusiness`: validación, madurez documental y trazas internas.
- `infrastructure/markdown/logicalbusiness`: parser/exporter del contrato `logical-business-master-v1`.
- `presentation/logicalbusiness`: workspace documental, SideDock y formularios.

Un ViewModel no debe parsear Markdown. El modelo conceptual no debe recibir dependencias del Levantamiento lógico.

## UX

El centro es documental. No se recomienda panel derecho fijo porque el expediente necesita espacio para lectura y edición amplia.

El SideDock modular izquierdo concentra:

- Estructura
- Propiedades
- Elementos lógicos
- Entidades y relaciones
- Validación
- Trazas internas
- Ayuda y glosario

## Relación con otros artefactos

Otros tipos de proyecto pueden reutilizar IDs, nombres y reglas del levantamiento como **artefactos compatibles revisables**, pero cada tipo mantiene su propia gramática, validación y alcance.

No hay sincronización automática entre proyectos. La alineación semántica corresponde al usuario y a la IA que genere o revise Markdown posterior.

## Fronteras

- No debe reutilizar `GRAPH` como dominio final.
- No debe contaminar el modelo conceptual.
- No debe prometer generación automática universal.
- No debe tratar entidades candidatas como tablas físicas aprobadas.
- No debe presentar artefactos compatibles como módulo principal visible.
