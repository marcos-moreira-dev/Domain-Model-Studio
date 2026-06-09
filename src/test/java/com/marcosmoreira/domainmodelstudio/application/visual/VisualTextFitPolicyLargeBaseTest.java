package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Verifica que la política textual no reduzca contenedores o zonas grandes. */
class VisualTextFitPolicyLargeBaseTest {

    @Test
    void textFitNeverShrinksLargeBaseContainers() {
        VisualTextFitPolicy policy = new VisualTextFitPolicy();
        VisualTextFitPolicy.BoxSize base = new VisualTextFitPolicy.BoxSize(560.0, 340.0);

        VisualTextFitPolicy.BoxSize fitted = policy.fitCard(base, "Sistema", "Zona contenedora");

        assertEquals(base.width(), fitted.width());
        assertEquals(base.height(), fitted.height());
    }
}
