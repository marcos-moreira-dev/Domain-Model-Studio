package com.marcosmoreira.domainmodelstudio.application.workspace;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class DefaultCreateWorkspaceUseCaseTest {

    @Test
    void conceptualModelOpensProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.CONCEPTUAL_MODEL,
                "Modelo conceptual nuevo"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
    }

    @Test
    void dataDictionaryOpensProductViewAsDocument() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.DATA_DICTIONARY,
                "Diccionario de datos"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
    }

    @Test
    void moduleMapOpensProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.ADMIN_MODULE_MAP,
                "Mapa de módulos"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
        assertEquals("Mapa de módulos abre una vista lista para revisar, editar y exportar según su tipo.",
                result.userMessage());
    }

    @Test
    void rolesPermissionsOpensProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.ROLES_PERMISSIONS_MAP,
                "Roles y permisos"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
        assertEquals("Roles y permisos abre una vista lista para revisar, editar y exportar según su tipo.",
                result.userMessage());
    }

    @Test
    void screenFlowOpensProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.SCREEN_FLOW,
                "Flujo de pantallas"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
        assertEquals("Flujo de pantallas abre una vista lista para revisar, editar y exportar según su tipo.",
                result.userMessage());
    }


    @Test
    void freeGraphOpensProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.FREE_GRAPH,
                "Grafo libre"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
        assertEquals("Grafo libre abre una vista lista para revisar, editar y exportar según su tipo.",
                result.userMessage());
    }

    @Test
    void logicalBusinessGraphOpensInitialProductViewWhenCanvasIsImplemented() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                "Grafo lógico del negocio"));

        assertEquals(WorkspaceSupportDecision.PRODUCT_VIEW, result.decision());
        assertEquals("Grafo lógico del negocio abre una vista lista para revisar, editar y exportar según su tipo.",
                result.userMessage());
    }

    @Test
    void unknownDiagramTypeDoesNotOpenProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.of("postgresql-physical-erd"),
                "ERD PostgreSQL"));

        assertEquals(WorkspaceSupportDecision.UNSUPPORTED, result.decision());
    }

    @Test
    void removedLogicalRelationalModelDoesNotOpenProductView() {
        DefaultCreateWorkspaceUseCase useCase = new DefaultCreateWorkspaceUseCase(new DefaultDiagramTypeRegistry());

        CreateWorkspaceResult result = useCase.execute(new CreateWorkspaceRequest(
                DiagramTypeId.of("logical-relational-model"),
                "Modelo relacional lógico"));

        assertEquals(WorkspaceSupportDecision.UNSUPPORTED, result.decision());
    }
}
