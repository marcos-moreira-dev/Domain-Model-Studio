/**
 * Casos de uso y puertos para exportar proyectos o documentos.
 *
 * <p>La capa de aplicación decide intención, validación de destino y contrato de salida. El
 * formato concreto queda detrás de puertos como exportadores Markdown o SVG.</p>
 *
 * <p>Esta separación permite probar reglas de exportación sin depender de JavaFX ni del
 * sistema de archivos real.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.export;
