package com.marcosmoreira.domainmodelstudio.domain.er;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElement;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementType;
import java.util.Objects;

/**
 * Relación conceptual entre dos entidades.
 *
 * <p>Esta versión MVP representa relaciones binarias, suficientes para el primer parser
 * Markdown y para el render Chen inicial. Las relaciones n-arias quedan documentadas
 * para evolución futura.</p>
 */
public final class RelationshipElement implements DiagramElement {

    private final DiagramElementId id;
    private final String name;
    private final DiagramElementId fromEntityId;
    private final DiagramElementId toEntityId;
    private final Cardinality fromCardinality;
    private final Cardinality toCardinality;
    private final RelationshipKind kind;
    private final ParticipationType fromParticipation;
    private final ParticipationType toParticipation;
    private final String description;

    public RelationshipElement(
            DiagramElementId id,
            String name,
            DiagramElementId fromEntityId,
            DiagramElementId toEntityId,
            Cardinality fromCardinality,
            Cardinality toCardinality,
            RelationshipKind kind,
            ParticipationType fromParticipation,
            ParticipationType toParticipation,
            String description
    ) {
        this.id = Objects.requireNonNull(id, "El ID de la relación no puede ser null");
        this.name = requireText(name, "El nombre de la relación no puede estar vacío");
        this.fromEntityId = Objects.requireNonNull(fromEntityId, "La entidad origen no puede ser null");
        this.toEntityId = Objects.requireNonNull(toEntityId, "La entidad destino no puede ser null");
        this.fromCardinality = Objects.requireNonNull(fromCardinality, "La cardinalidad origen no puede ser null");
        this.toCardinality = Objects.requireNonNull(toCardinality, "La cardinalidad destino no puede ser null");
        this.kind = kind == null ? RelationshipKind.REGULAR : kind;
        this.fromParticipation = fromParticipation == null ? ParticipationType.UNSPECIFIED : fromParticipation;
        this.toParticipation = toParticipation == null ? ParticipationType.UNSPECIFIED : toParticipation;
        this.description = normalizeOptionalText(description);
    }

    public static RelationshipElement between(
            String id,
            String name,
            String fromEntityId,
            String toEntityId,
            String fromCardinality,
            String toCardinality
    ) {
        return new RelationshipElement(
                DiagramElementId.of(id),
                name,
                DiagramElementId.of(fromEntityId),
                DiagramElementId.of(toEntityId),
                Cardinality.of(fromCardinality),
                Cardinality.of(toCardinality),
                RelationshipKind.REGULAR,
                ParticipationType.UNSPECIFIED,
                ParticipationType.UNSPECIFIED,
                ""
        );
    }

    @Override
    public DiagramElementId id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DiagramElementType type() {
        return DiagramElementType.RELATIONSHIP;
    }

    public DiagramElementId fromEntityId() {
        return fromEntityId;
    }

    public DiagramElementId toEntityId() {
        return toEntityId;
    }

    public Cardinality fromCardinality() {
        return fromCardinality;
    }

    public Cardinality toCardinality() {
        return toCardinality;
    }

    public RelationshipKind kind() {
        return kind;
    }

    public ParticipationType fromParticipation() {
        return fromParticipation;
    }

    public ParticipationType toParticipation() {
        return toParticipation;
    }

    public String description() {
        return description;
    }

    public RelationshipElement withDescription(String updatedDescription) {
        return new RelationshipElement(
                id,
                name,
                fromEntityId,
                toEntityId,
                fromCardinality,
                toCardinality,
                kind,
                fromParticipation,
                toParticipation,
                updatedDescription
        );
    }

    public RelationshipElement renamed(String updatedName) {
        return new RelationshipElement(
                id,
                updatedName,
                fromEntityId,
                toEntityId,
                fromCardinality,
                toCardinality,
                kind,
                fromParticipation,
                toParticipation,
                description
        );
    }

    public RelationshipElement withCardinalities(Cardinality updatedFromCardinality, Cardinality updatedToCardinality) {
        return new RelationshipElement(
                id,
                name,
                fromEntityId,
                toEntityId,
                updatedFromCardinality,
                updatedToCardinality,
                kind,
                fromParticipation,
                toParticipation,
                description
        );
    }

    public RelationshipElement withParticipations(
            ParticipationType updatedFromParticipation,
            ParticipationType updatedToParticipation
    ) {
        return new RelationshipElement(
                id,
                name,
                fromEntityId,
                toEntityId,
                fromCardinality,
                toCardinality,
                kind,
                updatedFromParticipation,
                updatedToParticipation,
                description
        );
    }

    public RelationshipElement withKind(RelationshipKind updatedKind) {
        return new RelationshipElement(
                id,
                name,
                fromEntityId,
                toEntityId,
                fromCardinality,
                toCardinality,
                updatedKind,
                fromParticipation,
                toParticipation,
                description
        );
    }

    private static String requireText(String value, String message) {
        String normalized = Objects.requireNonNull(value, message).trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private static String normalizeOptionalText(String value) {
        return value == null ? "" : value.trim();
    }
}
