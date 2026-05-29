package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

/** Marca la pestaña activa como modificada después de un comando de edición. */
@FunctionalInterface
public interface DirtyStateUpdater {

    void markCurrentSessionDirty();
}
