package simula.client;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Future;

import org.eclipse.lsp4e.server.StreamConnectionProvider;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

public class AbstractConnectionProvider implements StreamConnectionProvider {

    private InputStream inputStream;
    private OutputStream outputStream;
    private LanguageServer languageServer;
    private volatile Future<Void> listening;
    protected Launcher<LanguageClient> launcher;

    public AbstractConnectionProvider(LanguageServer languageServer) {
        this.languageServer = languageServer;
    }

    @Override
    public void start() throws IOException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in2 = new PipedInputStream();
        PipedOutputStream out2 = new PipedOutputStream();

        in.connect(out2);
        out.connect(in2);

        launcher = LSPLauncher.createServerLauncher(languageServer, in2, out2);
        inputStream = in;
        outputStream = out;

        listening = launcher.startListening();
    }

    // The returned streams log the protocol traffic to the console for learning
    // purposes, remove the wrappers for production use

    @Override
    public InputStream getInputStream() {
        return new FilterInputStream(inputStream) {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int bytesRead = super.read(b, off, len);
                if (bytesRead > 0) {
                    System.err.print(new String(b, off, bytesRead));
                }
                return bytesRead;
            }
        };
    }

    @Override
    public OutputStream getOutputStream() {
        return new FilterOutputStream(outputStream) {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                System.err.print(new String(b, off, len));
                super.write(b, off, len);
            }
        };
    }

    @Override
    public void stop() {
        // Stop the launcher before closing the streams, otherwise the server
        // logs an InterruptedIOException when the IDE shuts down
        if (listening != null) {
            listening.cancel(true);
        }
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Error closing streams: " + e.getMessage());
        }
    }

    @Override
    public InputStream getErrorStream() {
        return null;
    }
}