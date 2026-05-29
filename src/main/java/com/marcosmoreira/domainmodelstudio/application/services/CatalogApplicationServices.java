package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeDefinitions;
import com.marcosmoreira.domainmodelstudio.application.catalog.ListDiagramCategoriesUseCase;
import com.marcosmoreira.domainmodelstudio.application.catalog.ListDiagramTypesUseCase;
import com.marcosmoreira.domainmodelstudio.application.workspace.CreateWorkspaceUseCase;
import com.marcosmoreira.domainmodelstudio.application.runtime.DefaultDiagramTypeRuntimeRegistry;
import com.marcosmoreira.domainmodelstudio.application.runtime.DefaultPayloadRuntimeRegistry;
import com.marcosmoreira.domainmodelstudio.application.runtime.DiagramTypeRuntimeRegistry;
import com.marcosmoreira.domainmodelstudio.application.runtime.PayloadRuntimeRegistry;
import java.util.Objects;

/** Fachada de consulta de catálogo, capacidades y descriptors de workspace. */
public final class CatalogApplicationServices {

    private final ListDiagramCategoriesUseCase listDiagramCategoriesUseCase;
    private final ListDiagramTypesUseCase listDiagramTypesUseCase;
    private final CreateWorkspaceUseCase createWorkspaceUseCase;
    private final DiagramTypeRuntimeRegistry diagramTypeRuntimeRegistry;
    private final PayloadRuntimeRegistry payloadRuntimeRegistry;

    public CatalogApplicationServices(
            ListDiagramCategoriesUseCase listDiagramCategoriesUseCase,
            ListDiagramTypesUseCase listDiagramTypesUseCase,
            CreateWorkspaceUseCase createWorkspaceUseCase
    ) {
        this(
                listDiagramCategoriesUseCase,
                listDiagramTypesUseCase,
                createWorkspaceUseCase,
                new DefaultPayloadRuntimeRegistry());
    }

    public CatalogApplicationServices(
            ListDiagramCategoriesUseCase listDiagramCategoriesUseCase,
            ListDiagramTypesUseCase listDiagramTypesUseCase,
            CreateWorkspaceUseCase createWorkspaceUseCase,
            PayloadRuntimeRegistry payloadRuntimeRegistry
    ) {
        this(
                listDiagramCategoriesUseCase,
                listDiagramTypesUseCase,
                createWorkspaceUseCase,
                new DefaultDiagramTypeRuntimeRegistry(DefaultDiagramTypeDefinitions.all(), payloadRuntimeRegistry),
                payloadRuntimeRegistry);
    }

    public CatalogApplicationServices(
            ListDiagramCategoriesUseCase listDiagramCategoriesUseCase,
            ListDiagramTypesUseCase listDiagramTypesUseCase,
            CreateWorkspaceUseCase createWorkspaceUseCase,
            DiagramTypeRuntimeRegistry diagramTypeRuntimeRegistry,
            PayloadRuntimeRegistry payloadRuntimeRegistry
    ) {
        this.listDiagramCategoriesUseCase = Objects.requireNonNull(
                listDiagramCategoriesUseCase, "listDiagramCategoriesUseCase");
        this.listDiagramTypesUseCase = Objects.requireNonNull(listDiagramTypesUseCase, "listDiagramTypesUseCase");
        this.createWorkspaceUseCase = Objects.requireNonNull(createWorkspaceUseCase, "createWorkspaceUseCase");
        this.diagramTypeRuntimeRegistry = Objects.requireNonNull(
                diagramTypeRuntimeRegistry, "diagramTypeRuntimeRegistry");
        this.payloadRuntimeRegistry = Objects.requireNonNull(payloadRuntimeRegistry, "payloadRuntimeRegistry");
    }

    public ListDiagramCategoriesUseCase listDiagramCategoriesUseCase() {
        return listDiagramCategoriesUseCase;
    }

    public ListDiagramTypesUseCase listDiagramTypesUseCase() {
        return listDiagramTypesUseCase;
    }

    public CreateWorkspaceUseCase createWorkspaceUseCase() {
        return createWorkspaceUseCase;
    }

    public DiagramTypeRuntimeRegistry diagramTypeRuntimeRegistry() {
        return diagramTypeRuntimeRegistry;
    }

    public PayloadRuntimeRegistry payloadRuntimeRegistry() {
        return payloadRuntimeRegistry;
    }
}
