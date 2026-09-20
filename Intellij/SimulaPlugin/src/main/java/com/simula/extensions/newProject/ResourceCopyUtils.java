package com.simula.extensions.newProject;

import com.intellij.openapi.project.Project;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.stream.Stream;

public class ResourceCopyUtils {

    public static void copySamplesToSsf(Project project) {
        // 1. Define the target directory path "ssf" relative to the project base path
        String projectPath = project.getBasePath();
        if (projectPath == null) return;
        Path targetDir = Paths.get(projectPath, "ssf");

        try {
            // Create the "ssf" directory if it doesn't exist
            Files.createDirectories(targetDir);

            // 2. Resolve the path inside the plugin JAR
            URL resourceUrl = ResourceCopyUtils.class.getResource("/samples");
            if (resourceUrl == null) {
                System.err.println("Resource folder '/samples' not found!");
                return;
            }

            URI uri = resourceUrl.toURI();

            // Handle both running from a packaged JAR or direct running/testing IDE environment
            if ("jar".equals(uri.getScheme())) {
                // When running inside the packaged plugin JAR
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    Path pathInsideJar = fileSystem.getPath("/samples");
                    copyDirectoryContent(pathInsideJar, targetDir);
                }
            } else {
                // When running in a development environment (IDE sandbox)
                Path pathOnDisk = Paths.get(uri);
                copyDirectoryContent(pathOnDisk, targetDir);
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void copyDirectoryContent(Path source, Path target) throws IOException {
        try (Stream<Path> stream = Files.walk(source)) {
            stream.forEach(sourcePath -> {
                try {
                    // Resolve target path dynamically
                    Path relativePath = source.relativize(sourcePath);
                    // Standard structural string conversation to protect zip boundaries
                    Path targetPath = target.resolve(relativePath.toString());

                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy asset: " + sourcePath, e);
                }
            });
        }
    }
}
