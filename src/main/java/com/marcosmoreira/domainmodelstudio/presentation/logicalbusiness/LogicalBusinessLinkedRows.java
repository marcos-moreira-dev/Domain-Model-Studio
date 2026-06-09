package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.util.Set;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;

/** Renderiza referencias navegables dentro del formulario documental. */
final class LogicalBusinessLinkedRows {

    private LogicalBusinessLinkedRows() {
    }

    static void addSectionItems(
            LogicalBusinessDocument document,
            LogicalBusinessSection section,
            VBox form,
            LogicalBusinessViewModel viewModel
    ) {
        Set<String> sectionItems = Set.copyOf(section.itemIds());
        if (sectionItems.isEmpty()) {
            form.getChildren().add(LogicalBusinessFormControls.emptyNotice(
                    "Esta sección existe en la plantilla, pero el documento importado no trae elementos aquí."));
            return;
        }
        VBox block = block("Elementos vinculados automáticos");
        document.items().stream()
                .filter(item -> sectionItems.contains(item.id()))
                .forEach(item -> block.getChildren().add(link(item.id() + " — " + item.title() + " · "
                        + LogicalBusinessStatusFormatter.itemKind(item.kind()), () -> viewModel.selectReference(item.id()))));
        form.getChildren().add(block);
    }

    static void addEntityCandidateChildren(
            LogicalBusinessEntityCandidate entity,
            VBox form,
            LogicalBusinessViewModel viewModel
    ) {
        VBox attributes = block("Atributos candidatos navegables");
        if (entity.attributes().isEmpty()) {
            attributes.getChildren().add(LogicalBusinessFormControls.emptyNotice(
                    "Esta entidad todavía no tiene atributos candidatos registrados."));
        } else {
            entity.attributes().stream()
                    .sorted(java.util.Comparator.comparing(LogicalBusinessAttributeCandidate::id))
                    .forEach(attribute -> attributes.getChildren().add(link(attributeLabel(attribute),
                            () -> viewModel.selectAttribute(entity.id(), attribute.id()))));
        }
        form.getChildren().add(attributes);

        VBox relationships = block("Relaciones candidatas navegables");
        if (entity.relationships().isEmpty()) {
            relationships.getChildren().add(LogicalBusinessFormControls.emptyNotice(
                    "Esta entidad todavía no tiene relaciones candidatas registradas."));
        } else {
            entity.relationships().stream()
                    .sorted(java.util.Comparator.comparing(LogicalBusinessRelationshipCandidate::id))
                    .forEach(relationship -> relationships.getChildren().add(link(relationshipLabel(relationship),
                            () -> viewModel.selectRelationship(entity.id(), relationship.id()))));
        }
        form.getChildren().add(relationships);
    }

    private static String attributeLabel(LogicalBusinessAttributeCandidate attribute) {
        String suffix = attribute.tentativeType().isBlank() ? "" : " · " + attribute.tentativeType();
        if (attribute.calculated()) {
            suffix += " · calculado";
        }
        return attribute.id() + " — " + attribute.name() + suffix;
    }

    private static String relationshipLabel(LogicalBusinessRelationshipCandidate relationship) {
        String suffix = " · " + relationship.sourceEntityId() + " → " + relationship.targetEntityId();
        if (!relationship.cardinalityHint().isBlank()) {
            suffix += " · " + relationship.cardinalityHint();
        }
        return relationship.id() + " — " + relationship.name() + suffix;
    }

    private static VBox block(String title) {
        VBox block = new VBox(6);
        block.getStyleClass().add("logical-business-field");
        block.getChildren().add(LogicalBusinessFormControls.label(title, "logical-business-field-label"));
        return block;
    }

    private static Hyperlink link(String text, Runnable action) {
        Hyperlink link = new Hyperlink(LogicalBusinessDisplayText.clean(text));
        link.getStyleClass().add("logical-business-reference-link");
        link.setWrapText(true);
        link.setOnAction(event -> action.run());
        return link;
    }
}
