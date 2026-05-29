package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/** Configuración persistente del editor de código usado para abrir archivos fuente UML. */
final class UmlClassCodeEditorSettings {
    static final String PROPERTY = "dms.codeEditorCommand";
    static final String ENVIRONMENT = "DMS_CODE_EDITOR_COMMAND";
    static final String SYSTEM_DEFAULT_COMMAND = "<sistema>";
    static final String WINDOWS_OPEN_WITH_COMMAND = "<windows-abrir-con>";
    static final String DEFAULT_COMMAND = SYSTEM_DEFAULT_COMMAND;
    static final String PREFERENCES_NODE = "/com/marcosmoreira/domainmodelstudio";
    static final String PREFERENCE_KEY = "codeEditorCommand";

    private final Preferences preferences;
    private final Supplier<String> propertySupplier;
    private final Supplier<String> environmentSupplier;

    private UmlClassCodeEditorSettings(
            Preferences preferences,
            Supplier<String> propertySupplier,
            Supplier<String> environmentSupplier
    ) {
        this.preferences = Objects.requireNonNull(preferences, "preferences");
        this.propertySupplier = Objects.requireNonNull(propertySupplier, "propertySupplier");
        this.environmentSupplier = Objects.requireNonNull(environmentSupplier, "environmentSupplier");
    }

    static UmlClassCodeEditorSettings system() {
        return new UmlClassCodeEditorSettings(
                Preferences.userRoot().node(PREFERENCES_NODE),
                () -> System.getProperty(PROPERTY),
                () -> System.getenv(ENVIRONMENT));
    }

    static UmlClassCodeEditorSettings forTesting(
            Preferences preferences,
            Supplier<String> propertySupplier,
            Supplier<String> environmentSupplier
    ) {
        return new UmlClassCodeEditorSettings(preferences, propertySupplier, environmentSupplier);
    }

    String effectiveCommand() {
        String property = clean(propertySupplier.get());
        if (!property.isBlank()) {
            return property;
        }
        String environment = clean(environmentSupplier.get());
        if (!environment.isBlank()) {
            return environment;
        }
        String user = userCommand();
        return user.isBlank() ? DEFAULT_COMMAND : user;
    }

    String userCommand() {
        return clean(preferences.get(PREFERENCE_KEY, ""));
    }

    String sourceLabel() {
        if (!clean(propertySupplier.get()).isBlank()) {
            return "propiedad JVM -D" + PROPERTY;
        }
        if (!clean(environmentSupplier.get()).isBlank()) {
            return "variable de entorno " + ENVIRONMENT;
        }
        if (!userCommand().isBlank()) {
            return "preferencia guardada del programa";
        }
        return "valor por defecto";
    }

    String summary() {
        return "Editor efectivo: " + humanReadableCommand(effectiveCommand()) + " (" + sourceLabel() + ").";
    }

    boolean externallyOverridden() {
        return !clean(propertySupplier.get()).isBlank() || !clean(environmentSupplier.get()).isBlank();
    }

    void saveUserCommand(String command) {
        String normalized = clean(command);
        if (normalized.isBlank()) {
            resetUserCommand();
            return;
        }
        preferences.put(PREFERENCE_KEY, normalized);
        flushQuietly();
    }

    void saveSystemDefaultCommand() {
        saveUserCommand(SYSTEM_DEFAULT_COMMAND);
    }

    void saveWindowsOpenWithCommand() {
        saveUserCommand(WINDOWS_OPEN_WITH_COMMAND);
    }

    void resetUserCommand() {
        preferences.remove(PREFERENCE_KEY);
        flushQuietly();
    }

    private void flushQuietly() {
        try {
            preferences.flush();
        } catch (BackingStoreException ignored) {
            // La preferencia se mantiene en memoria del nodo aunque el flush falle; la UI informa el comando efectivo.
        }
    }

    private static String humanReadableCommand(String command) {
        if (SYSTEM_DEFAULT_COMMAND.equals(command)) {
            return "aplicación predeterminada del sistema";
        }
        if (WINDOWS_OPEN_WITH_COMMAND.equals(command)) {
            return "diálogo Abrir con de Windows";
        }
        return command;
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
