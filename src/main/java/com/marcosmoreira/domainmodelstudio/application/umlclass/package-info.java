/**
 * Casos de uso del editor UML Clases e importación desde código fuente.
 *
 * <p>La generación desde código pasa por un modelo neutral de análisis de fuentes y luego
 * se transforma en un documento UML propio. La política {@code SourceCodeUmlSummarySelectionPolicy}
 * limita proyectos grandes para que la vista Resumen siga siendo legible sin cambiar parsing
 * Java/TypeScript, inferencia de relaciones ni layout visual.</p>
 *
 * <p>El código de aplicación no debe depender de JavaFX ni de controles de presentación; la
 * vista solo consume el documento y las vistas preparadas por los casos de uso.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.umlclass;
