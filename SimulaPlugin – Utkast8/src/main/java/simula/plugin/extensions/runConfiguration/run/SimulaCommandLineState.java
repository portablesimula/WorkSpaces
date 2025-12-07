package simula.plugin.extensions.runConfiguration.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import simula.plugin.util.Global;
import simula.plugin.util.Util;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.Charset;

public class SimulaCommandLineState extends CommandLineState {

    public SimulaCommandLineState(@NotNull ExecutionEnvironment environment) {
        super(environment);
        System.out.println("NEW SimulaCommandLineState: ");
        System.out.println("NEW SimulaCommandLineState: ModulePath: "+environment.getModulePath());
        System.out.println("NEW SimulaCommandLineState: ModulePath: "+environment.getUserDataString());
        System.out.println("NEW SimulaCommandLineState: ModulePath: "+environment.isRunningCurrentFile());
        System.out.println("NEW SimulaCommandLineState: ModulePath: "+environment.isHeadless());
        Project project = environment.getProject();
        Util.printProject("NEW SimulaCommandLineState: ", project);

//        Thread.dumpStack();
//        if(true) throw new RuntimeException("NEW SimulaCommandLineState: ");
    }

    @Override
    public String toString() {
        ExecutionEnvironment environment = this.getEnvironment();
        return environment.toString();
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        // 1. Create a GeneralCommandLine object
        System.out.println("SimulaCommandLineState.startProcess: 1. Create a GeneralCommandLine object");
//        if (true) throw new RuntimeException("SimulaCommandLineState.startProcess: 1. Create a GeneralCommandLine object");
        Project project = getEnvironment().getProject();

        String workDirectory = project.getBasePath();
        System.out.println("SimulaCommandLineState.startProcess: workDirectory="+workDirectory);

        String sourceFile = Global.currentSourceFile;
        Util.TRACE("SimulaCommandLineState.startProcess: sourceFile=" + sourceFile);
        File file = new File(sourceFile);
        String name = file.getName();
        Util.TRACE("SimulaCommandLineState.startProcess: name=" + name);
        String ident = name.substring(0, name.length()-4);
        Util.TRACE("SimulaCommandLineState.startProcess: ident=" + ident);
        String jarName = ident + ".jar";
        Util.TRACE("SimulaCommandLineState.startProcess: jarName=" + jarName);

        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        GeneralCommandLine commandLine = new GeneralCommandLine()
            .withExePath(javaExePath) // Set the path to your executable

//                .withParameters("-version") // Add arguments
//                .withParameters("-jar", "bin/FittingRoom.jar", "-SPORT:noConsole") // Add arguments
                .withParameters("-jar", "bin/" + jarName, "-noPopup") // Add arguments

                .withWorkDirectory(workDirectory) // Set working directory
            .withCharset(Charset.forName("UTF-8")) // Set character set
        ;

        // 2. Wrap it in a ProcessHandler
        // OSProcessHandler is commonly used for standard external processes
        ProcessHandler processHandler = new OSProcessHandler(commandLine);

        // 3. Optional: attach a console view (though CommandLineState usually handles this automatically)
        // The console view will display stdout/stderr
        // consoleView.attachToProcess(processHandler);

        // 4. Return the handler
        return processHandler;
    }

}
