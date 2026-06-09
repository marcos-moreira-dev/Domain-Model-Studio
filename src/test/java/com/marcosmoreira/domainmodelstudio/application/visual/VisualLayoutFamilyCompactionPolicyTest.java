package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import org.junit.jupiter.api.Test;

class VisualLayoutFamilyCompactionPolicyTest {

    private final DefaultVisualLayoutGenerator generator = new DefaultVisualLayoutGenerator();

    @Test
    void processLayoutsWrapLongProceduresInsideLaneRows() {
        DiagramPoint first = generator.positionFor(behaviorNode(
                BusinessProcessAutoLayoutPolicy.BPMN_BASE + BusinessProcessAutoLayoutPolicy.NODE_OFFSET));
        DiagramPoint wrapped = generator.positionFor(behaviorNode(
                BusinessProcessAutoLayoutPolicy.BPMN_BASE
                        + BusinessProcessAutoLayoutPolicy.NODE_OFFSET
                        + BusinessProcessAutoLayoutPolicy.STEPS_PER_ROW));

        assertEquals(first.x(), wrapped.x());
        assertTrue(wrapped.y() > first.y(), "el sexto paso debe bajar a una nueva fila, no alargar el diagrama horizontalmente");
    }

    @Test
    void activityLayoutsWrapDeepFlowsIntoAdditionalColumns() {
        DiagramPoint first = generator.positionFor(behaviorNode(UmlFlowAutoLayoutPolicy.ACTIVITY_BASE));
        DiagramPoint wrapped = generator.positionFor(behaviorNode(
                UmlFlowAutoLayoutPolicy.ACTIVITY_BASE + 7 * UmlFlowAutoLayoutPolicy.ORDER_BUCKET));

        assertTrue(wrapped.x() > first.x(), "los flujos de actividad largos deben iniciar otra columna");
        assertEquals(first.y(), wrapped.y());
    }

    @Test
    void stateLayoutsWrapLongLifeCyclesIntoAdditionalRows() {
        DiagramPoint first = generator.positionFor(behaviorNode(UmlFlowAutoLayoutPolicy.STATE_BASE));
        DiagramPoint wrapped = generator.positionFor(behaviorNode(
                UmlFlowAutoLayoutPolicy.STATE_BASE + 6 * UmlFlowAutoLayoutPolicy.ORDER_BUCKET));

        assertEquals(first.x(), wrapped.x());
        assertTrue(wrapped.y() > first.y(), "los estados largos deben iniciar otra fila, no una línea horizontal infinita");
    }

    @Test
    void screenFlowGroupsWrapAfterTwoModuleColumns() {
        DiagramPoint firstGroup = generator.positionFor(screenNode(0));
        DiagramPoint secondColumn = generator.positionFor(screenNode(1_000));
        DiagramPoint secondRow = generator.positionFor(screenNode(2_000));

        assertTrue(secondColumn.x() > firstGroup.x());
        assertEquals(firstGroup.x(), secondRow.x());
        assertTrue(secondRow.y() > firstGroup.y());
    }

    @Test
    void wireframeScreensWrapAfterTwoScreens() {
        DiagramPoint firstScreen = generator.positionFor(wireframeScreen(0));
        DiagramPoint secondScreen = generator.positionFor(wireframeScreen(1_000));
        DiagramPoint thirdScreen = generator.positionFor(wireframeScreen(2_000));

        assertTrue(secondScreen.x() > firstScreen.x());
        assertEquals(firstScreen.x(), thirdScreen.x());
        assertTrue(thirdScreen.y() > firstScreen.y());
    }

    private static VisualNodeReference behaviorNode(int order) {
        return new VisualNodeReference(VisualElementLayoutIds.behaviorNode("n" + order), 180.0, 80.0, order);
    }

    private static VisualNodeReference screenNode(int order) {
        return new VisualNodeReference(VisualElementLayoutIds.screen("screen" + order), 210.0, 90.0, order);
    }

    private static VisualNodeReference wireframeScreen(int order) {
        return new VisualNodeReference(VisualElementLayoutIds.wireframeScreen("wire" + order), 360.0, 260.0, order);
    }
}
