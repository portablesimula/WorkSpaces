//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.bytes;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {
    byte nextByte();

    /** @deprecated */
    @Deprecated
    default Byte next() {
        return this.nextByte();
    }

    default void forEachRemaining(ByteConsumer action) {
        Objects.requireNonNull(action);

        while(this.hasNext()) {
            action.accept(this.nextByte());
        }

    }

    /** @deprecated */
    @Deprecated
    default void forEachRemaining(Consumer<? super Byte> action) {
        ByteConsumer var10001;
        if (action instanceof ByteConsumer) {
            var10001 = (ByteConsumer)action;
        } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
        }

        this.forEachRemaining(var10001);
    }
}
