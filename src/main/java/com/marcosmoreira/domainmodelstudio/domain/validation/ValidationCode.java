package com.marcosmoreira.domainmodelstudio.domain.validation;

/**
 * Códigos estables para hallazgos de validación.
 *
 * <p>Estos códigos forman parte de la trazabilidad humana: permiten que UI, tests,
 * documentación y futuras traducciones no dependan solo de textos libres.</p>
 */
public enum ValidationCode {
    ENTITY_WITHOUT_PRIMARY_KEY,
    ENTITY_WITH_DUPLICATED_ATTRIBUTE_ID,
    ENTITY_WITH_DUPLICATED_ATTRIBUTE_NAME,
    RELATIONSHIP_REFERENCES_UNKNOWN_ENTITY,
    RELATIONSHIP_SELF_REFERENCE,
    WEAK_ENTITY_WITHOUT_IDENTIFYING_RELATIONSHIP,
    LAYOUT_NODE_REFERENCES_UNKNOWN_ELEMENT,
    LAYOUT_CONNECTOR_REFERENCES_UNKNOWN_SOURCE,
    LAYOUT_CONNECTOR_REFERENCES_UNKNOWN_TARGET,
    STYLE_REFERENCES_UNKNOWN_ELEMENT
}
