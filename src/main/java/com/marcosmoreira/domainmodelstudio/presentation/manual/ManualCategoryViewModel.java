package com.marcosmoreira.domainmodelstudio.presentation.manual;

import java.util.List;

/** Categoría visible del manual categorizado. */
public record ManualCategoryViewModel(String id, String title, List<ManualTopicViewModel> topics) {
}
