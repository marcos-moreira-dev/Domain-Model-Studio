package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassStructuralSourceTest {

    @Test
    void umlClassRenderKitDoesNotCapMembersAtFourAnymore() throws Exception {
        String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java"));
        assertTrue(!source.contains("limit(4)"));
        assertTrue(source.contains("UmlClassMemberRenderPolicy"));
    }

    @Test
    void umlClassLayoutHasDedicatedPolicyAndMetricsCalculator() throws Exception {
        String factory = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutSpecificationFactory.java"));
        String policy = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/UmlClassLayoutPolicy.java"));
        assertTrue(factory.contains("UmlClassLayoutPolicy"));
        assertTrue(policy.contains("UmlClassBoxMetricsCalculator"));
        assertTrue(policy.contains("VisualElementLayoutIds.umlModule"));
    }
}
