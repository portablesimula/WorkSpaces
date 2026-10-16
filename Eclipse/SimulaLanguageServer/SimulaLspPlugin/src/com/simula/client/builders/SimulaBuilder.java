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
public class SimulaBuilder extends IncrementalProjectBuilder {

    public static final String BUILDER_ID = "com.simula.client.simulaBuilder";

    @Override
    protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
        if (kind == FULL_BUILD) {
            fullBuild(monitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        return null;
    }

    private void fullBuild(IProgressMonitor monitor) throws CoreException {
        // Gå gjennom hele prosjektet og kompiler alle .sim filer
        getProject().accept(resource -> {
            compileSimula(resource);
            return true; 
        });
    }

    private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
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
        if (resource.getType() == IResource.FILE && resource.getName().endsWith(".sim")) {
            // TODO: Kall din Simula-kompilator her og generer feilmarkører (Markers) hvis aktuelt
            IO.println("SimulaBuilder.compileSimula: TODO: Kall din Simula-kompilator her og generer feilmarkører (Markers) hvis aktuelt");
            IO.println("Kanskje det skal gjøres helt annerledes: ");
        }
    }
}
