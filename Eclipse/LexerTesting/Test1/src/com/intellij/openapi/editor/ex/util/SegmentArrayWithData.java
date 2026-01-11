package com.intellij.openapi.editor.ex.util;

import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;

public class SegmentArrayWithData extends SegmentArray {
    private DataStorage myStorage;

    public SegmentArrayWithData(@NotNull DataStorage storage) {
        this.myStorage = storage;
    }

    public @NotNull DataStorage createStorage() {
        return this.myStorage.createStorage();
    }

    public void setElementAt(int i, int startOffset, int endOffset, int data) {
        this.setElementAt(i, startOffset, endOffset);
        this.myStorage.setData(i, data);
    }

    public void remove(int startIndex, int endIndex) {
        this.myStorage.remove(startIndex, endIndex, this.mySegmentCount);
        super.remove(startIndex, endIndex);
    }

    public void replace(int startIndex, int endIndex, @NotNull SegmentArrayWithData newData) {
        int oldLen = endIndex - startIndex;
        int newLen = newData.getSegmentCount();
        int delta = newLen - oldLen;
        if (delta < 0) {
            this.remove(endIndex + delta, endIndex);
        } else if (delta > 0) {
            SegmentArrayWithData deltaData = new SegmentArrayWithData(this.myStorage.createStorage());

            for(int i = oldLen; i < newLen; ++i) {
                deltaData.setElementAt(i - oldLen, newData.getSegmentStart(i), newData.getSegmentEnd(i), newData.getSegmentData(i));
            }

            this.insert(deltaData, startIndex + oldLen);
        }

        int common = Math.min(newLen, oldLen);
        this.replace(startIndex, newData, common);
    }

    protected void replace(int startOffset, @NotNull SegmentArrayWithData data, int len) {
        this.myStorage.replace(data.myStorage, startOffset, len);
        super.replace(startOffset, data, len);
    }

    public void insert(@NotNull SegmentArrayWithData segmentArray, int startIndex) {
        this.myStorage.insert(segmentArray.myStorage, startIndex, segmentArray.getSegmentCount(), this.mySegmentCount);
        super.insert(segmentArray, startIndex);
    }

    public int getSegmentData(int index) {
        if (index >= 0 && index < this.mySegmentCount) {
            return this.myStorage.getData(index);
        } else {
            throw new IndexOutOfBoundsException("Wrong index: " + index);
        }
    }

    static int @NotNull [] reallocateArray(int @NotNull [] array, int index) {
        if (array == null) {
            $$$reportNull$$$0(5);
        }

        if (index < array.length) {
            if (array == null) {
                $$$reportNull$$$0(6);
            }

            return array;
        } else {
            int[] var10000 = ArrayUtil.realloc(array, calcCapacity(array.length, index));
            if (var10000 == null) {
                $$$reportNull$$$0(7);
            }

            return var10000;
        }
    }

    public @NotNull SegmentArrayWithData copy() {
        SegmentArrayWithData sa = new SegmentArrayWithData(this.createStorage());
        sa.mySegmentCount = this.mySegmentCount;
        sa.myStarts = (int[])this.myStarts.clone();
        sa.myEnds = (int[])this.myEnds.clone();
        sa.myStorage = this.myStorage.copy();
        return sa;
    }

    public int unpackStateFromData(int data) {
        return this.myStorage.unpackStateFromData(data);
    }

    public @NotNull IElementType unpackTokenFromData(int data) {
        return this.myStorage.unpackTokenFromData(data);
    }

    public int packData(@NotNull IElementType tokenType, int state, boolean isRestartableState) {
        return this.myStorage.packData(tokenType, state, isRestartableState);
    }
}
