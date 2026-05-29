package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VisualTextFitPolicyTest {

    private final VisualTextFitPolicy policy = new VisualTextFitPolicy();

    @Test
    void expandsCardsWhenTextRequiresMoreSpace() {
        VisualTextFitPolicy.BoxSize base = new VisualTextFitPolicy.BoxSize(160.0, 72.0);

        VisualTextFitPolicy.BoxSize fitted = policy.fitCard(
                base,
                "Actualizar asignación vigente del estudiante",
                "Valida cupo, año lectivo, sección y reglas administrativas antes de guardar.");

        assertTrue(fitted.width() > base.width(), "El ancho debe crecer para títulos largos.");
        assertTrue(fitted.height() > base.height(), "El alto debe crecer cuando hay más líneas de texto.");
    }

    @Test
    void expandsDiamondsAsSquareToPreserveDecisionGeometry() {
        VisualTextFitPolicy.BoxSize base = new VisualTextFitPolicy.BoxSize(104.0, 104.0);

        VisualTextFitPolicy.BoxSize fitted = policy.fitDiamond(
                base,
                "¿Sección activa con cupo disponible?",
                "");

        assertTrue(fitted.width() >= base.width());
        assertTrue(fitted.height() >= base.height());
        assertTrue(Math.abs(fitted.width() - fitted.height()) < 0.001,
                "El rombo debe crecer como cuadrado para conservar su geometría académica.");
    }
}
