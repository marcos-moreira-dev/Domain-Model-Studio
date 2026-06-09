package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.property.ObjectProperty;

/** Coordina la apertura de archivos fuente asociados a clases UML importadas. */
final class UmlClassSourceNavigationController {
    private final ObjectProperty<UmlClassNode> selectedClass;
    private final Supplier<UmlClassDiagramDocument> documentSupplier;
    private final Consumer<String> statusConsumer;
    private final UmlClassSourceFileResolver sourceFileResolver = new UmlClassSourceFileResolver();
    private final UmlClassSourcePreviewWriter sourcePreviewWriter = new UmlClassSourcePreviewWriter();
    private final UmlClassCodeEditorSettings codeEditorSettings = UmlClassCodeEditorSettings.system();

    UmlClassSourceNavigationController(
            ObjectProperty<UmlClassNode> selectedClass,
            Supplier<UmlClassDiagramDocument> documentSupplier,
            Consumer<String> statusConsumer
    ) {
        this.selectedClass = Objects.requireNonNull(selectedClass, "selectedClass");
        this.documentSupplier = Objects.requireNonNull(documentSupplier, "documentSupplier");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
    }

    String codeEditorUserCommand() {
        return codeEditorSettings.userCommand();
    }

    String codeEditorEffectiveCommand() {
        return codeEditorSettings.effectiveCommand();
    }

    String codeEditorConfigurationSummary() {
        return codeEditorSettings.summary();
    }

    void saveCodeEditorCommand(String command) {
        codeEditorSettings.saveUserCommand(command);
        String extra = codeEditorSettings.externallyOverridden()
                ? " Hay una propiedad JVM o variable de entorno con prioridad sobre la preferencia guardada."
                : "";
        statusConsumer.accept("Editor de código configurado: " + codeEditorSettings.effectiveCommand() + "." + extra);
    }

    void resetCodeEditorCommand() {
        codeEditorSettings.resetUserCommand();
        statusConsumer.accept("Configuración del editor de código restablecida. Editor efectivo: "
                + codeEditorSettings.effectiveCommand() + ".");
    }

    void saveCodeEditorSystemDefaultCommand() {
        codeEditorSettings.saveSystemDefaultCommand();
        statusConsumer.accept("Editor de código configurado: aplicación predeterminada del sistema.");
    }

    void saveCodeEditorWindowsOpenWithCommand() {
        codeEditorSettings.saveWindowsOpenWithCommand();
        statusConsumer.accept("Editor de código configurado: diálogo Abrir con de Windows.");
    }

    Optional<Path> selectedSourcePath() {
        UmlClassNode node = selectedClass.get();
        return node == null ? Optional.empty() : sourceFileResolver.inspect(node).path();
    }

    String selectedSourceStatusSummary() {
        return sourceStatusSummary(selectedClass.get());
    }

    String sourceStatusSummary(UmlClassNode node) {
        if (node == null) {
            return "Selecciona una clase para revisar si tiene archivo fuente asociado.";
        }
        return sourceFileResolver.inspect(node).panelSummary(node.displayName());
    }

    void openSelectedSourceInCodeEditor() {
        UmlClassNode node = selectedClass.get();
        if (node == null) {
            statusConsumer.accept("Selecciona una clase importada desde código para abrir su archivo.");
            return;
        }
        openSourceForClass(node, codeEditorSettings.effectiveCommand(), "editor de código");
    }

    void openSelectedSourceWithProgramChooser() {
        UmlClassNode node = selectedClass.get();
        if (node == null) {
            statusConsumer.accept("Selecciona una clase importada desde código para elegir programa.");
            return;
        }
        openSourceForClass(node, UmlClassCodeEditorSettings.WINDOWS_OPEN_WITH_COMMAND, "selector de programa");
    }

    void openSelectedSourceWithSystemDefault() {
        UmlClassNode node = selectedClass.get();
        if (node == null) {
            statusConsumer.accept("Selecciona una clase importada desde código para abrir su archivo.");
            return;
        }
        openSourceForClass(node, UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND, "aplicación predeterminada del sistema");
    }

    void openSelectedSourceFolder() {
        UmlClassNode node = selectedClass.get();
        if (node == null) {
            statusConsumer.accept("Selecciona una clase importada desde código para abrir su carpeta.");
            return;
        }
        UmlClassSourceFileResolution resolution = sourceFileResolver.inspect(node);
        if (!resolution.resolved()) {
            statusConsumer.accept(resolution.userMessage(node.displayName()));
            return;
        }
        Path parent = resolution.path().map(Path::getParent).orElse(null);
        if (parent == null) {
            statusConsumer.accept("No se pudo determinar la carpeta del archivo fuente seleccionado.");
            return;
        }
        openPathWithSystemDefault(parent, "carpeta fuente");
    }

    void openSourceForClassId(String classId) {
        UmlClassDiagramDocument currentDocument = documentSupplier.get();
        if (classId == null || classId.isBlank() || currentDocument == null) {
            statusConsumer.accept("No hay clase UML válida para abrir código.");
            return;
        }
        currentDocument.classById(classId.strip()).ifPresentOrElse(
                node -> openSourceForClass(node, codeEditorSettings.effectiveCommand(), "editor de código"),
                () -> statusConsumer.accept("No se encontró la clase UML seleccionada."));
    }

    private void openSourceForClass(UmlClassNode node, String commandTemplate, String channelLabel) {
        UmlClassSourceFileResolution resolution = sourceFileResolver.inspect(node);
        Path path;
        boolean generatedPreview = false;
        if (resolution.resolved()) {
            path = resolution.path().orElseThrow();
        } else {
            try {
                path = sourcePreviewWriter.writePreview(node);
                generatedPreview = true;
            } catch (IOException exception) {
                statusConsumer.accept(resolution.userMessage(node.displayName())
                        + " Además, no se pudo generar una vista temporal de código: " + exception.getMessage());
                return;
            }
        }
        CodeEditorLauncher launcher = CodeEditorLauncher.configured(commandTemplate);
        try {
            launcher.open(path);
            if (generatedPreview) {
                statusConsumer.accept("La clase no tiene ruta fuente registrada; se abrió una vista temporal generada: " + path);
                return;
            }
            statusConsumer.accept("Archivo enviado a " + channelLabel + ": " + path);
        } catch (IOException exception) {
            if (!UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND.equals(launcher.commandTemplate())) {
                try {
                    CodeEditorLauncher.configured(UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND).open(path);
                    statusConsumer.accept("No se pudo abrir con '" + launcher.commandTemplate()
                            + "', pero se envió a la aplicación predeterminada del sistema: " + path);
                    return;
                } catch (IOException ignored) {
                    // Se informa el fallo original y se ofrecen rutas de configuración.
                }
            }
            statusConsumer.accept("No se pudo abrir con '" + launcher.commandTemplate()
                    + "'. Usa Configuración > Editor de código para elegir Code.exe, el sistema predeterminado o Abrir con de Windows. "
                    + "También puedes usar -D" + UmlClassCodeEditorSettings.PROPERTY
                    + " o " + UmlClassCodeEditorSettings.ENVIRONMENT + ". " + exception.getMessage());
        }
    }

    private void openPathWithSystemDefault(Path path, String label) {
        CodeEditorLauncher launcher = CodeEditorLauncher.configured(UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND);
        try {
            launcher.open(path);
            statusConsumer.accept("Abriendo " + label + ": " + path);
        } catch (IOException exception) {
            statusConsumer.accept("No se pudo abrir " + label + " con la aplicación predeterminada del sistema. "
                    + exception.getMessage());
        }
    }
}
