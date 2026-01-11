/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.lang.impl;

import com.intellij.lang.WhitespacesAndCommentsBinder;
import com.intellij.openapi.util.NlsContexts;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.MutableIntSet;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;
import java.util.Collection;

import static com.intellij.lang.WhitespacesBinders.DEFAULT_LEFT_BINDER;
import static com.intellij.lang.WhitespacesBinders.DEFAULT_RIGHT_BINDER;

final class MarkerOptionalData extends BitSet {
	private final Int2ObjectMap<Throwable> myDebugAllocationPositions = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<@Nls String> myDoneErrors = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<WhitespacesAndCommentsBinder> myLeftBinders = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<WhitespacesAndCommentsBinder> myRightBinders = new Int2ObjectOpenHashMap<>();
//	private final IntSet myCollapsed = new IntOpenHashSet();
	private final IntSet myCollapsed = new MutableIntSet() {

		@Override
		public IntIterator iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean contains(int var1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int[] toIntArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int[] toArray(int[] var1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addAll(IntCollection var1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean containsAll(IntCollection var1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeAll(IntCollection var1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean retainAll(IntCollection var1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends Integer> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends Integer> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean add(int key) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean remove(int key) {
			// TODO Auto-generated method stub
			return false;
		}};

	void clean(int markerId) {
		if (get(markerId)) {
			set(markerId, false);
			myLeftBinders.remove(markerId);
			myRightBinders.remove(markerId);
			myDoneErrors.remove(markerId);
			myCollapsed.remove(markerId);
			myDebugAllocationPositions.remove(markerId);
		}
	}

	@Nullable @NlsContexts.DetailedDescription
	String getDoneError(int markerId) {
		return myDoneErrors.get(markerId);
	}

	boolean isCollapsed(int markerId) {
		return myCollapsed.contains(markerId);
	}

	void setErrorMessage(int markerId, @NotNull @Nls String message) {
		markAsHavingOptionalData(markerId);
		myDoneErrors.put(markerId, message);
	}

	void markCollapsed(int markerId) {
		markAsHavingOptionalData(markerId);
		myCollapsed.add(markerId);
	}

	private void markAsHavingOptionalData(int markerId) {
		set(markerId);
	}

	void notifyAllocated(int markerId) {
		markAsHavingOptionalData(markerId);
		myDebugAllocationPositions.put(markerId, new Throwable("Created at the following trace."));
	}

	Throwable getAllocationTrace(PsiBuilderImpl.StartMarker marker) {
		return myDebugAllocationPositions.get(marker.markerId);
	}

	WhitespacesAndCommentsBinder getBinder(int markerId, boolean right) {
		WhitespacesAndCommentsBinder binder = get(markerId) ? getBinderMap(right).get(markerId) : null;
		return binder != null ? binder : getDefaultBinder(right);
	}

	void assignBinder(int markerId, @NotNull WhitespacesAndCommentsBinder binder, boolean right) {
		Int2ObjectMap<WhitespacesAndCommentsBinder> map = getBinderMap(right);
		if (binder != getDefaultBinder(right)) {
			markAsHavingOptionalData(markerId);
			map.put(markerId, binder);
		}
		else {
			map.remove(markerId);
		}
	}

	private static WhitespacesAndCommentsBinder getDefaultBinder(boolean right) {
		return right ? DEFAULT_RIGHT_BINDER : DEFAULT_LEFT_BINDER;
	}

	private Int2ObjectMap<WhitespacesAndCommentsBinder> getBinderMap(boolean right) {
		return right ? myRightBinders : myLeftBinders;
	}

}