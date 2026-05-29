package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 16: la fuente lógica debe enseñar y demostrar el contrato completo. */
class LogicalBusinessStrongContractSourceTest {

    private static final Path ITEM_KIND = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/domain/logicalbusiness/LogicalBusinessItemKind.java");
    private static final Path SCANNER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/logicalbusiness/LogicalBusinessMarkdownScanner.java");
    private static final Path TEMPLATE = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/logical_business_intake_template.md");
    private static final Path GORDITO = Path.of(
            "src/main/resources/ai-resources/official-markdown/levantamiento-logico/logical_business_intake_optica_gordito.md");

    @Test
    void supportAndCalculationPrefixesAreFirstClassDomainElements() throws IOException {
        String itemKind = read(ITEM_KIND);
        String scanner = read(SCANNER);

        assertTrue(itemKind.contains("SUPPORTING_ASSUMPTION(\"SUP\")"));
        assertTrue(itemKind.contains("CALCULATION(\"CALC\")"));
        assertTrue(scanner.contains("contains(\"contexto\")"));
        assertTrue(scanner.contains("contains(\"calculo\")"));
    }

    @Test
    void templateTeachesAllOperationalPrefixesWithoutCreatingRealItems() throws IOException {
        String template = read(TEMPLATE);

        for (String token : new String[] {"SUP-XXX", "ACT-XXX", "EVID-XXX", "EST-XXX", "CON-XXX",
                "ENT-XXX", "ATR-XXX", "REL-XXX", "CALC-XXX"}) {
            assertTrue(template.contains(token), token);
        }
    }

    @Test
    void gorditoExampleDemonstratesRealTraceableIdsForDataAndLogic() throws IOException {
        String complete = read(GORDITO);

        for (String token : new String[] {"SUP-001", "ACT-001", "EVID-001", "EST-001", "CON-001",
                "ENT-001", "ATR-001", "REL-001", "CALC-001"}) {
            assertTrue(complete.contains(token), token);
        }
        assertTrue(complete.contains("total_orden - suma(PagoRecibido.monto)"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
