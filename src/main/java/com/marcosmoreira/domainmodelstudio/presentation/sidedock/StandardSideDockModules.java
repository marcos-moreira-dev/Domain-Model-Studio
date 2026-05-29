package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.Objects;
import javafx.scene.Parent;

/** Fábrica de módulos laterales transversales usados por varias familias visuales. */
public final class StandardSideDockModules {

    private StandardSideDockModules() {
    }

    public static SideDockModule appearance(Parent content) {
        return StaticSideDockModule.of(
                SideDockModuleId.APPEARANCE,
                "Apariencia",
                Objects.requireNonNull(content, "content")
        );
    }

    public static SideDockModule operationalHelp(WorkspaceKind kind, String title, String subtitle) {
        Objects.requireNonNull(kind, "kind");
        return StaticSideDockModule.of(
                SideDockModuleId.HELP,
                "Ayuda",
                OperationalHelpContent.create(kind, title, subtitle)
        );
    }
}
