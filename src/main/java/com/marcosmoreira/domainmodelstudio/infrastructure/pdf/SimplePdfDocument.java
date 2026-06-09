package com.marcosmoreira.domainmodelstudio.infrastructure.pdf;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.imageio.ImageIO;
/** Escritor PDF mínimo para reportes internos sin dependencias externas. */
final class SimplePdfDocument {
    private static final Locale PDF_LOCALE = Locale.ROOT;
    private static final double PAGE_WIDTH = 595.0;
    private static final double PAGE_HEIGHT = 842.0;
    private static final double MARGIN_LEFT = 50.0;
    private static final double MARGIN_RIGHT = 50.0;
    private static final double MARGIN_TOP = 56.0;
    private static final double MARGIN_BOTTOM = 54.0;
    private static final double NORMAL_SIZE = 10.0;
    private static final double CONTENT_WIDTH = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT;
    private static final double SMALL_SIZE = 8.5;
    private static final double TITLE_SIZE = 18.0;
    private static final double HEADING_SIZE = 13.0;
    private final String documentTitle;
    private final List<StringBuilder> pages = new ArrayList<>();
    private final List<PdfImageResource> images = new ArrayList<>();
    private final SimplePdfNavigation navigation = new SimplePdfNavigation();
    private StringBuilder currentPage;
    private double cursorY;
    SimplePdfDocument(String documentTitle) {
        this.documentTitle = Objects.requireNonNull(documentTitle, "documentTitle").strip();
        startPage();
    }
    void title(String text) {
        addLine(text, TITLE_SIZE, 22.0);
        separator();
        gap(3.0);
    }
    void heading(String text) {
        heading(text, "");
    }
    void heading(String text, String destinationId) {
        ensureSpace(42.0);
        if (destinationId != null && !destinationId.isBlank()) {
            anchor(destinationId);
        }
        currentPage.append("q 0.12 0.31 0.52 rg ")
                .append(format(MARGIN_LEFT)).append(' ').append(format(cursorY - 19.0)).append(" 4.00 17.00 re f Q 0 g\n");
        currentPage.append(textCommand(text, HEADING_SIZE, MARGIN_LEFT + 9.0, cursorY - 11.0));
        currentPage.append("q 0.74 0.82 0.91 RG 0.60 w ")
                .append(format(MARGIN_LEFT)).append(' ').append(format(cursorY - 24.0)).append(" m ")
                .append(format(PAGE_WIDTH - MARGIN_RIGHT)).append(' ').append(format(cursorY - 24.0))
                .append(" l S Q\n");
        cursorY -= 34.0;
    }
    void anchor(String destinationId) {
        double top = Math.min(PAGE_HEIGHT - 1.0, cursorY + 12.0);
        navigation.anchor(destinationId, pages.size() - 1, top);
    }
    void paragraph(String text) {
        for (String line : PdfTextWrapper.wrap(text, NORMAL_SIZE)) {
            addLine(line, NORMAL_SIZE, 13.0);
        }
        gap(5.0);
    }
    void keyValue(String key, String value) {
        paragraph(key + ": " + value);
    }
    void table(List<String> headers, List<List<String>> rows, double... columnFractions) {
        Objects.requireNonNull(headers, "headers");
        Objects.requireNonNull(rows, "rows");
        if (headers.isEmpty()) {
            return;
        }
        double[] widths = columnWidths(headers.size(), columnFractions);
        drawTableRow(headers, widths, true, false);
        for (int index = 0; index < rows.size(); index++) {
            drawTableRow(rows.get(index), widths, false, index % 2 == 1);
        }
        gap(8.0);
    }
    void linkedTable(List<String> headers, List<PdfTableRow> rows, double... columnFractions) {
        Objects.requireNonNull(headers, "headers");
        Objects.requireNonNull(rows, "rows");
        if (headers.isEmpty()) {
            return;
        }
        double[] widths = columnWidths(headers.size(), columnFractions);
        drawTableRow(headers, widths, true, false);
        for (int index = 0; index < rows.size(); index++) {
            PdfTableRow row = rows.get(index);
            PdfRowBounds bounds = drawTableRow(row.cells(), widths, false, index % 2 == 1);
            if (row.linked()) {
                navigation.link(
                        bounds.pageIndex(),
                        bounds.x(),
                        bounds.y(),
                        bounds.x() + bounds.width(),
                        bounds.y() + bounds.height(),
                        row.destinationId());
            }
        }
        gap(8.0);
    }
    void linkedIndex(List<PdfIndexEntry> entries) {
        Objects.requireNonNull(entries, "entries");
        for (PdfIndexEntry entry : entries) {
            drawIndexEntry(entry);
        }
        gap(8.0);
    }
    void callout(String title, String text) {
        List<String> lines = PdfTextWrapper.wrap((title == null || title.isBlank() ? "Nota" : title.strip())
                + ": " + (text == null || text.isBlank() ? "-" : text.strip()), NORMAL_SIZE, CONTENT_WIDTH - 18.0);
        double height = Math.max(30.0, lines.size() * 12.0 + 12.0);
        ensureSpace(height + 8.0);
        currentPage.append("q 0.94 0.97 1.00 rg ")
                .append(format(MARGIN_LEFT)).append(' ').append(format(cursorY - height + 4.0)).append(' ')
                .append(format(CONTENT_WIDTH)).append(' ').append(format(height)).append(" re f Q 0 G 0 g\n");
        currentPage.append("q 0.36 0.52 0.70 RG 0.65 w ")
                .append(format(MARGIN_LEFT)).append(' ').append(format(cursorY - height + 4.0)).append(' ')
                .append(format(CONTENT_WIDTH)).append(' ').append(format(height)).append(" re S Q\n");
        double textY = cursorY - 12.0;
        for (String line : lines) {
            currentPage.append(textCommand(line, NORMAL_SIZE, MARGIN_LEFT + 9.0, textY));
            textY -= 12.0;
        }
        cursorY -= height + 8.0;
    }
    void bullet(String text) {
        for (String line : PdfTextWrapper.wrap(text, NORMAL_SIZE, 6)) {
            addLine("- " + line, NORMAL_SIZE, 12.0);
        }
    }
    void small(String text) {
        for (String line : PdfTextWrapper.wrap(text, SMALL_SIZE)) {
            addLine(line, SMALL_SIZE, 11.0);
        }
    }
    void darkRedSmall(String text) {
        for (String line : PdfTextWrapper.wrap(text, SMALL_SIZE)) {
            ensureSpace(11.0);
            currentPage.append(textCommand(line, SMALL_SIZE, MARGIN_LEFT, cursorY, "0.45 0.05 0.05 rg"));
            cursorY -= 11.0;
        }
    }
    void gap(double amount) {
        ensureSpace(amount);
        cursorY -= amount;
    }
    boolean image(Path imagePath, double maxWidth, double maxHeight) {
        Objects.requireNonNull(imagePath, "imagePath");
        PdfImageResource resource = loadImage(imagePath);
        if (resource == null) {
            return false;
        }
        double[] size = scaledImageSize(resource.width(), resource.height(), maxWidth, maxHeight);
        double width = size[0];
        double height = size[1];
        ensureSpace(height + 12.0);
        String name = "Im" + (images.size() + 1);
        PdfImageResource pageResource = resource.withPagePlacement(pages.size() - 1, name);
        images.add(pageResource);
        double x = MARGIN_LEFT + Math.max(0.0, (CONTENT_WIDTH - width) / 2.0);
        double y = cursorY - height;
        currentPage.append("q ")
                .append(format(width)).append(" 0 0 ").append(format(height)).append(' ')
                .append(format(x)).append(' ').append(format(y)).append(" cm /")
                .append(name).append(" Do Q\n");
        cursorY -= height + 12.0;
        return true;
    }
    private PdfRowBounds drawTableRow(List<String> row, double[] widths, boolean header, boolean alternate) {
        List<List<String>> cellLines = new ArrayList<>();
        double rowHeight = header ? 24.0 : 22.0;
        for (int index = 0; index < widths.length; index++) {
            String value = index < row.size() ? row.get(index) : "";
            List<String> lines = PdfTextWrapper.wrap(value, SMALL_SIZE, widths[index] - 10.0);
            cellLines.add(lines);
            rowHeight = Math.max(rowHeight, lines.size() * 10.0 + 12.0);
        }
        ensureSpace(rowHeight + 2.0);
        int pageIndex = pages.size() - 1;
        double x = MARGIN_LEFT;
        double y = cursorY - rowHeight + 4.0;
        if (header || alternate) {
            currentPage.append(header ? "q 0.86 0.92 0.98 rg " : "q 0.97 0.99 1.00 rg ")
                    .append(format(MARGIN_LEFT)).append(' ').append(format(y)).append(' ')
                    .append(format(CONTENT_WIDTH)).append(' ').append(format(rowHeight)).append(" re f Q 0 G 0 g\n");
        }
        for (int index = 0; index < widths.length; index++) {
            drawCellBorder(x, y, widths[index], rowHeight);
            double textY = cursorY - 8.0;
            for (String line : cellLines.get(index)) {
                currentPage.append(textCommand(line, SMALL_SIZE, x + 4.0, textY));
                textY -= 10.0;
            }
            x += widths[index];
        }
        cursorY -= rowHeight;
        return new PdfRowBounds(pageIndex, MARGIN_LEFT, y, CONTENT_WIDTH, rowHeight);
    }
    private void drawIndexEntry(PdfIndexEntry entry) {
        if (entry == null || entry.label().isBlank()) {
            return;
        }
        double fontSize = entry.level() == 0 ? NORMAL_SIZE : SMALL_SIZE;
        double indent = entry.level() * 18.0;
        double x = MARGIN_LEFT + indent;
        double width = CONTENT_WIDTH - indent;
        List<String> lines = PdfTextWrapper.wrap(indexLabel(entry), fontSize, width);
        ensureSpace(lines.size() * 12.0 + 2.0);
        int pageIndex = pages.size() - 1;
        double top = cursorY + 4.0;
        for (String line : lines) {
            currentPage.append(textCommand(line, fontSize, x, cursorY));
            cursorY -= 12.0;
        }
        if (entry.linked()) {
            navigation.link(pageIndex, x, cursorY + 2.0, PAGE_WIDTH - MARGIN_RIGHT, top, entry.destinationId());
        }
    }
    private static String indexLabel(PdfIndexEntry entry) {
        return entry.level() == 0 ? entry.label() : "  ".repeat(entry.level()) + entry.label();
    }
    private void drawCellBorder(double x, double y, double width, double height) {
        currentPage.append("0.55 0.65 0.75 RG 0.38 w ")
                .append(format(x)).append(' ').append(format(y)).append(' ')
                .append(format(width)).append(' ').append(format(height)).append(" re S 0 G\n");
    }
    private static double[] columnWidths(int columnCount, double[] fractions) {
        double[] widths = new double[columnCount];
        double total = 0.0;
        for (int index = 0; index < columnCount; index++) {
            double fraction = index < fractions.length && fractions[index] > 0.0 ? fractions[index] : 1.0;
            widths[index] = fraction;
            total += fraction;
        }
        for (int index = 0; index < widths.length; index++) {
            widths[index] = CONTENT_WIDTH * widths[index] / total;
        }
        return widths;
    }
    void writeTo(Path destinationFile) throws IOException {
        Objects.requireNonNull(destinationFile, "destinationFile");
        Path parent = destinationFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(destinationFile, buildPdfBytes());
    }
    private byte[] buildPdfBytes() throws IOException {
        navigation.validate(pages.size());
        List<byte[]> pageStreams = pages.stream()
                .map(StringBuilder::toString)
                .map(PdfTextEscaper::bytes)
                .toList();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Long> offsets = new ArrayList<>();
        write(out, "%PDF-1.4\n%\u00E2\u00E3\u00CF\u00D3\n");
        writeObjects(out, offsets, pageStreams);
        writeXrefAndTrailer(out, offsets);
        return out.toByteArray();
    }
    private void writeObjects(ByteArrayOutputStream out, List<Long> offsets, List<byte[]> pageStreams)
            throws IOException {
        int pageCount = pageStreams.size();
        int firstPageObject = 4;
        int firstContentObject = firstPageObject + pageCount;
        int firstImageObject = firstContentObject + pageCount;
        int firstAnnotationObject = firstImageObject + images.size();
        writeObject(out, offsets, 1, "<< /Type /Catalog /Pages 2 0 R >>\n");
        writeObject(out, offsets, 2, pagesObject(pageCount, firstPageObject));
        writeObject(out, offsets, 3, "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\n");
        for (int i = 0; i < pageCount; i++) {
            int pageObject = firstPageObject + i;
            int contentObject = firstContentObject + i;
            writeObject(out, offsets, pageObject, pageObject(contentObject, i, firstImageObject, firstAnnotationObject));
        }
        for (int i = 0; i < pageCount; i++) {
            int contentObject = firstContentObject + i;
            writeStreamObject(out, offsets, contentObject, withFooter(pageStreams.get(i), i + 1, pageCount));
        }
        for (int i = 0; i < images.size(); i++) {
            writeImageObject(out, offsets, firstImageObject + i, images.get(i));
        }
        for (int i = 0; i < navigation.annotationCount(); i++) {
            writeObject(out, offsets, firstAnnotationObject + i,
                    navigation.annotationObject(i, firstPageObject, MARGIN_LEFT));
        }
    }
    private byte[] withFooter(byte[] content, int pageNumber, int pageCount) {
        String footer = textCommand("Página " + pageNumber + " de " + pageCount, SMALL_SIZE, MARGIN_LEFT, 30.0);
        ByteArrayOutputStream merged = new ByteArrayOutputStream();
        try {
            merged.write(content);
            merged.write(PdfTextEscaper.bytes(footer));
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo preparar pie de página PDF.", exception);
        }
        return merged.toByteArray();
    }
    private static String pagesObject(int pageCount, int firstPageObject) {
        StringBuilder kids = new StringBuilder();
        for (int i = 0; i < pageCount; i++) {
            kids.append(firstPageObject + i).append(" 0 R ");
        }
        return "<< /Type /Pages /Count " + pageCount + " /Kids [ " + kids + "] >>\n";
    }
    private String pageObject(int contentObject, int pageIndex, int firstImageObject, int firstAnnotationObject) {
        return "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 " + format(PAGE_WIDTH) + " "
                + format(PAGE_HEIGHT) + "] /Resources << /Font << /F1 3 0 R >>"
                + pageImageResources(pageIndex, firstImageObject) + " >> /Contents "
                + contentObject + " 0 R" + navigation.pageAnnotations(pageIndex, firstAnnotationObject) + " >>\n";
    }
    private String pageImageResources(int pageIndex, int firstImageObject) {
        StringBuilder refs = new StringBuilder();
        for (int index = 0; index < images.size(); index++) {
            PdfImageResource image = images.get(index);
            if (image.pageIndex() == pageIndex) {
                refs.append('/').append(image.resourceName()).append(' ')
                        .append(firstImageObject + index).append(" 0 R ");
            }
        }
        return refs.isEmpty() ? "" : " /XObject << " + refs + ">>";
    }
    private static void writeImageObject(ByteArrayOutputStream out, List<Long> offsets, int number,
            PdfImageResource image) throws IOException {
        offsets.add((long) out.size());
        write(out, number + " 0 obj\n<< /Type /XObject /Subtype /Image /Width " + image.width()
                + " /Height " + image.height()
                + " /ColorSpace /DeviceRGB /BitsPerComponent 8 /Length " + image.rgbData().length
                + " >>\nstream\n");
        out.write(image.rgbData());
        write(out, "\nendstream\nendobj\n");
    }
    private static void writeObject(ByteArrayOutputStream out, List<Long> offsets, int number, String body)
            throws IOException {
        offsets.add((long) out.size());
        write(out, number + " 0 obj\n" + body + "endobj\n");
    }
    private static void writeStreamObject(ByteArrayOutputStream out, List<Long> offsets, int number, byte[] stream)
            throws IOException {
        offsets.add((long) out.size());
        write(out, number + " 0 obj\n<< /Length " + stream.length + " >>\nstream\n");
        out.write(stream);
        write(out, "\nendstream\nendobj\n");
    }
    private static void writeXrefAndTrailer(ByteArrayOutputStream out, List<Long> offsets) throws IOException {
        long xrefStart = out.size();
        write(out, "xref\n0 " + (offsets.size() + 1) + "\n0000000000 65535 f \n");
        for (Long offset : offsets) {
            write(out, String.format(PDF_LOCALE, "%010d 00000 n \n", offset));
        }
        write(out, "trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\nstartxref\n"
                + xrefStart + "\n%%EOF\n");
    }
    private void addLine(String text, double fontSize, double leading) {
        ensureSpace(leading);
        currentPage.append(textCommand(text, fontSize, MARGIN_LEFT, cursorY));
        cursorY -= leading;
    }
    private void separator() {
        ensureSpace(10.0);
        currentPage.append("0.12 0.31 0.52 RG 0.90 w ")
                .append(format(MARGIN_LEFT)).append(' ').append(format(cursorY)).append(" m ")
                .append(format(PAGE_WIDTH - MARGIN_RIGHT)).append(' ').append(format(cursorY))
                .append(" l S 0 G\n");
        cursorY -= 14.0;
    }
    private void ensureSpace(double needed) {
        if (cursorY - needed < MARGIN_BOTTOM) {
            startPage();
        }
    }
    private void startPage() {
        currentPage = new StringBuilder();
        pages.add(currentPage);
        cursorY = PAGE_HEIGHT - MARGIN_TOP;
        currentPage.append(textCommand(documentTitle, SMALL_SIZE, MARGIN_LEFT, PAGE_HEIGHT - 30.0));
    }
    private PdfImageResource loadImage(Path imagePath) {
        try {
            if (!Files.isRegularFile(imagePath)) {
                return null;
            }
            BufferedImage image = ImageIO.read(imagePath.toFile());
            if (image == null) {
                return null;
            }
            return new PdfImageResource(-1, "", image.getWidth(), image.getHeight(), rgbData(image));
        } catch (IOException exception) {
            return null;
        }
    }
    private static byte[] rgbData(BufferedImage image) {
        ByteArrayOutputStream data = new ByteArrayOutputStream(image.getWidth() * image.getHeight() * 3);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int argb = image.getRGB(x, y);
                int alpha = (argb >>> 24) & 0xFF;
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;
                if (alpha < 255) {
                    red = compositeOnWhite(red, alpha);
                    green = compositeOnWhite(green, alpha);
                    blue = compositeOnWhite(blue, alpha);
                }
                data.write(red);
                data.write(green);
                data.write(blue);
            }
        }
        return data.toByteArray();
    }
    private static int compositeOnWhite(int channel, int alpha) {
        return Math.max(0, Math.min(255, Math.round((channel * alpha + 255 * (255 - alpha)) / 255.0f)));
    }
    private static double[] scaledImageSize(int width, int height, double maxWidth, double maxHeight) {
        double safeMaxWidth = maxWidth > 0.0 ? Math.min(maxWidth, CONTENT_WIDTH) : CONTENT_WIDTH;
        double safeMaxHeight = maxHeight > 0.0 ? maxHeight : 120.0;
        double scale = Math.min(safeMaxWidth / Math.max(1, width), safeMaxHeight / Math.max(1, height));
        scale = Math.min(1.0, scale);
        return new double[] {Math.max(1.0, width * scale), Math.max(1.0, height * scale)};
    }
    private static String textCommand(String text, double fontSize, double x, double y) {
        return textCommand(text, fontSize, x, y, "0 g");
    }
    private static String textCommand(String text, double fontSize, double x, double y, String colorCommand) {
        return colorCommand + " BT /F1 " + format(fontSize) + " Tf " + format(x) + ' ' + format(y)
                + " Td (" + PdfTextEscaper.escape(text) + ") Tj ET\n";
    }
    private static void write(ByteArrayOutputStream out, String value) throws IOException {
        out.write(PdfTextEscaper.bytes(value));
    }
    private static String format(double value) {
        return String.format(PDF_LOCALE, "%.2f", value);
    }
    private record PdfRowBounds(int pageIndex, double x, double y, double width, double height) {
    }
    private record PdfImageResource(int pageIndex, String resourceName, int width, int height, byte[] rgbData) {
        PdfImageResource withPagePlacement(int updatedPageIndex, String updatedResourceName) {
            return new PdfImageResource(updatedPageIndex, updatedResourceName, width, height, rgbData);
        }
    }
}
