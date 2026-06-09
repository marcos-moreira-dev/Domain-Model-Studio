/**
 * Contratos de ruteo y montaje de workspaces visibles del producto.
 *
 * <p>Este paquete separa la decisión "tipo de diagrama -> familia de workspace" de la vista
 * principal. También concentra descriptores de producto, política de paneles y registro de
 * raíces JavaFX para que el shell no crezca con condicionales por cada tipo.</p>
 *
 * <p>Flujo esperado: la pestaña activa actualiza el tipo activo; desde ese tipo se resuelve una
 * {@code WorkspaceRoute}; luego se consulta el descriptor y se monta el root registrado para esa
 * familia visual/documental.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation.workspace;
