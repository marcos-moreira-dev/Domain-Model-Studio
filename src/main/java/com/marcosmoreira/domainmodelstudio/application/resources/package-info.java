/**
 * Catálogo y exportación de recursos IA sin hardcodeo en la presentación.
 *
 * <p>Desde la Tanda 30, los descriptores oficiales se agrupan por familias de recurso.
 * El agregador público mantiene la lista completa, mientras cada familia explica si
 * ofrece plantillas, ejemplos mínimos, ejemplos UENS o recursos del Grafo lógico.</p>
 *
 * <p>La capa de presentación no debe conocer rutas físicas ni decidir qué recurso es
 * oficial. Debe consultar estos servicios y dejar la escritura de archivos al caso de
 * uso correspondiente.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.resources;
