/**
 * Infraestructura transversal para montar diagramas visuales con una experiencia coherente.
 *
 * <p>Este paquete define workbench, encabezado y slots laterales. No debe importar paquetes
 * concretos como modulemap, umlclass, behavior, architecture, wireframe o screenflow. Cada
 * familia se conecta mediante contributors/adaptadores ubicados en su propio paquete.</p>
 *
 * <p>{@code ProjectChangeSupport} centraliza el patrón compartido de listeners,
 * carga y notificación de cambios usado por ViewModels visuales/documentales. No es una superclase de ViewModels ni decide selección, layout o comandos; solo evita banderas
 * {@code loading} duplicadas.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation.workbench;
