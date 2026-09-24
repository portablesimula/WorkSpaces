package simula.client;

import org.eclipse.lsp4e.server.StreamConnectionProvider;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

public class REMOVED_SimulaLanguageServerProvider implements StreamConnectionProvider {
    private Process process;

    @Override
    public void start() throws IOException {
        // Code to start your language server process
        ProcessBuilder builder = new ProcessBuilder("path/to/your/server/executable");
        this.process = builder.start();
    }

    @Override
    public InputStream getInputStream() {
        return process.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    @Override
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    @Override
    public void stop() {
        if (process != null) {
            process.destroy();
        }
    }
}
