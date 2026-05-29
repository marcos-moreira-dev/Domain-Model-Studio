package com.marcosmoreira.domainmodelstudio.presentation.conceptual.sidedock;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.ConceptualCanvasLegacyBridge;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StaticSideDockModule;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Módulos operativos del SideDock común para el modelo conceptual.
 *
 * <p>Estos módulos completan la migración de carcasa iniciada en Tanda 4: estructura y
 * propiedades siguen encapsulando las vistas existentes, mientras Validación, Apariencia y
 * Ayuda ya se declaran explícitamente como módulos del SideDock transversal. No reemplazan
 * el render Chen/Crow's Foot ni introducen un segundo sidebar.</p>
 */
public final class ConceptualSideDockModules {

    private ConceptualSideDockModules() {
    }

    public static List<SideDockModule> create(
            ConceptualCanvasLegacyBridge bridge,
            Consumer<NotationType> notationSwitchAction
    ) {
        Objects.requireNonNull(bridge, "bridge");
        Objects.requireNonNull(notationSwitchAction, "notationSwitchAction");
        return List.of(
                StaticSideDockModule.of(
                        SideDockModuleId.VALIDATION,
                        "Validación",
                        new ConceptualValidationPanel(bridge).getRoot()
                ),
                StaticSideDockModule.of(
                        SideDockModuleId.APPEARANCE,
                        "Apariencia",
                        new ConceptualAppearancePanel(bridge, notationSwitchAction).getRoot()
                ),
                StaticSideDockModule.of(
                        SideDockModuleId.HELP,
                        "Ayuda",
                        ConceptualOperationalHelpPanel.create()
                )
        );
    }
}
