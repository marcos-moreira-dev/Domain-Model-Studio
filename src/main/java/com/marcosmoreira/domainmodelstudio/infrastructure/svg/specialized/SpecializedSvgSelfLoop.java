package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

/** Geometría Bézier de una autorrelación. */
record SpecializedSvgSelfLoop(
        SpecializedSvgPoint start,
        SpecializedSvgPoint control1,
        SpecializedSvgPoint control2,
        SpecializedSvgPoint end
) {
}
