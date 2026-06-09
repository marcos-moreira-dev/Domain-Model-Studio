/**
 * Capa de infraestructura.
 *
 * <p>Contiene detalles técnicos reemplazables: parsers Markdown, persistencia
 * JSON del formato {@code .dms}, exportadores SVG/PNG/PDF, carga de recursos IA
 * y acceso al sistema de archivos. La infraestructura traduce contratos de
 * aplicación hacia formatos concretos sin cambiar reglas de dominio.</p>
 *
 * <p>Para estudiar el proyecto, esta capa debe leerse como frontera de entrada y
 * salida: aquí se comprueba roundtrip, compatibilidad de versiones y errores de
 * formato recuperables.</p>
 *
 * <p>Ruta de estudio JD-7: entrar aquí solo después de entender dominio y caso de
 * uso. La pregunta guía es qué formato entra o sale, no qué regla de negocio debe
 * cumplirse.</p>
 * <p>Ruta JD-8: en recorridos por casos de uso completos, esta capa se lee
 * cuando el flujo cruza Markdown, {@code .dms}, SVG, PDF, recursos IA o
 * cualquier otro formato externo.</p>
 * <p>Ruta JD-9: las decisiones sobre formato, roundtrip y recursos externos explican por qué Markdown, {@code .dms}, SVG, PDF y recursos IA no comparten la misma responsabilidad.</p>
 */
package com.marcosmoreira.domainmodelstudio.infrastructure;
