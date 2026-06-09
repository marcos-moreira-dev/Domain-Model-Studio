/**
 * Capa de aplicación de Domain Model Studio.
 *
 * <p>Coordina casos de uso y servicios de política sin depender de JavaFX, de
 * serialización concreta ni de controles de presentación. Recibe objetos de
 * dominio, aplica decisiones de producto y devuelve resultados listos para que
 * infraestructura o UI los adapten.</p>
 *
 * <p>Para estudiar ingeniería de software, aquí se observa la frontera entre
 * reglas puras del dominio y orquestación de casos de uso. Una clase de
 * aplicación puede validar, derivar, preparar layout o consultar catálogos, pero
 * no debe dibujar, parsear archivos ni abrir ventanas.</p>
 *
 * <p>Ruta de estudio JD-7: después de leer el dominio, buscar el caso de uso o
 * servicio que coordina la intención. Esta capa responde qué se permite hacer,
 * qué capacidad se expone y qué resultado se entrega a infraestructura o
 * presentación.</p>
 * <p>Ruta JD-8: en recorridos por casos de uso completos, esta capa muestra
 * qué servicio coordina la intención del usuario y qué resultado entrega a
 * infraestructura o presentación.</p>
 * <p>Ruta JD-9: los ADRs ayudan a distinguir una decisión de orquestación de una regla de dominio y muestran qué alternativa descartada habría mezclado responsabilidades.</p>
 */
package com.marcosmoreira.domainmodelstudio.application;
