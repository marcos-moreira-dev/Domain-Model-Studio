# PF09 — Plan de pruebas de cierre funcional y smoke test visual real

Estado: **planificado**  
Tipo: **solo documentación de planificación**  
Alcance: **no implementa pruebas, no modifica código**

## 1. Propósito

PF09 planifica el cierre funcional real de Domain Model Studio. El objetivo es evitar que el producto vuelva a declarar capacidades que no están conectadas en la interfaz.

I12 agregó pruebas de coherencia mínima, pero no detectó un fallo visual crítico: algunos tipos de diagrama cambiaban toolbar, pero no montaban su lienzo/workspace correspondiente.

PF09 corrige ese enfoque con una política de pruebas orientada a producto:

```text
si el usuario puede verlo en la app, debe poder probarse como comportamiento real
```

## 2. Principios de prueba

### 2.1 Probar promesas visibles

Toda capacidad visible debe tener prueba o checklist:

- crear proyecto;
- abrir proyecto;
- guardar proyecto;
- importar Markdown;
- editar manualmente;
- mostrar workspace real;
- cambiar toolbar contextual;
- actualizar panel izquierdo;
- actualizar panel derecho;
- exportar PNG/PDF/SVG/Markdown según corresponda;
- mostrar ayuda teórica;
- cargar ejemplo oficial.

### 2.2 Probar por pestaña activa

El shell debe comportarse así:

```text
pestaña activa → proyecto activo → tipo activo → workspace activo → toolbar activa → acciones activas
```

La prueba debe cubrir que abrir un proyecto de otro tipo no contamine el workspace anterior.

### 2.3 Probar sin inflar clases

La implementación de pruebas y producción debe respetar SRP:

- no meter verificaciones enormes en `MainShellView`;
- no convertir `ProductMinimumCoherenceTest` en una clase gigante;
- separar pruebas por contrato;
- crear fixtures pequeños;
- usar nombres legibles.

## 3. Brecha principal que PF09 debe impedir

Caso real observado:

```text
UML Casos de uso nuevo
→ toolbar específica aparece
→ área central sigue en pantalla de inicio
→ panel izquierdo dice Sin proyecto abierto
→ panel derecho dice Sin proyecto abierto
```

Eso demuestra que las pruebas actuales no garantizan montaje visual real por tipo.

## 4. Nuevas familias de pruebas automáticas sugeridas

### 4.1 WorkspaceRegistryCoherenceTest

Objetivo: todo tipo visible con salida visual/documental debe tener workspace registrado.

Debe verificar:

```text
DefaultDiagramTypeRegistry.findAll()
→ para cada tipo disponible
→ existe definición de workspace/editor
→ el workspace declara si es visual o documental
→ el workspace declara acciones soportadas
```

No debe requerir abrir JavaFX completo si se diseña un contrato testeable sin UI pesada.

### 4.2 WorkspaceRoutingContractTest

Objetivo: al activar un tipo, el router resuelve el workspace correcto.

Casos mínimos:

- conceptual-model → canvas conceptual;
- data-dictionary → vista documental de diccionario;
- uml-class → editor UML clases;
- admin-wireframes → editor de wireframes;
- bpmn-basic → editor de comportamiento;
- uml-use-case → editor de comportamiento;
- uml-activity → editor de comportamiento;
- uml-sequence → editor de comportamiento;
- uml-state → editor de comportamiento;
- c4-context/c4-containers/technical-deployment → editor arquitectura.

### 4.3 ActiveWorkspaceActionContractTest

Objetivo: las acciones globales dependen del workspace activo.

Debe verificar:

- exportar PNG se habilita solo si el workspace activo lo soporta;
- exportar PDF se habilita para salida documental cuando aplique;
- exportar Markdown se habilita si el tipo lo promete;
- validar se enruta al validador correcto;
- ayuda abre la ficha teórica correcta;
- ejemplo oficial se asocia al tipo correcto.

### 4.4 ImportLayoutRoutingTest

Objetivo: importar Markdown no debe aplicar siempre layout Chen.

Debe verificar:

- conceptual-model usa layout conceptual;
- bpmn-basic usa layout de proceso;
- uml-use-case usa layout de casos de uso;
- uml-sequence usa layout temporal;
- admin-wireframes usa layout de pantallas;
- data-dictionary no exige lienzo visual si su salida principal es documental.

### 4.5 OfficialExamplesImportSmokeTest

Objetivo: ejemplos oficiales importables realmente importan.

Debe verificar:

- cada ejemplo importable tiene `diagram_type` registrado;
- el parser/dispatcher reconoce el tipo;
- el resultado abre el workspace correspondiente;
- no se pierden metadatos mínimos;
- no falla el roundtrip Markdown si el tipo lo promete.

### 4.6 HelpCenterContentCompletenessTest

Objetivo: la micro-Wikipedia no quede como lluvia de ideas.

Debe verificar por tipo:

- existe ficha teórica;
- existe título;
- existe explicación “qué es”;
- existe “cuándo usarlo”;
- existen elementos principales;
- existen relaciones/reglas de lectura cuando apliquen;
- existen errores comunes;
- existe al menos una figura didáctica o referencia de figura planificada;
- el contenido no habla de clases internas, viewmodels ni detalles de programación.

### 4.7 ProductVisibleLanguageTest

Objetivo: evitar jerga interna en la UI/documentación pública.

Debe bloquear o advertir frases visibles como:

```text
ViewModel
renderer interno
placeholder técnico
backend de la app de diagramas
implementado por infraestructura
estado interno
```

Excepción: cuando esos términos aparezcan como contenido de un diagrama C4/UML del usuario, no como lenguaje de la propia app.

### 4.8 ShellNoGodClassRegressionTest

Objetivo: sostener responsabilidad única y trazabilidad humana.

No debe ser una prueba rígida absurda, pero sí una auditoría de olores.

Puede revisar:

- tamaño aproximado de clases críticas;
- cantidad de métodos públicos;
- métodos excesivamente largos;
- crecimiento de `MainShellView`, `MainShellCommandHandler`, `ManualContent`;
- existencia de fachadas/routers/adaptadores cuando la complejidad ya lo exige.

La prueba puede empezar como reporte o checklist antes de volverse bloqueo duro.

## 5. Smoke test visual manual por tipo

Además de pruebas automáticas, debe existir un checklist manual porque JavaFX y exportaciones visuales requieren verificación humana.

Ruta futura sugerida:

```text
docs/testeo/checklists/smoke_visual_por_tipo.md
```

Cada tipo debe probar:

1. Crear proyecto nuevo.
2. Verificar que se abre workspace real.
3. Verificar que la toolbar específica cambia.
4. Agregar 2 o 3 elementos.
5. Seleccionar elemento y revisar panel de propiedades.
6. Revisar panel izquierdo/estructura.
7. Guardar `.dms`.
8. Cerrar y abrir `.dms`.
9. Exportar salida prometida.
10. Importar ejemplo oficial si aplica.
11. Abrir ayuda teórica del tipo.
12. Cambiar a otra pestaña y volver sin perder estado.

## 6. Matriz de cierre funcional por tipo

| Tipo | Crear | Workspace | Toolbar | Paneles | Guardar/abrir | Importar MD | Exportar salida | Ayuda | Ejemplo | Estado esperado |
|---|---|---|---|---|---|---|---|---|---|---|
| Modelo conceptual | Sí | Canvas conceptual | Sí | Sí | Sí | Sí | SVG/PNG/MD | Sí | Sí | Debe pasar |
| Diccionario de datos | Sí | Documento/tabla | Sí | Sí | Sí | Sí | PDF/MD | Sí | Sí | Debe pasar |
| BPMN básico | Sí | Comportamiento | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| Flujo operativo | Sí | Comportamiento | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| C4 Contexto | Sí | Arquitectura | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| C4 Contenedores | Sí | Arquitectura | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| Despliegue técnico | Sí | Arquitectura | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| UML Casos de uso | Sí | Comportamiento | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| UML Clases | Sí | UML clases | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| UML Actividad | Sí | Comportamiento | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| UML Secuencia | Sí | Comportamiento | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| UML Estados | Sí | Comportamiento | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| Mapa de módulos | Sí | Módulos | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| Roles y permisos | Sí | Roles/permisos | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| Flujo de pantallas | Sí | Flujo pantallas | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |
| Wireframes administrativos | Sí | Wireframes | Sí | Sí | Sí | Sí | PNG/MD | Sí | Sí | Debe pasar |

## 7. Scripts de validación propuestos

No se implementan en esta tanda, pero se recomienda terminar con scripts claros:

```text
scripts/11-validar-alineacion-funcional.bat
scripts/12-validar-ejemplos-uens.bat
scripts/13-validar-ayuda-teorica.bat
scripts/14-smoke-release-producto.bat
```

Cada script debe delegar en comandos pequeños, no duplicar lógica.

## 8. Criterios de cierre de producto

El cierre real de producto debe exigir:

- pruebas unitarias y de contrato pasando;
- ejemplos oficiales pasando importación/roundtrip cuando aplique;
- checklist visual manual completado;
- cero tipos visibles con workspace ausente;
- cero toolbars sin handler real;
- cero exportaciones visibles falsas;
- ayuda teórica disponible por tipo;
- textos visibles sin jerga interna;
- arquitectura sin crecimiento descontrolado de clases críticas.

## 9. Qué NO se implementa en esta tanda

- No se crean pruebas Java.
- No se modifican scripts.
- No se ejecuta Maven.
- No se modifica JavaFX.
- No se cambia el catálogo.
- No se corrigen workspaces.
- No se crean ejemplos UENS.

## 10. Relación con implementación posterior

PF09 debe convertirse después en las primeras tandas de implementación, porque las pruebas deben proteger la corrección antes de tocar muchas pantallas.

Orden recomendado:

```text
I13-A: crear contratos/pruebas de workspace y capacidades
I13-B: conectar BehaviorDiagramEditorView al shell
I13-C: crear router/registro de workspaces
I13-D: alinear menús y exportaciones con capacidades
I13-E: corregir importación/layout por tipo
I13-F: implementar ayuda teórica por recursos
I13-G: implementar selector y ejemplos UENS
I13-H: ejecutar smoke visual y cierre documental real
```
