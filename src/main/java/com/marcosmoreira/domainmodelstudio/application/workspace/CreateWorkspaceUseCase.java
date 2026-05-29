package com.marcosmoreira.domainmodelstudio.application.workspace;

/** Contrato para abrir proyectos sin que la interfaz decida reglas de disponibilidad. */
public interface CreateWorkspaceUseCase {

    CreateWorkspaceResult execute(CreateWorkspaceRequest request);
}
