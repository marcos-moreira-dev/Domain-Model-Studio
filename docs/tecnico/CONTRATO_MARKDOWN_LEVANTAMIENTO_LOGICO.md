# Contrato Markdown canónico — Levantamiento lógico

## Decisión de producto

El Markdown de Levantamiento lógico es la fuente canónica del módulo. La interfaz, los formularios, el árbol, la validación, las trazas internas y el uso como fuente para otros artefactos deben obedecer este contrato.

Flujo oficial:

```text
Plantillas oficiales descargadas desde Recursos IA
→ GPT rellena la plantilla maestra con un negocio real
→ Domain Model Studio importa el Markdown canónico
→ el usuario revisa, corrige y completa mediante controles
→ Domain Model Studio exporta nuevamente Markdown canónico
→ el usuario o una IA preparan otros Markdown compatibles reutilizando IDs canónicos
```

## Archivos oficiales

- Recurso interno: `ai-resources/official-markdown/levantamiento-logico/logical_business_intake_template.md`.
- Plantilla pública exportable: `examples/markdown/plantillas/logical_business_intake.md`.
- Ejemplos oficiales: deben usar negocios ficticios o genéricos; no deben usar el negocio personal del desarrollador.

## Secciones obligatorias

El contrato `logical-business-master-v1` define estas secciones y conserva su orden estable:

0. Portada lógica del levantamiento
1. Principio lógico central
2. Contexto observado del negocio
3. Sistema de estados del negocio
4. Vocabulario lógico del dominio
5. Predicados, proposiciones y símbolos permitidos
6. Reglas lógicas del negocio
7. Condiciones iniciales / precondiciones
8. Invariantes del negocio
9. Condiciones de cierre / postcondiciones
10. Acciones transformadoras
11. Árbol operativo de macroflujos, flujos y casos de uso
12. Catálogo único de casos de uso
13. Grafo lógico del negocio
14. Entidades candidatas
15. Estados y transiciones
16. Reportes y algoritmos internos
17. Indicadores para diseño de base de datos
18. Riesgos de inconsistencia
19. Uso como fuente para otros artefactos
20. Preguntas pendientes para el cliente
21. Nivel de madurez del levantamiento
22. Cierre del documento

## IDs permitidos

Los IDs externos del contrato pueden usar estos prefijos:

```text
RN, PRE, INV, POST, ACC, CU, MF, FL, ENT, ATR, REL, REP, RISK, PEND, ACT, EST, CON, EVID, SUP, CALC
```

El formato estable es:

```text
PREFIJO-001
PREFIJO-002
PREFIJO-003
```

Los placeholders como `RN-XXX` o `ACC-XXX` pertenecen a la plantilla y no deben crear elementos reales al importar.


## Elementos de primera clase del contrato actual

En el contrato vigente, los prefijos `SUP` y `CALC` no quedan solo como referencias externas: también pueden convertirse en elementos importables del expediente lógico cuando el Markdown los expresa con ID canónico.

Grupos principales:

- `RN`, `PRE`, `INV`, `POST`: reglas, precondiciones, invariantes y postcondiciones.
- `ACC`, `CU`, `MF`, `FL`: acciones transformadoras, casos de uso y flujos.
- `ENT`, `ATR`, `REL`: entidades, atributos y relaciones candidatas para análisis lógico de datos.
- `ACT`, `EST`, `CON`, `EVID`: actores, estados, conceptos y evidencias trazables.
- `SUP`, `CALC`: supuestos detectados y cálculos internos.
- `REP`, `RISK`, `PEND`: reportes, riesgos y preguntas pendientes.

La plantilla puede usar placeholders como `ATR-XXX` o `CALC-XXX`; esos placeholders enseñan la forma, pero no crean elementos reales al importar. Los ejemplos oficiales gorditos deben incluir IDs numéricos reales para demostrar el contrato completo.

## Estados permitidos

Estados documentales:

```text
borrador | validado parcialmente | validado | archivado
```

Estados de elementos:

```text
borrador | pendiente | pendiente de validar | validada | validado | descartada | descartado | revisar | validado parcialmente | confirmado | confirmada | bloqueante | usable como fuente
```

## Responsabilidad de alineación semántica

El Levantamiento lógico define IDs, nombres, reglas y contratos semánticos canónicos del negocio. Cuando el usuario o una IA genere otros Markdown compatibles con Domain Model Studio, debe reutilizar esos IDs y nombres para mantener consistencia entre artefactos.

Domain Model Studio no garantiza automáticamente la alineación entre proyectos independientes. Cada tipo de proyecto mantiene su propio alcance, parser, validación, edición y exportación. No es obligatorio generar todos los tipos de proyecto; el usuario decide cuáles necesita.

## Regla de importación

El parser debe aceptar la plantilla maestra completa. Si una sección está vacía, debe conservarse como sección canónica vacía. Si un campo aparece vacío o con placeholder, no se debe inventar información.

## Regla de exportación

El exportador debe emitir el orden canónico 0–22 y conservar `canonical_contract: "logical-business-master-v1"`. La salida debe ser reimportable sin perder IDs, referencias ni el orden de secciones.

## Regla sobre ejemplos

El ejemplo completo proporcionado por el usuario sirve como referencia de profundidad y estilo, pero no debe quedar como ejemplo oficial del producto si usa el negocio personal del desarrollador.


## Compatibilidad semántica de entidades candidatas

La sección 14 se emite como **Entidades candidatas**. El parser debe seguir aceptando Markdown histórico que use títulos o campos como `Entidades candidatas`, `Fuente de derivación`, `Derivada de` o `Derivado de`, pero el exportador y las plantillas oficiales deben enseñar el lenguaje nuevo: `Fuente lógica`, `Sustento lógico` o `Justificada por`.

Este cambio no altera el identificador `logical-business-master-v1`: la estructura 0–22, los prefijos y el formato de IDs se conservan. Cambia el lenguaje visible para evitar que el módulo prometa generación automática o derivación fuerte de otros proyectos.
