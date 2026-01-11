//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package it.unimi.dsi.fastutil;

public final class SafeMath {
    public static char safeIntToChar(int value) {
        if (value >= 0 && 65535 >= value) {
            return (char)value;
        } else {
            throw new IllegalArgumentException(value + " can't be represented as char");
        }
    }

    public static byte safeIntToByte(int value) {
        if (value >= -128 && 127 >= value) {
            return (byte)value;
        } else {
            throw new IllegalArgumentException(value + " can't be represented as byte (out of range)");
        }
    }

    public static short safeIntToShort(int value) {
        if (value >= -32768 && 32767 >= value) {
            return (short)value;
        } else {
            throw new IllegalArgumentException(value + " can't be represented as short (out of range)");
        }
    }

    public static float safeDoubleToFloat(double value) {
        if (Double.isNaN(value)) {
            return Float.NaN;
        } else if (Double.isInfinite(value)) {
            return value < (double)0.0F ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        } else if (!(value < (double)-Float.MAX_VALUE) && !((double)Float.MAX_VALUE < value)) {
            float floatValue = (float)value;
            if ((double)floatValue != value) {
                throw new IllegalArgumentException(value + " can't be represented as float (imprecise)");
            } else {
                return floatValue;
            }
        } else {
            throw new IllegalArgumentException(value + " can't be represented as float (out of range)");
        }
    }
}
