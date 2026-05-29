package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import org.junit.jupiter.api.Test;

import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlClassCodeEditorSettingsTest {

    @Test
    void usesSavedPreferenceWhenNoExternalOverrideExists() throws Exception {
        Preferences preferences = temporaryPreferences("saved");
        UmlClassCodeEditorSettings settings = UmlClassCodeEditorSettings.forTesting(preferences, () -> "", () -> "");

        settings.saveUserCommand("code --goto %f");

        assertEquals("code --goto %f", settings.effectiveCommand());
        assertEquals("preferencia guardada del programa", settings.sourceLabel());
        preferences.removeNode();
    }

    @Test
    void propertyOverridesSavedPreference() throws Exception {
        Preferences preferences = temporaryPreferences("property");
        preferences.put(UmlClassCodeEditorSettings.PREFERENCE_KEY, "code");
        UmlClassCodeEditorSettings settings = UmlClassCodeEditorSettings.forTesting(
                preferences,
                () -> "C:/Tools/Code/bin/code.cmd --reuse-window",
                () -> "ignored");

        assertEquals("C:/Tools/Code/bin/code.cmd --reuse-window", settings.effectiveCommand());
        assertTrue(settings.externallyOverridden());
        preferences.removeNode();
    }

    @Test
    void resetReturnsToDefaultWhenNoOverrideExists() throws Exception {
        Preferences preferences = temporaryPreferences("reset");
        UmlClassCodeEditorSettings settings = UmlClassCodeEditorSettings.forTesting(preferences, () -> null, () -> null);
        settings.saveUserCommand("idea %f");

        settings.resetUserCommand();

        assertEquals(UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND, settings.effectiveCommand());
        assertEquals("valor por defecto", settings.sourceLabel());
        preferences.removeNode();
    }

    @Test
    void canPersistSystemDefaultAndWindowsOpenWithCommands() throws Exception {
        Preferences preferences = temporaryPreferences("system-open");
        UmlClassCodeEditorSettings settings = UmlClassCodeEditorSettings.forTesting(preferences, () -> "", () -> "");

        settings.saveSystemDefaultCommand();
        assertEquals(UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND, settings.effectiveCommand());

        settings.saveWindowsOpenWithCommand();
        assertEquals(UmlClassCodeEditorSettings.WINDOWS_OPEN_WITH_COMMAND, settings.effectiveCommand());
        preferences.removeNode();
    }

    private Preferences temporaryPreferences(String name) throws Exception {
        Preferences preferences = Preferences.userRoot().node("/com/marcosmoreira/domainmodelstudio/tests/umlclass/code-editor/" + name);
        preferences.clear();
        return preferences;
    }
}
