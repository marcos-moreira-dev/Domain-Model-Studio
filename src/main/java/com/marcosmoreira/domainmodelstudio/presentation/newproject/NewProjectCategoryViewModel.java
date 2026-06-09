package com.marcosmoreira.domainmodelstudio.presentation.newproject;

/** Datos visibles de una categoría en el diálogo Nuevo proyecto. */
public record NewProjectCategoryViewModel(String id, String displayName, String purpose, int order) {
}
