/**
 * Dominio puro del grafo lógico del negocio.
 *
 * <p>El paquete modela una vista semántica derivada del levantamiento lógico:
 * macroflujos, flujos, casos de uso, acciones, reglas, condiciones, entidades,
 * estados, reportes, riesgos y preguntas pendientes. No es una variante del
 * grafo libre; cada nodo y cada relación tienen significado de negocio.</p>
 *
 * <p>Las clases de este paquete protegen invariantes de dominio: códigos de
 * nodo compatibles con su tipo, relaciones dirigidas hacia nodos existentes,
 * relaciones semánticamente válidas y documentos inmutables. Toda operación
 * de edición devuelve una nueva instancia.</p>
 *
 * <p>No depende de JavaFX, Markdown, JSON, persistencia, canvas ni
 * infraestructura. Es seguro usarlo desde parsers, persistencia, validadores,
 * exportadores y ViewModels sin arrastrar dependencias de presentación.</p>
 */
package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;
