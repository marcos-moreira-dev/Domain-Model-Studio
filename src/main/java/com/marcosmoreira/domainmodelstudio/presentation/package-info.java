/**
 * Capa de presentación JavaFX.
 *
 * <p>Contiene vistas, ViewModels, componentes visuales, shell, workspaces,
 * SideDock, canvas transversal y manejo de interacción. Es la única capa que
 * debe depender directamente de JavaFX.</p>
 *
 * <p>Ruta de estudio JD-7: leer esta capa al final. Primero entender dominio,
 * aplicación e infraestructura; luego revisar cómo la UI adapta esos contratos a
 * pestañas, paneles, toolbar, SideDock, canvas y validación visible.</p>
 *
 * <p>La presentación no debe contaminar el modelo conceptual protegido ni mover
 * reglas semánticas hacia renderers. Cuando necesita dibujar un tipo visual, usa
 * adaptadores y ViewModels específicos.</p>
 * <p>Ruta JD-8: en recorridos por casos de uso completos, esta capa se lee
 * al final para entender cómo el shell, SideDock, workbench y canvas hacen
 * visible la intención sin mover reglas de negocio a JavaFX.</p>
 * <p>Ruta JD-9: los ADRs explican por qué SideDock, canvas transversal, ayuda operativa y adapters viven en presentación sin mover semántica del negocio a JavaFX.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation;
