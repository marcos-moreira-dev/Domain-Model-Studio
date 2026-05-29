package com.marcosmoreira.domainmodelstudio.application.project;

/** Resultado de sincronizar el Markdown fuente asociado a un proyecto. */
public record SourceMarkdownSyncResult(Status status, String message) {

    public enum Status {
        SKIPPED,
        SYNCHRONIZED,
        FAILED
    }

    public static SourceMarkdownSyncResult skipped() {
        return new SourceMarkdownSyncResult(Status.SKIPPED, "");
    }

    public static SourceMarkdownSyncResult synchronizedOk(String message) {
        return new SourceMarkdownSyncResult(Status.SYNCHRONIZED, message == null ? "" : message);
    }

    public static SourceMarkdownSyncResult failed(String message) {
        return new SourceMarkdownSyncResult(Status.FAILED, message == null ? "" : message);
    }

    public boolean shouldNotifyUser() {
        return status == Status.SYNCHRONIZED || status == Status.FAILED;
    }
}
