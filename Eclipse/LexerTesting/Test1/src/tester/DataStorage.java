package tester;

import lang.IElementType;

/**
 * A storage for storing data in {@link SegmentArrayWithData}.
 * Encapsulates segment data logic processing.
 */
public interface DataStorage {
  void setData(int segmentIndex, int data);

  void remove(int startIndex, int endIndex, int mySegmentCount);

  void replace(DataStorage storage, int startOffset, int len);

  void insert(DataStorage storageToInsert, int startIndex, int segmentCountToInsert, int segmentCount);

  int getData(int index);

  int packData(IElementType tokenType, int state, boolean isRestartableState);

  int unpackStateFromData(int data);

  
  IElementType unpackTokenFromData(int data);

  
  DataStorage copy();

  
  DataStorage createStorage();
}
