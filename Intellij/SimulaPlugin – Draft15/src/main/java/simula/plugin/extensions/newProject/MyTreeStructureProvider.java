package simula.plugin.extensions.newProject;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import simula.plugin.util.Util;

import java.util.ArrayList;
import java.util.Collection;

final class MyTreeStructureProvider implements TreeStructureProvider {

    @NotNull
    @Override
    public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                  @NotNull Collection<AbstractTreeNode<?>> children,
                                                  ViewSettings settings) {
        ArrayList<AbstractTreeNode<?>> nodes = new ArrayList<>();
        for (AbstractTreeNode<?> child : children) {
            if (child instanceof PsiFileNode) {
                VirtualFile file = ((PsiFileNode) child).getVirtualFile();
//                if (file != null && !file.isDirectory() && !(file.getFileType() instanceof PlainTextFileType)) {
//                    continue;
//                }
                System.out.println("MyTreeStructureProvider.modify: Child: " + child);
            }
            nodes.add(child);
        }
        return nodes;
    }

}