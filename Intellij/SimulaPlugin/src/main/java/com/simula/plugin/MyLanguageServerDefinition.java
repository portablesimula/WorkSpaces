package com.simula.plugin;

import com.redhat.devtools.lsp4ij.server.ProcessLanguageServerDefinition;
import java.util.List;

public class MyLanguageServerDefinition extends ProcessLanguageServerDefinition {
    public MyLanguageServerDefinition() {
        // Define the system command to launch your language server executable/jar
        super(List.of("java", "-jar", "/path/to/your-language-server.jar"));
    }
}
