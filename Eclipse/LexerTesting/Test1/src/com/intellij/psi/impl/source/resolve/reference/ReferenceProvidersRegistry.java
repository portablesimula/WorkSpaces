package com.intellij.psi.impl.source.resolve.reference;

import com.intellij.codeInsight.highlighting.PassRunningAssert;
import com.intellij.lang.Language;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.PsiReferenceService;
import com.intellij.psi.PsiReferenceService.Hints;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.CachedValueProvider.Result;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ReferenceProvidersRegistry {
    private static final PassRunningAssert CONTRIBUTING_REFERENCES = new PassRunningAssert("the expensive method should not be called during the references contributing");
    public static final PsiReferenceProvider NULL_REFERENCE_PROVIDER = new PsiReferenceProvider() {
        public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
            PsiReference[] var10000 = PsiReference.EMPTY_ARRAY;
            if (var10000 == null) {
                $$$reportNull$$$0(2);
            }

            return var10000;
        }
    };

    public static ReferenceProvidersRegistry getInstance() {
        return (ReferenceProvidersRegistry)ApplicationManager.getApplication().getService(ReferenceProvidersRegistry.class);
    }

    public abstract @NotNull PsiReferenceRegistrar getRegistrar(@NotNull Language var1);

    public static PsiReference @NotNull [] getReferencesFromProviders(@NotNull PsiElement context) {
        return getReferencesFromProviders(context, Hints.NO_HINTS);
    }

    public static PsiReference @NotNull [] getReferencesFromProviders(@NotNull PsiElement context, PsiReferenceService.@NotNull Hints hints) {
        ProgressIndicatorProvider.checkCanceled();
        if (hints == Hints.NO_HINTS) {
            PsiReference[] var10000 = (PsiReference[])((PsiReference[])CachedValuesManager.getCachedValue(context, () -> {
                AccessToken ignored = CONTRIBUTING_REFERENCES.runPass();

                CachedValueProvider.Result var2;
                try {
                    var2 = Result.create(getInstance().doGetReferencesFromProviders(context, Hints.NO_HINTS), new Object[]{PsiModificationTracker.MODIFICATION_COUNT});
                } catch (Throwable var5) {
                    if (ignored != null) {
                        try {
                            ignored.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }

                    throw var5;
                }

                if (ignored != null) {
                    ignored.close();
                }

                return var2;
            })).clone();
            if (var10000 == null) {
                $$$reportNull$$$0(3);
            }

            return var10000;
        } else {
            AccessToken ignored = CONTRIBUTING_REFERENCES.runPass();

            PsiReference[] var3;
            try {
                var3 = getInstance().doGetReferencesFromProviders(context, hints);
            } catch (Throwable var6) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (ignored != null) {
                ignored.close();
            }

            if (var3 == null) {
                $$$reportNull$$$0(4);
            }

            return var3;
        }
    }

    public static void assertNotContributingReferences() {
        CONTRIBUTING_REFERENCES.assertPassNotRunning();
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public static AccessToken suppressAssertNotContributingReferences() {
        return CONTRIBUTING_REFERENCES.suppressAssertInPass();
    }

    @Internal
    public abstract void unloadProvidersFor(@NotNull Language var1);

    protected abstract PsiReference @NotNull [] doGetReferencesFromProviders(@NotNull PsiElement var1, PsiReferenceService.@NotNull Hints var2);
}
