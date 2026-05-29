package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de la Tanda 31 para mantener la persistencia .dms modular sin cambiar formato. */
class DmsProjectPersistenceSectionRefactorSourceTest {

    private static final Path READER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonReader.java");
    private static final Path WRITER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonWriter.java");
    private static final Path CONCEPTUAL_READER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectConceptualModelJsonReader.java");
    private static final Path CONCEPTUAL_WRITER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectConceptualModelJsonWriter.java");
    private static final Path PAYLOAD_READER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectSpecializedPayloadReader.java");
    private static final Path PAYLOAD = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectSpecializedPayload.java");

    @Test
    void principalReaderAndWriterShouldRemainFormatCoordinators() throws IOException {
        String reader = text(READER);
        String writer = text(WRITER);

        assertTrue(lines(READER) <= 260, "El lector .dms principal debe quedar como coordinador del formato.");
        assertTrue(lines(WRITER) <= 300, "El writer .dms principal debe quedar como coordinador del formato.");
        assertTrue(reader.contains("DmsProjectConceptualModelJsonReader"));
        assertTrue(reader.contains("DmsProjectSpecializedPayloadReader"));
        assertTrue(reader.contains("DmsProjectVisualStateJsonReader"));
        assertTrue(writer.contains("DmsProjectConceptualModelJsonWriter"));
        assertTrue(writer.contains("DmsProjectAssetsJsonWriter"));
        assertFalse(reader.contains("private DiagramModel readModel"));
        assertFalse(writer.contains("private void writeModel"));
    }

    @Test
    void conceptualModelPersistenceShouldOwnLegacyEntitiesAttributesAndRelationships() throws IOException {
        String conceptualReader = text(CONCEPTUAL_READER);
        String conceptualWriter = text(CONCEPTUAL_WRITER);

        assertTrue(conceptualReader.contains("DiagramModel readModel"));
        assertTrue(conceptualReader.contains("EntityElement"));
        assertTrue(conceptualReader.contains("AttributeElement"));
        assertTrue(conceptualReader.contains("RelationshipElement"));
        assertTrue(conceptualWriter.contains("void writeModel"));
        assertTrue(conceptualWriter.contains("writeEntities"));
        assertTrue(conceptualWriter.contains("writeRelationships"));
        assertTrue(conceptualWriter.contains("DmsProjectSpecializedJsonWriter"));
    }

    @Test
    void specializedPayloadReaderShouldOwnOptionalSpecializedDocuments() throws IOException {
        String payloadReader = text(PAYLOAD_READER);
        String payload = text(PAYLOAD);

        assertTrue(payloadReader.contains("DmsProjectSpecializedJsonReader"));
        assertTrue(payloadReader.contains("logicalBusinessDocument"));
        assertTrue(payloadReader.contains("logicalBusinessGraphDocument"));
        assertTrue(payloadReader.contains("dataDictionary"));
        assertTrue(payload.contains("record DmsProjectSpecializedPayload"));
        assertTrue(payload.contains("DataDictionaryDocument"));
        assertTrue(payload.contains("LogicalBusinessGraphDocument"));
    }

    @Test
    void persistentJsonKeysShouldStayStable() throws IOException {
        String combined = text(READER) + text(WRITER) + text(CONCEPTUAL_READER) + text(CONCEPTUAL_WRITER) + text(PAYLOAD_READER);

        assertTrue(combined.contains("formatVersion"));
        assertTrue(combined.contains("project"));
        assertTrue(combined.contains("model"));
        assertTrue(combined.contains("entities"));
        assertTrue(combined.contains("relationships"));
        assertTrue(combined.contains("layouts"));
        assertTrue(combined.contains("styles"));
        assertTrue(combined.contains("view"));
        assertTrue(combined.contains("assets"));
    }

    private static String text(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static long lines(Path path) throws IOException {
        return Files.lines(path, StandardCharsets.UTF_8).count();
    }
}
