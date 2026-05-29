package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import java.io.InputStream;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 * Iconos PNG de la barra principal.
 *
 * <p>JavaFX carga estos recursos como imágenes normales para evitar dependencias
 * frágiles de SVG o fuentes de glifos en la interfaz de escritorio.</p>
 */
public enum ToolbarIcon {
    NEW_PROJECT("new.png"),
    OPEN_PROJECT("open.png"),
    SAVE_PROJECT("save.png"),
    CLOSE_PROJECT("close.png"),
    UNDO_CHANGE("undo.png"),
    REDO_CHANGE("redo.png"),
    IMPORT_MODEL("import-md.png"),
    OPEN_EXAMPLE("example.png"),
    ADD_ENTITY("add-entity.png"),
    ADD_ATTRIBUTE("add-attribute.png"),
    ADD_RELATIONSHIP("add-relationship.png"),
    ADD_MODULE("add-module.png"),
    ADD_SUBMODULE("add-submodule.png"),
    ADD_MODULE_DEPENDENCY("add-dependency.png"),
    ADD_UML_CLASS("add-class.png"),
    ADD_UML_INTERFACE("add-interface.png"),
    ADD_UML_ENUM("add-enum.png"),
    ADD_UML_METHOD("add-method.png"),
    ADD_UML_RELATION("add-uml-relation.png"),
    ADD_ROLE("add-role.png"),
    ADD_PERMISSION("add-permission.png"),
    ADD_ASSIGNMENT("add-assignment.png"),
    ADD_SCREEN("add-screen.png"),
    ADD_TRANSITION("add-transition.png"),
    ADD_WIREFRAME_SCREEN("add-wireframe-screen.png"),
    ADD_WIREFRAME_SECTION("add-wireframe-section.png"),
    ADD_WIREFRAME_FORM("add-wireframe-form.png"),
    ADD_WIREFRAME_TABLE("add-wireframe-table.png"),
    ADD_WIREFRAME_FIELD("add-wireframe-field.png"),
    ADD_WIREFRAME_BUTTON("add-wireframe-button.png"),
    ADD_BPMN_START("add-start.png"),
    ADD_BPMN_ACTIVITY("add-activity.png"),
    ADD_BPMN_DECISION("add-decision.png"),
    ADD_BPMN_END("add-end.png"),
    ADD_LANE("add-lane.png"),
    ADD_ACTOR("add-actor.png"),
    ADD_USE_CASE("add-use-case.png"),
    ADD_SYSTEM_BOUNDARY("add-system-boundary.png"),
    ADD_MESSAGE("add-message.png"),
    ADD_STATE("add-state.png"),
    ADD_C4_PERSON("add-c4-person.png"),
    ADD_C4_SYSTEM("add-c4-system.png"),
    ADD_C4_CONTAINER("add-c4-container.png"),
    ADD_C4_DATABASE("add-c4-database.png"),
    ADD_C4_API("add-c4-api.png"),
    ADD_DEPLOYMENT_SERVER("add-deployment-server.png"),
    ADD_DEPLOYMENT_CLIENT("add-deployment-client.png"),
    ADD_DEPLOYMENT_NETWORK("add-deployment-network.png"),
    ADD_DEPLOYMENT_ARTIFACT("add-deployment-artifact.png"),
    DUPLICATE_ELEMENT("duplicate.png"),
    DELETE_ELEMENT("delete.png"),
    VALIDATE_MODEL("validate.png"),
    REORGANIZE_DIAGRAM("reorganize.png"),
    BRING_TO_FRONT("bring-to-front.png"),
    SEND_TO_BACK("send-to-back.png"),
    RAISE_LAYER("raise-layer.png"),
    LOWER_LAYER("lower-layer.png"),
    NOTATION_CHEN("chen.png"),
    NOTATION_CROWS_FOOT("crows-foot.png"),
    ZOOM_IN("zoom-in.png"),
    ZOOM_OUT("zoom-out.png"),
    ZOOM_ACTUAL("zoom-actual.png"),
    FIT_VIEW("fit-content.png"),
    CENTER_SELECTION("center-selection.png"),
    EXPORT_SVG("export-svg.png"),
    EXPORT_MARKDOWN("export-md.png"),
    EXPORT_PDF("export-pdf.png"),
    EXPORT_PNG("export-png.png"),
    EXPORT_OPEN_PROJECTS("package.png"),
    EXPORT_CLIENT("package.png"),
    MANUAL("help-book.png"),
    AI_RESOURCES("help-book.png");

    private static final String ICON_ROOT = "/icons/";
    private static final double ICON_SIZE = 16.0;

    private final String fileName;

    ToolbarIcon(String fileName) {
        this.fileName = Objects.requireNonNull(fileName, "fileName");
    }

    public ImageView imageView() {
        ImageView view = new ImageView(loadImage());
        view.getStyleClass().add("toolbar-icon");
        view.setFitWidth(ICON_SIZE);
        view.setFitHeight(ICON_SIZE);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        view.setPickOnBounds(true);
        view.setMouseTransparent(true);
        view.setFocusTraversable(false);
        return view;
    }

    private Image loadImage() {
        String resource = ICON_ROOT + fileName;
        try (InputStream stream = ToolbarIcon.class.getResourceAsStream(resource)) {
            if (stream == null) {
                return fallbackImage();
            }
            return new Image(stream);
        } catch (Exception ignored) {
            return fallbackImage();
        }
    }

    private Image fallbackImage() {
        return new WritableImage(1, 1);
    }
}
