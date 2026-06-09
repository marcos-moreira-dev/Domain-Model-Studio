package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.DiagramMarkdownImportDispatcher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Verifica que los tipos importables puedan guardarse y reabrirse sin degradarse. */
class DmsProjectJsonAvailableTypesRoundTripTest {

    @Test
    void shouldRoundTripEveryImportableOfficialExample() throws Exception {
        Map<DiagramTypeId, Path> examples = officialImportableExamples();
        DiagramMarkdownImportDispatcher importer = new DiagramMarkdownImportDispatcher(new DefaultDiagramTypeRegistry());
        DmsProjectJsonWriter writer = new DmsProjectJsonWriter();
        DmsProjectJsonReader reader = new DmsProjectJsonReader();

        for (Map.Entry<DiagramTypeId, Path> entry : examples.entrySet()) {
            DiagramProject imported = importer.parse(entry.getValue());
            assertEquals(entry.getKey(), imported.metadata().diagramTypeId(), "Tipo importado desde " + entry.getValue());
            assertEquals(entry.getValue().toString(), imported.metadata().sourceMarkdownPath(), "Ruta Markdown fuente");

            String json = writer.write(imported);
            DiagramProject reopened = reader.read(json);

            assertEquals(entry.getKey(), reopened.metadata().diagramTypeId(), "Tipo reabierto desde " + entry.getValue());
            assertEquals(entry.getValue().toString(), reopened.metadata().sourceMarkdownPath(), "Ruta Markdown fuente reabierta");
            assertPrimaryPayloadPresent(reopened, entry.getKey());
        }
    }

    private static Map<DiagramTypeId, Path> officialImportableExamples() {
        Map<DiagramTypeId, Path> examples = new LinkedHashMap<>();
        examples.put(DiagramTypeId.CONCEPTUAL_MODEL, Path.of("examples/markdown/diagramas/conceptual_model_colegio_minimo_importable.md"));
        examples.put(DiagramTypeId.ADMIN_MODULE_MAP, Path.of("examples/markdown/diagramas/admin_module_map_restaurante_minimo.md"));
        examples.put(DiagramTypeId.ROLES_PERMISSIONS_MAP, Path.of("examples/markdown/diagramas/roles_permissions_optica_minimo.md"));
        examples.put(DiagramTypeId.SCREEN_FLOW, Path.of("examples/markdown/diagramas/screen_flow_ventas_minimo.md"));
        examples.put(DiagramTypeId.ADMIN_WIREFRAMES, Path.of("examples/markdown/diagramas/admin_wireframes_ventas_minimo.md"));
        examples.put(DiagramTypeId.UML_CLASS, Path.of("examples/markdown/diagramas/uml_class_restaurante_minimo.md"));
        examples.put(DiagramTypeId.C4_CONTEXT, Path.of("examples/markdown/diagramas/c4_context_sistema_administrativo_minimo.md"));
        examples.put(DiagramTypeId.C4_CONTAINERS, Path.of("examples/markdown/diagramas/c4_containers_sistema_administrativo_minimo.md"));
        examples.put(DiagramTypeId.TECHNICAL_DEPLOYMENT, Path.of("examples/markdown/diagramas/technical_deployment_piloto_minimo.md"));
        examples.put(DiagramTypeId.BPMN_BASIC, Path.of("examples/markdown/diagramas/bpmn_basic_venta_minimo.md"));
        examples.put(DiagramTypeId.OPERATIONAL_FLOW, Path.of("examples/markdown/diagramas/operational_flow_soporte_minimo.md"));
        examples.put(DiagramTypeId.UML_USE_CASE, Path.of("examples/markdown/diagramas/uml_use_case_restaurante_minimo.md"));
        examples.put(DiagramTypeId.UML_ACTIVITY, Path.of("examples/markdown/diagramas/uml_activity_cierre_caja_minimo.md"));
        examples.put(DiagramTypeId.UML_SEQUENCE, Path.of("examples/markdown/diagramas/uml_sequence_login_minimo.md"));
        examples.put(DiagramTypeId.UML_STATE, Path.of("examples/markdown/diagramas/uml_state_orden_minimo.md"));
        examples.values().forEach(path -> assertTrue(Files.exists(path), "Debe existir " + path));
        return examples;
    }

    private static void assertPrimaryPayloadPresent(DiagramProject project, DiagramTypeId typeId) {
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(typeId)) {
            assertTrue(project.model().entityCount() > 0);
        } else if (DiagramTypeId.ADMIN_MODULE_MAP.equals(typeId)) {
            assertTrue(project.moduleMap().isPresent());
        } else if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(typeId)) {
            assertTrue(project.rolesPermissions().isPresent());
        } else if (DiagramTypeId.SCREEN_FLOW.equals(typeId)) {
            assertTrue(project.screenFlow().isPresent());
        } else if (DiagramTypeId.ADMIN_WIREFRAMES.equals(typeId)) {
            assertTrue(project.wireframe().isPresent());
        } else if (DiagramTypeId.UML_CLASS.equals(typeId)) {
            assertTrue(project.umlClassDiagram().isPresent());
        } else if (DiagramTypeId.C4_CONTEXT.equals(typeId)
                || DiagramTypeId.C4_CONTAINERS.equals(typeId)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(typeId)) {
            assertTrue(project.architectureDiagram().isPresent());
            assertEquals(typeId, project.architectureDiagram().get().diagramKind().diagramTypeId());
        } else {
            assertTrue(project.behaviorDiagram().isPresent());
            assertEquals(typeId, project.behaviorDiagram().get().diagramKind().diagramTypeId());
        }
    }
}
