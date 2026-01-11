//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.shorts;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ShortIterator extends PrimitiveIterator<Short, ShortConsumer> {
    short nextShort();

    /** @deprecated */
    @Deprecated
    default Short next() {
        return this.nextShort();
    }

    default void forEachRemaining(ShortConsumer action) {
        Objects.requireNonNull(action);

        while(this.hasNext()) {
            action.accept(this.nextShort());
        }

    }

    /** @deprecated */
    @Deprecated
    default void forEachRemaining(Consumer<? super Short> action) {
        ShortConsumer var10001;
        if (action instanceof ShortConsumer) {
            var10001 = (ShortConsumer)action;
        } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
        }

        this.forEachRemaining(var10001);
    }
}
