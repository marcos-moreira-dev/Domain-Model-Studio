package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.CreateDataDictionaryUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.RemoveDataDictionaryItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.UpdateDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.UpdateDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ValidateDataDictionaryUseCase;
import java.util.Objects;

/** Fachada de edición y validación del Diccionario de datos.
 *
 * <p>La exportación PDF/Markdown sigue viviendo en {@code ExportApplicationServices}
 * porque es una salida transversal.</p> */
public final class DataDictionaryApplicationServices {

    private final CreateDataDictionaryUseCase createDataDictionaryUseCase;
    private final AddDataDictionaryEntityUseCase addDataDictionaryEntityUseCase;
    private final AddDataDictionaryFieldUseCase addDataDictionaryFieldUseCase;
    private final UpdateDataDictionaryEntityUseCase updateDataDictionaryEntityUseCase;
    private final UpdateDataDictionaryFieldUseCase updateDataDictionaryFieldUseCase;
    private final RemoveDataDictionaryItemUseCase removeDataDictionaryItemUseCase;
    private final ValidateDataDictionaryUseCase validateDataDictionaryUseCase;

    public DataDictionaryApplicationServices(
            CreateDataDictionaryUseCase createDataDictionaryUseCase,
            AddDataDictionaryEntityUseCase addDataDictionaryEntityUseCase,
            AddDataDictionaryFieldUseCase addDataDictionaryFieldUseCase,
            UpdateDataDictionaryEntityUseCase updateDataDictionaryEntityUseCase,
            UpdateDataDictionaryFieldUseCase updateDataDictionaryFieldUseCase,
            RemoveDataDictionaryItemUseCase removeDataDictionaryItemUseCase,
            ValidateDataDictionaryUseCase validateDataDictionaryUseCase
    ) {
        this.createDataDictionaryUseCase = Objects.requireNonNull(createDataDictionaryUseCase, "createDataDictionaryUseCase");
        this.addDataDictionaryEntityUseCase = Objects.requireNonNull(addDataDictionaryEntityUseCase, "addDataDictionaryEntityUseCase");
        this.addDataDictionaryFieldUseCase = Objects.requireNonNull(addDataDictionaryFieldUseCase, "addDataDictionaryFieldUseCase");
        this.updateDataDictionaryEntityUseCase = Objects.requireNonNull(updateDataDictionaryEntityUseCase, "updateDataDictionaryEntityUseCase");
        this.updateDataDictionaryFieldUseCase = Objects.requireNonNull(updateDataDictionaryFieldUseCase, "updateDataDictionaryFieldUseCase");
        this.removeDataDictionaryItemUseCase = Objects.requireNonNull(removeDataDictionaryItemUseCase, "removeDataDictionaryItemUseCase");
        this.validateDataDictionaryUseCase = Objects.requireNonNull(validateDataDictionaryUseCase, "validateDataDictionaryUseCase");
    }

    public CreateDataDictionaryUseCase createDataDictionaryUseCase() {
        return createDataDictionaryUseCase;
    }

    public AddDataDictionaryEntityUseCase addDataDictionaryEntityUseCase() {
        return addDataDictionaryEntityUseCase;
    }

    public AddDataDictionaryFieldUseCase addDataDictionaryFieldUseCase() {
        return addDataDictionaryFieldUseCase;
    }

    public UpdateDataDictionaryEntityUseCase updateDataDictionaryEntityUseCase() {
        return updateDataDictionaryEntityUseCase;
    }

    public UpdateDataDictionaryFieldUseCase updateDataDictionaryFieldUseCase() {
        return updateDataDictionaryFieldUseCase;
    }

    public RemoveDataDictionaryItemUseCase removeDataDictionaryItemUseCase() {
        return removeDataDictionaryItemUseCase;
    }

    public ValidateDataDictionaryUseCase validateDataDictionaryUseCase() {
        return validateDataDictionaryUseCase;
    }

}
