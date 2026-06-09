/**
 * Superficie visual transversal para diagramas editables.
 *
 * <p>Este paquete contiene el lienzo canónico: workspace grande, grilla, capas,
 * zoom anclado, paneo por scrollbars, mapeo de coordenadas y contrato de exportación.
 * No debe importar familias concretas de diagramas. La lógica de módulos, UML, C4,
 * BPMN, wireframes o modelo conceptual debe llegar mediante adaptadores/render kits
 * externos.</p>
 *
 * <p>Esta capa es deliberadamente más baja que {@code interactivecanvas}: aquí se estudian
 * viewport, zoom, paneo, workspace dinámico y export surface; la semántica de selección y
 * edición llega después mediante adaptadores.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;
