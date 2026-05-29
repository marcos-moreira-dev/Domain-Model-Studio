/**
 * Capa de shell JavaFX: ventana principal, menú superior, pestañas, toolbar general,
 * coordinación de guardado, validación y activación de workspaces.
 *
 * <p>El shell orquesta la experiencia global, pero no debe contener semántica de negocio
 * ni reglas de diagramas. Cuando necesita operar un tipo especializado delega en
 * coordinadores o ViewModels específicos, de forma que agregar un nuevo workspace no obligue
 * a contaminar el modelo conceptual ni a crecer con cadenas largas de condicionales.</p>
 *
 * <p>Desde la Tanda 29, creación y apertura de proyectos viven en
 * {@code ProjectCreationCoordinator} y {@code ProjectOpenCoordinator}. El handler principal
 * conserva la fachada pública de comandos y la activación de pestañas, pero no concentra
 * todo el ciclo de vida.</p>
 *
 * <p>Para estudiar esta capa, leer primero {@code MainShellView} para la composición visual,
 * luego {@code MainShellCommandHandler} para el flujo de comandos y finalmente los
 * coordinadores especializados para guardado, validación, apertura y activación.</p>
 */
package com.marcosmoreira.domainmodelstudio.presentation.shell;
