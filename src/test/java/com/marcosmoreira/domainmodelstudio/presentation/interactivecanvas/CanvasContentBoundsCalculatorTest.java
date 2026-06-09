package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import org.junit.jupiter.api.Test;

class CanvasContentBoundsCalculatorTest {

    @Test
    void appliesPaddingAndMinimumSizeToNodeLayouts() {
        CanvasContentBoundsCalculator calculator = new CanvasContentBoundsCalculator(10, 100, 80);

        CanvasBounds bounds = calculator.fromNodeLayouts(List.of(
                NodeLayout.at("a", 20, 30, 40, 50),
                NodeLayout.at("b", 100, 90, 20, 20)));

        assertEquals(10, bounds.x());
        assertEquals(20, bounds.y());
        assertEquals(120, bounds.width());
        assertEquals(100, bounds.height());
    }

    @Test
    void returnsMinimumCanvasWhenThereIsNoContent() {
        CanvasContentBoundsCalculator calculator = new CanvasContentBoundsCalculator(10, 100, 80);

        CanvasBounds bounds = calculator.fromNodeLayouts(List.of());

        assertEquals(0, bounds.x());
        assertEquals(0, bounds.y());
        assertEquals(100, bounds.width());
        assertEquals(80, bounds.height());
    }
}
