# Matriz de capacidades reales — inventario anti-fachada inicial

> Estado de trabajo: matriz viva actualizada en Tanda 30. El catálogo Java sigue siendo fuente primaria; esta matriz resume promesas visibles y debe validarse con tests/smoke antes de release.

## Convenciones

- **Crear**: existe flujo visible para abrir/crear el tipo.
- **Importar MD**: existe parser/importador Markdown asociado al tipo.
- **Editar**: existe una edición mínima real, no solo pantalla estática.
- **Guardar/Abrir DMS**: el tipo entra en persistencia `.dms`.
- **PNG/SVG/MD/PDF**: el formato se ofrece según la promesa del producto. **SVG** significa SVG vectorial documental: vectorial y escalable, no WYSIWYG universal.
- **Fuente primaria**: el catálogo Java (`DefaultDiagramTypeDefinitions` + `DefaultDiagramCapabilityCatalog`) es la fuente oficial de capacidades; esta matriz es documentación derivada y debe alinearse cuando el catálogo cambie.
- **Ejemplo IA**: existe ejemplo/plantilla/recurso oficial para IA.
- **Pendiente de smoke**: debe verificarse manualmente en las tandas de revalidación local y smoke visual.


## Columnas de smoke vigentes

Para cierre final, cada fila debe poder respaldarse con al menos una evidencia:

```txt
catálogo Java
+ toolbar/menú coherente
+ implementación real
+ test o guardarraíl
+ smoke manual cuando sea visual/exportable
```

Estados de smoke usados en esta matriz:

```txt
Protegido: no tocar salvo corrección crítica.
Pendiente de smoke: requiere revisión manual local.
Smoke documental: revisar salida Markdown/PDF.
Smoke visual: revisar PNG/SVG/viewport/legibilidad.
```

## Visuales principales

| Tipo | Crear | Importar MD | Editar | Guardar/Abrir DMS | PNG | SVG documental | MD | PDF | Ejemplo IA | Estado |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| Modelo conceptual | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Protegido; no tocar salvo emergencia |
| Grafo libre | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Asumido estable tras smoke |
| Grafo lógico del negocio | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Vista visual semántica derivada; smoke UENS y teoría académica requeridos |
| UML Clases | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Corregido en Tanda 11; requiere smoke final |
| Mapa de módulos | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| Flujo de pantallas | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| Wireframes administrativos | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| BPMN básico | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| Flujo operativo | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| UML Casos de uso | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| UML Actividad | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| UML Secuencia | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| UML Estados | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| C4 Contexto | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| C4 Contenedores | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |
| Despliegue técnico | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke |

## Documentales / matrices

| Tipo | Crear | Importar MD | Editar | Guardar/Abrir DMS | PNG | SVG documental | MD | PDF | Ejemplo IA | Estado |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| Levantamiento lógico | Sí | Sí | Sí | Sí | No | No | Sí | Sí | Sí | Expediente documental y fuente lógica canónica; PDF/Markdown como salidas revisables |
| Diccionario de datos | Sí | Sí | Sí | Sí | No | No | Sí | Sí | Sí | Smoke documental pendiente; importación Markdown activa |
| Roles y permisos | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí | Pendiente de smoke matriz tabular |

## Observaciones de producto

1. Todo diagrama visual debe tener como mínimo **PNG, SVG documental y Markdown** si se presenta como diagrama exportable.
2. Las salidas documentales no deben fingir ser canvas si su naturaleza es tabla/documento. Roles y permisos es una excepción tabular controlada: no es canvas libre, pero sí exporta PNG/SVG documental/Markdown como matriz.
3. Si en la siguiente tanda se detecta un botón muerto, debe decidirse inmediatamente: implementar mínimo real, ocultar/desactivar o marcar como pendiente honesto.
4. El modelo conceptual queda fuera de refactor y solo se verifica como referencia estable.
