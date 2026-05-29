/**
 * Infraestructura interna de borradores Markdown compatibles desde el Levantamiento lógico.
 *
 * <p>Este paquete se conserva por compatibilidad técnica con pruebas e integraciones legadas,
 * pero ya no respalda un módulo visible de SideDock ni una acción de toolbar. Sus writers generan
 * insumos revisables que pueden importar otros tipos de proyecto cuando una persona decide usarlos.</p>
 *
 * <p>El contrato vigente es deliberadamente limitado: no hay importación automática, sincronización
 * entre proyectos, guardado implícito ni certificación de modelos. El Levantamiento lógico sigue
 * siendo fuente lógica canónica; estos borradores solo reutilizan IDs, nombres y reglas como ayuda
 * posterior.</p>
 */
package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;
