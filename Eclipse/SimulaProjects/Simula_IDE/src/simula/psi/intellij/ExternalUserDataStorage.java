package simula.psi.intellij;

public class ExternalUserDataStorage {

	public <T> T getUserData(UserDataHolderBase userDataHolderBase, Key<T> key) {
		// TODO Auto-generated method stub
		return null;
	}

	public KeyFMap getUserMap(UserDataHolderBase userDataHolderBase) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> void putUserData(UserDataHolderBase userDataHolderBase, Key<T> key, T value) {
		// TODO Auto-generated method stub
		
	}

	public boolean compareAndPutUserData(UserDataHolderBase userDataHolderBase, Key<KeyFMap> copyableUserMapKey,
			KeyFMap oldCopyableMap, KeyFMap newCopyableMap) {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> boolean compareAndPutUserData(UserDataHolderBase userDataHolderBase, Key<T> key, T oldValue,
			T newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T putUserDataIfAbsent(UserDataHolderBase userDataHolderBase, Key<T> key, T value) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUserMap(UserDataHolderBase userDataHolderBase, KeyFMap map) {
		// TODO Auto-generated method stub
		
	}

}
