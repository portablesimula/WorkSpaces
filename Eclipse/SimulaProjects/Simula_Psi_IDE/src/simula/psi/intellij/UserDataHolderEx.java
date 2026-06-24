package simula.psi.intellij;

public interface UserDataHolderEx {

	<T> T getUserData(Key<T> key);

	<T> void putUserData(Key<T> key, T value);

	<T> boolean replace(Key<T> key, T oldValue, T newValue);

	<T> T putUserDataIfAbsent(Key<T> key, T value);

}
