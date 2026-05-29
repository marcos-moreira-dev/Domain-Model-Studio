package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

/** Crea el documento base del diccionario de datos sin acoplarlo a UI ni PostgreSQL. */
public final class DefaultCreateDataDictionaryUseCase implements CreateDataDictionaryUseCase {

    private final Clock clock;

    public DefaultCreateDataDictionaryUseCase() {
        this(Clock.systemDefaultZone());
    }

    public DefaultCreateDataDictionaryUseCase(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public DataDictionaryDocument createBlank(String projectName) {
        return DataDictionaryDocument.blank(projectName, LocalDate.now(clock));
    }
}
