package test1;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;

import com.google.gson.TypeAdapterFactory;

import simula.lsp.SimulaLanguageServer;


// SEE: https://github.com/eclipse-lsp4j/lsp4j/blob/main/documentation/README.md
public class Test1_Main {
    public static void main(String[] args) {
    	
    	// Implement your language server
    	
    	// The first thing you should do is to implement your language server.
    	// To do so just implement the interface org.eclipse.lsp4j.services.LanguageServer.
        LanguageServer localServer = new SimulaLanguageServer();
        
        // Launch and connect with a LanguageClient
        
    	// Now that you have an actual implementation you can connect it with a remote client. Let's assume you have an Inputstream and an Outputstream, over which you want to communicate with a language client.
    	//
    	// The utility class LSPLauncher does most of the wiring for you. Here is the code needed.
        InputStream inputStream = System.in;  // Stream to read incoming messages from client
        OutputStream outputStream = System.out; // Stream to send outgoing messages to client

        Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(
        		localServer, 
        		inputStream, 
        		outputStream
        	);
        // With this we have a Launcher object on which we can obtain the remote proxy.
        // Usually a language server should also implement LanguageClientAware,
        // which defines a single method connect(LanguageClient) over which you can pass the remote proxy to the language server.
        
        if (localServer instanceof LanguageClientAware server) {
        	   LanguageClient client = launcher.getRemoteProxy();
        	   server.connect(client);
        	}
        // Now your language server is not only able to receive messages from the other side, but can send messages back as well.
        //
        // The final thing you need to do in order to start listening on the given inputstream, is this:


        
        Future<Void> future = launcher.startListening();
        // This will start the listening process in a new thread that reads messages from
        // the input stream and dispatches them to the corresponding message handlers.
        //
        // When implementing the handlers for requests or notifications,
        // you need to be aware that the calling thread is the thread that reads and dispatches incoming messages.
        // Therefore, blocking it may result in reduced throughput or even a deadlock (#775).
        // As a general rule, message handlers should be implemented in a non-blocking, asynchronous way.
        //
        //To stop listening for incoming messages, call future.cancel(true) (#770).
        

    }
}
