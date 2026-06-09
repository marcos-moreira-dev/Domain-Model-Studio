package com.marcosmoreira.domainmodelstudio.application.importbatch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarkdownBatchImportPolicyTest {

    @Test
    void defaultPolicyShouldImportFoldersRecursivelyForFilledTemplatePacks() {
        MarkdownBatchImportPolicy policy = MarkdownBatchImportPolicy.defaultPolicy();

        assertTrue(policy.recursive(), "Abrir carpeta Markdown debe recorrer subcarpetas por defecto.");
        assertTrue(policy.maxFiles() >= 300, "El límite debe soportar paquetes reales de plantillas por rubro.");
    }

    @Test
    void flatLegacyPolicyKeepsSingleFolderModeAvailableForTestsAndLegacyFlows() {
        MarkdownBatchImportPolicy policy = MarkdownBatchImportPolicy.flatLegacyPolicy();

        assertFalse(policy.recursive());
    }
}
