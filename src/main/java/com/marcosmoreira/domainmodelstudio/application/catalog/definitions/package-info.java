/**
 * Definiciones oficiales de tipos de proyecto organizadas por familia funcional.
 *
 * <p>La API pública sigue siendo {@code DefaultDiagramTypeDefinitions.all()}, pero los
 * contratos viven aquí en archivos pequeños: análisis de negocio, datos, procesos,
 * arquitectura, UML, administración y documentación técnica. Esta división evita que
 * un catálogo central mezcle todos los textos visibles, capacidades, ejemplos y
 * workspaces.</p>
 *
 * <p>Las familias no cambian comportamiento: preservan los 19 tipos oficiales, sus
 * ejemplos importables y la regla de un solo Levantamiento lógico oficial UENS. La
 * fábrica común solo reduce repetición de constructores y rutas de recursos.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;
