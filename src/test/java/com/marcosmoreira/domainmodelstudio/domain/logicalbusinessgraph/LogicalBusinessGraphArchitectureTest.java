package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class LogicalBusinessGraphArchitectureTest {

    @Test
    void logicalBusinessGraphDomainShouldNotDependOnFreeGraphCanvasOrInfrastructure() throws Exception {
        Path root = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusinessgraph");
        try (Stream<Path> paths = Files.walk(root)) {
            String source = paths.filter(path -> path.toString().endsWith(".java"))
                    .map(LogicalBusinessGraphArchitectureTest::read)
                    .reduce("", String::concat);

            assertFalse(source.contains("domain.freegraph"));
            assertFalse(source.contains("FreeGraphDocument"));
            assertFalse(source.contains("javafx"));
            assertFalse(source.contains("infrastructure"));
            assertFalse(source.contains("presentation"));
            assertTrue(source.contains("LogicalBusinessGraphDocument"));
        }
    }

    private static String read(Path path) {
        try {
            return Files.readString(path);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
