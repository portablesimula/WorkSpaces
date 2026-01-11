//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface CharIterator extends PrimitiveIterator<Character, CharConsumer> {
    char nextChar();

    /** @deprecated */
    @Deprecated
    default Character next() {
        return this.nextChar();
    }

    default void forEachRemaining(CharConsumer action) {
        Objects.requireNonNull(action);

        while(this.hasNext()) {
            action.accept(this.nextChar());
        }

    }

    /** @deprecated */
    @Deprecated
    default void forEachRemaining(Consumer<? super Character> action) {
        CharConsumer var10001;
        if (action instanceof CharConsumer) {
            var10001 = (CharConsumer)action;
        } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
        }

        this.forEachRemaining(var10001);
    }
}
