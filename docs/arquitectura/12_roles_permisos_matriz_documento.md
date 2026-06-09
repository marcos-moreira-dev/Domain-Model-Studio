# Tanda 12 — Roles y permisos como matriz/documento

## Decisión

Roles y permisos se trata como una matriz administrativa y documental, no como canvas libre ni grafo de nodos. Su salida principal es una tabla rol × permiso con decisiones visibles y exportación documental.

## Modelo semántico

- `RoleNode`: rol operativo o administrativo.
- `PermissionNode`: permiso ligado a módulo, acción, alcance y descripción.
- `PermissionAssignment`: asignación explícita de un permiso a un rol.
- `PermissionDecision`: decisión visible de celda (`Permitido`, `Condicionado`, `Denegado`, `Sin asignar`, `No aplica`).
- `RolesPermissionsMatrixCell`: celda semántica calculada para la matriz.

## Reglas de decisión

| Estado de asignación | Decisión visible | Símbolo |
|---|---|---|
| Asignación permitida sin condición | Permitido | ✓ |
| Asignación permitida con condición | Condicionado | △ |
| Asignación bloqueada | Denegado | × |
| Sin asignación explícita | Sin asignar | — |
| Caso excepcional documental | No aplica | N/A |

## Vista

La vista matricial debe hablar en términos de roles, permisos, asignaciones y decisiones. No debe mostrar herramientas de nodos, conectores, bendpoints ni elementos de canvas.

## Exportación

- Markdown: documento formal con resumen, roles, permisos, asignaciones y matriz.
- SVG/PNG: salida matricial, no captura de canvas libre.
- PDF: queda como evolución natural documental, pero no se declara en capacidades hasta que exista flujo de exportación específico para este módulo.

## Validación

La validación debe advertir:

- roles sin permisos;
- asignaciones con rol inexistente;
- asignaciones con permiso inexistente;
- asignaciones duplicadas para la misma celda rol × permiso.

## Criterio de cierre

La tanda queda cerrada si roles/permisos queda clasificado como matriz/documento, si la matriz muestra decisiones claras, si el Markdown exportado es formal y si el PNG/SVG siguen representando una matriz, no un canvas falso.
