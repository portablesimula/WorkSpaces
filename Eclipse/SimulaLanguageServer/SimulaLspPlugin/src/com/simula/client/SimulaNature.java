package com.simula.client;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class SimulaNature implements IProjectNature {
    private IProject project;

    @Override
    public void configure() throws CoreException {
        // Kjøres når naturen blir lagt til (f.eks. for å registrere en Simula Builder)
    }

    @Override
    public void deconfigure() throws CoreException {
        // Kjøres hvis naturen fjernes
    }

    @Override
    public IProject getProject() {
        return project;
    }

    @Override
    public void setProject(IProject project) {
        this.project = project;
    }
}
