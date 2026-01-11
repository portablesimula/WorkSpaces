package com.intellij.openapi.editor.ex;

import com.intellij.openapi.editor.event.DocumentListener;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PrioritizedDocumentListener extends DocumentListener {
    Comparator<? super DocumentListener> COMPARATOR = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            return Integer.compare(this.getPriority(o1), this.getPriority(o2));
        }

        private int getPriority(@NotNull Object o) {
            return o instanceof PrioritizedDocumentListener ? ((PrioritizedDocumentListener)o).getPriority() : Integer.MAX_VALUE;
        }
    };

    int getPriority();
}
