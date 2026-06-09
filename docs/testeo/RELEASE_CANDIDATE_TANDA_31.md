# Release Candidate local — Tanda 31

## Propósito

Guía vigente para validar Domain Model Studio como Release Candidate local después de la productización del `logical-business-graph`.

Esta guía reemplaza la línea histórica de Tanda 40 como continuidad activa, pero conserva compatibilidad con los scripts y reportes históricos de empaquetado.

## Precondiciones

- Java 21 disponible.
- Maven disponible.
- Para app-image/MSI: `jpackage` disponible.
- Proyecto limpio, sin cambios parciales no deseados.
- Última corrida de tests o smoke local revisada.

## Validación automatizada mínima

Ejecutar:

```bat
scripts\27-validar-cierre-tests-post-grafo-logico-productivo.bat
scripts\28-validar-tanda42-validacion-integral-grafo-logico.bat
scripts\02-ejecutar-tests.bat
```

Criterio:

```txt
BUILD SUCCESS
Failures: 0
Errors: 0
```

## Validación local completa

Ejecutar:

```bat
scripts\13-revalidacion-local-completa.bat
```

Debe generar o abrir evidencias de:

- tests completos;
- smoke render automático;
- métricas de refactor;
- smoke UI mínimo;
- smoke de levantamiento lógico.

## App-image

Ejecutar:

```bat
scripts\14-app-image-completa.bat
```

Comprobar:

- existe `dist\staging\APP_IMAGE_MANIFEST.txt`;
- existe `dist\staging\Domain Model Studio\Domain Model Studio.exe`;
- la app abre fuera del entorno de desarrollo;
- se puede abrir/importar un ejemplo oficial;
- se puede guardar/reabrir `.dms`.

## MSI

Ejecutar:

```bat
scripts\15-msi-completo.bat
```

Comprobar:

- existe `dist\installer\MSI_MANIFEST.txt`;
- existe un `.msi` en `dist\installer`;
- instala correctamente;
- abre la app instalada;
- desinstala correctamente.

## Release Candidate final

Ejecutar:

```bat
scripts\16-release-candidate.bat
```

Comprobar:

- existe `dist\release\RELEASE_CANDIDATE_MANIFEST.txt`;
- `docs\testeo\reportes\REPORTE_RELEASE_CANDIDATE_TANDA_31.md` queda completado;
- las limitaciones conocidas están aceptadas o existe decisión de no aprobar.

## Smoke manual mínimo

| Área | Acción esperada |
|---|---|
| Inicio | La pantalla de inicio abre sin cambios inesperados. |
| Modelo conceptual | Sigue usando canvas propio y no se contamina con SideDock nuevo. |
| Grafo lógico | Abre ejemplo UENS, muestra leyenda, valida sin hallazgos semánticos. |
| Conectores | Los puntos intermedios se insertan sin deformar rutas. |
| Exportación | Markdown, SVG y PNG funcionan donde se declaran. |
| Persistencia | Guardar/reabrir `.dms` conserva documentos especializados y layout. |
| Ayuda | Menú Ayuda abre guía académica; SideDock Ayuda es operativa. |
| Recursos IA | Se exportan plantillas, gramáticas, ejemplos y prompts sin contradicción. |

## Criterio de decisión

- **Aprobado:** tests verdes, app-image funcional, MSI validado, smoke manual sin bloqueos.
- **Aprobado con observaciones:** solo limitaciones documentadas o detalles visuales no bloqueantes.
- **No aprobado:** fallo de tests, app no abre, MSI inválido, pérdida de `.dms`, exportaciones rotas o contaminación del modelo conceptual.
