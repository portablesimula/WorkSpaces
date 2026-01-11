package com.intellij.psi.search;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiUtilCore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiElementProcessor<T extends PsiElement> {
    boolean execute(@NotNull T var1);

    public static class CollectElements<T extends PsiElement> implements PsiElementProcessor<T> {
        private final Collection<T> myCollection;

        public CollectElements() {
            this(new ArrayList());
        }

        public CollectElements(@NotNull Collection<T> collection) {
            this.myCollection = Collections.synchronizedCollection(collection);
        }

        public PsiElement @NotNull [] toArray() {
            PsiElement[] var10000 = PsiUtilCore.toPsiElementArray(this.myCollection);
            if (var10000 == null) {
                $$$reportNull$$$0(1);
            }

            return var10000;
        }

        public @NotNull Collection<T> getCollection() {
            return this.myCollection;
        }

        public T @NotNull [] toArray(T[] array) {
            PsiElement[] var10000 = (PsiElement[])this.myCollection.toArray(array);
            if (var10000 == null) {
                $$$reportNull$$$0(3);
            }

            return (T[])var10000;
        }

        public boolean execute(@NotNull T element) {
            this.myCollection.add(element);
            return true;
        }
    }

    /** @deprecated */
    @Deprecated
    public static class CollectFilteredElements<T extends PsiElement> extends CollectElements<T> {
        private final PsiElementFilter myFilter;

        public CollectFilteredElements(@NotNull PsiElementFilter filter, @NotNull Collection<T> collection) {
            super(collection);
            this.myFilter = filter;
        }

        public CollectFilteredElements(@NotNull PsiElementFilter filter) {
            this.myFilter = filter;
        }

        public boolean execute(@NotNull T element) {
            return !this.myFilter.isAccepted(element) || super.execute(element);
        }
    }

    public static class CollectElementsWithLimit<T extends PsiElement> extends CollectElements<T> {
        private final AtomicInteger myCount = new AtomicInteger(0);
        private volatile boolean myOverflow;
        private final int myLimit;

        public CollectElementsWithLimit(int limit) {
            this.myLimit = limit;
        }

        public CollectElementsWithLimit(int limit, @NotNull Collection<T> collection) {
            super(collection);
            this.myLimit = limit;
        }

        public boolean execute(@NotNull T element) {
            if (this.myCount.get() == this.myLimit) {
                this.myOverflow = true;
                return false;
            } else {
                this.myCount.incrementAndGet();
                return super.execute(element);
            }
        }

        public boolean isOverflow() {
            return this.myOverflow;
        }
    }

    public static class FindElement<T extends PsiElement> implements PsiElementProcessor<T> {
        private volatile T myFoundElement;

        public boolean isFound() {
            return this.myFoundElement != null;
        }

        public @Nullable T getFoundElement() {
            return this.myFoundElement;
        }

        public boolean setFound(T element) {
            this.myFoundElement = element;
            return false;
        }

        public boolean execute(@NotNull T element) {
            return this.setFound(element);
        }
    }
}
