package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import javafx.scene.Parent;

/** Módulo inyectable que el panel lateral puede mostrar según el contexto activo. */
public interface SideDockModule {

    SideDockModuleId id();

    String title();

    default String tooltip() {
        return id().tooltipText();
    }

    default String iconText() {
        return id().iconText();
    }

    default boolean supports(SideDockContext context) {
        return true;
    }

    Parent createView(SideDockContext context);
}
