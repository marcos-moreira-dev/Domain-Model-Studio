package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionDecision;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RolesPermissionsDocument;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

final class Tanda5BOfficialExamplesConsistencyTest {

    @Test
    void uensRolesPermissionsScopeColumnDoesNotTurnAssignmentsIntoConditionalCells() throws Exception {
        String markdown = readResource("src/main/resources/ai-resources/official-markdown/diagramas/roles_permissions_uens_gordito.md");

        RolesPermissionsDocument document = new RolesPermissionsMarkdownParser()
                .parse(markdown, "roles_permissions_uens_gordito.md")
                .rolesPermissions()
                .orElseThrow();

        assertEquals(4, document.conditionalAssignmentCount());
        assertEquals(PermissionDecision.ALLOWED,
                document.assignmentFor("admin", "estudiantes_leer").orElseThrow().decision());
        assertTrue(document.assignmentFor("admin", "estudiantes_leer").orElseThrow()
                .notes().contains("Alcance: global"));
    }

    @Test
    void uensDataDictionaryDocumentsTheCoreConceptualFamily() throws Exception {
        String markdown = readResource("src/main/resources/ai-resources/official-markdown/diagramas/data_dictionary_uens_gordito.md");

        DataDictionaryDocument document = new DataDictionaryMarkdownParser()
                .parse(markdown, "data_dictionary_uens_gordito.md")
                .dataDictionary()
                .orElseThrow();
        Set<String> entities = document.entities().stream()
                .map(entity -> normalize(entity.displayName()))
                .collect(Collectors.toSet());

        assertTrue(entities.contains("estudiante"));
        assertTrue(entities.contains("representante legal"));
        assertTrue(entities.contains("seccion"));
        assertTrue(entities.contains("clase"));
        assertTrue(entities.contains("calificacion"));
        assertTrue(entities.contains("usuario sistema administrativo"));
        assertTrue(entities.contains("docente"));
        assertTrue(entities.contains("asignatura"));
        String normalizedMarkdown = normalize(markdown);
        assertTrue(entities.contains("reporte solicitud queue")
                || normalizedMarkdown.contains("reporte solicitud queue"));
        assertTrue(entities.contains("auditoria evento")
                || normalizedMarkdown.contains("auditoria evento"));
    }

    @Test
    void umlActivitySupportsSwimlanesPromisedByTheoryAndTemplates() throws Exception {
        assertTrue(BehaviorDiagramKind.UML_ACTIVITY.nodeKinds().contains(BehaviorNodeKind.LANE));
        String markdown = readResource("src/main/resources/ai-resources/official-markdown/diagramas/uml_activity_registrar_matricula_uens_gordito.md");

        var document = new BehaviorMarkdownParser()
                .parse(markdown, "uml_activity_registrar_matricula_uens_gordito.md")
                .behaviorDiagram()
                .orElseThrow();

        assertTrue(document.nodes().stream().anyMatch(node -> node.kind() == BehaviorNodeKind.LANE));
        assertTrue(document.edges().stream().noneMatch(edge -> document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.LANE)
                .anyMatch(lane -> lane.id().equals(edge.sourceNodeId()) || lane.id().equals(edge.targetNodeId()))));
    }

    @Test
    void uensUseCaseExampleExercisesIncludeAndExtendRelationships() throws Exception {
        String markdown = readResource("src/main/resources/ai-resources/official-markdown/diagramas/uml_use_case_uens_gordito.md");

        var document = new BehaviorMarkdownParser()
                .parse(markdown, "uml_use_case_uens_gordito.md")
                .behaviorDiagram()
                .orElseThrow();

        assertTrue(document.edges().stream().anyMatch(edge -> edge.kind() == BehaviorEdgeKind.INCLUDE));
        assertTrue(document.edges().stream().anyMatch(edge -> edge.kind() == BehaviorEdgeKind.EXTEND));
    }

    private static String readResource(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }

    private static String normalize(String value) {
        return java.text.Normalizer.normalize(value == null ? "" : value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('_', ' ')
                .replace('-', ' ')
                .toLowerCase(java.util.Locale.ROOT)
                .replaceAll("\\s+", " ")
                .strip();
    }
}
