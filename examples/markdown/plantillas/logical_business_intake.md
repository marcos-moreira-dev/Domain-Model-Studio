---
dms_version: "1"
diagram_type: "logical-business-intake"
name: "Plantilla maestra — Levantamiento lógico de negocio"
sample_kind: "template"
domain: "general"
status: "importable-template"
canonical_contract: "logical-business-master-v1"
importable: true
intended_output: "expediente lógico documental"
---

# Plantilla maestra lógica para levantamiento de negocio

> **Propósito:** convertir entrevistas, observaciones y conversaciones con clientes en una estructura lógica estable basada en estados, acciones, reglas, precondiciones, invariantes y condiciones de cierre. Esta plantilla es la fuente lógica canónica para orientar decisiones de base de datos, backend, frontend, pruebas y, cuando el usuario lo decida, construir otros Markdown revisables compatibles con Domain Model Studio.

---

## 0. Portada lógica del levantamiento

- **Negocio:** `<nombre del negocio>`
- **Proyecto / sistema:** `<nombre del sistema>`
- **Versión del levantamiento:** `v0.1`
- **Fecha:** `<AAAA-MM-DD>`
- **Responsable del levantamiento:** `<nombre>`
- **Estado del documento:** `borrador | validado parcialmente | validado | archivado`
- **Fuente principal:** `entrevista | observación | documento | sistema existente | inferencia`

### Propósito específico del levantamiento

```text
<Explicar qué se busca entender, qué problema del negocio se intenta resolver y qué alcance inicial tendrá el sistema.>
```

### Indicadores iniciales

- [ ] El negocio está identificado.
- [ ] El problema principal está descrito.
- [ ] El alcance inicial está delimitado.
- [ ] Las dudas importantes están registradas.
- [ ] El documento indica si sus reglas son borrador o validadas.

---


### Responsabilidad de alineación semántica

Este Levantamiento lógico define IDs, nombres, reglas y contratos semánticos canónicos del negocio. Cuando el usuario o una IA genere otros Markdown compatibles con Domain Model Studio, debe reutilizar los IDs y nombres definidos aquí para mantener consistencia entre artefactos.

Domain Model Studio no garantiza automáticamente la alineación entre proyectos independientes. Cada tipo de proyecto mantiene su propio alcance, parser, validación, edición y exportación.

No es obligatorio generar todos los tipos de proyecto. El usuario decide qué artefactos necesita para el caso de negocio.

## 1. Principio lógico central

Esta plantilla entiende un negocio como un **sistema de estados transformado por acciones**.

Cada acción importante debe analizarse así:

```text
estado inicial
→ condiciones iniciales / precondiciones
→ transformación
→ reglas aplicadas
→ invariantes protegidas
→ estado de cierre / postcondiciones
→ evidencia o trazabilidad
```

### Equivalencia con teoría de algoritmos

| Teoría de algoritmos | Levantamiento de negocio |
|---|---|
| Entrada | Situación inicial del negocio |
| Variables / estado | Datos, documentos, saldos, stock, estados |
| Algoritmo | Acción, caso de uso o flujo operativo |
| Precondición | Condición inicial necesaria |
| Invariante | Regla que no debe romperse |
| Postcondición | Cierre válido y verificable |
| Corrección | Proceso válido según reglas del negocio |
| Análisis | Riesgo, impacto, costo operativo o volumen de datos |

### Reglas rectoras

1. No empezar por tablas.
2. No crear entidades importantes sin justificación lógica.
3. No duplicar casos de uso reutilizables.
4. No escribir fórmulas sin lectura humana.
5. No asumir reglas dudosas como definitivas.
6. Cada proceso importante debe tener inicio, transformación y cierre.
7. Cada caso de uso importante debe proteger invariantes.
8. Cada invariante importante debe poder convertirse en validación, restricción, prueba o auditoría.
9. Cada pregunta pendiente debe quedar registrada.
10. Este MD es la fuente lógica; otros artefactos pueden reutilizar sus IDs, nombres y reglas bajo revisión del usuario y la IA.

---

## 2. Contexto observado del negocio

### 2.1 Descripción general

```text
<Qué hace el negocio, a quién atiende, qué vende o qué servicios presta.>
```

### 2.2 Problemas actuales observados

- `<problema 1>`
- `<problema 2>`
- `<problema 3>`

### 2.3 Objetivos esperados del sistema

- `<objetivo 1>`
- `<objetivo 2>`
- `<objetivo 3>`

### 2.4 Frases importantes del cliente

> `<frase literal o resumida del cliente>`

### 2.5 Supuestos detectados

| ID | Supuesto | Fuente | Estado |
|---|---|---|---|
| SUP-XXX | `<supuesto>` | entrevista | pendiente |

Formato alternativo importable cuando el supuesto necesite trazabilidad propia:

```md
### SUP-XXX — <supuesto detectado>

- Fuente: entrevista | inferencia | observación | criterio de negocio
- Estado: pendiente | validado parcialmente | validado
- Descripción humana: <qué se está asumiendo y por qué importa>
- Relacionada con: RN-..., PRE-..., ENT-..., PEND-...
```

### 2.6 Actores y evidencias observadas

Usar estos bloques cuando una persona, rol o evidencia del negocio deba quedar trazable antes de redactar casos de uso, reportes o reglas compatibles.

```md
### ACT-XXX — <actor o rol operativo>

- Fuente: entrevista | observación | documento
- Estado: pendiente | validado parcialmente | validado
- Descripción humana: <responsabilidad del actor>
- Relacionada con: ACC-..., CU-..., RN-...

### EVID-XXX — <evidencia o comprobante>

- Fuente: entrevista | observación | documento
- Estado: pendiente | validado parcialmente | validado
- Descripción humana: <qué prueba o registro sostiene>
- Relacionada con: ACC-..., POST-..., REP-...
```

### 2.7 Preguntas pendientes del contexto

| ID | Pregunta | Afecta | Prioridad | Estado |
|---|---|---|---|---|
| PEND-001 | `<pregunta>` | contexto | alta/media/baja | pendiente |

### Indicadores de entrevista

- [ ] Se registraron frases importantes del cliente.
- [ ] Se distinguió entre hechos, deseos, suposiciones y problemas.
- [ ] Se identificaron operaciones repetitivas del negocio.
- [ ] Se identificaron dolores o riesgos actuales.
- [ ] Se identificaron documentos, comprobantes o registros usados.
- [ ] Se identificaron actores humanos involucrados.
- [ ] Se anotaron excepciones o casos raros.
- [ ] Se registraron preguntas pendientes.

---

## 3. Sistema de estados del negocio

### Propósito de la sección

Identificar qué cosas cambian en el negocio y qué estados deben considerarse válidos o inválidos antes de diseñar entidades o tablas.

### 3.1 Estados generales observables

- `<estado 1>`
- `<estado 2>`
- `<estado 3>`

### 3.2 Datos que cambian durante la operación

| Dato / estado | Qué representa | Puede cambiar por | Riesgo si queda mal |
|---|---|---|---|
| `<stock>` | `<existencia de producto>` | `<venta, ajuste, compra>` | `<inventario falso>` |
| `<saldo>` | `<deuda o cuenta pendiente>` | `<cargo, pago, anulación>` | `<deuda falsa>` |

### 3.3 Estados válidos

- `<estado válido 1>`
- `<estado válido 2>`

Formato importable cuando el estado deba quedar trazable para UML estados, reglas o validaciones futuras:

```md
### EST-XXX — <estado_del_negocio>

- Fuente: RN-..., ACC-..., POST-...
- Estado: pendiente | validado parcialmente | validado
- Descripción humana: <qué significa este estado>
- Transiciones relevantes: ACC-..., POST-...
- Riesgo si se usa mal: <consecuencia>
```

### 3.4 Estados inválidos prohibidos

- `<producto con stock negativo>`
- `<venta confirmada sin detalle>`
- `<pago sin operación origen>`
- `<orden entregada sin fecha de entrega>`

### Pregunta lógica central

```text
¿Qué verdades deben mantenerse aunque el negocio cambie de estado?
```

---

## 4. Vocabulario lógico del dominio

### Propósito de la sección

Definir conceptos antes de escribir fórmulas. Ningún predicado importante debe usarse sin una explicación humana.

### Formato de concepto

```md
### Concepto: <nombre>

- Tipo lógico:
  entidad | estado | relación | atributo | acción | evento | documento | evidencia

- Significado:
  <definición humana>

- Notación sugerida:
  <Predicado(x)> o <Función(x)>

- Ejemplo:
  <ejemplo concreto>

- Participa en:
  reglas | invariantes | casos de uso | reportes | base de datos
```

### Conceptos del dominio

Formato importable cuando un concepto de vocabulario deba quedar como elemento lógico referenciable:

```md
### CON-XXX — <concepto del dominio>

- Fuente: entrevista | observación | documento
- Estado: pendiente | validado parcialmente | validado
- Descripción humana: <definición operacional>
- Lectura: <cómo debe entenderlo negocio y equipo técnico>
- Relacionada con: RN-..., ENT-..., ACC-...
```

```md
### Concepto: <nombre>

- Tipo lógico:
  <tipo>

- Significado:
  <definición>

- Notación sugerida:
  <notación>

- Ejemplo:
  <ejemplo>

- Participa en:
  <secciones donde aparece>
```

---

## 5. Predicados, proposiciones y símbolos permitidos

### 5.1 Símbolos permitidos

| Símbolo | Lectura | Uso típico |
|---|---|---|
| ∀ | para todo | reglas generales |
| ∃ | existe | existencia obligatoria |
| ¬ | no | negación o prohibición |
| ∧ | y | condiciones simultáneas |
| ∨ | o | alternativas |
| → | implica | si ocurre A, debe cumplirse B |
| = | igual | igualdad de valores |
| ≠ | distinto | diferencia |
| ≤ | menor o igual | límites superiores |
| ≥ | mayor o igual | mínimos |
| ∈ | pertenece a | pertenencia a conjunto de estados |

### 5.2 Predicados del dominio

| Predicado | Lectura | Tipo | Usado en |
|---|---|---|---|
| `Cliente(c)` | `c es cliente` | entidad | RN-..., INV-... |
| `Venta(v)` | `v es venta` | entidad | RN-..., POST-... |
| `Confirmada(v)` | `v está confirmada` | estado | RN-..., POST-... |
| `PerteneceA(d, v)` | `d pertenece a v` | relación | RN-... |

### 5.3 Reglas editoriales para fórmulas

Toda fórmula debe tener:

1. Descripción humana.
2. Forma lógica.
3. Lectura en español.
4. Impacto práctico.

No escribir una fórmula si no se puede explicar en español.

---

## 6. Reglas lógicas del negocio

### Propósito de la sección

Escribir las reglas importantes del negocio con precisión suficiente para orientar validaciones, relaciones, restricciones, pruebas y otros artefactos revisables.

### Formato de regla

```md
### RN-XXX — <nombre de la regla>

- Tipo:
  existencia | unicidad | cálculo | estado | autorización | trazabilidad | integridad | temporal | prohibición | transición

- Fuente:
  entrevista | observación | inferencia | normativa | decisión del cliente | criterio técnico

- Estado:
  borrador | pendiente de validar | validada | descartada

- Descripción humana:
  <regla en lenguaje natural>

- Forma lógica:
  <fórmula>

- Lectura de la forma lógica:
  <explicación en español>

- Razón de negocio:
  <por qué importa>

- Consecuencia si se rompe:
  <qué problema causa>

- Aplica a:
  macroflujos | flujos | casos de uso | entidades | reportes | migraciones

- Relacionada con:
  PRE-..., INV-..., POST-..., CU-..., ENT-...

- Impacto técnico futuro:
  - Base de datos:
  - Backend:
  - Frontend:
  - Pruebas:
  - Documentación:
```

### Tipos de reglas frecuentes

#### Regla de existencia

```text
∀x (Condición(x) → ∃y Relación(y, x))
```

#### Regla de prohibición

```text
¬∃x (Tipo(x) ∧ EstadoInválido(x))
```

#### Regla de cálculo

```text
∀x (Tipo(x) → Valor(x) = ParteA(x) - ParteB(x))
```

#### Regla de unicidad

```text
¬∃x∃y (Tipo(x) ∧ Tipo(y) ∧ x ≠ y ∧ Clave(x)=Clave(y))
```

---

## 7. Condiciones iniciales / precondiciones

### Propósito de la sección

Definir qué debe ser verdadero antes de ejecutar una acción, flujo o caso de uso.

### Formato de precondición

```md
### PRE-XXX — <nombre>

- Aplica a:
  ACC-... | CU-... | FL-...

- Descripción humana:
  <qué debe ser verdad antes>

- Forma lógica:
  <fórmula si aplica>

- Lectura:
  <explicación en español>

- Si no se cumple:
  rechazar operación | pedir dato | abrir flujo alterno | escalar autorización

- Validación esperada:
  frontend | backend | base de datos | revisión humana
```

---

## 8. Invariantes del negocio

### Propósito de la sección

Registrar verdades que deben mantenerse mientras el negocio opera. Las invariantes son candidatas naturales a validaciones fuertes, constraints, pruebas o auditoría.

### Formato de invariante

```md
### INV-XXX — <nombre>

- Ámbito:
  sistema | macroflujo | flujo | caso de uso | entidad | reporte | migración

- Descripción humana:
  <verdad que debe mantenerse>

- Forma lógica:
  <fórmula>

- Lectura:
  <explicación en español>

- Debe mantenerse:
  siempre | durante el flujo | al confirmar | al migrar | al reportar

- Acciones que pueden romperla:
  - ACC-...
  - CU-...

- Reglas relacionadas:
  - RN-...

- Riesgo si se rompe:
  <consecuencia>

- Validación técnica futura:
  base de datos | backend | frontend | pruebas | auditoría
```

### Indicadores de invariantes

- [ ] Tiene ID único.
- [ ] Describe una verdad que debe mantenerse.
- [ ] Tiene ámbito claro.
- [ ] Tiene lectura humana.
- [ ] Indica cuándo debe mantenerse.
- [ ] Indica qué acciones pueden romperla.
- [ ] Indica riesgo si se rompe.
- [ ] Puede convertirse en validación, constraint, prueba o auditoría.

---

## 9. Condiciones de cierre / postcondiciones

### Propósito de la sección

Definir qué debe quedar verdadero al terminar una acción, flujo o caso de uso.

### Formato de postcondición

```md
### POST-XXX — <nombre>

- Aplica a:
  ACC-... | CU-... | FL-...

- Descripción humana:
  <qué debe quedar verdadero al final>

- Forma lógica:
  <fórmula si aplica>

- Lectura:
  <explicación en español>

- Evidencia:
  comprobante | registro | estado | auditoría | reporte | documento

- Validación esperada:
  <cómo se comprueba>
```

### Regla práctica

Una postcondición sin evidencia es débil. Conviene poder comprobarla con datos, estado, comprobante, historial o auditoría.

---

## 10. Acciones transformadoras

### Propósito de la sección

Capturar acciones del negocio como mini algoritmos: estado inicial, transformación, invariantes protegidas y estado de cierre.

### Formato de acción transformadora

```md
### ACC-XXX — <nombre de la acción>

- Tipo:
  creación | consulta | actualización | anulación | cálculo | cierre | migración | reporte

- Actor principal:
  <actor>

- Estado inicial:
  <situación antes de iniciar>

- Condiciones iniciales:
  - PRE-...

- Transformación:
  1. <paso>
  2. <paso>
  3. <paso>

- Invariantes protegidas:
  - INV-...

- Estado de cierre:
  <resultado esperado>

- Postcondiciones:
  - POST-...

- Reglas aplicadas:
  - RN-...

- Entidades afectadas:
  - Crea:
  - Modifica:
  - Consulta:
  - Anula/elimina:

- Evidencia:
  <registro o comprobante>
```

---

## 11. Árbol operativo de macroflujos, flujos y casos de uso

### Propósito de la sección

Organizar el negocio jerárquicamente antes de diseñar entidades o tablas.

### Reglas de uso

- Los macroflujos agrupan áreas grandes de operación.
- Los flujos representan variantes concretas.
- Los casos de uso pueden repetirse en varios flujos.
- Si un caso de uso se repite, se referencia aquí y se define una sola vez en el catálogo.
- El árbol debe poder convertirse luego en grafo lógico.

### Formato del árbol

```md
### MF-001 — <Nombre del macroflujo>

- Objetivo:
  <qué gran operación agrupa>

- Inicio general:
  <evento inicial general>

- Cierre general:
  <resultado amplio esperado>

#### FL-001 — <Nombre del flujo>
- Usa CU-001 — <caso de uso>
- Usa CU-002 — <caso de uso>
- Usa CU-003 — <caso de uso>

#### FL-002 — <Nombre del flujo>
- Usa CU-001 — <caso repetido>
- Usa CU-004 — <caso de uso>
```

### Indicadores del árbol operativo

- [ ] Cada macroflujo tiene al menos un flujo.
- [ ] Cada flujo tiene inicio y cierre.
- [ ] Cada flujo usa casos de uso identificables.
- [ ] Los casos repetidos están referenciados, no duplicados.
- [ ] Cada caso de uso está definido en el catálogo único.
- [ ] El árbol puede transformarse en grafo lógico.

---

## 12. Catálogo único de casos de uso

### Propósito de la sección

Definir cada caso de uso una sola vez, aunque se use en varios flujos.

### Formato de caso de uso

```md
### CU-XXX — <nombre>

- Tipo:
  creación | consulta | actualización | anulación | cálculo | cierre | reporte | validación

- Actor principal:
  <actor humano o sistema externo>

- Objetivo:
  <resultado concreto>

- Usado en:
  - MF-... / FL-...
  - MF-... / FL-...

- Acción transformadora asociada:
  ACC-...

- Estado inicial:
  <situación antes de ejecutar>

- Condiciones iniciales / precondiciones:
  - PRE-...

- Datos de entrada:
  - <dato>

- Transformación principal:
  1. <paso>
  2. <paso>
  3. <paso>

- Invariantes protegidas:
  - INV-...

- Condición de cierre / postcondiciones:
  - POST-...

- Reglas aplicadas:
  - RN-...

- Entidades afectadas:
  - Crea:
  - Modifica:
  - Consulta:
  - Anula/elimina:

- Evidencia o trazabilidad:
  <registro, historial, comprobante, auditoría>

- Excepciones:
  - <caso alterno o error>

- Preguntas pendientes:
  - <pregunta para el cliente>
```

### Indicadores de caso de uso

- [ ] Tiene ID único.
- [ ] Tiene nombre en forma de acción.
- [ ] Tiene actor principal.
- [ ] Tiene objetivo verificable.
- [ ] Indica en qué flujos se usa.
- [ ] Tiene estado inicial.
- [ ] Tiene precondiciones.
- [ ] Tiene pasos principales.
- [ ] Tiene reglas aplicadas.
- [ ] Tiene invariantes protegidas.
- [ ] Tiene condición de cierre.
- [ ] Tiene entidades afectadas.
- [ ] Tiene evidencia o trazabilidad.
- [ ] Tiene excepciones.
- [ ] Tiene preguntas pendientes si hay ambigüedad.

---

## 13. Grafo lógico del negocio

### Propósito de la sección

Representar dependencias reales del negocio: reutilización de casos de uso, reglas aplicadas, invariantes protegidas, entidades afectadas y preguntas pendientes.

### 13.1 Tipos de nodos permitidos

| Prefijo | Tipo de nodo | Uso |
|---|---|---|
| MF | Macroflujo | Área grande de operación |
| FL | Flujo | Variante concreta de operación |
| CU | Caso de uso | Acción ejecutada por un actor |
| ACC | Acción transformadora | Transformación de estado |
| RN | Regla de negocio | Regla lógica del dominio |
| PRE | Precondición | Condición inicial |
| INV | Invariante | Verdad protegida |
| POST | Postcondición | Cierre verificable |
| ENT | Entidad candidata | Concepto persistente o documental propuesto desde la lógica |
| EST | Estado | Estado permitido o prohibido |
| REP | Reporte | Consulta o salida analítica |
| RISK | Riesgo | Posible inconsistencia |
| PEND | Pregunta pendiente | Duda que afecta diseño |

### 13.2 Nodos del grafo

| ID | Tipo | Nombre | Descripción | Estado |
|---|---|---|---|---|
| MF-001 | Macroflujo | `<nombre>` | `<descripción>` | borrador |

### 13.3 Relaciones del grafo

Relaciones recomendadas:

```text
contiene, usa, reutiliza, ejecuta, aplica, requiere, protege, garantiza,
crea, modifica, consulta, genera, alimenta, bloquea, habilita, depende_de, sustenta
```

| Origen | Relación | Destino | Lectura |
|---|---|---|---|
| MF-001 | contiene | FL-001 | `<lectura>` |

### 13.4 Reutilización de casos de uso

| Caso de uso | Usado en flujos | Observación |
|---|---|---|
| CU-XXX | FL-..., FL-... | `<caso reutilizable>` |

### 13.5 Invariantes críticas del grafo

| Invariante | Protegida por | Puede romperse en | Riesgo |
|---|---|---|---|
| INV-XXX | CU-... | FL-... | `<riesgo>` |

### 13.6 Preguntas pendientes conectadas al grafo

| Pregunta | Afecta nodos | Bloquea diseño |
|---|---|---|
| PEND-XXX | CU-..., ENT-... | sí/no/parcial |

### 13.7 Lectura para diseño de base de datos

| Relación del grafo | Lectura para base de datos |
|---|---|
| CU crea ENT | Probable tabla o registro persistente |
| CU modifica ENT | La entidad necesita estado o campos editables |
| CU consulta ENT | Puede requerir índice, filtro o búsqueda |
| RN sustenta ENT | La entidad o relación queda justificada por una regla |
| INV protege atributo | Puede requerir constraint, validación o test |
| REP depende_de ENT | La entidad alimenta reportes |

### Indicadores del grafo lógico

- [ ] Cada macroflujo importante aparece como nodo.
- [ ] Cada flujo importante aparece como nodo.
- [ ] Cada caso de uso importante aparece como nodo único.
- [ ] Los casos reutilizados están conectados a varios flujos.
- [ ] Cada regla crítica aparece como nodo.
- [ ] Cada invariante crítica aparece como nodo.
- [ ] Cada entidad candidata importante aparece como nodo.
- [ ] Cada relación tiene verbo claro.
- [ ] El grafo puede convertirse luego en MD importable para Domain Model Studio.

---

## 14. Entidades candidatas

### Propósito de la sección

Pasar de lógica a modelo de datos candidato sin inventar tablas innecesarias.

### Regla fuerte

```text
No crear entidad importante sin justificarla desde una acción, regla, estado, evidencia o reporte.
```

### Formato de entidad candidata

```md
### ENT-XXX — <nombre>

- Tipo:
  entidad fuerte | entidad débil | documento | evento | catálogo | historial | movimiento

- Fuente lógica:
  ACC-..., CU-..., RN-..., INV-..., POST-..., REP-...

- Justificación lógica:
  <por qué debe existir>

- Atributos candidatos:
  - ATR-XXX — <atributo>: <razón lógica>

- Relaciones candidatas:
  - REL-XXX — <entidad_origen> — <entidad_destino>: <regla o flujo que la justifica>

- Reglas asociadas:
  - RN-...

- Invariantes asociadas:
  - INV-...

- Casos de uso que la crean:
  - CU-...

- Casos de uso que la modifican:
  - CU-...

- Casos de uso que la consultan:
  - CU-...

- Riesgo si se modela mal:
  <consecuencia>
```


### Formato de atributo candidato

Usar este bloque cuando el atributo deba quedar importable y trazable, no solo narrado dentro de la entidad.

```md
### ATR-XXX — <Entidad>.<atributo>

- Pertenece a:
  ENT-XXX

- Tipo tentativo:
  texto | número | decimal | fecha | hora | booleano | enum | archivo | referencia

- Razón operativa:
  <por qué el negocio necesita registrar o calcular este dato>

- ¿Es calculado?:
  sí | no

- Fórmula o lectura de cálculo:
  <si aplica>

- Riesgo si se modela mal:
  <consecuencia>

- Fuente lógica:
  ACC-..., CU-..., RN-..., INV-..., POST-..., REP-...

- Reglas asociadas:
  - RN-...

- Invariantes asociadas:
  - INV-...
```

### Formato de relación candidata

Usar este bloque cuando una relación entre entidades deba quedar importable y justificable para modelos conceptuales, diccionario de datos o diseño posterior.

```md
### REL-XXX — <Entidad origen> — <Entidad destino>

- Entidad origen:
  ENT-XXX

- Entidad destino:
  ENT-XXX

- Cardinalidad tentativa:
  1 a 1 | 1 a muchos | muchos a muchos | opcional | obligatoria

- Justificación lógica:
  <regla, acción, flujo, estado o reporte que exige la relación>

- Fuente lógica:
  ACC-..., CU-..., RN-..., INV-..., POST-..., REP-...
```

### Indicadores de entidad candidata

- [ ] Está justificada por acción, regla, invariante, reporte, estado o evidencia.
- [ ] Tiene atributos candidatos justificados.
- [ ] Tiene relaciones candidatas justificadas.
- [ ] Indica qué casos de uso la crean, modifican o consultan.
- [ ] Tiene reglas e invariantes asociadas.

---

## 15. Estados y transiciones

### Propósito de la sección

Documentar estados permitidos y transiciones reales del negocio.

```md
### Entidad o proceso: <nombre>

#### Estados permitidos
- <estado 1>
- <estado 2>

#### Transiciones permitidas

| Desde | Hacia | Acción que la produce | Regla |
|---|---|---|---|
| <estado> | <estado> | ACC-... | RN-... |

#### Estados prohibidos
- <estado inválido>
```

---

## 16. Reportes y algoritmos internos

### Propósito de la sección

Capturar consultas, cálculos, filtros, ordenamientos y rankings que revelan datos necesarios.

### Formato de reporte

```md
### REP-XXX — <nombre del reporte>

- Objetivo:
- Pregunta que responde:
- Datos necesarios:
- Filtros:
- Ordenamiento:
- Cálculos:
- Entidades involucradas:
- Reglas relacionadas:
- Postcondición del reporte:
- Implicación para base de datos:
- Complejidad o riesgo si crece el volumen:
```

### Formato de cálculo

```md
### CALC-XXX — <nombre del cálculo>

- Descripción humana:
- Fórmula:
- Lectura:
- Datos necesarios:
- Entidades involucradas:
- Reglas relacionadas:
- Riesgo si se calcula mal:
- Validación esperada:
```

---

## 17. Indicadores para diseño de base de datos

### Propósito de la sección

Determinar si ya existe base lógica suficiente para proponer modelo de datos.

- [ ] Cada tabla candidata, si se propone después, proviene de una entidad candidata justificada.
- [ ] Cada entidad candidata tiene justificación lógica.
- [ ] Cada relación viene de una regla, acción, evidencia o flujo.
- [ ] Cada cardinalidad tiene explicación de negocio.
- [ ] Cada atributo importante tiene razón operativa.
- [ ] Cada atributo calculado tiene fórmula o regla.
- [ ] Cada constraint viene de una regla o invariante.
- [ ] Cada estado viene de una transición real.
- [ ] Cada dato usado en reportes tiene origen claro.
- [ ] Cada dato crítico tiene responsable o trazabilidad.
- [ ] Las reglas dudosas están marcadas como pendientes antes de diseñar.

### Traducción lógica → base de datos

| Elemento lógico | Traducción posible en base de datos |
|---|---|
| Entidad candidata | Tabla candidata |
| Atributo lógico | Columna |
| Relación lógica | Foreign key o tabla intermedia |
| Regla de existencia | Relación obligatoria, NOT NULL, validación |
| Regla de unicidad | UNIQUE |
| Regla de prohibición | CHECK, trigger o validación backend |
| Invariante | Constraint, validación, test o auditoría |
| Estado permitido | Enum, catálogo de estados o tabla de estados |
| Transición de estado | Regla backend o tabla de historial |
| Evidencia | Tabla de auditoría, historial o documento |
| Reporte | Consulta, vista, índice o tabla materializada |

---

## 18. Riesgos de inconsistencia

### Propósito de la sección

Identificar dónde se puede romper el sistema lógico del negocio.

```md
### RISK-XXX — <nombre>

- Descripción:
- Regla que podría romperse:
- Invariante afectada:
- Flujo donde puede ocurrir:
- Consecuencia:
- Prevención:
- Validación técnica futura:
```

### Indicadores de riesgos

- [ ] Se identificaron operaciones que pueden romper invariantes.
- [ ] Se identificaron reglas críticas que requieren validación fuerte.
- [ ] Se identificaron datos que pueden quedar desincronizados.
- [ ] Se identificaron procesos que requieren auditoría.
- [ ] Se identificaron excepciones peligrosas.
- [ ] Se identificaron reglas pendientes que bloquean diseño de datos.

---

## 19. Uso como fuente para otros artefactos

### Propósito de la sección

Indicar cómo este MD lógico puede alimentar futuras plantillas importables de Domain Model Studio.

> Este documento no reemplaza los Markdown importables de cada tipo de proyecto. Es una fuente lógica que puede orientar otros artefactos revisables cuando el usuario y la IA reutilizan IDs, nombres y reglas canónicas.

| Sección del MD lógico | Salida futura en Model Studio |
|---|---|
| Árbol operativo + grafo lógico | Grafo libre |
| Macroflujos y flujos | BPMN básico |
| Catálogo de casos de uso | UML casos de uso |
| Acciones transformadoras | UML actividad |
| Interacciones actor-sistema | UML secuencia |
| Entidades candidatas | Modelo conceptual |
| Atributos candidatos | Diccionario de datos |
| Estados y transiciones | UML estados |
| Actores y permisos | Roles/permisos |
| Pantallas necesarias | Wireframes / flujo de pantallas |
| Arquitectura futura | C4 / despliegue técnico |

### Indicadores para uso como fuente

- [ ] El árbol operativo puede servir como fuente para grafo libre.
- [ ] Los macroflujos y flujos pueden servir como fuente para BPMN.
- [ ] Los casos de uso pueden servir como fuente para UML casos de uso.
- [ ] Las acciones transformadoras pueden servir como fuente para UML actividad.
- [ ] Las entidades candidatas pueden servir como fuente revisable para modelo conceptual.
- [ ] Los atributos candidatos pueden servir como fuente para diccionario de datos.
- [ ] Los estados y transiciones pueden servir como fuente para UML estados.
- [ ] Los actores y permisos pueden servir como fuente para roles/permisos.
- [ ] Las pantallas necesarias pueden servir como fuente para wireframes o flujo de pantallas.

---

## 20. Preguntas pendientes para el cliente

### Propósito de la sección

Evitar que el analista o la IA invente reglas donde falta información.

| ID | Pregunta | Afecta | Prioridad | Bloquea diseño | Estado |
|---|---|---|---|---|---|
| PEND-001 | `<pregunta>` | `<CU, RN, ENT, INV>` | alta/media/baja | sí/no/parcial | pendiente |

### Indicadores de preguntas pendientes

- [ ] Cada pregunta tiene ID.
- [ ] Cada pregunta indica qué sección afecta.
- [ ] Cada pregunta tiene prioridad.
- [ ] Cada pregunta indica si bloquea diseño de base de datos.
- [ ] Cada pregunta indica si bloquea flujo, regla, entidad o reporte.
- [ ] Cada pregunta tiene estado: pendiente, respondida, descartada.

---

## 21. Nivel de madurez del levantamiento

### Nivel 0 — Conversación inicial
Hay contexto general, pero faltan flujos y reglas.

### Nivel 1 — Procesos identificados
Ya existen macroflujos y flujos principales.

### Nivel 2 — Casos de uso identificados
Cada flujo tiene casos de uso asociados.

### Nivel 3 — Reglas e invariantes identificadas
Las reglas principales están escritas y algunas formalizadas.

### Nivel 4 — Base lógica para diseño
Entidades, relaciones, estados y constraints están justificados.

### Nivel 5 — Listo como fuente revisable
El MD lógico puede usarse como fuente revisable para construir grafo, modelo conceptual, casos de uso, BPMN y diccionario de datos, sin generación automática ni sincronización entre proyectos.

### Nivel actual

```text
<Nivel 0 | 1 | 2 | 3 | 4 | 5>
```

---

## 22. Cierre del documento

### Resumen lógico

```text
<Resumen de las verdades principales descubiertas del negocio.>
```

### Decisiones confirmadas

- `<decisión validada>`

### Decisiones pendientes

- `<decisión pendiente>`

### Próximos pasos sugeridos

- [ ] Validar reglas pendientes con cliente.
- [ ] Completar casos de uso incompletos.
- [ ] Revisar invariantes críticas.
- [ ] Preparar Markdown revisable de modelo conceptual cuando el usuario lo necesite.
- [ ] Preparar Markdown revisable de grafo lógico importable cuando el usuario lo necesite.
- [ ] Proponer diseño inicial de base de datos.
