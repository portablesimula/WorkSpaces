package com.simula.lsp;

import com.intellij.openapi.project.Project;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SimulaLspManager {

    public static File getLspJarFile() {
        // 1. Hent ressursen som en Stream fra klasselasteren
        InputStream resourceStream = SimulaLspManager.class.getResourceAsStream("/server/SimulaLspServer.jar");
        
        if (resourceStream == null) {
            throw new IllegalStateException("Fant ikke SimulaLspServer.jar i ressursene");
        }
        File tempFile = null;

        try {
            // 2. Opprett en midlertidig fil på disken som slettes når IDE-en lukkes
            tempFile = File.createTempFile("SimulaLspServer", ".jar");
            tempFile.deleteOnExit();

            // 3. Kopier innholdet ut til den fysiske filen
            Files.copy(resourceStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tempFile;
    }

    public static String getWorkingDir(Project project){
        String basePath = project.getBasePath();
        File file = new File(basePath);
        String parent = file.getParent().toString();
        IO.println("SimulaLspManager.getWorkingDir: project: "+project);
        IO.println("SimulaLspManager.getWorkingDir: basePath: "+basePath);
        IO.println("SimulaLspManager.getWorkingDir: parent: "+parent);
        return parent;
    }
}
