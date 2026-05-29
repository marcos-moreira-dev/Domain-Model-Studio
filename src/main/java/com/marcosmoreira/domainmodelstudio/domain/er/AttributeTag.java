package com.marcosmoreira.domainmodelstudio.domain.er;

/**
 * Etiquetas conceptuales de un atributo ER.
 *
 * <p>Son combinables. Por ejemplo, un atributo puede ser opcional y sensible. El parser
 * Markdown será responsable de traducir tags como {@code [pk]} o {@code [derived]} a
 * estos valores.</p>
 */
public enum AttributeTag {
    PRIMARY_KEY,
    PARTIAL_KEY,
    OPTIONAL,
    DERIVED,
    UNIQUE,
    MULTIVALUED,
    COMPOSITE,
    SENSITIVE
}
