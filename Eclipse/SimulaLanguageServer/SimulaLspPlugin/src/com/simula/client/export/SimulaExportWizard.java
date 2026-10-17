package com.simula.client.export;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Bundle;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SimulaExportWizard extends Wizard implements IExportWizard {
    private SimulaExportWizardPage mainPage;

    public SimulaExportWizard() {
        setNeedsProgressMonitor(true);
        setWindowTitle("Export Simula Application");
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // Initialization using active selection if needed
    }

    @Override
    public void addPages() {
        mainPage = new SimulaExportWizardPage();
        addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        String projectName = mainPage.getSelectedProjectName();
        String destinationJar = mainPage.getDestinationPath();

        if (projectName.isEmpty() || destinationJar.isEmpty()) {
            mainPage.setErrorMessage("Please select a project and valid destination.");
            return false;
        }

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        
        try {
            exportRunnableJar(project, destinationJar);
            return true;
        } catch (Exception e) {
            mainPage.setErrorMessage("Export failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void exportRunnableJar(IProject project, String destPath) throws Exception {
        // 1. Prepare Manifest metadata
        Manifest manifest = new Manifest();
        Attributes attrs = manifest.getMainAttributes();
        attrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        // Assume you track or generate the Entry point class somewhere in your plugin properties
        attrs.put(Attributes.Name.MAIN_CLASS, "simula.runtime.SimulaMainRunner"); 

        Set<String> addedEntries = new HashSet<>();

        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(destPath), manifest)) {
            // 2. Package Compiled Simula .class files (Assuming an output folder named "bin")
            IFolder binFolder = project.getFolder("bin");
            if (binFolder.exists()) {
                File binDir = binFolder.getLocation().toFile();
                addDirectoryToJar(binDir, binDir, jos, addedEntries);
            }

            // 3. Package Runtime system dependencies 
            // Locating your runtime JAR bundle inside your plugin environment
            Bundle pluginBundle = Platform.getBundle("simula.ide.plugin.id"); // Replace with your plugin ID
            URL runtimeJarUrl = FileLocator.find(pluginBundle, new org.eclipse.core.runtime.Path("lib/simula-runtime.jar"), null);
            
            if (runtimeJarUrl != null) {
                URL localRuntimeJarUrl = FileLocator.toFileURL(runtimeJarUrl);
                try (ZipInputStream zis = new ZipInputStream(localRuntimeJarUrl.openStream())) {
                    ZipEntry entry;
                    byte[] buffer = new byte[4096];
                    while ((entry = zis.getNextEntry()) != null) {
                        // Skip custom or manifest configurations from the dependent runtime jar
                        if (entry.isDirectory() || entry.getName().startsWith("META-INF/")) {
                            continue;
                        }
                        if (addedEntries.add(entry.getName())) {
                            jos.putNextEntry(new JarEntry(entry.getName()));
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                jos.write(buffer, 0, len);
                            }
                            jos.closeEntry();
                        }
                    }
                }
            }
        }
    }

    private void addDirectoryToJar(File baseDir, File source, JarOutputStream jos, Set<String> addedEntries) throws IOException {
        byte[] buffer = new byte[4096];
        for (File file : source.listFiles()) {
            if (file.isDirectory()) {
                addDirectoryToJar(baseDir, file, jos, addedEntries);
            } else {
                // Calculate relative zip pathway name string
                String entryName = baseDir.toURI().relativize(file.toURI()).getPath();
                if (addedEntries.add(entryName)) {
                    jos.putNextEntry(new JarEntry(entryName));
                    try (FileInputStream fis = new FileInputStream(file)) {
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            jos.write(buffer, 0, len);
                        }
                    }
                    jos.closeEntry();
                }
            }
        }
    }
}
