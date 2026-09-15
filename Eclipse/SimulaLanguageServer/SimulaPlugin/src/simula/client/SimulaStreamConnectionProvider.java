package simula.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JOptionPane;

import org.eclipse.lsp4e.server.StreamConnectionProvider;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;

import simula.runtime.RTS_Dialog;
import simula.runtime.RTS_EndProgram;
import simula.server.SimulaLanguageServer;


public class SimulaStreamConnectionProvider implements StreamConnectionProvider {

    private PipedInputStream clientInputStream = new PipedInputStream();
    private PipedOutputStream clientOutputStream = new PipedOutputStream();
    
    @Override
    public void start() throws IOException {
//    	Comn.popUp("HURRA !!!");
        PipedOutputStream serverOutputStream = new PipedOutputStream(clientInputStream);
        PipedInputStream serverInputStream = new PipedInputStream(clientOutputStream);

        // Instantiate your server directly from the package
        SimulaLanguageServer server = new SimulaLanguageServer();

        // Launch the server inside the same JVM process
        Launcher<LanguageClient> launcher = Launcher.createLauncher(
                server, 
                LanguageClient.class, 
                serverInputStream, 
                serverOutputStream
        );
        
        // If your server needs a reference to the client to send diagnostics
        server.connect(launcher.getRemoteProxy()); 
        
        // Start listening on a background thread
        launcher.startListening();
    }

    @Override
    public InputStream getInputStream() {
//        return clientInputStream;
        return new LoggingInputStream(clientInputStream);
    }

    @Override
    public OutputStream getOutputStream() {
//        return clientOutputStream;
        return new LoggingOutputStream(clientOutputStream);
    }

    @Override
    public InputStream getErrorStream() {
        return null; 
    }

    @Override
    public void stop() {
        // Handle stream and cleanup logic here
    }

}
