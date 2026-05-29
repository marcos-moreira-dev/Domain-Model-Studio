/**
 * Catálogo oficial de tipos, categorías, capacidades y metadatos de proyecto.
 *
 * <p>Este paquete es la fuente de verdad para saber qué promete cada tipo visible: estado
 * de producto, workspace, ejemplos, gramática, guía académica y capacidades reales. La UI
 * debe consultar estos contratos en vez de quemar listas paralelas.</p>
 *
 * <p>Desde la Tanda 30, las definiciones concretas viven en familias bajo
 * {@code application.catalog.definitions}; {@code DefaultDiagramTypeDefinitions}
 * queda como agregador público. Esa separación permite tocar una familia de tipos
 * sin editar un catálogo monolítico ni arriesgar contratos de UENS, UML o Markdown.</p>
 *
 * <p>El catálogo es una pieza anti-fachada: si una capacidad aparece aquí, debe existir una
 * implementación, una prueba y una documentación coherentes.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.catalog;
