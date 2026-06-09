package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.prefs.Preferences;
import org.junit.jupiter.api.Test;

/** La acción Abrir código debe funcionar con el sistema por defecto sin exigir VS Code instalado. */
class Tanda17UmlClassOpenCodeDefaultSourceTest {

    @Test
    void defaultCodeOpeningShouldUseOperatingSystemDefault() throws Exception {
        Preferences preferences = Preferences.userRoot().node("/com/marcosmoreira/domainmodelstudio/tests/umlclass/code-editor/tanda17-default");
        preferences.clear();
        UmlClassCodeEditorSettings settings = UmlClassCodeEditorSettings.forTesting(preferences, () -> "", () -> "");

        assertEquals(UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND, settings.effectiveCommand(),
                "Abrir código debe usar por defecto la aplicación asociada por Windows o el diálogo del sistema, no exigir 'code'.");
        assertTrue(settings.summary().contains("aplicación predeterminada"));
        preferences.removeNode();
    }
}
