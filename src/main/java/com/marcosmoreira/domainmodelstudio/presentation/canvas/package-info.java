/**
 * Canvas conceptual legacy: figuras, conectores, selección, zoom y movimiento visual.
 *
 * <p>Este paquete conserva el canvas conceptual legacy y editor conceptual clásico y su integración con Chen y
 * Crow's Foot. La Tanda 35 solo extrajo mecánicas internas pequeñas: historial de
 * edición y resolución de anclas. No migró el canvas conceptual al canvas transversal,
 * no cambió formato {@code .dms} y no alteró la semántica ER.</p>
 *
 * <p>Para estudiar esta zona, leer primero {@code DiagramCanvasViewModel}; luego revisar
 * {@code ConceptualCanvasEditHistory} para undo/redo y {@code ConceptualAnchorResolver}
 * para el criterio geométrico de anclas.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation.canvas;
