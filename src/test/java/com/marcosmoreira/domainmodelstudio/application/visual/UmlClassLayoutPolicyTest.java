package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UmlClassLayoutPolicyTest {

    @Test
    void moduleContainerExpandsAccordingToContainedClasses() {
        UmlModuleGroup module = new UmlModuleGroup("academico", "Académico", "uens.academico", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 1),
                List.of(module),
                List.of(node("estudiante", module.id()), node("seccion", module.id()), node("matricula", module.id())),
                List.of(), "");

        var references = new UmlClassLayoutPolicy().visualReferences(document, 0);
        var moduleReference = references.stream()
                .filter(reference -> reference.layoutId().value().equals("uml-module:academico"))
                .findFirst()
                .orElseThrow();

        assertTrue(moduleReference.preferredWidth() >= 460.0);
        assertTrue(moduleReference.preferredHeight() > 250.0);
    }

    @Test
    void classReferenceUsesContentAwareMetrics() {
        UmlModuleGroup module = new UmlModuleGroup("academico", "Académico", "uens.academico", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 1),
                List.of(module),
                List.of(new UmlClassNode("estudiante", module.id(), "Estudiante", "uens.academico",
                        UmlClassKind.CLASS, UmlVisibility.PUBLIC, "Persona matriculada", "",
                        List.of(member("id"), member("nombres"), member("apellidos")), "")),
                List.of(), "");

        var reference = new UmlClassLayoutPolicy().visualReferences(document, 0).stream()
                .filter(item -> item.layoutId().value().equals("uml-class:estudiante"))
                .findFirst()
                .orElseThrow();

        assertTrue(reference.preferredHeight() > 130.0);
        assertTrue(reference.preferredWidth() >= 220.0);
    }



    @Test
    void classReferenceHeightUsesConfiguredRenderProfile() {
        UmlModuleGroup module = new UmlModuleGroup("academico", "Académico", "uens.academico", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "UENS", "borrador", LocalDate.of(2026, 5, 1),
                List.of(module),
                List.of(new UmlClassNode("estudiante", module.id(), "Estudiante", "uens.academico",
                        UmlClassKind.CLASS, UmlVisibility.PUBLIC, "Persona matriculada", "", manyMembers(40), "")),
                List.of(), "");

        var light = new UmlClassLayoutPolicy(UmlSourceImportRenderProfile.LIGHT).visualReferences(document, 0).stream()
                .filter(item -> item.layoutId().value().equals("uml-class:estudiante"))
                .findFirst()
                .orElseThrow();
        var detailed = new UmlClassLayoutPolicy(UmlSourceImportRenderProfile.DETAILED).visualReferences(document, 0).stream()
                .filter(item -> item.layoutId().value().equals("uml-class:estudiante"))
                .findFirst()
                .orElseThrow();

        assertTrue(light.preferredHeight() < detailed.preferredHeight(),
                "El layout debe usar el mismo perfil que el render para evitar cajas sobredimensionadas.");
    }

    @Test
    void sourceImportedModulesAreGroupedByRootBeforeVisualOrder() {
        UmlModuleGroup frontend = new UmlModuleGroup("frontend-productos", "productos", "src/app/features/productos", "", "Source root: frontend");
        UmlModuleGroup backend = new UmlModuleGroup("backend-pedidos", "pedidos", "com/acme/pedidos", "", "Source root: backend");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "Cedro", "borrador", LocalDate.of(2026, 5, 1),
                List.of(frontend, backend),
                List.of(node("productoList", frontend.id()), node("pedidoController", backend.id())),
                List.of(), "");

        var references = new UmlClassLayoutPolicy().visualReferences(document, 0);
        int backendOrder = orderOf(references, "uml-module:backend-pedidos");
        int frontendOrder = orderOf(references, "uml-module:frontend-productos");

        assertTrue(backendOrder < frontendOrder, "Backend debe quedar antes que frontend en el mega diagrama.");
    }

    @Test
    void classesInsideModuleFollowApplicationLayerOrder() {
        UmlModuleGroup module = new UmlModuleGroup("backend-pedidos", "pedidos", "com/acme/pedidos", "", "Source root: backend");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "Cedro", "borrador", LocalDate.of(2026, 5, 1),
                List.of(module),
                List.of(
                        classNode("repo", module.id(), "PedidoRepository", UmlClassKind.REPOSITORY),
                        classNode("dto", module.id(), "PedidoDto", UmlClassKind.DTO),
                        classNode("controller", module.id(), "PedidoController", UmlClassKind.CONTROLLER),
                        classNode("service", module.id(), "PedidoService", UmlClassKind.SERVICE)),
                List.of(), "");

        var references = new UmlClassLayoutPolicy().visualReferences(document, 0);

        assertTrue(orderOf(references, "uml-class:controller") < orderOf(references, "uml-class:service"));
        assertTrue(orderOf(references, "uml-class:service") < orderOf(references, "uml-class:repo"));
        assertTrue(orderOf(references, "uml-class:repo") < orderOf(references, "uml-class:dto"));
    }


    @Test
    void hierarchyClassesAreOrderedFromAbstractParentsToSpecificChildren() {
        UmlModuleGroup module = new UmlModuleGroup("dominio", "Dominio", "com.acme.dominio", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "Herencia", "borrador", LocalDate.of(2026, 5, 17),
                List.of(module),
                List.of(
                        classNode("concreta", module.id(), "FacturaEspecial", UmlClassKind.CLASS),
                        classNode("base", module.id(), "FacturaBase", UmlClassKind.ABSTRACT_CLASS),
                        classNode("intermedia", module.id(), "Factura", UmlClassKind.CLASS)),
                List.of(
                        new UmlClassRelation("r1", "intermedia", "base", UmlRelationKind.INHERITANCE, "", "", ""),
                        new UmlClassRelation("r2", "concreta", "intermedia", UmlRelationKind.INHERITANCE, "", "", "")),
                "");

        var references = new UmlClassLayoutPolicy().visualReferences(document, 0);

        assertTrue(orderOf(references, "uml-class:base") < orderOf(references, "uml-class:intermedia"));
        assertTrue(orderOf(references, "uml-class:intermedia") < orderOf(references, "uml-class:concreta"));
    }

    @Test
    void generatedUmlClassPositionsKeepBreathingMargin() {
        UmlModuleGroup module = new UmlModuleGroup("dominio", "Dominio", "com.acme.dominio", "", "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "Respira", "borrador", LocalDate.of(2026, 5, 17),
                List.of(module),
                List.of(
                        classNode("base", module.id(), "FacturaBase", UmlClassKind.ABSTRACT_CLASS),
                        classNode("concreta", module.id(), "FacturaEspecial", UmlClassKind.CLASS)),
                List.of(new UmlClassRelation("r1", "concreta", "base", UmlRelationKind.INHERITANCE, "", "", "")),
                "");

        var references = new UmlClassLayoutPolicy().visualReferences(document, 0);
        Map<com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId, com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint> positions =
                new DefaultVisualLayoutGenerator().positionsFor(references);
        var base = referenceOf(references, "uml-class:base");
        var child = referenceOf(references, "uml-class:concreta");
        double verticalMargin = positions.get(child.layoutId()).y() - positions.get(base.layoutId()).y() - base.preferredHeight();

        assertTrue(verticalMargin >= 30.0, "Las clases generadas deben conservar al menos 30 px de respiración vertical.");
    }

    @Test
    void defaultLayoutPlacesUmlModulesInTwoColumnBands() {
        DefaultVisualLayoutGenerator generator = new DefaultVisualLayoutGenerator();
        var first = generator.positionFor(new VisualNodeReference(
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("uml-module:backend"), 700, 300, 0));
        var second = generator.positionFor(new VisualNodeReference(
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("uml-module:frontend"), 700, 300, 1000));
        var third = generator.positionFor(new VisualNodeReference(
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("uml-module:shared"), 700, 300, 2000));

        assertTrue(second.x() > first.x(), "El segundo grupo debe avanzar horizontalmente.");
        assertTrue(third.y() > first.y(), "El tercer grupo debe bajar a una nueva banda.");
    }



    private static VisualNodeReference referenceOf(List<VisualNodeReference> references, String layoutId) {
        return references.stream()
                .filter(reference -> reference.layoutId().value().equals(layoutId))
                .findFirst()
                .orElseThrow();
    }

    private static int orderOf(List<VisualNodeReference> references, String layoutId) {
        return references.stream()
                .filter(reference -> reference.layoutId().value().equals(layoutId))
                .findFirst()
                .orElseThrow()
                .orderIndex();
    }


    private static List<UmlClassMember> manyMembers(int pairs) {
        ArrayList<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < pairs; index++) {
            members.add(new UmlClassMember("attr-" + index, UmlMemberKind.ATTRIBUTE, "campo" + index,
                    "String", "", UmlVisibility.PRIVATE, false, ""));
            members.add(new UmlClassMember("met-" + index, UmlMemberKind.METHOD, "operacion" + index,
                    "void", "operacion" + index + "()", UmlVisibility.PUBLIC, false, ""));
        }
        return members;
    }

    private static UmlClassNode classNode(String id, String moduleId, String name, UmlClassKind kind) {
        return new UmlClassNode(id, moduleId, name, "com.acme.pedidos", kind,
                UmlVisibility.PUBLIC, "", "", List.of(member("id")), "");
    }

    private static UmlClassNode node(String id, String moduleId) {
        return new UmlClassNode(id, moduleId, id, "uens.academico", UmlClassKind.CLASS,
                UmlVisibility.PUBLIC, "", "", List.of(member("id")), "");
    }

    private static UmlClassMember member(String name) {
        return new UmlClassMember("m-" + name, UmlMemberKind.ATTRIBUTE, name, "String", "",
                UmlVisibility.PRIVATE, false, "");
    }
}
