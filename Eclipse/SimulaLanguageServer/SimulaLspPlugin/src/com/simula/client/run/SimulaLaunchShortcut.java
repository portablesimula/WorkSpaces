package com.simula.client.run;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.osgi.framework.Bundle;

import com.simula.client.DEF;
import com.simula.client.Option;
import com.simula.dap.SimulaDapClient;


public class SimulaLaunchShortcut implements ILaunchShortcut {

	@Override
    public void launch(ISelection selection, String mode) {
    	IO.println("SimulaLaunchShortcut.launch:" + mode + ", selection: " + selection);
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();
            IFile file = null;
            
            // Adapt the selection to an IFile
            if (element instanceof IFile)
                file = (IFile) element;
            else if (element instanceof IAdaptable)
                file = ((IAdaptable) element).getAdapter(IFile.class);
            
            if (file != null) launchFile(file, mode);
        }
    }

    @Override
    public void launch(IEditorPart editor, String mode) {
    	IO.println("SimulaLaunchShortcut.launch:" + mode + ", editor: " + editor);
        IFile file = editor.getEditorInput().getAdapter(IFile.class);
        if (file != null) {
            launchFile(file, mode);
        }
    }

    private void launchFile(IFile file, String mode) {
    	try {
//    		if(Option.simulaRtsLib == null) copyRuntimeSystemToWorkspace();
    		if(Option.simulaRtsLib == null) unpackRuntimeSystemToWorkspace();
    		IO.println("SimulaLaunchShortcut.launchFile: simulaRtsLib=" + Option.simulaRtsLib);
    		
    		IO.println("SimulaLaunchShortcut.launchFile: " + file);
    		IO.println("SimulaLaunchShortcut.launchFile: mode: " + mode);
            
            // NOTE: To ensure correct URI: Eg.g  file:///
            //       By converting the URI into a Path object and then back into a URI,
            //       Java will automatically format it according to the applicable standard (file:/// on Windows/Unix).
            URI documentUri = java.nio.file.Path.of(file.getLocationURI()).toUri();
            IO.println("SimulaLaunchShortcut.launchFile: documentUri: " + documentUri);

        	IO.println("SimulaLaunchShortcut.launchFile: DETTE MÅ RETTES/IMPLEMENTERES SEINERE: ");
        	
        	SimulaDapClient simulaDapClient = new SimulaDapClient();
        	simulaDapClient.launchSimula(documentUri, mode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void unpackRuntimeSystemToWorkspace() throws IOException {
    	IO.println("SimulaLaunchShortcut.copyRuntimeSystem: BEGIN");
    	
    	IWorkspace workspace = ResourcesPlugin.getWorkspace();
    	File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    	String workspacePath = workspaceDirectory.getAbsolutePath() + "/rts";
    	IO.println("SimulaLaunchShortcut.copyRuntimeSystem: workspacePath=" + workspacePath);
    	
    	String resourcePath = "server/SimulaRuntimeSystem.jar";
    	java.nio.file.Path targetOsDir = java.nio.file.Path.of(workspacePath);
    	unpackJarFromResource(DEF.SIMULA_PLUGIN_ID, resourcePath, targetOsDir);
    	
    	Option.simulaRtsLib = workspacePath;
   	}

    /**
     * Unpacks a JAR file from plugin resources into a target OS directory.
     * 
     * @param pluginId The Symbolic Name of your plugin (e.g., "org.simula.plugin")
     * @param resourcePath Path inside the plugin (e.g., "resources/my-compiler.jar")
     * @param targetOsPath The destination OS directory
     */
    public static void unpackJarFromResource(String pluginId, String resourcePath, Path targetOsPath) throws IOException {
        Bundle bundle = Platform.getBundle(pluginId);
        if (bundle == null) {
            throw new IOException("Could not find bundle: " + pluginId);
        }

        // 1. Resolve the internal bundle URL
        URL bundleUrl = bundle.getEntry(resourcePath);
        if (bundleUrl == null) {
            throw new IOException("Resource not found at: " + resourcePath);
        }

        // 2. Convert OSGi 'bundleentry://' or 'bundleresource://' URL to a usable stream
        URL fileUrl = FileLocator.toFileURL(bundleUrl);
        
        // 3. Read the resource stream as a JAR
        File targetOsDir = targetOsPath.toFile();
        try (InputStream is = fileUrl.openStream();
             JarInputStream jis = new JarInputStream(is)) {
            
            if (!targetOsDir.exists()) {
                targetOsDir.mkdirs();
            }

            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                File outputFile = new File(targetOsDir, entry.getName());
                
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    // Create parent directories if they don't exist yet
                    File parent = outputFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    
                    // Stream the individual file out to the OS filesystem
                    Files.copy(jis, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                jis.closeEntry();
            }
        }
    }

//	private void copyRuntimeSystemToWorkspace() throws IOException {
//    	IO.println("SimulaLaunchShortcut.copyRuntimeSystem: BEGIN");
//    	
//    	IWorkspace workspace = ResourcesPlugin.getWorkspace();
//    	File workspaceDirectory = workspace.getRoot().getLocation().toFile();
//    	String workspacePath = workspaceDirectory.getAbsolutePath();
//    	IO.println("SimulaLaunchShortcut.copyRuntimeSystem: workspacePath=" + workspacePath);
//    	
//    	String resourcePath = "server/SimulaRuntimeSystem.jar";
//    	java.nio.file.Path targetOsDir = java.nio.file.Path.of(workspacePath);
//    	String targetFileName = "SimulaRuntimeSystem.jar";
//    	copyResourceToOsDirectory(resourcePath, targetOsDir, targetFileName);
//    	
//    	Option.simulaRtsLib = workspacePath + '/' + targetFileName;
//   	}
//
//    /**
//     * Copies a file embedded in the plugin's resources to an external OS directory.
//     * 
//     * @param resourcePath The path relative to the plugin root (e.g., "resources/templates/simula_main.sim")
//     * @param targetOsDir  The absolute path to the destination OS directory (e.g., "C:/Users/Name/Documents")
//     * @param targetFileName The desired name of the file in the target directory
//     * @throws IOException If the stream cannot be read or written
//     */
//    public static void copyResourceToOsDirectory(String resourcePath, java.nio.file.Path targetOsDir, String targetFileName) throws IOException {
//        // 1. Grab your specific Simula plugin bundle using its unique symbolic ID
//        Bundle bundle = Platform.getBundle(DEF.SIMULA_PLUGIN_ID);
//        if (bundle == null) {
//            throw new IOException("Simula plugin bundle could not be found.");
//        }
//
//        // 2. Open an input stream directly through OSGi's FileLocator
//        // This abstracts away whether your plugin is a directory or a compressed JAR
//        try (InputStream in = FileLocator.openStream(bundle, new Path(resourcePath), false)) {
//            
//            // 3. Ensure the target OS directory exists
//            if (!Files.exists(targetOsDir)) {
//                Files.createDirectories(targetOsDir);
//            }
//
//            // 4. Resolve the complete absolute file path and copy
//            java.nio.file.Path destinationFile = targetOsDir.resolve(targetFileName);
//            Files.copy(in, destinationFile, StandardCopyOption.REPLACE_EXISTING);
//        }
//    }

}
