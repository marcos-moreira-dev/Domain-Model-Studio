package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.AddUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.CreateUmlClassDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.RemoveUmlClassDiagramItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlClassUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlMemberUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.UpdateUmlRelationUseCase;
import com.marcosmoreira.domainmodelstudio.application.umlclass.ValidateUmlClassDiagramUseCase;
import java.util.Objects;

/** Fachada de edición y validación del diagrama UML de clases.
 *
 * <p>La generación desde código fuente queda en importación para no mezclar
 * entrada de datos con edición manual del diagrama.</p> */
public final class UmlClassApplicationServices {

    private final CreateUmlClassDiagramUseCase createUmlClassDiagramUseCase;
    private final AddUmlModuleUseCase addUmlModuleUseCase;
    private final AddUmlClassUseCase addUmlClassUseCase;
    private final AddUmlMemberUseCase addUmlMemberUseCase;
    private final AddUmlRelationUseCase addUmlRelationUseCase;
    private final UpdateUmlModuleUseCase updateUmlModuleUseCase;
    private final UpdateUmlClassUseCase updateUmlClassUseCase;
    private final UpdateUmlMemberUseCase updateUmlMemberUseCase;
    private final UpdateUmlRelationUseCase updateUmlRelationUseCase;
    private final RemoveUmlClassDiagramItemUseCase removeUmlClassDiagramItemUseCase;
    private final ValidateUmlClassDiagramUseCase validateUmlClassDiagramUseCase;

    public UmlClassApplicationServices(
            CreateUmlClassDiagramUseCase createUmlClassDiagramUseCase,
            AddUmlModuleUseCase addUmlModuleUseCase,
            AddUmlClassUseCase addUmlClassUseCase,
            AddUmlMemberUseCase addUmlMemberUseCase,
            AddUmlRelationUseCase addUmlRelationUseCase,
            UpdateUmlModuleUseCase updateUmlModuleUseCase,
            UpdateUmlClassUseCase updateUmlClassUseCase,
            UpdateUmlMemberUseCase updateUmlMemberUseCase,
            UpdateUmlRelationUseCase updateUmlRelationUseCase,
            RemoveUmlClassDiagramItemUseCase removeUmlClassDiagramItemUseCase,
            ValidateUmlClassDiagramUseCase validateUmlClassDiagramUseCase
    ) {
        this.createUmlClassDiagramUseCase = Objects.requireNonNull(createUmlClassDiagramUseCase, "createUmlClassDiagramUseCase");
        this.addUmlModuleUseCase = Objects.requireNonNull(addUmlModuleUseCase, "addUmlModuleUseCase");
        this.addUmlClassUseCase = Objects.requireNonNull(addUmlClassUseCase, "addUmlClassUseCase");
        this.addUmlMemberUseCase = Objects.requireNonNull(addUmlMemberUseCase, "addUmlMemberUseCase");
        this.addUmlRelationUseCase = Objects.requireNonNull(addUmlRelationUseCase, "addUmlRelationUseCase");
        this.updateUmlModuleUseCase = Objects.requireNonNull(updateUmlModuleUseCase, "updateUmlModuleUseCase");
        this.updateUmlClassUseCase = Objects.requireNonNull(updateUmlClassUseCase, "updateUmlClassUseCase");
        this.updateUmlMemberUseCase = Objects.requireNonNull(updateUmlMemberUseCase, "updateUmlMemberUseCase");
        this.updateUmlRelationUseCase = Objects.requireNonNull(updateUmlRelationUseCase, "updateUmlRelationUseCase");
        this.removeUmlClassDiagramItemUseCase = Objects.requireNonNull(removeUmlClassDiagramItemUseCase, "removeUmlClassDiagramItemUseCase");
        this.validateUmlClassDiagramUseCase = Objects.requireNonNull(validateUmlClassDiagramUseCase, "validateUmlClassDiagramUseCase");
    }

    public CreateUmlClassDiagramUseCase createUmlClassDiagramUseCase() {
        return createUmlClassDiagramUseCase;
    }

    public AddUmlModuleUseCase addUmlModuleUseCase() {
        return addUmlModuleUseCase;
    }

    public AddUmlClassUseCase addUmlClassUseCase() {
        return addUmlClassUseCase;
    }

    public AddUmlMemberUseCase addUmlMemberUseCase() {
        return addUmlMemberUseCase;
    }

    public AddUmlRelationUseCase addUmlRelationUseCase() {
        return addUmlRelationUseCase;
    }

    public UpdateUmlModuleUseCase updateUmlModuleUseCase() {
        return updateUmlModuleUseCase;
    }

    public UpdateUmlClassUseCase updateUmlClassUseCase() {
        return updateUmlClassUseCase;
    }

    public UpdateUmlMemberUseCase updateUmlMemberUseCase() {
        return updateUmlMemberUseCase;
    }

    public UpdateUmlRelationUseCase updateUmlRelationUseCase() {
        return updateUmlRelationUseCase;
    }

    public RemoveUmlClassDiagramItemUseCase removeUmlClassDiagramItemUseCase() {
        return removeUmlClassDiagramItemUseCase;
    }

    public ValidateUmlClassDiagramUseCase validateUmlClassDiagramUseCase() {
        return validateUmlClassDiagramUseCase;
    }

}
