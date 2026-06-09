# Tanda 14 — Limpieza SOLID de clases grandes

## Objetivo

Reducir deuda de responsabilidad única en zonas de alta coordinación sin reescribir el proyecto ni tocar de forma agresiva el canvas conceptual. Esta tanda prioriza extracciones pequeñas, seguras y trazables.

## Criterio aplicado

- Si una clase coordina muchos comandos, se extraen coordinadores pequeños.
- Si una vista o viewmodel depende de infraestructura concreta, se introduce un puerto de aplicación.
- Si una clase grande aún no puede dividirse con seguridad, se deja inventariada con prioridad y riesgo.
- No se parte código por cantidad de líneas; se parte cuando aparece una responsabilidad nombrable.

## Cambios estructurales realizados

### 1. `EditorActivationGuard`

Se extrajo la verificación común de editor activo desde `MainShellCommandHandler`.

Responsabilidad:

- comprobar si un editor contextual está activo;
- emitir mensaje de estado cuando el usuario intenta ejecutar una acción fuera de contexto.

Esto reduce repetición de mensajes y evita que los comandos especializados acumulen detalles de presentación.

### 2. `SpecializedProjectSynchronizer`

Se extrajo la sincronización común de proyectos editados por workspaces especializados.

Responsabilidad:

- actualizar el proyecto de la sesión activa;
- marcar la pestaña como modificada;
- refrescar estado visible del proyecto;
- marcar el shell como sucio.

Esto evita que UML, roles/permisos, wireframes, flujo de pantallas, comportamiento y arquitectura repitan la misma regla.

### 3. `UnsavedChangesDialog`

Se aisló el diálogo de confirmación para acciones que pueden descartar cambios.

Responsabilidad:

- presentar la decisión guardar / no guardar / cancelar;
- delegar el guardado al callback recibido;
- devolver una decisión booleana al shell.

`MainShellCommandHandler` conserva la orquestación, pero ya no contiene toda la construcción del diálogo principal de reemplazo/cierre.

### 4. Puerto `SourceMarkdownSynchronizer`

Se corrigió una frontera de capas detectada en `InspectorViewModel`.

Antes:

```txt
presentation/inspector -> infrastructure/markdown/MarkdownProjectWriter
```

Después:

```txt
presentation/inspector -> application/project/SourceMarkdownSynchronizer
infrastructure/markdown/FileSystemSourceMarkdownSynchronizer -> MarkdownProjectWriter + Files
```

Esto reduce acoplamiento directo de presentación hacia infraestructura. El inspector ya no conoce el writer Markdown concreto ni `Files.writeString(...)`.

## Inventario de clases grandes

| Clase | Problema | Acción en esta tanda | Acción futura |
|---|---|---|---|
| `MainShellCommandHandler` | Mucha coordinación de comandos y sesiones. | Extracción de guard, sincronizador y diálogo. | Extraer `ProjectSessionCoordinator`, `EditorTabCoordinator`, `ExportCoordinator`. |
| `ApplicationServices` | Fachada demasiado grande. | Documentada como fachada superior. | Dividir en fachadas por familia: conceptual, visual, documental, exportación, catálogos. |
| `DmsProjectJsonReader` | Lectura monolítica de secciones del `.dms`. | Inventariada. | Dividir por secciones: metadata, conceptual, visual, documentos, matrices. |
| `DmsProjectJsonWriter` | Escritura monolítica. | Inventariada. | Crear writers por secciones y una fachada de escritura. |
| `InspectorViewModel` | ViewModel grande y tenía dependencia directa a infraestructura. | Se corrigió la dependencia directa a Markdown infra. | Separar inspector conceptual, visual, documental y matriz. |
| `DiagramCanvasView` | Canvas conceptual maduro pero grande. | Protegido, no tocado. | Extraer controladores solo con cobertura: selección, conectores, etiquetas, viewport. |
| `DiagramCanvasViewModel` | Estado y comandos conceptuales concentrados. | Protegido. | Separar estados de selección, layout, historial y comandos. |
| `MainShellView` | Construcción UI extensa. | No tocado. | Extraer factories/componentes de menú, toolbar, workspace host. |

## Reglas para siguientes refactors

1. Mantener fachadas temporales si reducen riesgo.
2. Extraer de una responsabilidad a la vez.
3. Validar después de cada extracción.
4. No tocar el modelo conceptual de forma agresiva.
5. No introducir jerga técnica en textos visibles de usuario.
6. Las clases nuevas deben tener nombre de patrón o responsabilidad clara cuando corresponda.

## Estado

Esta tanda no busca cerrar toda la deuda SOLID. Deja una primera reducción segura y marca el camino para que la limpieza continúe sin romper funcionalidades estabilizadas.
