/**
 * Fachadas de servicios de aplicación agrupadas por familia funcional.
 *
 * <p>Desde la Tanda 28, {@code ApplicationServices} funciona como fachada de
 * compatibilidad y delega en estas familias. La intención es que la presentación
 * pueda pedir una familia concreta —proyectos, importación, exportación, catálogo,
 * visualización, UML, diccionario, wireframes, comportamiento o arquitectura— sin
 * depender de un objeto monolítico ni construir casos de uso manualmente.</p>
 *
 * <p>Estas clases no contienen JavaFX, persistencia concreta ni reglas de dominio.
 * Solo agrupan casos de uso ya definidos para mantener fronteras claras entre
 * aplicación, infraestructura y presentación.</p>
 *
 * <p>Tanda 38A — JavaDoc post-refactor: esta documentación existe para orientar
 * mantenimiento después del refactor por familias, no para describir getters triviales ni cambiar comportamiento funcional.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.services;
