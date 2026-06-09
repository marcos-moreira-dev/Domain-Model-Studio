package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import java.util.List;
import java.util.Optional;
import javafx.scene.Parent;

/**
 * Aporte específico de un tipo de diagrama al workbench común.
 *
 * <p>El contrato no expone operaciones como agregar módulo, clase o pantalla. Esas
 * acciones pertenecen a toolbars/comandos específicos. El contributor solo entrega las
 * piezas visuales que el workbench puede montar de forma uniforme.</p>
 */
public interface DiagramWorkbenchContributor {

    DiagramWorkbenchDescriptor descriptor();

    Parent centerContent();

    default Optional<Parent> structurePanel() {
        return Optional.empty();
    }

    default Optional<Parent> propertiesPanel() {
        return Optional.empty();
    }


    default List<SideDockModule> additionalSideDockModules() {
        return List.of();
    }

    default void onActivated() {
        // Hook intencionalmente vacío para migraciones graduales.
    }

    default void onDeactivated() {
        // Hook intencionalmente vacío para migraciones graduales.
    }
}
