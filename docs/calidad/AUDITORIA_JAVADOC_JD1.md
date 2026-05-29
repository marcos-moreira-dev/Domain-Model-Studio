# Auditoría JavaDoc — JD-1 dominio y onboarding

## Resumen

La primera tanda JavaDoc reforzó documentación de contratos en dominio y agregó una guía de lectura del código para onboarding.

## Cobertura cualitativa agregada

| Área | Estado JD-1 |
|---|---|
| `domain/logicalbusinessgraph` | Contrato principal documentado |
| `domain/logicalbusiness` | Contrato principal documentado |
| `domain/diagram` | Frontera del agregado general documentada |
| `domain/layout` | Contrato de layout persistible documentado |
| `domain/style` | Contrato de estilo persistible documentado |
| Onboarding de código | Guía nueva creada |

## Criterio de calidad

Esta tanda prioriza JavaDoc útil para estudiar diseño:

```txt
- por qué existe una clase;
- qué invariantes protege;
- qué no debe depender de qué;
- qué errores se rechazan;
- cómo leer el contrato desde una capa superior.
```

## No objetivo de JD-1

```txt
- No cubrir todos los métodos públicos del sistema.
- No documentar getters triviales.
- No tocar código funcional.
- No modificar pantalla de inicio ni modelo conceptual.
```

## Próximo foco

La siguiente tanda recomendada es JD-2: casos de uso y servicios de aplicación.
