package simula.plugin.extensions.runConfiguration;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiElement;

public class SimulaFileRunConfigurationProducer extends LazyRunConfigurationProducer<SimulaRunConfiguration> {

    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        System.out.println("SimulaFileRunConfigurationProducer.getConfigurationFactory: ");

        // Return an instance of your ConfigurationFactory
        // The type associated with this factory should be the one you want this producer to create
        return new SimulaConfigurationFactory(new SimulaRunConfigurationType());
    }


    /// Attempts to create a new run configuration from the current context.
    @Override
    protected boolean setupConfigurationFromContext(
            @NotNull SimulaRunConfiguration configuration,
            @NotNull ConfigurationContext context,
            @NotNull Ref<PsiElement> sourceElement) {

        // This method is called to create a *new* run configuration from the context.
        // 1. Determine if the current context (e.g., the right-clicked file) is valid for your config.
        PsiElement element = sourceElement.get();
        if (element == null || !isValidContext(element)) {
            return false; // Not applicable
        }

        // 2. Configure the 'configuration' object based on the context.
        // For example, set the main class or script path.
        // configuration.setMySpecificParameters(element.getName());
        configuration.setName("Simula Config: " + getName(element));

        // 3. Update the source element to point to the relevant PSI element if necessary.
        // sourceElement.set(relevantPsiElement);

        return true; // Return true to indicate a new configuration was set up successfully
    }

    private boolean isValidContext(PsiElement element) {
        // Implement logic to determine if the element is something your config can run
        // e.g. checking file extension, class type, etc.
        System.out.println("SimulaFileRunConfigurationProducer.isValidContext: element=" + element);
        if (element != null) {
            PsiFile psiFile = element.getContainingFile();
            System.out.println("SimulaFileRunConfigurationProducer.isValidContext: psiFile=" + psiFile);
            if (psiFile != null) {
                String fileName = psiFile.getName();
                System.out.println("SimulaFileRunConfigurationProducer.isValidContext: psiFile'name=" + fileName);
                if (fileName.toLowerCase().endsWith(".sim")) {
                    return true;
                }
            }
        }
        return true;
    }

    private String getName(PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        return (psiFile == null) ? "Simula" : psiFile.getName();
    }


    public String getId() {
        if (true) throw new RuntimeException("SimulaFileRunConfigurationProducer.getId: ");
        return "Simula";
    }


    public String getName() {
       if(true) throw new RuntimeException("SimulaFileRunConfigurationProducer.getName: ");
       return "Simula";
    }

    /**
     * Checks if an existing configuration matches the current context.
     */
    @Override
    public boolean isConfigurationFromContext(
            @NotNull SimulaRunConfiguration configuration,
            @NotNull ConfigurationContext context) {
        // This method is called to check if an *existing* configuration matches the current context.
        // Return true if the existing configuration's parameters match the current context's data.
        PsiElement element = context.getPsiLocation();
        if (element == null || !isValidContext(element)) return false;

        // Example check:
        // return configuration.getMySpecificParameters().equals(element.getName());
        return true;
    }

}
