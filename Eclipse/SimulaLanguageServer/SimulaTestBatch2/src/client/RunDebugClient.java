package client;

import org.eclipse.lsp4j.debug.ExitedEventArguments;
import org.eclipse.lsp4j.debug.OutputEventArguments;
import org.eclipse.lsp4j.debug.StoppedEventArguments;
import org.eclipse.lsp4j.debug.TerminatedEventArguments;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;

public class RunDebugClient implements IDebugProtocolClient {
	
	
    @Override
    public void initialized() {
        System.out.println("Debug adapter initialized.");
        // Client can now send configuration requests like setBreakpoints
    }

    @Override
    public void stopped(StoppedEventArguments args) {
        System.out.println("Execution stopped. Reason: " + args.getReason());
        System.out.println("Stopped on Thread ID: " + args.getThreadId());
    }

    @Override
    public void output(OutputEventArguments args) {
        System.out.print("[Target Output]: " + args.getOutput());
    }

    @Override
    public void terminated(TerminatedEventArguments args) {
        System.out.println("Debug session terminated.");
    }

    @Override
    public void exited(ExitedEventArguments args) {
        System.out.println("Debugged process exited with status: " + args.getExitCode());
    }
    
    // Implement remaining IDebugProtocolClient methods as needed...

}
