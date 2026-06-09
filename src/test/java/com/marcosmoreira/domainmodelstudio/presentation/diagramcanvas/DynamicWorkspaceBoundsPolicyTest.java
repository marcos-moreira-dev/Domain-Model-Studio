package com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.geometry.BoundingBox;
import org.junit.jupiter.api.Test;

class DynamicWorkspaceBoundsPolicyTest {

    @Test
    void keepsBaseWorkspaceForSmallContent() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();
        DynamicWorkspaceBoundsPolicy policy = DynamicWorkspaceBoundsPolicy.defaults();

        DiagramSurfaceWorkspaceSize size = policy.expandToFit(
                DiagramSurfaceWorkspaceSize.fromConfig(config),
                new BoundingBox(100, 100, 800, 600),
                config
        );

        assertEquals(config.workspaceWidth(), size.width());
        assertEquals(config.workspaceHeight(), size.height());
    }

    @Test
    void expandsWorkspaceWhenContentExceedsBaseBounds() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();
        DynamicWorkspaceBoundsPolicy policy = new DynamicWorkspaceBoundsPolicy(900, 1000);

        DiagramSurfaceWorkspaceSize size = policy.expandToFit(
                DiagramSurfaceWorkspaceSize.fromConfig(config),
                new BoundingBox(11000, 7000, 3500, 2800),
                config
        );

        assertTrue(size.width() >= 15400);
        assertTrue(size.height() >= 10700);
        assertEquals(16000.0, size.width());
        assertEquals(11000.0, size.height());
    }

    @Test
    void neverShrinksExpandedWorkspace() {
        DiagramSurfaceConfig config = DiagramSurfaceConfig.defaults();
        DynamicWorkspaceBoundsPolicy policy = DynamicWorkspaceBoundsPolicy.defaults();
        DiagramSurfaceWorkspaceSize expanded = new DiagramSurfaceWorkspaceSize(30000, 22000);

        DiagramSurfaceWorkspaceSize size = policy.expandToFit(
                expanded,
                new BoundingBox(1000, 1000, 300, 300),
                config
        );

        assertEquals(expanded.width(), size.width());
        assertEquals(expanded.height(), size.height());
    }
}
