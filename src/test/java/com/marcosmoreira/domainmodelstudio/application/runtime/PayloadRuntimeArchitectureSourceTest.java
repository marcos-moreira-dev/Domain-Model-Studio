package com.marcosmoreira.domainmodelstudio.application.runtime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class PayloadRuntimeArchitectureSourceTest {

    @Test
    void payloadRuntimeShouldStayApplicationLevelAndNoJavaFx() throws IOException {
        Path runtime = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/runtime");
        String source = Files.walk(runtime)
                .filter(path -> path.toString().endsWith(".java"))
                .map(this::read)
                .reduce("", (left, right) -> left + "\n" + right);

        assertTrue(source.contains("class DefaultPayloadRuntimeRegistry"));
        assertTrue(source.contains("record DiagramTypeRuntimeDescriptor"));
        assertTrue(source.contains("Optional<PayloadRuntimeDescriptor> payload"));
        assertFalse(source.contains("javafx."));
        assertFalse(source.contains("com.marcosmoreira.domainmodelstudio.presentation"));
        assertFalse(source.contains("com.marcosmoreira.domainmodelstudio.infrastructure"));
    }

    @Test
    void dmsPayloadValidatorShouldDelegateToPayloadRuntimeRegistry() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectPayloadConsistencyValidator.java"));

        assertTrue(source.contains("PayloadRuntimeRegistry"));
        assertTrue(source.contains("DefaultPayloadRuntimeRegistry"));
        assertTrue(source.contains("payloadRuntimeRegistry.require(typeId)"));
        assertTrue(source.contains("detectSpecializedPayloadTypeIds"));
        assertFalse(source.contains("private boolean isBehaviorType"));
        assertFalse(source.contains("private boolean isArchitectureType"));
    }

    private String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
