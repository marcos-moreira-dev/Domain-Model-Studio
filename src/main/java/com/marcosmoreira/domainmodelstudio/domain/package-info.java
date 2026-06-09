/**
 * Capa de dominio puro de Domain Model Studio.
 *
 * <p>Contiene los conceptos semánticos del sistema: proyectos, documentos,
 * diagramas, reglas de negocio, layouts persistibles y estilos como datos. Esta
 * capa no debe depender de JavaFX, sistema de archivos, JSON, Markdown, SVG ni
 * ningún detalle de presentación.</p>
 *
 * <p>Ruta de estudio JD-7: empezar aquí permite entender qué invariantes protege
 * el programa antes de mirar casos de uso o pantallas. Para estudiar una
 * funcionalidad, leer primero el agregado de dominio y después pasar a la capa de
 * aplicación que lo orquesta.</p>
 * <p>Ruta JD-8: en recorridos por casos de uso completos, esta capa se lee
 * primero para identificar el agregado y las invariantes antes de pasar a
 * servicios, formatos o pantallas.</p>
 * <p>Ruta JD-9: al leer una decisión de diseño, empezar por el agregado de dominio afectado permite entender qué invariante se protegió antes de mirar la alternativa descartada.</p>
 */
package com.marcosmoreira.domainmodelstudio.domain;
