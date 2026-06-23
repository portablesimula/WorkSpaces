package com.example.mylanguage.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;

public class MyStreamConnectionProvider extends ProcessStreamConnectionProvider {

    public MyStreamConnectionProvider() {
        List<String> commands = new ArrayList<>();
        
        // Example: If your server is a Node application
        commands.add("node");
        commands.add("/path/to/your/languageserver.js");
        
        // Example: If your server is an executable binary
        // commands.add("/path/to/your/server-binary");

        this.setCommands(commands);
        this.setWorkingDirectory(System.getProperty("user.home"));
    }

    @Override
    public void start() throws IOException {
        // You can intercept or configure environment variables before startup here
        super.start();
    }
}
