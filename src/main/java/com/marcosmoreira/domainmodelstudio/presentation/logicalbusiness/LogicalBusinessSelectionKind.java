package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

/**
 * Tipo de foco seleccionado dentro del expediente de Levantamiento lógico.
 *
 * <p>Este enum evita que la UI dependa de tres selecciones paralelas
 * (sección, item y entidad) y prepara el TreeView del expediente para elegir
 * cualquier nodo relevante sin comprimir el workspace.</p>
 */
public enum LogicalBusinessSelectionKind {
    NONE,
    DOCUMENT,
    GROUP,
    SECTION,
    ITEM,
    ENTITY,
    ATTRIBUTE,
    RELATIONSHIP,
    PENDING_QUESTION,
    MATURITY
}
