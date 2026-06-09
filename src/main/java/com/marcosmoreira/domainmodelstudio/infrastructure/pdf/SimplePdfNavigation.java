package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Estado de navegacion interna del PDF: destinos y anotaciones GoTo. */
final class SimplePdfNavigation {

    private static final Locale PDF_LOCALE = Locale.ROOT;

    private final Map<String, PdfDestination> destinations = new LinkedHashMap<>();
    private final List<PdfLinkAnnotation> annotations = new ArrayList<>();

    void anchor(String destinationId, int pageIndex, double top) {
        String normalizedId = requireDestinationId(destinationId);
        if (destinations.containsKey(normalizedId)) {
            throw new IllegalArgumentException("Destino PDF duplicado: " + normalizedId);
        }
        destinations.put(normalizedId, new PdfDestination(pageIndex, top));
    }

    void link(int pageIndex, double left, double bottom, double right, double top, String destinationId) {
        annotations.add(new PdfLinkAnnotation(
                pageIndex,
                left,
                bottom,
                right,
                top,
                requireDestinationId(destinationId)));
    }

    int annotationCount() {
        return annotations.size();
    }

    void validate(int pageCount) {
        for (PdfLinkAnnotation annotation : annotations) {
            if (!destinations.containsKey(annotation.destinationId())) {
                throw new IllegalStateException("Link PDF apunta a un destino inexistente: "
                        + annotation.destinationId());
            }
            if (annotation.pageIndex() < 0 || annotation.pageIndex() >= pageCount) {
                throw new IllegalStateException("Link PDF registrado en pagina inexistente: "
                        + annotation.pageIndex());
            }
        }
        for (Map.Entry<String, PdfDestination> entry : destinations.entrySet()) {
            PdfDestination destination = entry.getValue();
            if (destination.pageIndex() < 0 || destination.pageIndex() >= pageCount) {
                throw new IllegalStateException("Destino PDF apunta a una pagina inexistente: "
                        + entry.getKey());
            }
        }
    }

    String pageAnnotations(int pageIndex, int firstAnnotationObject) {
        StringBuilder refs = new StringBuilder();
        for (int index = 0; index < annotations.size(); index++) {
            PdfLinkAnnotation annotation = annotations.get(index);
            if (annotation.pageIndex() == pageIndex) {
                refs.append(firstAnnotationObject + index).append(" 0 R ");
            }
        }
        return refs.isEmpty() ? "" : " /Annots [ " + refs + "]";
    }

    String annotationObject(int index, int firstPageObject, double destinationLeft) {
        PdfLinkAnnotation annotation = annotations.get(index);
        PdfDestination destination = destinations.get(annotation.destinationId());
        int destinationPageObject = firstPageObject + destination.pageIndex();
        return "<< /Type /Annot /Subtype /Link /Rect ["
                + format(annotation.left()) + ' '
                + format(annotation.bottom()) + ' '
                + format(annotation.right()) + ' '
                + format(annotation.top())
                + "] /Border [0 0 0] /H /I /A << /S /GoTo /D ["
                + destinationPageObject + " 0 R /XYZ "
                + format(destinationLeft) + ' '
                + format(destination.top()) + " null] >> >>\n";
    }

    private static String requireDestinationId(String destinationId) {
        String normalizedId = Objects.requireNonNull(destinationId, "destinationId").strip();
        if (normalizedId.isBlank()) {
            throw new IllegalArgumentException("El destino PDF no puede estar vacio.");
        }
        return normalizedId;
    }

    private static String format(double value) {
        return String.format(PDF_LOCALE, "%.2f", value);
    }

    private record PdfDestination(int pageIndex, double top) {
    }

    private record PdfLinkAnnotation(
            int pageIndex,
            double left,
            double bottom,
            double right,
            double top,
            String destinationId
    ) {
    }
}
