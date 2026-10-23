package com.simula.client.builders;

import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/// Kanskje det skal gjøres helt annerledes.
/// 
/// I SimulaLspServer:
/// Ved Notification didSave  ==> Fullfør Code Generation
/// 
/// Ved Notification didOpen  ==> Fullfør Code Generation
/// 
/// Ved Notification didChange  ==> INGEN TING
/// 
public class SimulaDummyBuilder extends IncrementalProjectBuilder {

    public static final String BUILDER_ID = "com.simula.client.simulaDummyBuilder";

    // Definer feltet her
    public String debugCmd;

    public SimulaDummyBuilder() {
        // Initialiser med en standardverdi om ønskelig
        this.debugCmd = "simreg -d"; 
        IO.println("NEW SimulaDummyBuilder: debugCmd=" + debugCmd);
    }

//    public String getDebugCmd() {
//        return debugCmd;
//    }
//
//    public void setDebugCmd(String debugCmd) {
//        this.debugCmd = debugCmd;
//    }
    
    @Override
    protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
    	IO.println("SimulaDummyBuilder.build: Simula dummy builder triggered");

    	  // get the project to build
    	IProject project = getProject();        // Her kan du bruke feltet i byggeprosessen
    	
        if (debugCmd != null) {
            // Logikk for å kjøre eller klargjøre debug-kommandoen
        }
        
        switch (kind) {
	        case FULL_BUILD:
	        	fullBuild(monitor);
	        	break;
	
	        case INCREMENTAL_BUILD:
	        	IResourceDelta delta = getDelta(getProject());
	        	if (delta == null) {
	        		fullBuild(monitor);
	        	} else {
	        		incrementalBuild(delta, monitor);
	        	}
	        	break;
	
	        case AUTO_BUILD:
	        	break;
        }
         return null;
    }

    private void fullBuild(IProgressMonitor monitor) throws CoreException {
        IO.println("SimulaDummyBuilder.fullBuild: debugCmd=" + debugCmd);
        // Gå gjennom hele prosjektet og kompiler alle .sim filer
        getProject().accept(resource -> {
            compileSimula(resource);
            return true; 
        });
    }

    private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        IO.println("SimulaDummyBuilder.incrementalBuild: debugCmd=" + debugCmd);
        // Gå kun gjennom filene som faktisk er endret
        delta.accept(new IResourceDeltaVisitor() {
            public boolean visit(IResourceDelta delta) throws CoreException {
                IResource resource = delta.getResource();
                if (delta.getKind() == IResourceDelta.CHANGED || delta.getKind() == IResourceDelta.ADDED) {
                    compileSimula(resource);
                }
                return true;
            }
        });
    }

    private void compileSimula(IResource resource) {
        IO.println("SimulaDummyBuilder.compileSimula: resource=" + resource);
        if (resource.getType() == IResource.FILE && resource.getName().endsWith(".sim")) {
            // TODO: Kall din Simula-kompilator her og generer feilmarkører (Markers) hvis aktuelt
            IO.println("SimulaDummyBuilder.compileSimula: TODO: Kall din Simula-kompilator her og generer feilmarkører (Markers) hvis aktuelt");
            IO.println("SimulaDummyBuilder.compileSimula: Kanskje det skal gjøres helt annerledes: ");
        }
    }
}
