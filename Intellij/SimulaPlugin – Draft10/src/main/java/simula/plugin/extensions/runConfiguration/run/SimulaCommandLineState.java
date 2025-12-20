package simula.plugin.extensions.runConfiguration.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import simula.plugin.extensions.runConfiguration.DemoRunConfiguration;
import simula.plugin.extensions.runConfiguration.DemoRunConfigurationOptions;
import simula.plugin.util.Global;
import simula.plugin.util.Util;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

public class SimulaCommandLineState extends CommandLineState {

    public SimulaCommandLineState(@NotNull ExecutionEnvironment environment) {
        super(environment);
//        System.out.println("NEW SimulaCommandLineState: ");
//        System.out.println("NEW SimulaCommandLineState: ModulePath: "+environment.getModulePath());
//        System.out.println("NEW SimulaCommandLineState: UserDataString: "+environment.getUserDataString());
//        System.out.println("NEW SimulaCommandLineState: isRunningCurrentFile: "+environment.isRunningCurrentFile());
//        System.out.println("NEW SimulaCommandLineState: isHeadless: "+environment.isHeadless());
//        Project project = environment.getProject();
//        Util.printProject("NEW SimulaCommandLineState: ", project);

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
//        Util.TRACE("SimulaCommandLineState.startProcess: sourceFile=" + sourceFile);
        File file = new File(sourceFile);
        String name = file.getName();
        Util.TRACE("SimulaCommandLineState.startProcess: name=" + name);
        String ident = name.substring(0, name.length()-4);
//        Util.TRACE("SimulaCommandLineState.startProcess: ident=" + ident);
        String jarName = ident + ".jar";
//        Util.TRACE("SimulaCommandLineState.startProcess: jarName=" + jarName);

        // TODO: DETTE MÅ RETTES FØR ENDELIG VERSJON
//        String javaExePath = "java";
        String javaExePath = "C:\\Program Files\\Java\\jdk-25\\bin\\java.exe";

        GeneralCommandLine commandLine = new GeneralCommandLine()
            .withExePath(javaExePath) // Set the path to your executable
//          .withParameters("-jar", "bin/" + jarName, "-noPopup") // Add arguments
            .withParameters("-jar", "bin/" + jarName) // Add arguments
            .withWorkDirectory(workDirectory) // Set working directory
            .withCharset(Charset.forName("UTF-8")) // Set character set
        ;

        RunProfile runProfile = getEnvironment().getRunProfile();
        if (runProfile instanceof DemoRunConfiguration myRunConfiguration) {

            DemoRunConfigurationOptions options = myRunConfiguration.getState();
            Map<String, String> optionMap = options.getOptionsMap();
            DemoRunConfigurationOptions.setDefaults(optionMap);
            Util.TRACE("SimulaCompiler.runCommandFromPlugin: optionMap=" + optionMap);
            if (optionMap.get("simula.runtime.verbose").equals("true")) commandLine.addParameters("-verbose");
            if (optionMap.get("simula.runtime.noPopup").equals("true")) commandLine.addParameters("-noPopup");
//            if (optionMap.get("simula.compiler.noExecution").equals("true")) commandLine.addParameters("-noExecution");
//            if (optionMap.get("simula.compiler.warnings").equals("true")) commandLine.addParameters("-warnings");
//            if (optionMap.get("simula.compiler.extensions").equals("true")) commandLine.addParameters("-extensions");
        }
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
