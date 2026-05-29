/**
 * Dominio puro del proyecto documental de levantamiento lógico del negocio.
 *
 * <p>El paquete representa el expediente fuente de un proyecto: reglas,
 * acciones transformadoras, precondiciones, invariantes, postcondiciones,
 * entidades candidatas, atributos, relaciones, reportes, riesgos, supuestos y
 * preguntas pendientes. Su propósito es capturar lógica de negocio antes de
 * usar el expediente como fuente revisable para otros trabajos.</p>
 *
 * <p>La capa mantiene contratos de trazas internas e integridad: IDs compatibles
 * con el tipo lógico, fuentes explícitas para entidades candidatas, relaciones
 * entre entidades existentes y preguntas críticas visibles hasta que sean
 * resueltas.</p>
 *
 * <p>No depende de JavaFX, Markdown, JSON, persistencia ni infraestructura.</p>
 */
package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;
