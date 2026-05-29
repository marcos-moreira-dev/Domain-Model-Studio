# Checklist manual — UML Clases desde código fuente

## Preparación

- Abrir Domain Model Studio.
- Tener un proyecto de prueba con backend Java/Spring y frontend TypeScript/Angular.
- Confirmar que existan carpetas pesadas para validar filtros: `target`, `node_modules`, `dist` o similares.

## Flujo principal

1. Crear o abrir un proyecto UML Clases.
2. Usar `Archivo → Importar UML desde código fuente...`.
3. Seleccionar la carpeta raíz del sistema.
4. Revisar la vista previa:
   - raíces detectadas;
   - lenguajes;
   - archivos incluidos;
   - rutas ignoradas;
   - vistas sugeridas.
5. Confirmar la importación.

## Verificación visual

- Deben aparecer clases Java y TypeScript.
- Backend y frontend no deben quedar mezclados sin agrupación.
- Deben existir vistas internas como Backend, Frontend, Integración y Todo cuando aplique.
- Los contenedores deben mostrar módulos/carpeta/package.
- Los nombres largos deben truncarse visualmente sin perder tooltip/metadatos.
- El lienzo debe permitir panear y hacer zoom sobre todo el contenido.

## Verificación funcional

- Buscar una clase por nombre.
- Filtrar por tipo de clase.
- Cambiar entre vistas internas.
- Centrar selección.
- Exportar Markdown.
- Exportar SVG.
- Exportar PNG desde la vista activa.
- Guardar como `.dms`.
- Reabrir `.dms` y confirmar que vistas/metadatos siguen presentes.

## Verificación de compatibilidad

- Importar un Markdown oficial de UML Clases generado por IA.
- Confirmar que el flujo Markdown sigue funcionando independientemente del importador desde código.
