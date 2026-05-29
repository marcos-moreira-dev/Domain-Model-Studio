# Gramática IA — Wireframes administrativos

Estado: recurso de trabajo con IA.  
Importable por la app: sí, mediante `diagram_type: "admin-wireframes"`.  
Editor visual implementado: sí, como maqueta estructural exportable.  
Uso recomendado: planificar pantallas administrativas con geometrías primitivas, sin convertir el resultado en diseño gráfico final ni en frontend real.

## Principio

Un wireframe administrativo en Domain Model Studio es un **diagrama/maqueta de interfaz**. Su objetivo es levantar requerimientos: qué pantallas existen, qué datos se muestran, qué acciones hay, cómo se filtra, cómo se navega y qué estados debe contemplar la operación.

No debe confundirse con una pantalla final de Angular, JavaFX, React ni Figma. Los bloques son rectángulos, líneas y controles simulados; su finalidad es estructural y conservadora.

## Bloques admitidos

- Barra superior: navegación global, usuario, búsqueda o acciones generales.
- Menú lateral: módulos o secciones principales.
- Sección / panel / tarjeta: agrupadores funcionales.
- Formulario: conjunto de campos para crear o editar un registro.
- Campo: dato visible o editable.
- Filtro / búsqueda: criterios para reducir listados.
- Tabla: resultados, registros o matriz.
- Paginación: navegación de resultados.
- Botón: acción del usuario.
- Pestañas: subdivisiones de una pantalla o detalle.
- Modal: diálogo de confirmación o edición puntual.
- Alerta: mensaje de error, advertencia o estado.
- Gráfico simulado: visualización resumida, sin cálculo real.
- Reporte: bloque de documento, vista previa o salida administrativa.
- Detalle: panel con información ampliada del registro seleccionado.

## Estructura importable sugerida

```md
---
diagram_type: "admin-wireframes"
name: "Wireframes administrativos — ejemplo"
importable: true
---
# Pantallas

## Listado administrativo
id: listado_administrativo
módulo: gestión
propósito: consultar, filtrar y abrir registros.
notas: maqueta estructural para levantamiento.

### Secciones
- barra superior: título del módulo y acciones globales.
- menú lateral: navegación de módulos.
- panel de filtros: criterios principales.
- alerta de validación: mensajes operativos.

### Controles
- búsqueda rápida: nombre, código o documento.
- tabla de resultados: columnas principales y acciones por fila.
- paginación: total, página actual y tamaño.
- botón nuevo registro: abre formulario.
```

## Plantillas de pantalla recomendadas

- Login.
- Dashboard administrativo.
- Listado CRUD.
- Formulario de registro.
- Reportes.
- Roles y permisos.
- Configuración.
- Maestro-detalle.

## Reglas

- Usar lenguaje del negocio, no jerga interna de programación.
- Priorizar datos, acciones, validaciones visibles y estados operativos.
- Incluir pantallas vacías, errores, permisos insuficientes y confirmaciones cuando sean importantes.
- Evitar color, branding, pixel perfect o detalles de frontend final.
