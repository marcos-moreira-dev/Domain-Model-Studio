package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind.*;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogicalBusinessGraphRelationKindTest {

    @Test
    void consultsShouldAllowReportsBecauseReportsCanBeReadWithoutTransformingThem() {
        assertTrue(CONSULTS.canConnect(USE_CASE, REPORT));
        assertTrue(CONSULTS.canConnect(ACTION, REPORT));
        assertFalse(CONSULTS.canConnect(MACRO_FLOW, REPORT));
    }

    @Test
    void enablesShouldAllowStateAsAConditionThatUnlocksLaterBehavior() {
        assertTrue(ENABLES.canConnect(STATE, USE_CASE));
        assertTrue(ENABLES.canConnect(STATE, ACTION));
        assertFalse(ENABLES.canConnect(STATE, REPORT));
    }
}
