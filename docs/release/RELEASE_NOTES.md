# Release notes

## 0.0.1-SNAPSHOT — Pre-RC instalable post Recursos IA

Fecha de cierre documental: 2026-05-29

### Resumen

La línea actual queda como **MVP funcional avanzado / pre-RC instalable Windows**. La base local reportada por el usuario tiene tests completos en verde después de P0, RIA-1 y RIA-1A. La prioridad de cierre pasa a empaquetado Windows: app-image, MSI y release candidate local mediante los scripts públicos vigentes.

### Cambios principales recientes

- P0 desbloqueó el build al retirar lenguaje obsoleto literal del estado documental vivo.
- RIA-1 auditó Recursos IA: plantillas públicas sincronizadas, plantilla conceptual importable, frontmatter reforzado y lenguaje de Grafo lógico/Levantamiento lógico alineado con fuente revisable.
- RIA-1A rebaselinizó el test del ejemplo lógico oficial para exigir `Entidades candidatas` y bloquear el título viejo.
- La documentación de ejecución y empaquetado se alinea con `scripts\00`, `01`, `02`, `13`, `14`, `15`, `16` y `31`.
- El README raíz vuelve a apuntar a `14-app-image-completa.bat`, `15-msi-completo.bat` y `16-release-candidate.bat`.

### Evidencia automatizada local

El usuario reportó tests completos verdes después de RIA-1A. Para release instalable, la evidencia final debe quedar en los reportes manuales y manifiestos generados por `dist\staging`, `dist\installer` y `dist\release`.

### Flujo vigente de cierre

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

### Criterio de publicación local

Puede tratarse como base automatizada verde si `scripts\02-ejecutar-tests.bat` termina en `BUILD SUCCESS`.

Puede tratarse como release candidate instalable solo si además:

- app-image abre;
- MSI instala y desinstala;
- la app instalada encuentra sus recursos;
- guardar/reabrir `.dms` funciona;
- exportaciones básicas funcionan;
- Recursos IA se exportan correctamente;
- limitaciones conocidas están documentadas;
- el reporte `docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md` queda completado.

## Antecedentes compactos

- El Grafo lógico del negocio está productizado como tipo disponible con catálogo, teoría, SideDock, validación, Markdown, SVG, PNG, Recursos IA y persistencia `.dms`.
- UML Secuencia incorpora fragmentos combinados básicos `alt`, `opt`, `loop`, `par`, `break`, `critical` y `ref`.
- UML Clases permite importación desde código Java/TypeScript con vistas internas y selección segura para proyectos grandes.
- SVG se declara vectorial documental, no copia WYSIWYG universal del canvas.
- Wireframes, BPMN, UML y C4 se mantienen como artefactos documentales/académicos simplificados, no reemplazos industriales completos.
