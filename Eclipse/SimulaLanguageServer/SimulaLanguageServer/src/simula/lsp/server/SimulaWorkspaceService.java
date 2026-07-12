package simula.lsp.server;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.services.WorkspaceService;
import java.util.concurrent.CompletableFuture;

public class SimulaWorkspaceService implements WorkspaceService {

    // Triggered when settings change in the client/IDE
    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams params) {
        // e.g., Update internal configuration objects 
        Object settings = params.getSettings();
    }

    // Triggered when files watched by the client are modified/deleted/created
    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        // e.g., Clear build caches or re-index your workspace
        params.getChanges().forEach(fileEvent -> {
            String uri = fileEvent.getUri();
            // Handle fileEvent.getType() (Created, Changed, Deleted)
        });
    }

    // Triggered when a workspace folder is added or removed (multi-root workspaces)
    @Override
    public void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params) {
        // Handle added and removed folders
        params.getEvent().getAdded().forEach(folder -> { /* Index folder */ });
        params.getEvent().getRemoved().forEach(folder -> { /* Clean up folder */ });
    }

    // Handles execution of custom server commands triggered by the client
    @Override
    public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
        return CompletableFuture.supplyAsync(() -> {
            String command = params.getCommand();
            if ("myExtension.runRefactoring".equals(command)) {
                // Execute logic
                return "Success";
            }
            throw new UnsupportedOperationException("Unknown command: " + command);
        });
    }
}
