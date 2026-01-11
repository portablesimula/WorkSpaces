package com.intellij.psi.util;

import com.intellij.diagnostic.PluginException;
import com.intellij.lang.ASTNode;
import com.intellij.lang.FileASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.FileIndexFacade;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.meta.PsiMetaData;
import com.intellij.psi.meta.PsiMetaOwner;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.templateLanguages.TemplateLanguageUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.TimeoutUtil;
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread;
import com.intellij.util.containers.ContainerUtil;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.swing.Icon;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class PsiUtilCore {
    private static final Logger LOG = Logger.getInstance(PsiUtilCore.class);
    public static final PsiElement NULL_PSI_ELEMENT = new NullPsiElement();
    private static final boolean ourSleepDuringValidityCheck = Registry.is("psi.sleep.in.validity.check");
    public static final PsiFile NULL_PSI_FILE = new NullPsiFile();

    public static PsiElement @NotNull [] toPsiElementArray(@NotNull Collection<? extends PsiElement> collection) {
        PsiElement[] var10000 = collection.isEmpty() ? PsiElement.EMPTY_ARRAY : (PsiElement[])collection.toArray(PsiElement.EMPTY_ARRAY);
        if (var10000 == null) {
            $$$reportNull$$$0(1);
        }

        return var10000;
    }

    public static Language getNotAnyLanguage(ASTNode node) {
        if (node == null) {
            return Language.ANY;
        } else {
            Language lang = node.getElementType().getLanguage();
            return lang == Language.ANY ? getNotAnyLanguage(node.getTreeParent()) : lang;
        }
    }

    public static @Nullable VirtualFile getVirtualFile(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        } else if (element instanceof PsiFileSystemItem) {
            return element.isValid() ? ((PsiFileSystemItem)element).getVirtualFile() : null;
        } else {
            PsiFile containingFile = element.getContainingFile();
            if (containingFile != null && containingFile.isValid()) {
                VirtualFile file = containingFile.getVirtualFile();
                if (file == null) {
                    PsiFile originalFile = containingFile.getOriginalFile();
                    if (originalFile != containingFile && originalFile.isValid()) {
                        file = originalFile.getVirtualFile();
                    }
                }

                return file;
            } else {
                return null;
            }
        }
    }

    public static int compareElementsByPosition(@Nullable PsiElement element1, @Nullable PsiElement element2) {
        if (element1 == null && element2 == null) {
            return 0;
        } else if (element1 == null) {
            return -1;
        } else if (element2 == null) {
            return 1;
        } else if (element1.equals(element2)) {
            return 0;
        } else {
            PsiFile psiFile1 = element1.getContainingFile();
            PsiFile psiFile2 = element2.getContainingFile();
            if (psiFile1 == null && psiFile2 == null) {
                return 0;
            } else if (psiFile1 == null) {
                return -1;
            } else if (psiFile2 == null) {
                return 1;
            } else if (!psiFile1.equals(psiFile2)) {
                String name1 = psiFile1.getName();
                String name2 = psiFile2.getName();
                return name1.compareToIgnoreCase(name2);
            } else {
                if (element1 instanceof StubBasedPsiElement && element2 instanceof StubBasedPsiElement) {
                    StubElement<?> stub1 = ((StubBasedPsiElement)element1).getStub();
                    StubElement<?> stub2 = ((StubBasedPsiElement)element2).getStub();
                    if (stub1 != null && stub2 != null) {
                        return compareStubPositions(stub1, stub2);
                    }
                }

                TextRange textRange1 = element1.getTextRange();
                TextRange textRange2 = element2.getTextRange();
                if (textRange1 == null && textRange2 == null) {
                    return 0;
                } else if (textRange1 == null) {
                    return -1;
                } else {
                    return textRange2 == null ? 1 : Segment.BY_START_OFFSET_THEN_END_OFFSET.compare(textRange1, textRange2);
                }
            }
        }
    }

    private static int compareStubPositions(StubElement<?> stub1, StubElement<?> stub2) {
        int depth1 = getStubDepth(stub1);
        int depth2 = getStubDepth(stub2);

        int diff;
        for(diff = Integer.compare(depth1, depth2); depth1 > depth2; --depth1) {
            stub1 = stub1.getParentStub();
        }

        while(depth2 > depth1) {
            stub2 = stub2.getParentStub();
            --depth2;
        }

        int cmp = compareBalancedStubs(stub1, stub2);
        return cmp == 0 ? diff : cmp;
    }

    private static int getStubDepth(StubElement<?> stub) {
        int depth;
        for(depth = 0; stub != null; ++depth) {
            stub = stub.getParentStub();
        }

        return depth;
    }

    private static int compareBalancedStubs(StubElement<?> stub1, StubElement<?> stub2) {
        if (stub1 == stub2) {
            return 0;
        } else {
            StubElement<?> parent1 = stub1.getParentStub();
            StubElement<?> parent2 = stub2.getParentStub();
            int parentCmp = compareBalancedStubs(parent1, parent2);
            return parentCmp != 0 ? parentCmp : Integer.compare(parent1.getChildrenStubs().indexOf(stub1), parent2.getChildrenStubs().indexOf(stub2));
        }
    }

    public static boolean hasErrorElementChild(@NotNull PsiElement element) {
        for(PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiErrorElement) {
                return true;
            }
        }

        return false;
    }

    public static @NotNull PsiElement getElementAtOffset(@NotNull PsiFile file, int offset) {
        PsiElement elt = file.findElementAt(offset);
        if (elt == null && offset > 0) {
            elt = file.findElementAt(offset - 1);
        }

        Object var10000 = elt == null ? file : elt;
        if ((elt == null ? file : elt) == null) {
            $$$reportNull$$$0(4);
        }

        return (PsiElement)var10000;
    }

    public static PsiFile getTemplateLanguageFile(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        } else {
            PsiFile containingFile = element.getContainingFile();
            return containingFile == null ? null : TemplateLanguageUtil.getBaseFile(containingFile);
        }
    }

    public static PsiFile @NotNull [] toPsiFileArray(@NotNull Collection<? extends PsiFile> collection) {
        if (collection.isEmpty()) {
            PsiFile[] var1 = PsiFile.EMPTY_ARRAY;
            if (var1 == null) {
                $$$reportNull$$$0(6);
            }

            return var1;
        } else {
            PsiFile[] var10000 = (PsiFile[])collection.toArray(PsiFile.EMPTY_ARRAY);
            if (var10000 == null) {
                $$$reportNull$$$0(7);
            }

            return var10000;
        }
    }

    public static @NotNull @Unmodifiable List<PsiFile> toPsiFiles(@NotNull PsiManager psiManager, @NotNull Collection<? extends VirtualFile> virtualFiles) {
        Objects.requireNonNull(psiManager);
        return ContainerUtil.mapNotNull(virtualFiles, psiManager::findFile);
    }

    public static String getName(PsiElement element) {
        String name = null;
        if (element instanceof PsiMetaOwner) {
            PsiMetaData data = ((PsiMetaOwner)element).getMetaData();
            if (data != null) {
                name = data.getName(element);
            }
        }

        if (name == null && element instanceof PsiNamedElement) {
            name = ((PsiNamedElement)element).getName();
        }

        return name;
    }

    public static @NotNull String getQualifiedNameAfterRename(String qName, @NotNull String newName) {
        if (qName == null) {
            return newName;
        } else {
            int index = qName.lastIndexOf(46);
            return index < 0 ? newName : qName.substring(0, index + 1) + newName;
        }
    }

    public static @NotNull Language getDialect(@NotNull PsiElement element) {
        return narrowLanguage(element.getLanguage(), element.getContainingFile().getLanguage());
    }

    protected static @NotNull Language narrowLanguage(@NotNull Language language, @NotNull Language candidate) {
        return candidate.isKindOf(language) ? candidate : language;
    }

    public static void ensureValid(@NotNull PsiElement element) {
        if (!element.isValid()) {
            if (ourSleepDuringValidityCheck) {
                TimeoutUtil.sleep(1L);
                if (element.isValid()) {
                    LOG.error("PSI resurrected: " + element + " of " + element.getClass());
                    return;
                }
            }

            throw PluginException.createByClass(new PsiInvalidElementAccessException(element), element.getClass());
        }
    }

    @RequiresBackgroundThread(
        generateAssertion = false
    )
    public static @Nullable PsiFileSystemItem findFileSystemItem(@Nullable Project project, @Nullable VirtualFile file) {
        if (project != null && file != null) {
            if (!project.isDisposed() && file.isValid()) {
                PsiManager psiManager = PsiManager.getInstance(project);
                return (PsiFileSystemItem)(file.isDirectory() ? psiManager.findDirectory(file) : psiManager.findFile(file));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static @NotNull PsiFile getPsiFile(@NotNull Project project, @NotNull VirtualFile file) {
        PsiManager psiManager = PsiManager.getInstance(project);
        PsiFile psi = psiManager.findFile(file);
        if (psi == null) {
            logFileIsNotFound(file, psiManager, project);
            throw new AssertionError();
        } else {
            return psi;
        }
    }

    private static void logFileIsNotFound(@NotNull VirtualFile file, @NotNull PsiManager psiManager, @NotNull Project project) {
        if (project == null) {
            $$$reportNull$$$0(24);
        }

        FileType fileType = file.getFileType();
        FileViewProvider viewProvider = psiManager.findViewProvider(file);
        Document document = FileDocumentManager.getInstance().getDocument(file);
        boolean ignored = !(file instanceof LightVirtualFile) && FileTypeRegistry.getInstance().isFileIgnored(file);
        VirtualFile vDir = file.getParent();
        PsiDirectory psiDir = vDir == null ? null : PsiManager.getInstance(project).findDirectory(vDir);
        FileIndexFacade indexFacade = FileIndexFacade.getInstance(project);
        StringBuilder sb = new StringBuilder();
        sb.append("valid=").append(file.isValid()).append(" isDirectory=").append(file.isDirectory()).append(" hasDocument=").append(document != null).append(" length=").append(file.getLength());
        sb.append("\nproject=").append(project.getName()).append(" default=").append(project.isDefault()).append(" open=").append(project.isOpen());
        sb.append("\nfileType=").append(fileType.getName()).append("/").append(fileType.getClass().getName());
        sb.append("\nisIgnored=").append(ignored);
        sb.append(" underIgnored=").append(indexFacade.isUnderIgnored(file));
        sb.append(" inLibrary=").append(indexFacade.isInLibrary(file));
        sb.append(" parentDir=").append(vDir == null ? "no-vfs" : (vDir.isDirectory() ? "has-vfs-dir" : "has-vfs-file")).append("/").append(psiDir == null ? "no-psi" : "has-psi");
        sb.append("\nviewProvider=").append(viewProvider == null ? "null" : viewProvider.getClass().getName());
        if (viewProvider != null) {
            List<PsiFile> files = viewProvider.getAllFiles();
            sb.append(" language=").append(viewProvider.getBaseLanguage().getID());
            sb.append(" physical=").append(viewProvider.isPhysical());
            sb.append(" rootCount=").append(files.size());

            for(PsiFile o : files) {
                sb.append("\n  root=").append(o.getLanguage().getID()).append("/").append(o.getClass().getName());
            }
        }

        LOG.error("no PSI for file '" + file.getName() + "'", new Attachment[]{new Attachment(file.getPresentableUrl(), sb.toString())});
    }

    /** @deprecated */
    @Deprecated
    public static <T extends PsiElement> @Nullable T getOriginalElement(@NotNull T psiElement, @NotNull Class<? extends T> elementClass) {
        PsiFile psiFile = psiElement.getContainingFile();
        PsiFile originalFile = psiFile.getOriginalFile();
        if (originalFile == psiFile) {
            return psiElement;
        } else {
            TextRange range = psiElement.getTextRange();
            PsiElement element = originalFile.findElementAt(range.getStartOffset());
            int maxLength = range.getLength();
            T parent = PsiTreeUtil.getParentOfType(element, elementClass, false);

            for(T next = parent; next != null && next.getTextLength() <= maxLength; next = PsiTreeUtil.getParentOfType(next, elementClass, true)) {
                parent = next;
            }

            return parent;
        }
    }

    public static @NotNull Language findLanguageFromElement(@NotNull PsiElement elt) {
        if (!(elt instanceof PsiFile) && elt.getFirstChild() == null) {
            PsiElement parent = elt.getParent();
            if (parent != null) {
                return parent.getLanguage();
            }
        }

        return elt.getLanguage();
    }

    public static @NotNull Language getLanguageAtOffset(@NotNull PsiFile file, int offset) {
        PsiElement elt = file.findElementAt(offset);
        if (elt == null) {
            return file.getLanguage();
        } else {
            if (elt instanceof PsiWhiteSpace) {
                TextRange textRange = elt.getTextRange();
                if (!textRange.contains(offset)) {
                    LOG.error("PSI corrupted: in file " + file + " (" + file.getViewProvider().getVirtualFile() + ") offset=" + offset + " returned element " + elt + " with text range " + textRange);
                }

                int decremented = textRange.getStartOffset() - 1;
                if (decremented >= 0) {
                    return getLanguageAtOffset(file, decremented);
                }
            }

            return findLanguageFromElement(elt);
        }
    }

    public static @NotNull Project getProjectInReadAction(@NotNull PsiElement element) {
        return (Project)ReadAction.compute(() -> element.getProject());
    }

    @Contract("null -> null;!null -> !null")
    public static IElementType getElementType(@Nullable ASTNode node) {
        return node == null ? null : node.getElementType();
    }

    @Contract("null -> null")
    public static IElementType getElementType(@Nullable PsiElement element) {
        return (IElementType)(element == null ? null : (element instanceof StubBasedPsiElement ? ((StubBasedPsiElement)element).getIElementType() : (element instanceof PsiFile ? ((PsiFile)element).getFileElementType() : getElementType(element.getNode()))));
    }

    protected static class NullPsiElement implements PsiElement {
        public @NotNull Project getProject() {
            throw this.createException();
        }

        public @NotNull Language getLanguage() {
            throw this.createException();
        }

        public PsiManager getManager() {
            throw this.createException();
        }

        public PsiElement @NotNull [] getChildren() {
            throw this.createException();
        }

        public PsiElement getParent() {
            throw this.createException();
        }

        public @Nullable PsiElement getFirstChild() {
            throw this.createException();
        }

        public @Nullable PsiElement getLastChild() {
            throw this.createException();
        }

        public @Nullable PsiElement getNextSibling() {
            throw this.createException();
        }

        public @Nullable PsiElement getPrevSibling() {
            throw this.createException();
        }

        public PsiFile getContainingFile() {
            throw this.createException();
        }

        public TextRange getTextRange() {
            throw this.createException();
        }

        public int getStartOffsetInParent() {
            throw this.createException();
        }

        public int getTextLength() {
            throw this.createException();
        }

        public PsiElement findElementAt(int offset) {
            throw this.createException();
        }

        public @Nullable PsiReference findReferenceAt(int offset) {
            throw this.createException();
        }

        public int getTextOffset() {
            throw this.createException();
        }

        public String getText() {
            throw this.createException();
        }

        public char @NotNull [] textToCharArray() {
            throw this.createException();
        }

        public PsiElement getNavigationElement() {
            throw this.createException();
        }

        public PsiElement getOriginalElement() {
            throw this.createException();
        }

        public boolean textMatches(@NotNull CharSequence text) {
            throw this.createException();
        }

        public boolean textMatches(@NotNull PsiElement element) {
            throw this.createException();
        }

        public boolean textContains(char c) {
            throw this.createException();
        }

        public void accept(@NotNull PsiElementVisitor visitor) {
            throw this.createException();
        }

        public void acceptChildren(@NotNull PsiElementVisitor visitor) {
            throw this.createException();
        }

        public PsiElement copy() {
            throw this.createException();
        }

        public PsiElement add(@NotNull PsiElement element) {
            throw this.createException();
        }

        public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) {
            throw this.createException();
        }

        public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) {
            throw this.createException();
        }

        public void checkAdd(@NotNull PsiElement element) {
            throw this.createException();
        }

        public PsiElement addRange(PsiElement first, PsiElement last) {
            throw this.createException();
        }

        public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor) {
            throw this.createException();
        }

        public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) {
            throw this.createException();
        }

        public void delete() {
            throw this.createException();
        }

        public void checkDelete() {
            throw this.createException();
        }

        public void deleteChildRange(PsiElement first, PsiElement last) {
            throw this.createException();
        }

        public PsiElement replace(@NotNull PsiElement newElement) {
            throw this.createException();
        }

        public boolean isValid() {
            throw this.createException();
        }

        public boolean isWritable() {
            throw this.createException();
        }

        PsiInvalidElementAccessException createException() {
            return new PsiInvalidElementAccessException(this, this.toString(), (Throwable)null);
        }

        public @Nullable PsiReference getReference() {
            throw this.createException();
        }

        public PsiReference @NotNull [] getReferences() {
            throw this.createException();
        }

        public <T> T getCopyableUserData(@NotNull Key<T> key) {
            throw this.createException();
        }

        public <T> void putCopyableUserData(@NotNull Key<T> key, T value) {
            throw this.createException();
        }

        public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
            if (place == null) {
                $$$reportNull$$$0(15);
            }

            throw this.createException();
        }

        public PsiElement getContext() {
            throw this.createException();
        }

        public boolean isPhysical() {
            throw this.createException();
        }

        public @NotNull GlobalSearchScope getResolveScope() {
            throw this.createException();
        }

        public @NotNull SearchScope getUseScope() {
            throw this.createException();
        }

        public ASTNode getNode() {
            throw this.createException();
        }

        public <T> T getUserData(@NotNull Key<T> key) {
            throw this.createException();
        }

        public <T> void putUserData(@NotNull Key<T> key, T value) {
            throw this.createException();
        }

        public Icon getIcon(int flags) {
            throw this.createException();
        }

        public boolean isEquivalentTo(PsiElement another) {
            return this == another;
        }

        public String toString() {
            return "NULL_PSI_ELEMENT";
        }
    }

    private static class NullPsiFile extends NullPsiElement implements PsiFile {
        private NullPsiFile() {
        }

        public FileASTNode getNode() {
            throw this.createException();
        }

        public PsiDirectory getParent() {
            throw this.createException();
        }

        public VirtualFile getVirtualFile() {
            throw this.createException();
        }

        public PsiDirectory getContainingDirectory() {
            throw this.createException();
        }

        public long getModificationStamp() {
            throw this.createException();
        }

        public @NotNull PsiFile getOriginalFile() {
            throw this.createException();
        }

        public @NotNull FileType getFileType() {
            throw this.createException();
        }

        public PsiFile @NotNull [] getPsiRoots() {
            throw this.createException();
        }

        public @NotNull FileViewProvider getViewProvider() {
            throw this.createException();
        }

        public void subtreeChanged() {
            throw this.createException();
        }

        public boolean isDirectory() {
            throw this.createException();
        }

        public @NotNull String getName() {
            throw this.createException();
        }

        public boolean processChildren(@NotNull PsiElementProcessor<? super PsiFileSystemItem> processor) {
            throw this.createException();
        }

        public @Nullable ItemPresentation getPresentation() {
            throw this.createException();
        }

        public void navigate(boolean requestFocus) {
            throw this.createException();
        }

        public boolean canNavigate() {
            throw this.createException();
        }

        public boolean canNavigateToSource() {
            throw this.createException();
        }

        public void checkSetName(String name) throws IncorrectOperationException {
            throw this.createException();
        }

        public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
            throw this.createException();
        }

        public String toString() {
            return "NULL_PSI_FILE";
        }
    }
}
