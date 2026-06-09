package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

/**
 * Foco único de selección del expediente lógico.
 *
 * <p>La selección mantiene un ID principal y, cuando aplica, un ID propietario.
 * Por ejemplo, un atributo usa {@code id = ATR-001} y
 * {@code ownerId = ENT-001}. Esta forma permite que el SideDock seleccione
 * secciones, grupos, items, entidades, atributos, relaciones, preguntas
 * y madurez sin crear estados paralelos.</p>
 */
public record LogicalBusinessSelection(
        LogicalBusinessSelectionKind kind,
        String id,
        String ownerId
) {

    public LogicalBusinessSelection {
        kind = kind == null ? LogicalBusinessSelectionKind.NONE : kind;
        id = normalize(id);
        ownerId = normalize(ownerId);
    }

    public static LogicalBusinessSelection none() {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.NONE, "", "");
    }

    public static LogicalBusinessSelection document() {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.DOCUMENT, "document", "");
    }

    public static LogicalBusinessSelection group(String groupId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.GROUP, groupId, "");
    }

    public static LogicalBusinessSelection section(String sectionId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.SECTION, sectionId, "");
    }

    public static LogicalBusinessSelection item(String itemId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.ITEM, itemId, "");
    }

    public static LogicalBusinessSelection entity(String entityId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.ENTITY, entityId, "");
    }

    public static LogicalBusinessSelection attribute(String entityId, String attributeId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.ATTRIBUTE, attributeId, entityId);
    }

    public static LogicalBusinessSelection relationship(String entityId, String relationshipId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.RELATIONSHIP, relationshipId, entityId);
    }

    public static LogicalBusinessSelection pendingQuestion(String questionId) {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.PENDING_QUESTION, questionId, "");
    }

    public static LogicalBusinessSelection maturity() {
        return new LogicalBusinessSelection(LogicalBusinessSelectionKind.MATURITY, "maturity", "");
    }

    public boolean empty() {
        return kind == LogicalBusinessSelectionKind.NONE || id.isBlank();
    }

    public boolean kindIs(LogicalBusinessSelectionKind expected) {
        return kind == expected;
    }

    /** ID que puede usarse como foco de validación/trazabilidad cuando el servicio lo soporte. */
    public String traceabilityFocusId() {
        return switch (kind) {
            case ITEM, ENTITY, ATTRIBUTE, RELATIONSHIP, PENDING_QUESTION -> id;
            default -> "";
        };
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
