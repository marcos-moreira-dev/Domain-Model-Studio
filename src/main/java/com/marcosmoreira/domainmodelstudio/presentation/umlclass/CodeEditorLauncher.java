package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.Objects;

/** Lanza el editor de código configurado para abrir archivos fuente importados. */
final class CodeEditorLauncher {
    private static final String DEFAULT_COMMAND = "code";

    private final String commandTemplate;

    private CodeEditorLauncher(String commandTemplate) {
        this.commandTemplate = normalize(commandTemplate);
    }

    static CodeEditorLauncher configured(String commandTemplate) {
        return new CodeEditorLauncher(commandTemplate);
    }

    static CodeEditorLauncher fromSystem() {
        return new CodeEditorLauncher(UmlClassCodeEditorSettings.system().effectiveCommand());
    }

    String commandTemplate() {
        return commandTemplate;
    }

    void open(Path file) throws IOException {
        Objects.requireNonNull(file, "file");
        Path normalizedFile = file.toAbsolutePath().normalize();
        if (usesSystemDefault()) {
            openWithSystemDefault(normalizedFile);
            return;
        }
        if (usesWindowsOpenWith()) {
            openWithWindowsOpenWithDialog(normalizedFile);
            return;
        }
        try {
            new ProcessBuilder(processCommandFor(normalizedFile)).start();
        } catch (IOException exception) {
            if (isWindows()) {
                openWithWindowsOpenWithDialog(normalizedFile);
                return;
            }
            throw exception;
        }
    }


    List<String> processCommandFor(Path file) {
        List<String> command = resolveExecutableForRuntime(commandFor(file));
        if (command.isEmpty()) {
            return command;
        }
        if (isWindows() && isWindowsCommandScript(command.get(0))) {
            ArrayList<String> wrapped = new ArrayList<>();
            wrapped.add("cmd.exe");
            wrapped.add("/c");
            wrapped.addAll(command);
            return wrapped;
        }
        return command;
    }

    List<String> commandFor(Path file) {
        String fileText = file.toString();
        List<String> parts = splitCommand(commandTemplate);
        if (parts.isEmpty()) {
            parts = List.of(DEFAULT_COMMAND);
        }
        boolean replaced = false;
        List<String> command = new ArrayList<>();
        for (String part : parts) {
            if (part.contains("%f")) {
                command.add(part.replace("%f", fileText));
                replaced = true;
            } else {
                command.add(part);
            }
        }
        if (!replaced) {
            command.add(fileText);
        }
        return command;
    }

    private static List<String> resolveExecutableForRuntime(List<String> command) {
        List<String> resolved = resolveKnownEditorAlias(command);
        return resolveKnownEditorExecutable(resolved);
    }

    private boolean usesSystemDefault() {
        return UmlClassCodeEditorSettings.SYSTEM_DEFAULT_COMMAND.equalsIgnoreCase(commandTemplate);
    }

    private boolean usesWindowsOpenWith() {
        return UmlClassCodeEditorSettings.WINDOWS_OPEN_WITH_COMMAND.equalsIgnoreCase(commandTemplate);
    }

    private static void openWithSystemDefault(Path file) throws IOException {
        if (isWindows()) {
            try {
                new ProcessBuilder("rundll32.exe", "url.dll,FileProtocolHandler", file.toUri().toString()).start();
                return;
            } catch (IOException ignored) {
                new ProcessBuilder("cmd.exe", "/c", "start", "", file.toString()).start();
                return;
            }
        }
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new IOException("El sistema no expone una aplicación predeterminada para abrir archivos.");
        }
        Desktop.getDesktop().open(file.toFile());
    }

    private static void openWithWindowsOpenWithDialog(Path file) throws IOException {
        if (!isWindows()) {
            openWithSystemDefault(file);
            return;
        }
        new ProcessBuilder("rundll32.exe", "shell32.dll,OpenAs_RunDLL", file.toString()).start();
    }

    private static List<String> resolveKnownEditorAlias(List<String> parts) {
        if (parts.isEmpty() || !isWindows()) {
            return parts;
        }
        String executable = parts.get(0);
        if (!executable.equalsIgnoreCase("code") && !executable.equalsIgnoreCase("code.cmd")) {
            return parts;
        }
        Optional<String> resolved = findWindowsVsCodeExecutable();
        if (resolved.isEmpty()) {
            return parts;
        }
        ArrayList<String> copy = new ArrayList<>(parts);
        copy.set(0, resolved.get());
        return copy;
    }

    private static List<String> resolveKnownEditorExecutable(List<String> parts) {
        if (parts.isEmpty() || !isWindows()) {
            return parts;
        }
        String executable = parts.get(0);
        if (!executable.toLowerCase(Locale.ROOT).endsWith("code.exe")) {
            return parts;
        }
        Path codeExe = Path.of(executable);
        Path codeCmd = codeExe.getParent() == null ? null : codeExe.getParent().resolve("bin").resolve("code.cmd");
        if (codeCmd == null || !java.nio.file.Files.isRegularFile(codeCmd)) {
            return parts;
        }
        ArrayList<String> copy = new ArrayList<>(parts);
        copy.set(0, codeCmd.toString());
        if (copy.stream().noneMatch("--reuse-window"::equalsIgnoreCase)) {
            copy.add(1, "--reuse-window");
        }
        return copy;
    }

    private static boolean isWindowsCommandScript(String executable) {
        String normalized = Objects.toString(executable, "").toLowerCase(Locale.ROOT);
        return normalized.endsWith(".cmd") || normalized.endsWith(".bat");
    }

    private static Optional<String> findWindowsVsCodeExecutable() {
        ArrayList<Path> candidates = new ArrayList<>();
        addVsCodeCandidates(candidates, System.getenv("LOCALAPPDATA"));
        addVsCodeCandidates(candidates, System.getenv("ProgramFiles"));
        addVsCodeCandidates(candidates, System.getenv("ProgramFiles(x86)"));
        for (Path candidate : candidates) {
            if (java.nio.file.Files.isRegularFile(candidate)) {
                return Optional.of(candidate.toString());
            }
        }
        return Optional.empty();
    }

    private static void addVsCodeCandidates(List<Path> candidates, String base) {
        if (base == null || base.isBlank()) {
            return;
        }
        Path root = Path.of(base);
        candidates.add(root.resolve("Programs/Microsoft VS Code/bin/code.cmd"));
        candidates.add(root.resolve("Programs/Microsoft VS Code/Code.exe"));
        candidates.add(root.resolve("Microsoft VS Code/bin/code.cmd"));
        candidates.add(root.resolve("Microsoft VS Code/Code.exe"));
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
    }

    private static List<String> splitCommand(String command) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < command.length(); index++) {
            char value = command.charAt(index);
            if (value == '"') {
                quoted = !quoted;
                continue;
            }
            if (Character.isWhitespace(value) && !quoted) {
                addPart(result, current);
                continue;
            }
            current.append(value);
        }
        addPart(result, current);
        return result;
    }

    private static void addPart(List<String> result, StringBuilder current) {
        if (!current.isEmpty()) {
            result.add(current.toString());
            current.setLength(0);
        }
    }

    private static String normalize(String value) {
        return value == null || value.isBlank() ? DEFAULT_COMMAND : value.strip();
    }
}
