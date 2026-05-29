package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.editing.AddBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorAnchorsUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorMarkerOrientationUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeDiagramAppearanceUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeElementStyleUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveConnectorLabelUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateNodeLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialCrowsFootLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.notation.SwitchNotationUseCase;
import java.util.Objects;

/**
 * Fachada de casos de uso visuales y de layout.
 *
 * <p>Contiene operaciones de posicionamiento, apariencia, bendpoints y notación.
 * No contiene objetos JavaFX.</p>
 */
public final class VisualApplicationServices {

    private final GenerateInitialChenLayoutUseCase generateInitialChenLayoutUseCase;
    private final GenerateInitialCrowsFootLayoutUseCase generateInitialCrowsFootLayoutUseCase;
    private final SwitchNotationUseCase switchNotationUseCase;
    private final MoveElementUseCase moveElementUseCase;
    private final AddBendPointUseCase addBendPointUseCase;
    private final MoveBendPointUseCase moveBendPointUseCase;
    private final MoveConnectorLabelUseCase moveConnectorLabelUseCase;
    private final RemoveBendPointUseCase removeBendPointUseCase;
    private final UpdateNodeLayoutUseCase updateNodeLayoutUseCase;
    private final ChangeElementStyleUseCase changeElementStyleUseCase;
    private final ChangeDiagramAppearanceUseCase changeDiagramAppearanceUseCase;
    private final ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase;
    private final ChangeConnectorMarkerOrientationUseCase changeConnectorMarkerOrientationUseCase;

    public VisualApplicationServices(
            GenerateInitialChenLayoutUseCase generateInitialChenLayoutUseCase,
            GenerateInitialCrowsFootLayoutUseCase generateInitialCrowsFootLayoutUseCase,
            SwitchNotationUseCase switchNotationUseCase,
            MoveElementUseCase moveElementUseCase,
            AddBendPointUseCase addBendPointUseCase,
            MoveBendPointUseCase moveBendPointUseCase,
            MoveConnectorLabelUseCase moveConnectorLabelUseCase,
            RemoveBendPointUseCase removeBendPointUseCase,
            UpdateNodeLayoutUseCase updateNodeLayoutUseCase,
            ChangeElementStyleUseCase changeElementStyleUseCase,
            ChangeDiagramAppearanceUseCase changeDiagramAppearanceUseCase,
            ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase,
            ChangeConnectorMarkerOrientationUseCase changeConnectorMarkerOrientationUseCase
    ) {
        this.generateInitialChenLayoutUseCase = Objects.requireNonNull(
                generateInitialChenLayoutUseCase, "generateInitialChenLayoutUseCase");
        this.generateInitialCrowsFootLayoutUseCase = Objects.requireNonNull(
                generateInitialCrowsFootLayoutUseCase, "generateInitialCrowsFootLayoutUseCase");
        this.switchNotationUseCase = Objects.requireNonNull(switchNotationUseCase, "switchNotationUseCase");
        this.moveElementUseCase = Objects.requireNonNull(moveElementUseCase, "moveElementUseCase");
        this.addBendPointUseCase = Objects.requireNonNull(addBendPointUseCase, "addBendPointUseCase");
        this.moveBendPointUseCase = Objects.requireNonNull(moveBendPointUseCase, "moveBendPointUseCase");
        this.moveConnectorLabelUseCase = Objects.requireNonNull(moveConnectorLabelUseCase, "moveConnectorLabelUseCase");
        this.removeBendPointUseCase = Objects.requireNonNull(removeBendPointUseCase, "removeBendPointUseCase");
        this.updateNodeLayoutUseCase = Objects.requireNonNull(updateNodeLayoutUseCase, "updateNodeLayoutUseCase");
        this.changeElementStyleUseCase = Objects.requireNonNull(changeElementStyleUseCase, "changeElementStyleUseCase");
        this.changeDiagramAppearanceUseCase = Objects.requireNonNull(
                changeDiagramAppearanceUseCase, "changeDiagramAppearanceUseCase");
        this.changeConnectorAnchorsUseCase = Objects.requireNonNull(
                changeConnectorAnchorsUseCase, "changeConnectorAnchorsUseCase");
        this.changeConnectorMarkerOrientationUseCase = Objects.requireNonNull(
                changeConnectorMarkerOrientationUseCase, "changeConnectorMarkerOrientationUseCase");
    }

    public GenerateInitialChenLayoutUseCase generateInitialChenLayoutUseCase() { return generateInitialChenLayoutUseCase; }
    public GenerateInitialCrowsFootLayoutUseCase generateInitialCrowsFootLayoutUseCase() { return generateInitialCrowsFootLayoutUseCase; }
    public SwitchNotationUseCase switchNotationUseCase() { return switchNotationUseCase; }
    public MoveElementUseCase moveElementUseCase() { return moveElementUseCase; }
    public AddBendPointUseCase addBendPointUseCase() { return addBendPointUseCase; }
    public MoveBendPointUseCase moveBendPointUseCase() { return moveBendPointUseCase; }
    public MoveConnectorLabelUseCase moveConnectorLabelUseCase() { return moveConnectorLabelUseCase; }
    public RemoveBendPointUseCase removeBendPointUseCase() { return removeBendPointUseCase; }
    public UpdateNodeLayoutUseCase updateNodeLayoutUseCase() { return updateNodeLayoutUseCase; }
    public ChangeElementStyleUseCase changeElementStyleUseCase() { return changeElementStyleUseCase; }
    public ChangeDiagramAppearanceUseCase changeDiagramAppearanceUseCase() { return changeDiagramAppearanceUseCase; }
    public ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase() { return changeConnectorAnchorsUseCase; }
    public ChangeConnectorMarkerOrientationUseCase changeConnectorMarkerOrientationUseCase() { return changeConnectorMarkerOrientationUseCase; }
}
