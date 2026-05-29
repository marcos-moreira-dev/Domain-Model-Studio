# Auditoría JavaDoc post-refactor — Tanda 38A

## Resultado

JavaDoc queda revisado en las zonas de mayor cambio del refactor integral. La revisión no intenta documentar cada getter ni reescribir comentarios correctos; solo alinea contratos técnicos que sí importan para mantenimiento.

## Zonas revisadas

| Zona | Criterio |
|---|---|
| `ApplicationServices` | Fachada de compatibilidad; código nuevo debe preferir familias. |
| `application.services` | Familias de casos de uso sin JavaFX, JSON ni Markdown concreto. |
| `ApplicationServicesFactory` | Ensamblador bootstrap, no ejecutor de comandos ni UI. |
| `application.catalog.definitions` | Catálogo por familias sin cambiar 19 tipos oficiales ni UENS. |
| `infrastructure.resources.definitions` | Recursos IA por familias sin cambiar importabilidad. |
| `.dms` | Readers/writers principales como coordinadores; no cambia formato JSON. |
| Markdown importable | `MarkdownImportDocument` centraliza frontmatter/cuerpo sin cambiar gramáticas, ejemplos oficiales ni comportamiento visible. |
| `ProjectChangeSupport` | Listener/loading/notificación común; no superclase visual. |
| UML Clases | Selección Resumen segura: 120 clases y 180 relaciones. |
| Canvas conceptual legacy | Refactor focalizado, sin migración forzada ni cambio de `.dms`. |
| Levantamiento lógico/Grafo lógico | Fuente lógica canónica y vista compatible, no promesa de derivación automática. |

## Lenguaje retirado del código main

- `fuente madre`.
- `vistas derivadas`.
- `artefactos derivados` como promesa general.

## Comando de generación vigente

```bat
scripts\31-generar-javadoc.bat
```

Salida esperada:

```text
target\site\apidocs\index.html
```

## Conclusión

La línea JavaDoc queda cerrada para esta etapa. Cualquier mejora posterior debe ser puntual y vinculada a una modificación real de código.
