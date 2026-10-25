package com.simula.dap;
import org.eclipse.lsp4j.debug.*;
import org.eclipse.lsp4j.debug.launch.DSPLauncher;
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer;
import org.eclipse.lsp4j.jsonrpc.Launcher;

import com.simula.client.Option;

import java.net.Socket;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SimulaDapClient {

    private IDebugProtocolServer dapServer;
    private Socket socket;
    public Capabilities serverCapabilities;

	//    DAP Handshake Sequence Flow
	//
	//    Client (Eclipse Plugin)                         SimulaLspServer.jar
	//
	//           |                                                 |
	//           | ------------ 1. initialize request -----------> |
	//           | <----------- 2. initialize response ----------  |
	//           |                                                 |
	//           | <----------- 3. initialized event ------------- | (Sent asynchronously by server)
	//           |                                                 |
	//           | ------------ 4. launch request ---------------> | (Sent in parallel / fire-and-forget)
	//           | ------------ 5. setBreakpoints (Optional) ----> |
	//           |                                                 |
	//           | ------------ 6. configurationDone request ----> | (Signals client setup is complete)
	//           |                                                 |
	//           | <----------- 7. launch response --------------  | (Server responds to step 4)
	//           
    private void connectAndHandshake() {
    	try {
    		// 1. Opprett TCP-tilkobling til SimulaLspServer på port 4711
    		socket = new Socket("localhost", 4711);

    		// 2. Lag en lokal klient-lytter for å håndtere events fra serveren
    		SimulaDebugClientLauncher client = new SimulaDebugClientLauncher();

    		// 3. Bruk LSP4J for å koble input/output-strømmer til DAP-grensesnittet
    		Launcher<IDebugProtocolServer> launcher = DSPLauncher.createClientLauncher(
    				client, 
    				socket.getInputStream(), 
    				socket.getOutputStream()
    				);

    		// Start lytteren i en egen tråd
    		launcher.startListening();
    		dapServer = launcher.getRemoteProxy();

            // 4. UTFOR_ HANDSHAKE-SEKVENSEN

            // Steg A: Send 'initialize'
            InitializeRequestArguments initArgs = new InitializeRequestArguments();
            initArgs.setClientID("eclipse-simula-plugin");
            initArgs.setAdapterID("simula");
            initArgs.setLinesStartAt1(true);
            initArgs.setColumnsStartAt1(true);
            initArgs.setPathFormat("path");

            serverCapabilities = dapServer.initialize(initArgs).get();
            System.out.println("DAP initialisert. Server capabilities mottatt.");

            // [Valgfritt] Her sender du vanligvis setBreakpoints, setExceptionBreakpoints osv.
            // basert på hva brukeren har satt i Eclipse UI før de trykket på "Debug".

            // Steg B: Send 'configurationDone' for å fortelle serveren at oppsettet er ferdig
            ConfigurationDoneArguments configDoneArgs = new ConfigurationDoneArguments();
            dapServer.configurationDone(configDoneArgs).get();

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void launchSimula(URI documentUri, String mode) {
    	if(dapServer == null) connectAndHandshake();
        try {
            IO.println("SimulaDapClient.launchSimula: " + documentUri);

            // 5. KALL LAUNCH-METODEN MED DINE ARGS
            Map<String, Object> launchArgs = new HashMap<>();
//            launchArgs.put("program", "/path/to/my_simula_program.sim");
            launchArgs.put("documentUri", documentUri.toString());
            launchArgs.put("mode", mode);
            launchArgs.put("stopOnEntry", mode.equals("debug"));
//            launchArgs.put("compileBeforeLaunch", true);
            // Legg til andre Simula-spesifikke parametere her...
            launchArgs.put("simulaRtsLib", Option.simulaRtsLib);

            IO.println("SimulaDapClient.launchSimula: Request Launch");
            CompletableFuture<Void> launchFuture = dapServer.launch(launchArgs);
            
            // Vent på at serveren bekrefter at prosessen har startet (eller feilet)
            launchFuture.get(); 
            IO.println("SimulaDapClient.launchSimula: Started in mode: " + mode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Minimal klient-implementasjon for å motta hendelser (Events) fra DAP-serveren.
     */
    private static class SimulaDebugClientLauncher implements org.eclipse.lsp4j.debug.services.IDebugProtocolClient {
        @Override
        public void initialized() {
            System.out.println("Event mottatt: initialized");
        }

        @Override
        public void stopped(StoppedEventArguments args) {
            System.out.println("Event mottatt: Tråd stoppet pga: " + args.getReason());
        }

        @Override
        public void terminated(TerminatedEventArguments args) {
            System.out.println("Event mottatt: Debug-sesjon avsluttet.");
        }

        @Override
        public void exited(ExitedEventArguments args) {
            System.out.println("Event mottatt: Programmet avsluttet med kode: " + args.getExitCode());
        }

        @Override
        public void output(OutputEventArguments args) {
            System.out.print("[Simula Console]: " + args.getOutput());
        }
    }
}
