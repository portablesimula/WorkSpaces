package com.simula.client.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class SimulaProjectNature implements IProjectNature {
    private IProject project;

    public static final String NATURE_ID = "com.simula.client.simulanature";
    
    @Override
    public void configure() throws CoreException {
        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();

        // Check if the builder is already configured
        for (ICommand command : commands) {
            if (command.getBuilderName().equals("com.simula.builder")) {
                return; 
            }
        }

        // Add the builder to the project description
        ICommand[] newCommands = new ICommand[commands.length + 1];
        System.arraycopy(commands, 0, newCommands, 0, commands.length);
        ICommand command = desc.newCommand();
        command.setBuilderName("com.simula.builder");
        newCommands[newCommands.length - 1] = command;
        
        desc.setBuildSpec(newCommands);
        project.setDescription(desc, null);
    }

    @Override
    public void deconfigure() throws CoreException {
        // Remove the builder when nature is removed
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
