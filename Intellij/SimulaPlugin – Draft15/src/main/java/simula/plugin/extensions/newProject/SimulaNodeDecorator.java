package simula.plugin.extensions.newProject;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.openapi.vfs.VirtualFile;
import simula.plugin.util.Util;

public class SimulaNodeDecorator implements ProjectViewNodeDecorator {
    @Override
    public void decorate(ProjectViewNode node, PresentationData data) {
        final VirtualFile virtualFile = node.getVirtualFile();
//        System.out.println("MyNodeDecorator.decorate: " + virtualFile);
        if(virtualFile == null) return;
//        data.setIcon(Util.getSimulaIcon());
//        if (virtualFile != null && virtualFile.getName().endsWith(".sim")) {
        if (virtualFile != null) {
            if(virtualFile.getName().endsWith("bin")) {
                // Modify the presentation data: add location string, change icon, change text attributes
                data.setIcon(Util.getSimulaIcon());
                data.setLocationString(" (Simula Executables)");
                // You can also change the icon or use setAttributesKey to change colors/styles
            } else if(virtualFile.getName().endsWith("ssf")) {
                // Modify the presentation data: add location string, change icon, change text attributes
                data.setIcon(Util.getSimulaIcon());
                data.setLocationString(" (Simula Sample Files)");
                // You can also change the icon or use setAttributesKey to change colors/styles
            } else if(virtualFile.getName().endsWith("src")) {
                // Modify the presentation data: add location string, change icon, change text attributes
                data.setIcon(Util.getSimulaIcon());
                data.setLocationString(" (Simula Source Files)");
                // You can also change the icon or use setAttributesKey to change colors/styles
            }
        }
    }

}
