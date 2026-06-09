---
dms_version: "1"
diagram_type: "logical-business-intake"
name: "Óptica Horizonte — levantamiento lógico completo"
sample_kind: "full-example"
domain: "óptica"
status: "importable"
canonical_contract: "logical-business-master-v1"
importable: true
intended_output: "expediente lógico documental"
---

# Levantamiento lógico completo — Óptica Horizonte

> **Tipo de recurso:** ejemplo ficticio gordito para el módulo de Levantamiento lógico.  
> **Dominio:** óptica pequeña con atención presencial, fórmulas visuales, órdenes ópticas, pagos, entregas, garantías y trazabilidad.  
> **Estado:** ejemplo importable desde la UI del módulo de Levantamiento lógico.

---

## 0. Portada lógica del levantamiento

- **Negocio:** Óptica Horizonte
- **Proyecto / sistema:** Sistema administrativo para atención, órdenes ópticas, pagos, entregas, garantías y reportes
- **Versión del levantamiento:** `v0.1`
- **Fecha:** `<AAAA-MM-DD>`
- **Responsable del levantamiento:** `<analista>`
- **Estado del documento:** `borrador`
- **Fuente principal:** `entrevista | observación | inferencia`

### Propósito específico del levantamiento

```text
Convertir la operación de una óptica pequeña en una estructura lógica estable que permita entender estados, acciones, reglas, precondiciones, invariantes, postcondiciones, riesgos y evidencias. El alcance inicial cubre clientes, fórmulas visuales, órdenes ópticas, productos, pagos, entregas, anulaciones, garantías y reportes operativos básicos.
```

### Indicadores iniciales

- [x] El negocio está identificado.
- [x] El problema principal está descrito.
- [x] El alcance inicial está delimitado.
- [x] Las dudas importantes están registradas.
- [ ] El documento indica cuáles reglas fueron validadas por el cliente.
- [ ] El documento está listo para preparar artefactos compatibles sin revisión humana.

---

## 1. Principio lógico central

Este levantamiento interpreta la óptica como un sistema de estados transformado por acciones. El negocio no se entiende primero como tablas de base de datos, sino como objetos operativos que cambian de estado: cliente identificado, fórmula registrada, orden creada, orden cotizada, anticipo recibido, orden en preparación, orden lista, pago completado, entrega realizada, garantía abierta o cierre administrativo.

La lectura algorítmica es útil porque cada operación importante debe poder responder qué estado existía antes, qué condiciones iniciales hacían válida la operación, qué transformación se ejecutó, qué reglas se aplicaron, qué invariantes se protegieron y qué evidencia permite verificar el cierre.

```text
estado inicial
→ precondiciones
→ acción transformadora
→ reglas aplicadas
→ invariantes protegidas
→ postcondiciones
→ evidencia
```

### Regla rectora del ejemplo

```text
Una orden óptica no debe avanzar a un estado operativo irreversible si no conserva cliente, fórmula cuando aplique, productos solicitados, montos, pagos, estado y evidencia suficiente.
```

### Lectura práctica

La óptica puede funcionar con cuadernos, chats o memoria del vendedor cuando hay pocas órdenes. El problema aparece cuando existen varias órdenes pendientes, pagos parciales, cambios de montura, recetas externas, garantías, entregas a familiares o anulaciones. El sistema debe reducir ambigüedad, no reemplazar el criterio humano. Por eso se registran reglas, fuentes, dudas y estados de validación.

---

## 2. Contexto observado del negocio

### 2.1 Descripción general

Óptica Horizonte atiende clientes que solicitan lentes completos, cambio de lunas, monturas, reparación menor, accesorios y ajustes. La atención suele iniciar con una conversación presencial o por mensaje. El cliente puede traer receta externa, hacerse una evaluación en la óptica o pedir reposición a partir de una fórmula previa. La operación mezcla venta, servicio, preparación, seguimiento, cobro y entrega.

### 2.2 Problemas actuales observados

- Las órdenes pueden registrarse en cuadernos, notas sueltas o chats.
- El estado de una orden puede depender de memoria humana.
- Un anticipo puede confundirse con pago completo.
- La fórmula visual puede quedar separada de la orden que la usa.
- No siempre queda claro si una orden está pendiente, en preparación, lista, entregada, anulada o en garantía.
- La entrega puede realizarse a una persona distinta al cliente sin evidencia suficiente.
- Las garantías pueden mezclarse con nuevas ventas o ajustes menores.
- Los reportes de órdenes pendientes, saldos y entregas pueden requerir revisión manual.

### 2.3 Objetivos esperados del sistema

- Registrar clientes con datos mínimos de contacto.
- Registrar fórmulas visuales propias o externas.
- Crear órdenes ópticas con productos, servicios, precios y estados.
- Controlar anticipos, saldos y pagos completos.
- Registrar preparación, entrega, anulación y garantías.
- Mantener evidencia básica: comprobantes, observaciones, responsable y fecha.
- Consultar órdenes pendientes, listas, entregadas, anuladas y en garantía.
- Preparar modelos posteriores revisables: conceptual, diccionario, flujo de pantallas, roles/permisos, BPMN y grafo libre.

### 2.4 Frases importantes del cliente

> “A veces el cliente deja abonado y vuelve después.”

> “No siempre la receta la hacemos nosotros; a veces viene de otro lado.”

> “Lo importante es saber qué está pendiente, qué ya está listo y quién debe todavía.”

> “Si hay garantía, no quiero mezclarlo como si fuera una venta nueva.”

### 2.5 Supuestos detectados

| ID | Supuesto | Fuente | Estado |
|---|---|---|---|
| SUP-001 | La óptica permite registrar recetas externas. | entrevista | pendiente |
| SUP-002 | Toda orden debe tener cliente, aunque sea con datos mínimos. | inferencia | pendiente |
| SUP-003 | La entrega requiere pago completo salvo autorización excepcional. | criterio de negocio | pendiente |
| SUP-004 | Una garantía puede abrir una revisión sin crear venta nueva. | inferencia | pendiente |

### 2.6 Preguntas pendientes del contexto

| ID | Pregunta | Afecta | Prioridad | Estado |
|---|---|---|---|---|
| PEND-001 | ¿La fórmula visual siempre es obligatoria para lentes completos? | fórmula / orden | alta | pendiente |
| PEND-002 | ¿Existe porcentaje mínimo de anticipo para iniciar preparación? | pagos / preparación | alta | pendiente |
| PEND-003 | ¿Quién puede autorizar entrega con saldo pendiente? | autorización / entrega | alta | pendiente |
| PEND-004 | ¿La garantía puede generar una nueva orden vinculada? | garantía / trazabilidad | media | pendiente |
| PEND-005 | ¿Qué datos mínimos del cliente son obligatorios? | clientes / validación | media | pendiente |

### Indicadores de entrevista

- [x] Se registraron frases importantes del cliente.
- [x] Se distinguió entre hechos, deseos, suposiciones y problemas.
- [x] Se identificaron operaciones repetitivas del negocio.
- [x] Se identificaron dolores o riesgos actuales.
- [x] Se identificaron documentos, comprobantes o registros usados.
- [x] Se identificaron actores humanos involucrados.
- [x] Se anotaron excepciones o casos raros.
- [x] Se registraron preguntas pendientes.

### ACT-001 — Secretaría de atención

- Fuente: entrevista.
- Estado: pendiente de validar.
- Descripción humana: responsable de registrar clientes, órdenes, pagos, entregas y garantías en la operación diaria.
- Relacionada con: ACC-001, ACC-002, ACC-003, CU-001, CU-002.

### EVID-001 — Comprobante de entrega

- Fuente: entrevista.
- Estado: pendiente de validar.
- Descripción humana: evidencia mínima que sostiene que una orden fue retirada por el cliente o autorizado.
- Relacionada con: ACC-003, POST-003, RN-003.

---

## 3. Sistema de estados del negocio

### Propósito de la sección

Identificar qué cosas cambian durante la operación de la óptica antes de diseñar tablas o pantallas. Si no se entiende el sistema de estados, la base de datos puede terminar guardando datos sin distinguir qué significan operativamente.

### 3.1 Estados generales observables

```text
cliente identificado
→ fórmula visual registrada o seleccionada
→ orden óptica creada
→ productos/servicios definidos
→ monto calculado
→ anticipo registrado
→ orden en preparación
→ orden lista
→ pago completado
→ orden entregada
→ garantía o cierre posterior
```

### 3.2 Datos que cambian durante la operación

| Dato / estado | Qué representa | Puede cambiar por | Riesgo si queda mal |
|---|---|---|---|
| Estado de orden | Situación operativa de la orden | creación, preparación, entrega, anulación | entregar algo no listo o perder seguimiento |
| Saldo pendiente | deuda restante del cliente | anticipo, pago, ajuste, anulación | cobrar mal o entregar con deuda |
| Fórmula visual | datos usados para fabricar lentes | receta externa, evaluación, corrección | preparar lentes incorrectos |
| Estado de garantía | revisión posterior a entrega | reclamo, evaluación, resolución | mezclar garantía con venta nueva |
| Evidencia de entrega | comprobación de cierre | firma, observación, responsable | discusión con cliente |

### 3.3 Estados válidos

- Orden en borrador.
- Orden pendiente de anticipo.
- Orden con anticipo registrado.
- Orden en preparación.
- Orden lista para entrega.
- Orden entregada.
- Orden anulada.
- Orden en garantía.
- Garantía resuelta.

### EST-001 — orden_creada

- Fuente: ACC-001, POST-001.
- Estado: pendiente de validar.
- Descripción humana: la orden existe, pertenece a un cliente y puede comenzar preparación si cumple condiciones mínimas.
- Transiciones relevantes: ACC-002, ACC-003.
- Riesgo si se usa mal: preparar o cobrar una orden incompleta.

### EST-002 — orden_entregada

- Fuente: ACC-003, POST-003, RN-003.
- Estado: pendiente de validar.
- Descripción humana: la orden queda cerrada operativamente con pago completo o autorización y evidencia de entrega.
- Transiciones relevantes: ACC-004, RN-006.
- Riesgo si se usa mal: cerrar operaciones sin trazabilidad de pago o receptor.

### 3.4 Estados inválidos prohibidos

- Orden entregada sin pago completo ni autorización registrada.
- Orden óptica sin cliente.
- Orden que requiere fórmula visual pero no tiene fórmula asociada.
- Pago recibido con monto menor o igual a cero.
- Anticipo mayor al total de la orden sin registro de ajuste o saldo a favor.
- Garantía sin orden original relacionada.
- Entrega sin fecha, responsable o evidencia mínima.

### Pregunta lógica central

```text
¿Qué verdades deben mantenerse aunque una orden avance desde solicitud hasta entrega, anulación o garantía?
```

Respuesta tentativa:

```text
Cada orden debe conservar trazabilidad de cliente, fórmula, productos, montos, pagos, estado operativo, responsables y evidencia de cierre.
```

---

## 4. Vocabulario lógico del dominio

### Concepto: Cliente

- Tipo lógico: entidad.
- Significado: persona que solicita productos o servicios ópticos.
- Notación sugerida: `Cliente(c)`.
- Ejemplo: una persona que deja anticipo para lentes completos.
- Participa en: órdenes, fórmulas, pagos, entregas y garantías.

### CON-001 — Cliente

- Fuente: entrevista.
- Estado: pendiente de validar.
- Descripción humana: persona identificable que solicita productos o servicios ópticos y queda asociada a órdenes, pagos, entregas y garantías.
- Lectura: cuando se hable de cliente, se habla del responsable operativo de una orden, aunque falten datos completos.
- Relacionada con: RN-001, ENT-001, ACC-001.

### Concepto: Fórmula visual

- Tipo lógico: documento / estado técnico.
- Significado: datos de graduación o prescripción usados para preparar lentes.
- Notación sugerida: `FormulaVisual(f)`.
- Ejemplo: receta externa con esfera, cilindro, eje y observaciones.
- Participa en: órdenes ópticas, validaciones de preparación y trazabilidad.

### Concepto: Orden óptica

- Tipo lógico: entidad operativa.
- Significado: solicitud concreta de productos o servicios ópticos para un cliente.
- Notación sugerida: `OrdenOptica(o)`.
- Ejemplo: orden de lentes completos con montura, lunas y tratamiento antirreflejo.
- Participa en: pagos, preparación, entrega, anulación, garantía y reportes.

### Concepto: Anticipo

- Tipo lógico: movimiento financiero parcial.
- Significado: pago inicial que permite iniciar o reservar una orden.
- Notación sugerida: `Anticipo(a)`.
- Ejemplo: cliente abona 20 dólares para iniciar preparación.
- Participa en: saldo, autorización de preparación y control financiero.

### Concepto: Pago recibido

- Tipo lógico: movimiento financiero.
- Significado: dinero efectivamente recibido por la óptica.
- Notación sugerida: `PagoRecibido(p)`.
- Ejemplo: pago final antes de entregar lentes.
- Participa en: saldo, cierre de orden, comprobantes y reportes.

### Concepto: Entrega

- Tipo lógico: evento / cierre operativo.
- Significado: acción mediante la cual la óptica entrega el producto o servicio al cliente o representante.
- Notación sugerida: `Entrega(e)`.
- Ejemplo: cliente retira lentes terminados y se registra fecha.
- Participa en: cierre de orden, evidencia, garantía y reportes.

### Concepto: Garantía

- Tipo lógico: flujo posterior / estado.
- Significado: revisión o corrección solicitada luego de una entrega.
- Notación sugerida: `Garantia(g)`.
- Ejemplo: cliente reporta incomodidad y se revisa el lente entregado.
- Participa en: orden original, revisión, resolución y evidencia.

---

## 5. Predicados, proposiciones y símbolos permitidos

### 5.1 Símbolos permitidos

| Símbolo | Lectura | Uso típico |
|---|---|---|
| ∀ | para todo | reglas generales |
| ∃ | existe | existencia obligatoria |
| ¬ | no | prohibición |
| ∧ | y | condiciones simultáneas |
| ∨ | o | alternativas |
| → | implica | si ocurre A, debe cumplirse B |
| = | igual | igualdad de valores |
| ≠ | distinto | diferencia |
| ≤ | menor o igual | límites superiores |
| ≥ | mayor o igual | mínimos |

### 5.2 Predicados del dominio

| Predicado | Lectura | Tipo | Usado en |
|---|---|---|---|
| `Cliente(c)` | c es cliente | entidad | RN-001, PRE-001 |
| `FormulaVisual(f)` | f es fórmula visual | documento | RN-002, INV-002 |
| `OrdenOptica(o)` | o es orden óptica | entidad | RN-001, RN-003 |
| `PerteneceA(o,c)` | o pertenece al cliente c | relación | RN-001 |
| `RequiereFormula(o)` | o requiere fórmula visual | estado/regla | RN-002 |
| `TieneFormula(o,f)` | o tiene fórmula asociada | relación | RN-002, INV-002 |
| `PagoRecibido(p)` | p es pago recibido | movimiento | RN-004 |
| `Monto(p)` | monto del pago p | atributo | RN-004 |
| `PagoCompleto(o)` | o tiene pago completo | estado | RN-003, INV-001 |
| `Entregada(o)` | o fue entregada | estado | RN-003, POST-003 |
| `Garantia(g)` | g es garantía | flujo posterior | RN-006 |
| `OrdenOrigen(g,o)` | g pertenece a orden original o | relación | RN-006 |

### 5.3 Regla editorial para fórmulas

Toda fórmula debe tener descripción humana, forma lógica, lectura en español e impacto práctico. Una fórmula sin lectura humana no está lista para validación con cliente ni para reutilización técnica.

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
- Razón de negocio: evita órdenes huérfanas imposibles de entregar, cobrar o consultar.
- Consecuencia si se rompe: pérdida de trazabilidad y dificultad para contactar al responsable.
- Aplica a: órdenes, clientes, pagos y entregas.
- Relacionada con: PRE-001, POST-001, ACC-001.
- Impacto técnico futuro:
  - Base de datos: `orden_optica.cliente_id` obligatorio.
  - Backend: rechazar creación de orden sin cliente.
  - Frontend: seleccionar o registrar cliente antes de crear orden.
  - Pruebas: intentar crear orden sin cliente debe fallar.
  - Documentación: explicar datos mínimos del cliente.

### RN-002 — Una orden que requiere fórmula debe tener fórmula asociada

- Tipo: existencia / integridad.
- Fuente: criterio técnico-operativo.
- Estado: pendiente de validar.
- Descripción humana: si la orden implica lentes graduados, debe existir una fórmula visual asociada o una referencia explícita a receta externa.
- Forma lógica:

```text
∀o (OrdenOptica(o) ∧ RequiereFormula(o) → ∃f (FormulaVisual(f) ∧ TieneFormula(o,f)))
```

- Lectura de la forma lógica: toda orden que requiere fórmula debe tener una fórmula visual asociada.
- Razón de negocio: reduce el riesgo de preparar lentes con datos incompletos o incorrectos.
- Consecuencia si se rompe: reproceso, reclamos, pérdida económica o garantía innecesaria.
- Relacionada con: INV-002, PRE-002, ACC-001.
- Impacto técnico futuro:
  - Base de datos: relación orden-fórmula o campo de receta externa controlado.
  - Backend: validar fórmula para tipos de orden que la requieren.
  - Frontend: mostrar advertencia si falta fórmula.
  - Pruebas: orden graduada sin fórmula debe fallar.

### RN-003 — Una orden entregada debe tener pago completo o autorización excepcional

- Tipo: estado / autorización.
- Fuente: criterio de negocio.
- Estado: pendiente de validar.
- Descripción humana: por defecto, una orden no debe entregarse con saldo pendiente. Si se permite excepción, debe registrarse autorización.
- Forma lógica:

```text
∀o (OrdenOptica(o) ∧ Entregada(o) → PagoCompleto(o) ∨ AutorizacionEntregaConSaldo(o))
```

- Lectura de la forma lógica: si una orden fue entregada, entonces tiene pago completo o autorización excepcional documentada.
- Razón de negocio: protege caja sin negar que el negocio pueda tomar decisiones humanas excepcionales.
- Consecuencia si se rompe: deuda no controlada, reclamos o pérdida financiera.
- Relacionada con: INV-001, PRE-004, POST-003.
- Impacto técnico futuro:
  - Backend: bloquear entrega con saldo salvo autorización.
  - Frontend: pedir motivo y responsable si hay excepción.
  - Auditoría: registrar quién autorizó entrega con saldo.

### RN-004 — Todo pago recibido debe tener monto positivo

- Tipo: integridad financiera.
- Fuente: regla financiera básica.
- Estado: validada por criterio técnico.
- Descripción humana: no tiene sentido registrar pagos con monto menor o igual a cero.
- Forma lógica:

```text
∀p (PagoRecibido(p) → Monto(p) > 0)
```

- Lectura de la forma lógica: todo pago recibido debe tener monto positivo.
- Razón de negocio: evita movimientos financieros inválidos.
- Consecuencia si se rompe: saldos incorrectos y reportes contaminados.
- Relacionada con: INV-003, ACC-002.
- Impacto técnico futuro:
  - Base de datos: `CHECK monto > 0`.
  - Backend: validación previa al registro.
  - Frontend: bloquear valores cero o negativos.
  - Pruebas: pago con monto cero o negativo debe fallar.

### RN-005 — Una orden anulada no debe entregarse

- Tipo: prohibición / estado.
- Fuente: inferencia operativa.
- Estado: pendiente de validar.
- Descripción humana: una orden anulada queda fuera del flujo normal de entrega.
- Forma lógica:

```text
¬∃o (OrdenOptica(o) ∧ Anulada(o) ∧ Entregada(o))
```

- Lectura de la forma lógica: no debe existir una orden que esté anulada y entregada al mismo tiempo.
- Razón de negocio: evita estados contradictorios.
- Consecuencia si se rompe: reportes y responsabilidades inconsistentes.
- Relacionada con: INV-004, ACC-004.

### RN-006 — Toda garantía debe relacionarse con una orden original

- Tipo: trazabilidad.
- Fuente: inferencia operativa.
- Estado: pendiente de validar.
- Descripción humana: una garantía no debe existir aislada; debe saberse qué orden entregada la originó.
- Forma lógica:

```text
∀g (Garantia(g) → ∃o (OrdenOptica(o) ∧ OrdenOrigen(g,o)))
```

- Lectura de la forma lógica: para toda garantía debe existir una orden óptica original asociada.
- Razón de negocio: diferencia garantía, reclamo, ajuste menor y venta nueva.
- Consecuencia si se rompe: confusión financiera y operativa.
- Relacionada con: ACC-005, POST-005.

---

## 7. Condiciones iniciales / precondiciones

### PRE-001 — Cliente identificado antes de crear orden

- Aplica a: ACC-001.
- Descripción humana: antes de crear una orden debe existir un cliente o al menos datos mínimos suficientes para identificarlo.
- Forma lógica:

```text
CrearOrden(o) → ∃c Cliente(c)
```

- Lectura: crear una orden presupone que hay cliente identificable.
- Si no se cumple: registrar cliente o detener creación de orden.
- Validación esperada: frontend y backend.

### PRE-002 — Fórmula disponible para orden graduada

- Aplica a: ACC-001.
- Descripción humana: si la orden requiere lentes graduados, debe existir fórmula visual o receta externa registrada.
- Forma lógica:

```text
CrearOrdenGraduada(o) → ∃f FormulaVisual(f)
```

- Si no se cumple: pedir fórmula, registrar receta externa o marcar orden como incompleta.
- Validación esperada: frontend, backend y revisión humana.

### PRE-003 — Orden existente antes de registrar pago

- Aplica a: ACC-002.
- Descripción humana: un pago debe asociarse a una orden existente.
- Forma lógica:

```text
RegistrarPago(p,o) → OrdenOptica(o)
```

- Si no se cumple: rechazar pago o registrarlo como movimiento no aplicado bajo política separada.
- Validación esperada: backend.

### PRE-004 — Orden lista antes de entregar

- Aplica a: ACC-003.
- Descripción humana: antes de entregar, la orden debe estar lista o autorizada para entrega.
- Forma lógica:

```text
EntregarOrden(o) → ListaParaEntrega(o)
```

- Si no se cumple: impedir entrega o escalar autorización.
- Validación esperada: frontend y backend.

---

## 8. Invariantes del negocio

### INV-001 — No entregar órdenes con saldo pendiente sin autorización

- Ámbito: flujo de entrega.
- Descripción humana: una orden no debe quedar entregada con saldo pendiente salvo autorización explícita.
- Forma lógica:

```text
∀o (Entregada(o) → PagoCompleto(o) ∨ AutorizacionEntregaConSaldo(o))
```

- Lectura: toda orden entregada debe estar pagada o tener autorización registrada.
- Debe mantenerse: al confirmar entrega.
- Acciones que pueden romperla:
  - ACC-003 — Registrar entrega.
- Reglas relacionadas:
  - RN-003.
- Riesgo si se rompe: pérdida financiera o discusión con cliente.
- Validación técnica futura: backend, pruebas y auditoría.

### INV-002 — Orden graduada conserva fórmula asociada

- Ámbito: orden óptica.
- Descripción humana: si una orden requiere fórmula visual, esa relación debe conservarse durante preparación, entrega y garantía.
- Forma lógica:

```text
∀o (OrdenOptica(o) ∧ RequiereFormula(o) → ∃f TieneFormula(o,f))
```

- Debe mantenerse: durante preparación, entrega y garantía.
- Acciones que pueden romperla:
  - ACC-001 — Crear orden.
  - ACC-004 — Modificar orden.
- Reglas relacionadas:
  - RN-002.
- Riesgo si se rompe: lentes preparados con información incompleta.
- Validación técnica futura: backend y pruebas.

### INV-003 — Pagos válidos tienen monto positivo

- Ámbito: pagos.
- Descripción humana: todo pago recibido debe aportar un monto positivo al saldo de la orden.
- Forma lógica:

```text
∀p (PagoRecibido(p) → Monto(p) > 0)
```

- Debe mantenerse: al registrar pagos y recalcular saldos.
- Acciones que pueden romperla:
  - ACC-002 — Registrar pago.
- Reglas relacionadas:
  - RN-004.
- Riesgo si se rompe: saldos y reportes falsos.
- Validación técnica futura: base de datos, backend y pruebas.

### INV-004 — Una orden no puede estar anulada y entregada al mismo tiempo

- Ámbito: estados de orden.
- Descripción humana: anulación y entrega son estados finales incompatibles en el flujo principal.
- Forma lógica:

```text
¬∃o (Anulada(o) ∧ Entregada(o))
```

- Debe mantenerse: siempre.
- Acciones que pueden romperla:
  - ACC-003 — Registrar entrega.
  - ACC-004 — Anular orden.
- Reglas relacionadas:
  - RN-005.
- Riesgo si se rompe: reportes operativos contradictorios.
- Validación técnica futura: backend, pruebas y auditoría.

---

## 9. Condiciones de cierre / postcondiciones

### POST-001 — Orden creada con estado inicial válido

- Aplica a: ACC-001.
- Descripción humana: al terminar la creación, la orden debe quedar asociada a cliente, con estado inicial y datos mínimos.
- Forma lógica:

```text
OrdenCreada(o) → OrdenOptica(o) ∧ ∃c PerteneceA(o,c)
```

- Evidencia: registro de orden.
- Validación esperada: orden persistida, cliente asociado y estado inicial.

### POST-002 — Pago registrado y saldo actualizado

- Aplica a: ACC-002.
- Descripción humana: al registrar un pago, el saldo de la orden debe recalcularse.
- Evidencia: comprobante, movimiento financiero o recibo.
- Validación esperada: pago guardado y saldo consistente.

### POST-003 — Entrega registrada con evidencia

- Aplica a: ACC-003.
- Descripción humana: al entregar una orden, debe quedar fecha, responsable, receptor y evidencia mínima.
- Evidencia: registro de entrega, firma, observación o comprobante.
- Validación esperada: orden en estado entregada con datos de cierre.

### POST-004 — Orden anulada con motivo

- Aplica a: ACC-004.
- Descripción humana: si se anula una orden, debe conservarse motivo y responsable.
- Evidencia: registro de anulación.
- Validación esperada: orden en estado anulada y no entregada.

### POST-005 — Garantía abierta con orden original

- Aplica a: ACC-005.
- Descripción humana: una garantía debe quedar vinculada a la orden que originó el reclamo.
- Evidencia: registro de garantía.
- Validación esperada: garantía con orden original y motivo.

---

## 10. Acciones transformadoras

### ACC-001 — Crear orden óptica

- Tipo: creación.
- Actor principal: asesor de óptica.
- Estado inicial: cliente solicita producto o servicio óptico.
- Condiciones iniciales:
  - PRE-001.
  - PRE-002 si la orden requiere fórmula.
- Transformación:
  1. Identificar o registrar cliente.
  2. Registrar fórmula visual o receta externa si aplica.
  3. Seleccionar productos o servicios.
  4. Calcular total inicial.
  5. Registrar observaciones relevantes.
  6. Guardar orden en estado inicial.
- Invariantes protegidas:
  - INV-002.
- Estado de cierre: orden creada y pendiente de pago/preparación.
- Postcondiciones:
  - POST-001.
- Reglas aplicadas:
  - RN-001.
  - RN-002.
- Entidades afectadas:
  - Crea: OrdenOptica.
  - Consulta: Cliente, FormulaVisual, Producto.
  - Modifica: saldo inicial de orden.
- Evidencia: registro de orden.

### ACC-002 — Registrar pago

- Tipo: actualización / movimiento financiero.
- Actor principal: cajero o asesor.
- Estado inicial: orden existente con total definido.
- Condiciones iniciales:
  - PRE-003.
- Transformación:
  1. Seleccionar orden.
  2. Ingresar monto recibido.
  3. Registrar forma de pago.
  4. Asociar comprobante si existe.
  5. Recalcular saldo.
  6. Actualizar estado financiero de la orden.
- Invariantes protegidas:
  - INV-003.
- Estado de cierre: pago registrado y saldo actualizado.
- Postcondiciones:
  - POST-002.
- Reglas aplicadas:
  - RN-004.
- Evidencia: comprobante o registro de caja.

### ACC-003 — Registrar entrega

- Tipo: cierre.
- Actor principal: asesor de óptica.
- Estado inicial: orden lista para entrega.
- Condiciones iniciales:
  - PRE-004.
- Transformación:
  1. Verificar estado de orden.
  2. Verificar saldo.
  3. Registrar receptor.
  4. Registrar fecha y responsable.
  5. Confirmar entrega.
- Invariantes protegidas:
  - INV-001.
  - INV-004.
- Estado de cierre: orden entregada con evidencia.
- Postcondiciones:
  - POST-003.
- Reglas aplicadas:
  - RN-003.
- Evidencia: registro de entrega.

### ACC-004 — Anular orden

- Tipo: anulación.
- Actor principal: administrador o asesor autorizado.
- Estado inicial: orden existente no entregada.
- Condiciones iniciales:
  - La orden existe.
  - La orden no está entregada.
- Transformación:
  1. Seleccionar orden.
  2. Registrar motivo de anulación.
  3. Registrar responsable.
  4. Actualizar estado a anulada.
- Invariantes protegidas:
  - INV-004.
- Estado de cierre: orden anulada con motivo.
- Postcondiciones:
  - POST-004.
- Reglas aplicadas:
  - RN-005.
- Evidencia: registro de anulación.

### ACC-005 — Abrir garantía

- Tipo: creación / revisión posterior.
- Actor principal: asesor de óptica.
- Estado inicial: cliente reporta problema sobre orden entregada.
- Condiciones iniciales:
  - Existe orden original entregada.
- Transformación:
  1. Buscar orden original.
  2. Registrar motivo de garantía.
  3. Registrar observaciones del cliente.
  4. Abrir revisión.
  5. Definir estado inicial de garantía.
- Invariantes protegidas:
  - La garantía debe conservar orden original.
- Estado de cierre: garantía abierta y trazable.
- Postcondiciones:
  - POST-005.
- Reglas aplicadas:
  - RN-006.
- Evidencia: registro de garantía.

---

## 11. Árbol operativo de macroflujos, flujos y casos de uso

### MF-001 — Gestión de atención y órdenes ópticas

- Objetivo: controlar el ciclo desde solicitud del cliente hasta entrega o anulación.
- Inicio general: cliente solicita producto o servicio óptico.
- Cierre general: orden entregada, anulada o pendiente con estado trazable.

#### FL-001 — Atención inicial y creación de orden

- Usa CU-001 — Registrar cliente.
- Usa CU-002 — Registrar fórmula visual.
- Usa CU-003 — Crear orden óptica.

#### FL-002 — Cobro y preparación

- Usa CU-004 — Registrar anticipo o pago.
- Usa CU-005 — Cambiar estado de preparación.
- Usa CU-006 — Consultar órdenes pendientes.

#### FL-003 — Entrega y cierre

- Usa CU-007 — Registrar entrega.
- Usa CU-008 — Consultar saldos pendientes.

#### FL-004 — Anulación y garantía

- Usa CU-009 — Anular orden.
- Usa CU-010 — Abrir garantía.
- Usa CU-011 — Resolver garantía.

---

## 12. Catálogo mínimo de casos de uso

### CU-001 — Registrar cliente

- Actor principal: asesor.
- Objetivo: guardar datos mínimos del cliente para futuras órdenes.
- Precondiciones: datos mínimos disponibles.
- Postcondiciones: cliente creado o actualizado.
- Reglas relacionadas: RN-001.

### CU-003 — Crear orden óptica

- Actor principal: asesor.
- Objetivo: registrar una solicitud óptica concreta.
- Precondiciones: PRE-001, PRE-002 si aplica.
- Postcondiciones: POST-001.
- Reglas relacionadas: RN-001, RN-002.

### CU-004 — Registrar anticipo o pago

- Actor principal: cajero o asesor.
- Objetivo: registrar dinero recibido asociado a una orden.
- Precondiciones: PRE-003.
- Postcondiciones: POST-002.
- Reglas relacionadas: RN-004.

### CU-007 — Registrar entrega

- Actor principal: asesor.
- Objetivo: cerrar la orden con evidencia de entrega.
- Precondiciones: PRE-004.
- Postcondiciones: POST-003.
- Reglas relacionadas: RN-003.

---

## 13. Grafo lógico textual inicial

```text
Cliente
  → crea / solicita → OrdenOptica
OrdenOptica
  → puede requerir → FormulaVisual
OrdenOptica
  → genera → PagoRecibido
PagoRecibido
  → actualiza → SaldoOrden
OrdenOptica
  → termina en → Entrega
Entrega
  → puede originar → Garantia
Garantia
  → referencia → OrdenOptica original
```

Este grafo lógico textual no reemplaza a un diagrama especializado. Sirve para detectar relaciones candidatas que luego podrían reutilizarse como fuente para modelo conceptual, grafo libre o BPMN.

---

## 14. Entidades candidatas

| ID | Entidad candidata | Justificación lógica | Fuente |
|---|---|---|---|
| ENT-001 | Cliente | Toda orden debe pertenecer a alguien identificable. | RN-001 |
| ENT-002 | FormulaVisual | Las órdenes graduadas necesitan datos técnicos asociados. | RN-002 |
| ENT-003 | OrdenOptica | Es el expediente operativo central del negocio. | ACC-001 |
| ENT-004 | PagoRecibido | Los pagos transforman el saldo de la orden. | ACC-002 |
| ENT-005 | Entrega | El cierre operativo necesita evidencia. | ACC-003 |
| ENT-006 | Garantia | Las revisiones posteriores deben separarse de venta nueva. | RN-006 |

### Atributos candidatos

### ATR-001 — Cliente.nombres

- Pertenece a: ENT-001.
- Tipo tentativo: texto.
- Razón operativa: permite identificar y contactar al responsable de la orden.
- ¿Es calculado?: no.
- Riesgo si se modela mal: órdenes difíciles de entregar o consultar.
- Fuente lógica: RN-001, ACC-001.
- Reglas asociadas: RN-001.

### ATR-002 — OrdenOptica.estado

- Pertenece a: ENT-003.
- Tipo tentativo: enum.
- Razón operativa: permite saber si la orden está creada, en preparación, lista, entregada, anulada o en garantía.
- ¿Es calculado?: no.
- Riesgo si se modela mal: entregas y reportes inconsistentes.
- Fuente lógica: INV-001, POST-001, POST-003.
- Reglas asociadas: RN-003, RN-005.
- Invariantes asociadas: INV-001.

### ATR-003 — PagoRecibido.monto

- Pertenece a: ENT-004.
- Tipo tentativo: dinero.
- Razón operativa: registra cuánto se pagó para calcular saldo pendiente.
- ¿Es calculado?: no.
- Riesgo si se modela mal: saldos y reportes de caja incorrectos.
- Fuente lógica: ACC-002, RN-004.
- Reglas asociadas: RN-004.

### ATR-004 — OrdenOptica.saldo_pendiente

- Pertenece a: ENT-003.
- Tipo tentativo: dinero.
- Razón operativa: lectura operativa calculada desde el total de la orden menos pagos registrados.
- ¿Es calculado?: sí.
- Fórmula o lectura de cálculo: total_orden - suma(PagoRecibido.monto).
- Riesgo si se modela mal: entregar con deuda o cobrar de más.
- Fuente lógica: CALC-001, RN-003, RN-004.
- Reglas asociadas: RN-003, RN-004.
- Invariantes asociadas: INV-003.

### ATR-005 — Entrega.fecha_entrega

- Pertenece a: ENT-005.
- Tipo tentativo: fecha.
- Razón operativa: sostiene la evidencia temporal del cierre de la orden.
- ¿Es calculado?: no.
- Riesgo si se modela mal: reclamos sin trazabilidad.
- Fuente lógica: ACC-003, EVID-001.

### Relaciones candidatas

### REL-001 — Cliente — OrdenOptica

- Entidad origen: ENT-001.
- Entidad destino: ENT-003.
- Cardinalidad tentativa: 1 a muchos obligatoria.
- Justificación lógica: RN-001 obliga que toda orden pertenezca a un cliente.
- Fuente lógica: RN-001, ACC-001.

### REL-002 — OrdenOptica — FormulaVisual

- Entidad origen: ENT-003.
- Entidad destino: ENT-002.
- Cardinalidad tentativa: 0..1 a 1 según tipo de orden.
- Justificación lógica: RN-002 exige fórmula cuando la orden requiere graduación.
- Fuente lógica: RN-002, PRE-002.

### REL-003 — OrdenOptica — PagoRecibido

- Entidad origen: ENT-003.
- Entidad destino: ENT-004.
- Cardinalidad tentativa: 1 a muchos.
- Justificación lógica: los pagos transforman el saldo de la orden sin reemplazar la orden.
- Fuente lógica: ACC-002, RN-004.

### REL-004 — OrdenOptica — Entrega

- Entidad origen: ENT-003.
- Entidad destino: ENT-005.
- Cardinalidad tentativa: 0..1 obligatoria al cerrar.
- Justificación lógica: POST-003 exige evidencia de cierre cuando la orden se entrega.
- Fuente lógica: ACC-003, POST-003, EVID-001.

### REL-005 — OrdenOptica — Garantia

- Entidad origen: ENT-003.
- Entidad destino: ENT-006.
- Cardinalidad tentativa: 0 a muchos.
- Justificación lógica: RN-006 separa garantía de venta nueva y conserva vínculo con orden original.
- Fuente lógica: RN-006, PEND-004.

---

## 15. Estados y transiciones candidatas

| Estado origen | Acción | Estado destino | Regla crítica |
|---|---|---|---|
| solicitud recibida | crear orden | orden creada | RN-001 |
| orden creada | registrar pago | orden con anticipo / pagada | RN-004 |
| orden creada | enviar a preparación | en preparación | PRE-002 |
| en preparación | marcar lista | lista para entrega | pendiente validar |
| lista para entrega | registrar entrega | entregada | RN-003 |
| orden no entregada | anular orden | anulada | RN-005 |
| entregada | abrir garantía | en garantía | RN-006 |

---

## 16. Reportes candidatos

| ID | Reporte | Pregunta que responde | Fuente lógica |
|---|---|---|---|
| REP-001 | Órdenes pendientes | ¿Qué órdenes aún no se entregan? | estados de orden |
| REP-002 | Saldos pendientes | ¿Qué clientes deben dinero? | pagos / saldo |
| REP-003 | Órdenes listas | ¿Qué puede entregarse hoy? | estado lista |
| REP-004 | Garantías abiertas | ¿Qué reclamos están pendientes? | garantía |
| REP-005 | Pagos por período | ¿Cuánto ingresó por fecha? | pagos recibidos |

### CALC-001 — Saldo pendiente de orden

- Estado: pendiente de validar.
- Descripción humana: cálculo de saldo operativo que indica cuánto queda por pagar de una orden óptica.
- Fórmula: total_orden - suma(PagoRecibido.monto).
- Lectura: una orden queda sin saldo cuando los pagos acumulados cubren su total.
- Datos necesarios: total de orden, pagos recibidos, anulaciones o ajustes autorizados.
- Entidades involucradas: ENT-003, ENT-004.
- Reglas relacionadas: RN-003, RN-004.
- Riesgo si se calcula mal: cobrar mal, entregar con deuda o reportar caja incorrecta.
- Validación esperada: comparar saldo contra pagos registrados antes de permitir entrega.

---

## 17. Indicadores de base de datos futura

- `orden_optica` debería referenciar obligatoriamente a `cliente`.
- `pago_recibido` debería tener `monto > 0`.
- `garantia` debería referenciar orden original.
- `entrega` debería conservar fecha, responsable y receptor.
- Estados de orden deberían controlarse por catálogo o enum estable.
- Fórmula visual debería distinguir fuente: propia, externa o histórica.

---

## 18. Riesgos del levantamiento

| ID | Riesgo | Consecuencia | Mitigación |
|---|---|---|---|
| RISK-001 | Confundir anticipo con pago completo | entrega con deuda | validar saldo antes de entrega |
| RISK-002 | Fórmula externa sin registro suficiente | error de fabricación | registrar fuente y observaciones |
| RISK-003 | Garantía tratada como venta nueva | reportes falsos | vincular garantía a orden original |
| RISK-004 | Anulación sin motivo | pérdida de auditoría | exigir responsable y motivo |

---

## 19. Uso como fuente para otros artefactos

### Puede servir como fuente para modelo conceptual

- Cliente.
- FormulaVisual.
- OrdenOptica.
- PagoRecibido.
- Entrega.
- Garantia.

### Puede servir como fuente para diccionario de datos

- Campos de cliente.
- Campos de orden.
- Campos de pago.
- Estados de orden.
- Campos de evidencia.

### Puede servir como fuente para BPMN / flujo operativo

- Crear orden.
- Registrar pago.
- Preparar orden.
- Entregar orden.
- Abrir garantía.

### Puede servir como fuente para roles/permisos

- Asesor.
- Cajero.
- Administrador.
- Técnico de preparación.

### Puede servir como fuente para flujo de pantallas

- Clientes.
- Fórmulas.
- Órdenes.
- Pagos.
- Entregas.
- Garantías.
- Reportes.

---

## 20. Preguntas pendientes consolidadas

| ID | Pregunta | Bloquea | Prioridad | Estado |
|---|---|---|---|---|
| PEND-001 | ¿La fórmula visual siempre es obligatoria para lentes completos? | RN-002 / PRE-002 | alta | pendiente |
| PEND-002 | ¿Existe porcentaje mínimo de anticipo para iniciar preparación? | flujo de preparación | alta | pendiente |
| PEND-003 | ¿Quién puede autorizar entrega con saldo pendiente? | RN-003 / INV-001 | alta | pendiente |
| PEND-004 | ¿La garantía puede generar una nueva orden vinculada? | garantía / entidades | media | pendiente |
| PEND-005 | ¿Qué datos mínimos del cliente son obligatorios? | cliente / orden | media | pendiente |

---

## 21. Nivel de madurez del levantamiento

| Área | Estado | Comentario |
|---|---|---|
| Contexto | completo para ejemplo | faltaría entrevista real |
| Reglas | parcial | varias reglas son inferidas |
| Precondiciones | inicial | cubren creación, pago y entrega |
| Invariantes | parcial | faltan reglas de inventario y productos |
| Acciones | parcial | faltan ajustes, devoluciones y compras |
| Entidades candidatas | inicial | listas para revisión conceptual |
| Uso como fuente para otros artefactos | parcial | generar solo borradores revisables |
| Validación con cliente | pendiente | no asumir reglas como definitivas |

---

## 22. Cierre del levantamiento

Este ejemplo muestra cómo una entrevista de óptica puede transformarse en una fuente lógica canónica. Todavía no reemplaza la validación humana. Su utilidad principal es mostrar cómo estados, reglas, invariantes, acciones y evidencias pueden organizarse antes de crear diagramas o tablas.

La conclusión operativa es que la óptica no necesita empezar por una base de datos. Primero necesita distinguir qué verdades deben mantenerse mientras una orden cambia de estado. Luego esas verdades se pueden reutilizar como fuente para modelo conceptual, diccionario de datos, flujos, roles, pantallas y pruebas.
