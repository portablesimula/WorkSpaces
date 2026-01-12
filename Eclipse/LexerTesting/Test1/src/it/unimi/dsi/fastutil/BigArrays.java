//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import simula.compiler.utilities.LOG;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class BigArrays {
    protected BigArrays() {
    }

    public static int segment(long index) {
        return (int)(index >>> 27);
    }

    public static int displacement(long index) {
        return (int)(index & 134217727L);
    }

    public static long start(int segment) {
        return (long)segment << 27;
    }

    public static void ensureLength(long bigArrayLength) {
        if (bigArrayLength < 0L) {
            throw new IllegalArgumentException("Negative big-array size: " + bigArrayLength);
        } else if (bigArrayLength >= 288230376017494016L) {
            throw new IllegalArgumentException("Big-array size too big: " + bigArrayLength);
        }
    }

    public static void set(int[][] array, long index, int value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static long length(int[][] array) {
        int length = array.length;
        return length == 0 ? 0L : start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(int[][] srcArray, long srcPos, int[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);

            int l;
            for(int destDispl = displacement(destPos); length > 0L; length -= (long)l) {
                l = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }

                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }

                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
            }
        } else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);

            int l;
            for(int destDispl = displacement(destPos + length); length > 0L; length -= (long)l) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }

                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }

                l = (int)Math.min(length, (long)Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }

                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
            }
        }

    }

    public static int[][] forceCapacity(int[][] array, long length, long preserve) {
        ensureLength(length);
        int valid = array.length - (array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728) ? 1 : 0);
        int baseLength = (int)(length + 134217727L >>> 27);
        int[][] base = (int[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 134217727L);
        if (residual != 0) {
            for(int i = valid; i < baseLength - 1; ++i) {
                base[i] = new int[134217728];
            }

            base[baseLength - 1] = new int[residual];
        } else {
            for(int i = valid; i < baseLength; ++i) {
                base[i] = new int[134217728];
            }
        }

        if (preserve - (long)valid * 134217728L > 0L) {
            copy(array, (long)valid * 134217728L, base, (long)valid * 134217728L, preserve - (long)valid * 134217728L);
        }

        return base;
    }

    public static int[][] ensureCapacity(int[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static int[][] grow(int[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static int[][] grow(int[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static int[][] trim(int[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        } else {
            int baseLength = (int)(length + 134217727L >>> 27);
            int[][] base = (int[][])Arrays.copyOf(array, baseLength);
            int residual = (int)(length & 134217727L);
            if (residual != 0) {
                base[baseLength - 1] = IntArrays.trim(base[baseLength - 1], residual);
            }

            return base;
        }
    }

    public static boolean equals(int[][] a1, int[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        } else {
            int i = a1.length;

            while(i-- != 0) {
                int[] t = a1[i];
                int[] u = a2[i];
                int j = t.length;

                while(j-- != 0) {
                    if (t[j] != u[j]) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static <K> void set(K[][] array, long index, K value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static <K> long length(K[][] array) {
        int length = array.length;
        return length == 0 ? 0L : start(length - 1) + (long)array[length - 1].length;
    }

    public static <K> void copy(K[][] srcArray, long srcPos, K[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);

            int l;
            for(int destDispl = displacement(destPos); length > 0L; length -= (long)l) {
                l = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }

                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }

                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
            }
        } else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);

            int l;
            for(int destDispl = displacement(destPos + length); length > 0L; length -= (long)l) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }

                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }

                l = (int)Math.min(length, (long)Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }

                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
            }
        }

    }

    public static <K> K[][] forceCapacity(K[][] array, long length, long preserve) {
        ensureLength(length);
        int valid = array.length - (array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728) ? 1 : 0);
        int baseLength = (int)(length + 134217727L >>> 27);
        K[][] base = (K[][])((Object[][])Arrays.copyOf(array, baseLength));
        Class<?> componentType = array.getClass().getComponentType();
        int residual = (int)(length & 134217727L);
//        if (residual != 0) {
//            for(int i = valid; i < baseLength - 1; ++i) {
//                base[i] = Array.newInstance(componentType.getComponentType(), 134217728);
//            }
//
//            base[baseLength - 1] = Array.newInstance(componentType.getComponentType(), residual);
//        } else {
//            for(int i = valid; i < baseLength; ++i) {
//                base[i] = Array.newInstance(componentType.getComponentType(), 134217728);
//            }
//        }
        LOG.error("NOT IMPL");

        if (preserve - (long)valid * 134217728L > 0L) {
            copy(array, (long)valid * 134217728L, base, (long)valid * 134217728L, preserve - (long)valid * 134217728L);
        }

        return base;
    }

    public static <K> K[][] ensureCapacity(K[][] array, long length, long preserve) {
        return (K[][])(length > length(array) ? forceCapacity(array, length, preserve) : array);
    }

    public static <K> K[][] grow(K[][] array, long length) {
        long oldLength = length(array);
        return (K[][])(length > oldLength ? grow(array, length, oldLength) : array);
    }

    public static <K> K[][] grow(K[][] array, long length, long preserve) {
        long oldLength = length(array);
        return (K[][])(length > oldLength ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array);
    }

    public static <K> K[][] trim(K[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        } else {
            int baseLength = (int)(length + 134217727L >>> 27);
            K[][] base = (K[][])((Object[][])Arrays.copyOf(array, baseLength));
            int residual = (int)(length & 134217727L);
            if (residual != 0) {
                base[baseLength - 1] = ObjectArrays.trim(base[baseLength - 1], residual);
            }

            return base;
        }
    }

    public static <K> boolean equals(K[][] a1, K[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        } else {
            int i = a1.length;

            while(i-- != 0) {
                K[] t = (K[])a1[i];
                K[] u = (K[])a2[i];
                int j = t.length;

                while(j-- != 0) {
                    if (!Objects.equals(t[j], u[j])) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
