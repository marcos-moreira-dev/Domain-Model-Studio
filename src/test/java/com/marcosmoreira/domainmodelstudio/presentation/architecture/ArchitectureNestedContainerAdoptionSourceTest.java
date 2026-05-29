package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ArchitectureNestedContainerAdoptionSourceTest {

    @Test
    void movedArchitectureContainersAreClampedInsideAdoptedParentContainers() throws Exception {
        String support = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/ArchitectureContainerLayoutSupport.java"));

        assertTrue(support.contains("keepMovedContainerInsideAdoptedParent"));
        assertTrue(support.contains("NESTED_CONTAINER_MARGIN"));
        assertTrue(support.contains("containerPolicy.centerInside(child, candidate)"));
    }
}
