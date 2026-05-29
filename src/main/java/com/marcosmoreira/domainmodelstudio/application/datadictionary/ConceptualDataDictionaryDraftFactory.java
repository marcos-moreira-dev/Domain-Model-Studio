package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/** Construye documentos de diccionario editables desde el modelo conceptual. */
public final class ConceptualDataDictionaryDraftFactory {

    private final Clock clock;
    private final ConceptualDataDictionaryDraftPolicy policy;

    public ConceptualDataDictionaryDraftFactory(Clock clock) {
        this(clock, new ConceptualDataDictionaryDraftPolicy());
    }

    public ConceptualDataDictionaryDraftFactory(Clock clock, ConceptualDataDictionaryDraftPolicy policy) {
        this.clock = Objects.requireNonNull(clock, "clock");
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    public DataDictionaryDocument createDraft(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        DiagramModel model = project.model();
        List<DataDictionaryEntity> entities = model.entities().stream()
                .map(entity -> toDictionaryEntity(entity, model.relationships()))
                .toList();
        return new DataDictionaryDocument(
                project.metadata().title(),
                "",
                "",
                "",
                "borrador desde modelo conceptual",
                LocalDate.now(clock),
                DataDictionaryStatus.DRAFT,
                introductionFor(project),
                "",
                entities,
                documentNotes(model)
        );
    }

    private DataDictionaryEntity toDictionaryEntity(EntityElement entity, List<RelationshipElement> relationships) {
        List<DataDictionaryField> fields = entity.attributes().stream()
                .map(this::toDictionaryField)
                .toList();
        return new DataDictionaryEntity(
                entity.id().value(),
                entity.name(),
                technicalName(entity.name()),
                entity.description(),
                entity.module(),
                policy.inferEntityKind(entity),
                policy.entityOrigin(),
                DataDictionaryStatus.DRAFT,
                fields,
                entityNotes(entity, relationships)
        );
    }

    private DataDictionaryField toDictionaryField(AttributeElement attribute) {
        return new DataDictionaryField(
                attribute.name(),
                attribute.name(),
                technicalName(attribute.name()),
                policy.inferLogicalType(attribute),
                "",
                policy.constraintsFor(attribute),
                "",
                "",
                "",
                attribute.description(),
                "",
                "",
                "",
                defaultVisibility(attribute),
                !attribute.isDerived(),
                policy.fieldNotes(attribute)
        );
    }

    private static String introductionFor(DiagramProject project) {
        return "Borrador editable generado desde el modelo conceptual \""
                + project.metadata().title()
                + "\". Revise nombres técnicos, tipos lógicos, restricciones, reglas de negocio y visibilidad antes de usar este documento como entregable o base de implementación.";
    }

    private static String documentNotes(DiagramModel model) {
        if (model.relationships().isEmpty()) {
            return "El borrador fue creado desde entidades y atributos conceptuales. No hay relaciones conceptuales registradas para revisar.";
        }
        return "Revisar relaciones conceptuales para decidir claves foráneas, tablas asociativas o reglas de negocio: "
                + model.relationships().stream()
                .map(ConceptualDataDictionaryDraftFactory::relationshipSummary)
                .reduce((left, right) -> left + "; " + right)
                .orElse("")
                + ".";
    }

    private static String entityNotes(EntityElement entity, List<RelationshipElement> relationships) {
        List<String> related = relationships.stream()
                .filter(relationship -> relationship.fromEntityId().equals(entity.id())
                        || relationship.toEntityId().equals(entity.id()))
                .map(ConceptualDataDictionaryDraftFactory::relationshipSummary)
                .toList();
        if (related.isEmpty()) {
            return "Entidad sugerida desde modelo conceptual; revisar alcance y responsabilidad.";
        }
        return "Entidad sugerida desde modelo conceptual. Relaciones por revisar: "
                + String.join("; ", related)
                + ".";
    }

    private static String relationshipSummary(RelationshipElement relationship) {
        return relationship.name()
                + " ["
                + relationship.fromEntityId().value()
                + " "
                + relationship.fromCardinality().displayText()
                + " → "
                + relationship.toCardinality().displayText()
                + " "
                + relationship.toEntityId().value()
                + "]";
    }

    private static java.util.Set<FieldVisibility> defaultVisibility(AttributeElement attribute) {
        if (attribute.isDerived()) {
            return java.util.Set.of(FieldVisibility.REPORT);
        }
        return java.util.Set.of(FieldVisibility.FORM, FieldVisibility.TABLE, FieldVisibility.REPORT);
    }

    private static String technicalName(String label) {
        String normalized = java.text.Normalizer.normalize(label == null ? "" : label, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "pendiente" : normalized;
    }
}
