package com.intellij.psi.search;

import com.intellij.notebook.editor.BackedVirtualFile;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.UnloadedModuleDescription;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

public abstract class GlobalSearchScope extends SearchScope implements ProjectAwareFileFilter {
    public static final GlobalSearchScope[] EMPTY_ARRAY = new GlobalSearchScope[0];
    private final Project myProject;
    private static final Key<Boolean> USE_WEAK_FILE_SCOPE = Key.create("virtual.file.use.weak.scope");
    public static final GlobalSearchScope EMPTY_SCOPE = new EmptyScope();

    protected GlobalSearchScope(@Nullable Project project) {
        this.myProject = project;
    }

    protected GlobalSearchScope() {
        this((Project)null);
    }

    @NonExtendable
    public Project getProject() {
        return this.myProject;
    }

    public int compare(@NotNull VirtualFile file1, @NotNull VirtualFile file2) {
        return 0;
    }

    public abstract boolean isSearchInModuleContent(@NotNull Module var1);

    public boolean isSearchInModuleContent(@NotNull Module aModule, boolean testSources) {
        return this.isSearchInModuleContent(aModule);
    }

    public final boolean accept(@NotNull VirtualFile file) {
        return this.contains(file);
    }

    public abstract boolean isSearchInLibraries();

    public boolean isForceSearchingInLibrarySources() {
        return false;
    }

    public @Unmodifiable @NotNull Collection<UnloadedModuleDescription> getUnloadedModulesBelongingToScope() {
        return Collections.emptySet();
    }

    @Contract(
        pure = true
    )
    public @NotNull GlobalSearchScope intersectWith(@NotNull GlobalSearchScope scope) {
        return this.defaultIntersectWith(scope);
    }

    @Internal
    @Contract(
        pure = true
    )
    protected final @NotNull GlobalSearchScope defaultIntersectWith(@NotNull GlobalSearchScope scope) {
        if (scope == this) {
            return this;
        } else {
            return (GlobalSearchScope)(scope instanceof IntersectionScope && ((IntersectionScope)scope).containsScope(this) ? scope : new IntersectionScope(this, scope));
        }
    }

    @Contract(
        pure = true
    )
    public @NotNull SearchScope intersectWith(@NotNull SearchScope scope2) {
        return (SearchScope)(scope2 instanceof LocalSearchScope ? this.intersectWith((LocalSearchScope)scope2) : this.intersectWith((GlobalSearchScope)scope2));
    }

    @Contract(
        pure = true
    )
    public @NotNull LocalSearchScope intersectWith(@NotNull LocalSearchScope localScope2) {
        PsiElement[] elements2 = localScope2.getScope();
        List<PsiElement> result = new ArrayList(elements2.length);

        for(PsiElement element2 : elements2) {
            if (PsiSearchScopeUtil.isInScope(this, element2)) {
                result.add(element2);
            }
        }

        return result.isEmpty() ? LocalSearchScope.EMPTY : new LocalSearchScope((PsiElement[])result.toArray(PsiElement.EMPTY_ARRAY), (String)null, localScope2.isIgnoreInjectedPsi());
    }

    @Contract(
        pure = true
    )
    public @NotNull GlobalSearchScope union(@NotNull SearchScope scope) {
        return scope instanceof GlobalSearchScope ? this.uniteWith((GlobalSearchScope)scope) : this.union((LocalSearchScope)scope);
    }

    @Contract(
        pure = true
    )
    public @NotNull GlobalSearchScope union(@NotNull LocalSearchScope scope) {
        PsiElement[] localScopeElements = scope.getScope();
        return (GlobalSearchScope)(localScopeElements.length == 0 ? this : new GlobalAndLocalUnionScope(this, scope, localScopeElements[0].getProject()));
    }

    @Contract(
        pure = true
    )
    public @NotNull GlobalSearchScope uniteWith(@NotNull GlobalSearchScope scope) {
        return UnionScope.create(new GlobalSearchScope[]{this, scope});
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope union(@NotNull Collection<? extends GlobalSearchScope> scopes) {
        if (scopes.isEmpty()) {
            throw new IllegalArgumentException("Empty scope collection");
        } else {
            return scopes.size() == 1 ? (GlobalSearchScope)scopes.iterator().next() : UnionScope.create((GlobalSearchScope[])scopes.toArray(EMPTY_ARRAY));
        }
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope union(GlobalSearchScope @NotNull [] scopes) {
        if (scopes == null) {
            $$$reportNull$$$0(23);
        }

        if (scopes.length == 0) {
            throw new IllegalArgumentException("Empty scope array");
        } else if (scopes.length == 1) {
            GlobalSearchScope var1 = scopes[0];
            if (scopes[0] == null) {
                $$$reportNull$$$0(24);
            }

            return var1;
        } else {
            return UnionScope.create(scopes);
        }
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope allScope(@NotNull Project project) {
        return ProjectScope.getAllScope(project);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope projectScope(@NotNull Project project) {
        return ProjectScope.getProjectScope(project);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope everythingScope(@NotNull Project project) {
        return ProjectScope.getEverythingScope(project);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope notScope(@NotNull GlobalSearchScope scope) {
        return new NotScope(scope);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleScope(@NotNull Module module) {
        return module.getModuleScope();
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleWithLibrariesScope(@NotNull Module module) {
        return module.getModuleWithLibrariesScope();
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleWithDependenciesScope(@NotNull Module module) {
        return module.getModuleWithDependenciesScope();
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleRuntimeScope(@NotNull Module module, boolean includeTests) {
        return module.getModuleRuntimeScope(includeTests);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleWithDependenciesAndLibrariesScope(@NotNull Module module) {
        return moduleWithDependenciesAndLibrariesScope(module, true);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleWithDependenciesAndLibrariesScope(@NotNull Module module, boolean includeTests) {
        return module.getModuleWithDependenciesAndLibrariesScope(includeTests);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleWithDependentsScope(@NotNull Module module) {
        return module.getModuleWithDependentsScope();
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope moduleTestsWithDependentsScope(@NotNull Module module) {
        return module.getModuleTestsWithDependentsScope();
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope fileScope(@NotNull PsiFile psiFile) {
        VirtualFile virtualFile = psiFile.getVirtualFile();
        return new FileScope(psiFile.getProject(), virtualFile != null ? BackedVirtualFile.getOriginFileIfBacked(virtualFile) : null, (String)null);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope fileScope(@NotNull Project project, @Nullable VirtualFile virtualFile) {
        return fileScope(project, virtualFile, (String)null);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope fileScope(@NotNull Project project, @Nullable VirtualFile virtualFile, @Nullable @Nls String displayName) {
        return (GlobalSearchScope)(virtualFile != null && virtualFile.getUserData(USE_WEAK_FILE_SCOPE) == Boolean.TRUE ? new FileWeakScope(project, virtualFile, displayName) : new FileScope(project, virtualFile, displayName));
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope filesScope(@NotNull Project project, @NotNull Collection<? extends VirtualFile> files) {
        return filesScope(project, files, (String)null);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope filesScope(@NotNull Project project, @NotNull Supplier<? extends Collection<? extends VirtualFile>> files) {
        return new LazyFilesScope(project, files);
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope filesWithoutLibrariesScope(@NotNull Project project, @NotNull Collection<? extends VirtualFile> files) {
        return (GlobalSearchScope)(files.isEmpty() ? EMPTY_SCOPE : new FilesScope(project, files, false));
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope filesWithLibrariesScope(@NotNull Project project, @NotNull Collection<? extends VirtualFile> files) {
        return (GlobalSearchScope)(files.isEmpty() ? EMPTY_SCOPE : new FilesScope(project, files, true));
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope filesScope(@NotNull Project project, @NotNull Collection<? extends VirtualFile> files, @Nullable @Nls String displayName) {
        if (files.isEmpty()) {
            return EMPTY_SCOPE;
        } else if (files.size() == 1) {
            return fileScope(project, (VirtualFile)files.iterator().next(), displayName);
        } else {
            return (GlobalSearchScope)(displayName != null ? new FilesScopeWithDisplayName(project, files, displayName) : new FilesScope(project, files, (Boolean)null));
        }
    }

    @Contract(
        pure = true
    )
    public static @NotNull GlobalSearchScope getScopeRestrictedByFileTypes(@NotNull GlobalSearchScope scope, @NotNull FileType... fileTypes) {
        if (fileTypes == null) {
            $$$reportNull$$$0(65);
        }

        if (scope == EMPTY_SCOPE) {
            return EMPTY_SCOPE;
        } else if (fileTypes.length == 0) {
            throw new IllegalArgumentException("empty fileTypes");
        } else {
            return new FileTypeRestrictionScope(scope, fileTypes);
        }
    }

    @Internal
    public static void markFileForWeakScope(@NotNull VirtualFile file) {
        file.putUserData(USE_WEAK_FILE_SCOPE, Boolean.TRUE);
    }

    private static class EmptyScope extends GlobalSearchScope {
        private EmptyScope() {
        }

        public boolean contains(@NotNull VirtualFile file) {
            return false;
        }

        public boolean isSearchInModuleContent(@NotNull Module aModule) {
            return false;
        }

        public boolean isSearchInLibraries() {
            return false;
        }

        public @NotNull GlobalSearchScope intersectWith(@NotNull GlobalSearchScope scope) {
            return this;
        }

        public @NotNull GlobalSearchScope uniteWith(@NotNull GlobalSearchScope scope) {
            return scope;
        }

        public String toString() {
            return "EMPTY";
        }
    }

    public static class FilesScope extends AbstractFilesScope {
        private final VirtualFileSet myFiles;

        @Internal
        FilesScope(@Nullable Project project, @NotNull Collection<? extends VirtualFile> files, @Nullable Boolean hasFilesOutOfProjectRoots) {
            super(project, hasFilesOutOfProjectRoots);
            this.myFiles = VfsUtilCore.createCompactVirtualFileSet(files);
            this.myFiles.freeze();
        }

        public @NotNull VirtualFileSet getFiles() {
            return this.myFiles;
        }

        public String toString() {
            return "Files: [" + StringUtil.join(this.myFiles, ", ") + "]; search in libraries: " + (this.myHasFilesOutOfProjectRoots != null ? this.myHasFilesOutOfProjectRoots : "unknown");
        }
    }
}
