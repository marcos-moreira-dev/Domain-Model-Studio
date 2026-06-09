# Tipo de proyecto — Levantamiento lógico

Estado: **vigente después de Tanda 27**

El **Levantamiento lógico** es un tipo de proyecto documental estructurado de Domain Model Studio. Su responsabilidad es convertir entrevistas, observaciones, reglas, estados, acciones, preguntas y evidencia del negocio en una **fuente lógica canónica** que pueda revisarse, validarse internamente, exportarse como PDF/Markdown y usarse luego como referencia para otros artefactos.

```text
Levantamiento lógico = fuente lógica canónica
Otros artefactos = artefactos compatibles revisables
Alineación = responsabilidad del usuario y la IA mediante IDs y nombres canónicos
```

## Alcance real

El módulo:

- importa Markdown `logical-business-master-v1`;
- edita el expediente como documento estructurado, no como canvas;
- agrupa elementos lógicos por familias;
- muestra entidades, atributos y relaciones como candidatos;
- valida coherencia interna;
- muestra impacto y dependencias del mismo expediente;
- ofrece ayuda y glosario obligatorio;
- exporta PDF formal del expediente completo con índice navegable, guía de códigos y Markdown canónico.

## Fuera de alcance

El módulo no debe:

- ser editor Markdown libre;
- exportar SVG/PNG como si fuera diagrama visual;
- generar automáticamente todos los proyectos;
- sincronizar proyectos externos;
- convertir entidades candidatas en tablas físicas;
- prometer importación automática de artefactos compatibles.

## SideDock vigente

Módulos visibles:

1. Estructura
2. Ficha rápida
3. Elementos lógicos
4. Entidades y relaciones
5. Validación
6. Impacto y dependencias
7. Exportar
8. Ayuda y glosario

## Contrato Markdown

El contrato técnico vigente está en:

- `docs/tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md`

Sección clave:

- `## 14. Entidades candidatas`
- `## 19. Uso como fuente para otros artefactos`

## Ejemplo oficial

El ejemplo oficial automático del selector es UENS:

- `logical_business_intake_uens_gordito.md`

Óptica queda como recurso IA/fixture histórico cuando se conserve, no como ejemplo rector automático.


## Capacidades honestas actuales

El Levantamiento lógico es una fuente lógica canónica. No genera automáticamente todos los demás proyectos, no sincroniza proyectos y expone coherencia interna, impacto y dependencias, ayuda y glosario. UENS es el ejemplo oficial rector.


## Contrato resumido de capacidades

- No debe ser editor Markdown libre
- No debe exportar SVG/PNG
- Debe exportar PDF/Markdown desde la salida activa
