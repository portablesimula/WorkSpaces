//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

public final class IntBigArrays {
    public static final int[][] EMPTY_BIG_ARRAY = new int[0][];
    public static final int[][] DEFAULT_EMPTY_BIG_ARRAY = new int[0][];
    public static final AtomicIntegerArray[] EMPTY_BIG_ATOMIC_ARRAY = new AtomicIntegerArray[0];
    public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy();

    public static int[][] newBigArray(long length) {
        if (length == 0L) {
            return EMPTY_BIG_ARRAY;
        } else {
            BigArrays.ensureLength(length);
            int baseLength = (int)(length + 134217727L >>> 27);
            int[][] base = new int[baseLength][];
            int residual = (int)(length & 134217727L);
            if (residual != 0) {
                for(int i = 0; i < baseLength - 1; ++i) {
                    base[i] = new int[134217728];
                }

                base[baseLength - 1] = new int[residual];
            } else {
                for(int i = 0; i < baseLength; ++i) {
                    base[i] = new int[134217728];
                }
            }

            return base;
        }
    }

    /** @deprecated */
    @Deprecated
    public static boolean equals(int[][] a1, int[][] a2) {
        return BigArrays.equals(a1, a2);
    }

    private static final class BigArrayHashStrategy implements Hash.Strategy<int[][]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private BigArrayHashStrategy() {
        }

        public int hashCode(int[][] o) {
            return Arrays.deepHashCode(o);
        }

        public boolean equals(int[][] a, int[][] b) {
            return IntBigArrays.equals(a, b);
        }
    }
}
