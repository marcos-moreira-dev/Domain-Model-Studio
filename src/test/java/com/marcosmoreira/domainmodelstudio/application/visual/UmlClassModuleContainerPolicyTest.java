package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassModuleContainerPolicyTest {

    private final UmlClassModuleContainerPolicy policy = new UmlClassModuleContainerPolicy();

    @Test
    void describesBackendContainerWithSourceRootAndClassCount() {
        UmlModuleGroup module = new UmlModuleGroup(
                "backend-ventas",
                "ventas",
                "src/main/java/com/acme/ventas",
                "Agrupador generado desde código fuente. Package/carpeta: com.acme.ventas",
                "Source root: backend-java");
        UmlClassNode controller = node("venta-controller", module.id(), "VentaController");
        UmlClassNode service = node("venta-service", module.id(), "VentaService");

        UmlClassModuleContainerDescriptor descriptor = policy.describe(module, List.of(controller, service));

        assertEquals("ventas", descriptor.title());
        assertEquals("Root: backend-java", descriptor.sourceRootLabel());
        assertEquals("2 clases", descriptor.classCountLabel());
        assertEquals("uml-class-canvas-module-backend", descriptor.roleStyleClass());
        assertTrue(descriptor.subtitle().contains("src/main/java"));
    }

    @Test
    void detectsFrontendContainersWithoutMixingSourceRoots() {
        UmlModuleGroup module = new UmlModuleGroup(
                "frontend-productos",
                "productos",
                "src/app/features/productos",
                "Agrupador generado desde código fuente. Ruta: src/app/features/productos",
                "Source root: frontend-typescript");

        UmlClassModuleContainerDescriptor descriptor = policy.describe(module, List.of(node("component", module.id(), "ProductoPage")));

        assertEquals("uml-class-canvas-module-frontend", descriptor.roleStyleClass());
        assertEquals("1 clase", descriptor.classCountLabel());
        assertTrue(descriptor.subtitle().contains("frontend-typescript"));
    }

    @Test
    void verifiesContainmentByModuleId() {
        UmlModuleGroup module = new UmlModuleGroup("backend-clientes", "clientes", "", "", "Source root: backend-java");
        UmlClassNode inside = node("cliente-service", module.id(), "ClienteService");
        UmlClassNode outside = node("venta-service", "backend-ventas", "VentaService");

        assertTrue(policy.contains(module, inside));
        assertTrue(!policy.contains(module, outside));
    }

    private static UmlClassNode node(String id, String moduleId, String name) {
        return new UmlClassNode(id, moduleId, name, "com.acme", UmlClassKind.CLASS,
                UmlVisibility.PUBLIC, "", "", List.of(), "");
    }
}
