package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Puerto simple para que toolbar/shell ejecuten comandos sin conocer la vista concreta.
 */
@FunctionalInterface
public interface InteractiveCanvasCommandSink {

    void execute(InteractiveCanvasCommand command);
}
