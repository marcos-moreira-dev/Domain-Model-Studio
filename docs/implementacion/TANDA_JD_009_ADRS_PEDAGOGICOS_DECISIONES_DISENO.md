# Tanda JD-009 — ADRs pedagógicos y decisiones de diseño

## Estado

Aplicada.

## Alcance

Esta tanda no modifica lógica funcional. Agrega registros pedagógicos de decisiones arquitectónicas para explicar por qué el proyecto quedó organizado de cierta forma.

## Archivos agregados

```txt
docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md
docs/calidad/AUDITORIA_JAVADOC_JD9.md
src/test/java/com/marcosmoreira/domainmodelstudio/productization/Jd9PedagogicalArchitecturalDecisionRecordsSourceTest.java
scripts\02-ejecutar-tests.bat
```

## Archivos actualizados

```txt
docs/desarrollo/ONBOARDING.md
docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md
docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md
docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md
docs/calidad/PLAN_TANDAS_JAVADOC.md
docs/documentacion/MAPA_DOCUMENTACION_VIVA.md
docs/raiz/PLAN_TANDAS_RESTANTES.md
docs/implementacion/00_indice_implementacion.md
scripts/README.md
src/main/java/com/marcosmoreira/domainmodelstudio/*/package-info.java
```

## Decisiones cubiertas

```txt
ADR-001 — SideDock transversal y sidebar conceptual separado.
ADR-002 — Grafo lógico con dominio propio, no como FreeGraphDocument.
ADR-003 — Ayuda académica separada de ayuda operativa.
ADR-004 — Canvas transversal mediante adaptadores.
ADR-005 — Markdown oficial y .dms como formatos con responsabilidades distintas.
ADR-006 — Regla visual de esquinas rectas en UI nueva o corregida.
ADR-007 — Release Candidate con evidencia automatizada y smoke manual separado.
```

## Criterio pedagógico

Los ADRs explican alternativas, decisión y consecuencias. La intención no es justificar todo el código, sino conservar decisiones de diseño que ayudan a estudiar ingeniería de software.

## Validación

Ejecutar:

```bat
scripts\02-ejecutar-tests.bat
```

Luego generar JavaDoc:

```bat
scripts\31-generar-javadoc.bat
```

Y finalmente validar global:

```bat
scripts\02-ejecutar-tests.bat
```


## Nota de alcance JD-9

Esta documentación se conserva como ADR pedagógico; sin cambios funcionales.
