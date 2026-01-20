package simula.parser;

// <extensions defaultExtensionNs="com.intellij">
//    <lang.ast.factory language="MyLanguage"
// implementationClass="com.example.MyLanguageASTFactory"/>
// </extensions>

import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;

import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimulaASTFactory extends ASTFactory {
	public static final SimulaASTFactory INSTANCE = new SimulaASTFactory();
    @Override
    @Nullable
    public CompositeElement createComposite(@NotNull IElementType type) {
    	LOG.println("SimulaASTFactory.createComposite: "+type.getClass().getSimpleName());
    	Util.IERR();
        // Return a custom CompositeElement for specific types
//        if (type == MyTypes.MY_CUSTOM_BLOCK) {
//            return new MyCustomBlockElement(type);
//        }
        return null; // Fallback to default
    }

    @Override
    @Nullable
    public LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
    	LOG.println("SimulaASTFactory.createLeaf: "+type.getClass().getSimpleName()+" "+text);
    	Util.IERR();
        // Return a custom LeafElement (e.g., for specialized tokens)
//        if (type == MyTypes.MY_SPECIAL_TOKEN) {
//            return new MySpecialLeafElement(type, text);
//        }
        return null; // Fallback to default
    }
}
