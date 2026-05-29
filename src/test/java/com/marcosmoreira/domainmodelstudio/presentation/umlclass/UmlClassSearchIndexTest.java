package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlClassSearchIndexTest {

    @Test
    void precomputesModulesClassesMembersAndRelationsForSearch() {
        UmlClassDiagramDocument document = document();
        UmlClassSearchIndex index = UmlClassSearchIndex.from(document);

        assertEquals(1, index.moduleEntryCount());
        assertEquals(2, index.classEntryCount());
        assertEquals(1, index.relationEntryCount());
        assertTrue(index.matchesModule("ventas", document.modules().getFirst()));
        assertTrue(index.matchesClass("calcularTotal", document.classes().getFirst()));
        assertTrue(index.matchesClass("src/main/java", document.classes().getFirst()));
        assertTrue(index.matchesRelation("pedido service", document.relations().getFirst()));
        assertFalse(index.matchesClass("inventario", document.classes().getFirst()));
    }

    @Test
    void filterEngineCanReusePrecomputedIndexWithoutChangingResults() {
        UmlClassDiagramDocument document = document();
        UmlClassSearchIndex index = UmlClassSearchIndex.from(document);
        UmlClassDiagramFilterEngine engine = new UmlClassDiagramFilterEngine();

        UmlClassDiagramFilterResult result = engine.apply(document,
                new UmlClassDiagramFilterState("", "calcularTotal", null, null), index);

        assertEquals(List.of("pedido-service"), result.classes().stream().map(UmlClassNode::id).toList());
        assertEquals(List.of("ventas"), result.modules().stream().map(UmlModuleGroup::id).toList());
    }

    private UmlClassDiagramDocument document() {
        UmlModuleGroup module = new UmlModuleGroup("ventas", "Ventas", "src/main/java/com/acme/ventas",
                "Módulo de ventas", "Source root path: C:/repo/src/main/java");
        UmlClassNode service = new UmlClassNode("pedido-service", "ventas", "PedidoService", "com.acme.ventas",
                UmlClassKind.SERVICE, UmlVisibility.PUBLIC, "Orquesta pedidos",
                "Origen: java; ruta absoluta: C:/repo/src/main/java/com/acme/ventas/PedidoService.java",
                List.of(new UmlClassMember("calcular-total", UmlMemberKind.METHOD, "calcularTotal", "BigDecimal",
                        "calcularTotal(Pedido pedido)", UmlVisibility.PUBLIC, false, "Método de dominio")),
                "roles: service");
        UmlClassNode repository = new UmlClassNode("pedido-repository", "ventas", "PedidoRepository", "com.acme.ventas",
                UmlClassKind.REPOSITORY, UmlVisibility.PUBLIC, "", "", List.of(), "");
        UmlClassRelation relation = new UmlClassRelation("service-repository", "pedido-service", "pedido-repository",
                UmlRelationKind.ASSOCIATION, "usa", "Pedido service usa repositorio", "");
        return new UmlClassDiagramDocument("Demo", "borrador", LocalDate.now(), List.of(module),
                List.of(service, repository), List.of(relation), List.of(), "");
    }
}
