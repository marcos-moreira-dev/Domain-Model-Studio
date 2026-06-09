---
dms_version: "1"
diagram_type: "logical-business-intake"
name: "Óptica Horizonte — levantamiento lógico mínimo"
sample_kind: "minimal-example"
domain: "óptica"
status: "importable"
canonical_contract: "logical-business-master-v1"
importable: true
intended_output: "expediente lógico documental"
---

# Levantamiento lógico mínimo — Óptica Horizonte

> **Tipo de recurso:** ejemplo ficticio mínimo para el módulo de Levantamiento lógico.  
> **Dominio:** óptica pequeña con venta de lentes, registro de clientes, órdenes, pagos y entregas.  
> **Estado:** ejemplo importable desde la UI del módulo de Levantamiento lógico.

---

## 0. Portada lógica del levantamiento

- **Negocio:** Óptica Horizonte
- **Proyecto / sistema:** Sistema administrativo para órdenes ópticas, pagos y entregas
- **Versión del levantamiento:** `v0.1`
- **Fecha:** `<AAAA-MM-DD>`
- **Responsable del levantamiento:** `<analista>`
- **Estado del documento:** `borrador`
- **Fuente principal:** `entrevista | observación | inferencia`

### Propósito específico del levantamiento

```text
Entender el flujo mínimo de una óptica pequeña: registrar clientes, registrar fórmulas visuales, crear órdenes de lentes, controlar anticipos, confirmar pagos, gestionar entregas y conservar evidencia básica.
```

### Indicadores iniciales

- [x] El negocio está identificado.
- [x] El problema principal está descrito.
- [x] El alcance inicial está delimitado.
- [ ] Las dudas importantes están registradas.
- [ ] El documento indica qué reglas fueron validadas por el cliente.

---

## 1. Principio lógico central

La óptica puede entenderse como un sistema de estados transformado por acciones. Una orden óptica inicia como solicitud del cliente, se arma con una fórmula visual y productos seleccionados, recibe pagos parciales o totales, pasa por preparación y termina entregada o anulada.

```text
cliente identificado
→ fórmula visual registrada
→ orden óptica creada
→ anticipo registrado
→ orden en preparación
→ orden lista
→ pago completado
→ orden entregada
```

La regla central es que una orden no debe avanzar a entrega si no existe evidencia suficiente de pago, productos seleccionados y fórmula asociada cuando aplique.

---

## 2. Contexto observado del negocio

### 2.1 Descripción general

Óptica Horizonte atiende clientes que necesitan lentes, monturas, lunas, reparación menor o reposición de piezas. El negocio trabaja con atención presencial, registro de datos del cliente, fórmula visual, selección de productos, anticipo, saldo pendiente y entrega posterior.

### 2.2 Problemas actuales observados

- Las órdenes pueden quedar anotadas en cuadernos o chats sin trazabilidad uniforme.
- Puede confundirse anticipo con pago total.
- Puede perderse la relación entre fórmula, cliente y orden.
- Puede no quedar claro si una orden está pendiente, lista, entregada o anulada.

### 2.3 Objetivos esperados del sistema

- Registrar clientes y fórmulas visuales.
- Crear órdenes ópticas con productos, montos y estados.
- Controlar anticipos, saldos y pagos.
- Registrar entrega y evidencia.
- Consultar órdenes pendientes, listas y entregadas.

### 2.4 Supuestos detectados

| ID | Supuesto | Fuente | Estado |
|---|---|---|---|
| SUP-001 | La fórmula visual puede venir de receta externa, pero debe quedar asociada a la orden. | entrevista | pendiente |

### 2.5 Preguntas pendientes del contexto

| ID | Pregunta | Afecta | Prioridad | Estado |
|---|---|---|---|---|
| PEND-001 | ¿La fórmula visual siempre debe registrarse dentro de la óptica o puede venir de receta externa? | fórmula / orden | alta | pendiente |
| PEND-002 | ¿Qué porcentaje mínimo de anticipo exige el negocio para iniciar preparación? | pagos / estados | alta | pendiente |

---

## 3. Sistema de estados del negocio

### 3.1 Estados generales observables

- Cliente identificado.
- Fórmula visual registrada.
- Orden creada.
- Orden con anticipo.
- Orden en preparación.
- Orden lista para entrega.
- Orden entregada.
- Orden anulada.

### 3.2 Estados inválidos prohibidos

- Orden entregada sin pago completo.
- Pago recibido con monto menor o igual a cero.
- Orden óptica sin cliente.
- Orden que requiere fórmula pero no tiene fórmula asociada.
- Entrega sin fecha ni responsable.

---

## 4. Vocabulario lógico del dominio

### Concepto: Cliente

- Tipo lógico: entidad.
- Significado: persona que solicita productos o servicios ópticos.
- Notación sugerida: `Cliente(c)`.
- Participa en: órdenes, fórmulas, pagos y entregas.

### Concepto: Fórmula visual

- Tipo lógico: documento / estado clínico-operativo.
- Significado: datos de graduación o prescripción usados para preparar lentes.
- Notación sugerida: `FormulaVisual(f)`.
- Participa en: órdenes ópticas y validaciones de preparación.

### Concepto: Orden óptica

- Tipo lógico: entidad operativa.
- Significado: solicitud concreta de productos o servicios ópticos para un cliente.
- Notación sugerida: `OrdenOptica(o)`.
- Participa en: productos, pagos, estados y entrega.

---

## 5. Predicados principales

| Predicado | Lectura | Tipo |
|---|---|---|
| `Cliente(c)` | c es cliente | entidad |
| `FormulaVisual(f)` | f es fórmula visual | documento |
| `OrdenOptica(o)` | o es orden óptica | entidad operativa |
| `PerteneceA(o,c)` | la orden o pertenece al cliente c | relación |
| `RequiereFormula(o)` | la orden requiere fórmula visual | estado/regla |
| `TieneFormula(o,f)` | la orden tiene fórmula asociada | relación |
| `PagoCompleto(o)` | la orden tiene pago completo | estado |
| `Entregada(o)` | la orden fue entregada | estado |

---

## 6. Reglas lógicas del negocio

### RN-001 — Toda orden óptica debe pertenecer a un cliente

- Tipo: existencia.
- Fuente: inferencia operativa.
- Estado: pendiente de validar.
- Descripción humana: una orden no debe existir sin cliente asociado.
- Forma lógica:

```text
∀o (OrdenOptica(o) → ∃c (Cliente(c) ∧ PerteneceA(o,c)))
```

- Lectura de la forma lógica: para toda orden óptica debe existir un cliente al que pertenece.
- Razón de negocio: evita órdenes huérfanas imposibles de entregar o cobrar.
- Impacto técnico futuro:
  - Base de datos: orden referencia cliente obligatorio.
  - Backend: rechazar creación de orden sin cliente.
  - Frontend: seleccionar o registrar cliente antes de crear orden.
  - Pruebas: intentar crear orden sin cliente debe fallar.

### RN-002 — Una orden entregada debe tener pago completo

- Tipo: estado / prohibición.
- Fuente: criterio de negocio.
- Estado: pendiente de validar.
- Descripción humana: la óptica no debe marcar una orden como entregada si todavía mantiene saldo pendiente.
- Forma lógica:

```text
∀o (OrdenOptica(o) ∧ Entregada(o) → PagoCompleto(o))
```

- Lectura de la forma lógica: si una orden óptica está entregada, entonces debe tener pago completo.
- Razón de negocio: protege caja, saldos y trazabilidad de entrega.
- Impacto técnico futuro:
  - Backend: bloquear entrega si saldo pendiente es mayor a cero.
  - Frontend: mostrar advertencia de saldo antes de entregar.
  - Pruebas: entrega con saldo pendiente debe fallar.

---

## 7. Precondiciones

### PRE-001 — Cliente identificado antes de crear orden

- Aplica a: `ACC-001`.
- Descripción humana: antes de crear una orden óptica debe existir un cliente identificable.
- Forma lógica:

```text
CrearOrden(o) → ∃c Cliente(c)
```

- Si no se cumple: registrar cliente o detener creación de orden.
- Validación esperada: frontend y backend.

---

## 8. Invariantes del negocio

### INV-001 — No entregar órdenes con saldo pendiente

- Ámbito: flujo de entrega.
- Descripción humana: durante el cierre de una orden, no debe permitirse que una orden quede entregada con saldo pendiente.
- Forma lógica:

```text
∀o (Entregada(o) → PagoCompleto(o))
```

- Debe mantenerse: al confirmar entrega.
- Acciones que pueden romperla:
  - ACC-003 — Registrar entrega.
- Reglas relacionadas:
  - RN-002.
- Riesgo si se rompe: pérdida financiera o discusión con el cliente.
- Validación técnica futura: backend, pruebas y auditoría.

---

## 9. Postcondiciones

### POST-001 — Orden creada con estado inicial válido

- Aplica a: `ACC-001`.
- Descripción humana: al terminar la creación, la orden debe quedar asociada a cliente y en estado inicial controlado.
- Evidencia: registro de orden.
- Validación esperada: existencia de orden, cliente asociado y estado inicial.

---

## 10. Acciones transformadoras

### ACC-001 — Crear orden óptica

- Tipo: creación.
- Actor principal: asesor de óptica.
- Estado inicial: cliente solicita producto o servicio óptico.
- Condiciones iniciales:
  - PRE-001.
- Transformación:
  1. Identificar o registrar cliente.
  2. Registrar datos básicos de la orden.
  3. Asociar fórmula visual si aplica.
  4. Registrar productos o servicios solicitados.
  5. Guardar orden en estado inicial.
- Invariantes protegidas:
  - INV-001.
- Estado de cierre: orden óptica creada y pendiente de preparación o pago.
- Postcondiciones:
  - POST-001.
- Reglas aplicadas:
  - RN-001.
- Evidencia: registro de orden.

---

## 11. Árbol operativo mínimo

### MF-001 — Gestión de órdenes ópticas

- Objetivo: controlar creación, preparación, cobro y entrega de órdenes.
- Inicio general: cliente solicita lentes, montura, luna o servicio.
- Cierre general: orden entregada, anulada o pendiente con estado trazable.

#### FL-001 — Crear y preparar orden

- Usa `ACC-001` — Crear orden óptica.

#### FL-002 — Cobrar y entregar orden

- Usa `ACC-002` — Registrar pago.
- Usa `ACC-003` — Registrar entrega.

---

## 14. Entidades candidatas

### ENT-001 — Cliente

- Estado: pendiente de validar.
- Justificación lógica: RN-001 exige que toda orden pertenezca a un cliente identificable.
- Fuente lógica: RN-001.
- Acciones que la crean:
  - ACC-001.
- Riesgo si se modela mal: las órdenes quedarían sin responsable operativo.

### ENT-002 — OrdenOptica

- Estado: pendiente de validar.
- Justificación lógica: ACC-001 crea el expediente operativo central de la atención.
- Fuente lógica: ACC-001.
- Reglas asociadas:
  - RN-001.
  - RN-002.
- Riesgo si se modela mal: pagos, fórmula y entrega no tendrían trazabilidad común.

### Atributos candidatos

### ATR-001 — Cliente.nombre

- Pertenece a: ENT-001.
- Tipo tentativo: texto.
- Razón operativa: permite identificar al cliente durante la atención.
- ¿Es calculado?: no.
- Fuente lógica: RN-001.

### ATR-002 — OrdenOptica.estado

- Pertenece a: ENT-002.
- Tipo tentativo: catálogo.
- Razón operativa: permite saber si la orden está creada, en preparación, lista, entregada o anulada.
- ¿Es calculado?: no.
- Fuente lógica: INV-001.
- Reglas asociadas:
  - RN-002.

### Relaciones candidatas

### REL-001 — Cliente — OrdenOptica

- Entidad origen: ENT-001.
- Entidad destino: ENT-002.
- Cardinalidad tentativa: 1 a muchas.
- Justificación lógica: RN-001 obliga que toda orden óptica pertenezca a un cliente.
- Fuente lógica: RN-001.

---

## 16. Reportes y algoritmos internos

### CALC-001 — Saldo pendiente de orden

- Descripción humana: cálculo operativo que compara el total de la orden contra pagos registrados.
- Fórmula: total_orden - suma(pagos_recibidos).
- Lectura: si el saldo pendiente es cero, la orden puede evaluarse para entrega.
- Datos necesarios: total de orden y pagos asociados.
- Entidades involucradas: ENT-002.
- Reglas relacionadas: RN-002.
- Riesgo si se calcula mal: entrega con deuda o cobro incorrecto.
- Validación esperada: revisar saldo antes de confirmar entrega.

---

## 20. Preguntas pendientes consolidadas

| ID | Pregunta | Bloquea | Prioridad | Estado |
|---|---|---|---|---|
| PEND-001 | ¿La fórmula visual puede venir de receta externa? | reglas de fórmula | alta | pendiente |
| PEND-002 | ¿Cuál es el anticipo mínimo para iniciar preparación? | pagos / estados | alta | pendiente |

---

## 21. Nivel de madurez del levantamiento

- **Contexto:** parcial.
- **Reglas:** borrador.
- **Invariantes:** iniciales.
- **Acciones:** mínimas.
- **Uso como fuente para otros artefactos:** no lista.
- **Cierre:** requiere validación con cliente.
