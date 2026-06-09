package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.editing.AddAttributeUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddRelationshipUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.DuplicateEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveDiagramElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RenameElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateElementDescriptionUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateRelationshipCardinalityUseCase;
import java.util.Objects;

/** Fachada de edición semántica del modelo conceptual clásico.
 *
 * <p>Agrupa operaciones de creación, renombrado y edición de elementos del
 * modelo conceptual sin mezclar layout, estilo visual ni persistencia.</p> */
public final class ConceptualModelApplicationServices {

    private final AddEntityUseCase addEntityUseCase;
    private final AddAttributeUseCase addAttributeUseCase;
    private final AddRelationshipUseCase addRelationshipUseCase;
    private final DuplicateEntityUseCase duplicateEntityUseCase;
    private final RemoveDiagramElementUseCase removeDiagramElementUseCase;
    private final RenameElementUseCase renameElementUseCase;
    private final UpdateElementDescriptionUseCase updateElementDescriptionUseCase;
    private final UpdateRelationshipCardinalityUseCase updateRelationshipCardinalityUseCase;

    public ConceptualModelApplicationServices(
            AddEntityUseCase addEntityUseCase,
            AddAttributeUseCase addAttributeUseCase,
            AddRelationshipUseCase addRelationshipUseCase,
            DuplicateEntityUseCase duplicateEntityUseCase,
            RemoveDiagramElementUseCase removeDiagramElementUseCase,
            RenameElementUseCase renameElementUseCase,
            UpdateElementDescriptionUseCase updateElementDescriptionUseCase,
            UpdateRelationshipCardinalityUseCase updateRelationshipCardinalityUseCase
    ) {
        this.addEntityUseCase = Objects.requireNonNull(addEntityUseCase, "addEntityUseCase");
        this.addAttributeUseCase = Objects.requireNonNull(addAttributeUseCase, "addAttributeUseCase");
        this.addRelationshipUseCase = Objects.requireNonNull(addRelationshipUseCase, "addRelationshipUseCase");
        this.duplicateEntityUseCase = Objects.requireNonNull(duplicateEntityUseCase, "duplicateEntityUseCase");
        this.removeDiagramElementUseCase = Objects.requireNonNull(removeDiagramElementUseCase, "removeDiagramElementUseCase");
        this.renameElementUseCase = Objects.requireNonNull(renameElementUseCase, "renameElementUseCase");
        this.updateElementDescriptionUseCase = Objects.requireNonNull(updateElementDescriptionUseCase, "updateElementDescriptionUseCase");
        this.updateRelationshipCardinalityUseCase = Objects.requireNonNull(updateRelationshipCardinalityUseCase, "updateRelationshipCardinalityUseCase");
    }

    public AddEntityUseCase addEntityUseCase() {
        return addEntityUseCase;
    }

    public AddAttributeUseCase addAttributeUseCase() {
        return addAttributeUseCase;
    }

    public AddRelationshipUseCase addRelationshipUseCase() {
        return addRelationshipUseCase;
    }

    public DuplicateEntityUseCase duplicateEntityUseCase() {
        return duplicateEntityUseCase;
    }

    public RemoveDiagramElementUseCase removeDiagramElementUseCase() {
        return removeDiagramElementUseCase;
    }

    public RenameElementUseCase renameElementUseCase() {
        return renameElementUseCase;
    }

    public UpdateElementDescriptionUseCase updateElementDescriptionUseCase() {
        return updateElementDescriptionUseCase;
    }

    public UpdateRelationshipCardinalityUseCase updateRelationshipCardinalityUseCase() {
        return updateRelationshipCardinalityUseCase;
    }

}
