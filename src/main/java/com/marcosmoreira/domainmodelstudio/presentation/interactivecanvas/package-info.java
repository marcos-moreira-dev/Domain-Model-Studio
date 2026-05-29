/**
 * Contratos y controladores comunes para lienzos interactivos de diagramas.
 *
 * <p>Este paquete no conoce BPMN, C4, UML, módulos ni wireframes. Las vistas especializadas
 * deben entrar mediante adaptadores para reutilizar paneo, zoom, selección, drag, etiquetas,
 * resize y puntos intermedios sin copiar la lógica del modelo conceptual.</p>
 *
 * <p>La regla de estudio es separar lectura, comandos y renderizado: los adaptadores exponen
 * el modelo visual, los puertos reciben operaciones, y la superficie coordina gestos sin
 * modificar directamente el dominio.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;
