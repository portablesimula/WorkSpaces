//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil;

public interface Stack<K> {
    void push(K var1);

    K pop();

    boolean isEmpty();

    default K top() {
        return (K)this.peek(0);
    }

    default K peek(int i) {
        throw new UnsupportedOperationException();
    }
}
