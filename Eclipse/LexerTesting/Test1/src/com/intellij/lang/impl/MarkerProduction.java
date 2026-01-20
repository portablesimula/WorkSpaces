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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.ObjectUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import simula.compiler.utilities.LOG;
import simula.compiler.utilities.Util;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//final class MarkerProduction extends IntArrayList {
final class MarkerProduction extends ArrayList<Integer> {
//	private static final Logger LOG = Logger.getInstance(MarkerProduction.class);
	private static final int LINEAR_SEARCH_LIMIT = 20;
	private final MarkerPool myPool;
	private final MarkerOptionalData myOptionalData;

	MarkerProduction(MarkerPool pool, MarkerOptionalData optionalData) {
		super(256);
		myPool = pool;
		myOptionalData = optionalData;
	}
	
	public void debugPrint(String title) { // TODO: AD'HOC
		System.out.println("=== "+ title +" ===");
		for(Integer obj:this) {
			System.out.println(""+obj.getClass().getSimpleName()+" "+obj);
		}
	}

	public int[] elements() {  // TODO: AD'HOC
		int[] elts = new int[this.size()];
		int i = 0;
		for(Integer val:this) elts[i++] = val;
		// TODO Auto-generated method stub
		return elts;
	}
	
	public int getInt(int index) {  // TODO: AD'HOC
		return this.get(index);
	}

	void addBefore(PsiBuilderImpl.ProductionMarker marker, PsiBuilderImpl.ProductionMarker anchor) {
		System.out.println("MarkerProduction.addBefore: " + marker.markerId);
		add(indexOf(anchor), marker.markerId);
		System.out.println("MarkerProduction.addBefore: " + this);
	}

	private int indexOf(PsiBuilderImpl.ProductionMarker marker) {
		int idx = findLinearly(marker.markerId);
		if (idx < 0) {
			for (int i = findMarkerAtLexeme(marker.getLexemeIndex(false)); i < size(); i++) {
//				if (a[i] == marker.markerId) {
				if (i >= 0 && this.get(i) == marker.markerId) {
					idx = i;
					break;
				}
			}
		}
		if (idx < 0) {
			LOG.error("Dropped or rolled-back marker");
			Util.IERR();
		}
		return idx;
	}

	private int findLinearly(int markerId) {
		int low = Math.max(0, size() - LINEAR_SEARCH_LIMIT);
		for (int i = size() - 1; i >= low; i--) {
			if (this.get(i) == markerId) {
				return i;
			}
		}
		return -1;
	}

	private int findMarkerAtLexeme(int lexemeIndex) {
		int i = ObjectUtils.binarySearch(0, size() - LINEAR_SEARCH_LIMIT, mid -> Integer.compare(getLexemeIndexAt(mid), lexemeIndex));
		return i < 0 ? -1 : findSameLexemeGroupStart(lexemeIndex, i);
	}

	private int findSameLexemeGroupStart(int lexemeIndex, int prodIndex) {
		while (prodIndex > 0 && getLexemeIndexAt(prodIndex - 1) == lexemeIndex) prodIndex--;
		return prodIndex;
	}

	void addMarker(PsiBuilderImpl.ProductionMarker marker) {
		add(marker.markerId);
		System.out.println("MarkerProduction.addMarker: marker="+marker);
		System.out.println("MarkerProduction.addMarker: marker.markerId="+marker.markerId);
//		Util.IERR();
	}

	void rollbackTo(PsiBuilderImpl.ProductionMarker marker) {
		int idx = indexOf(marker);
		for (int i = size() - 1; i >= idx; i--) {
			int markerId = a[i];
			if (markerId > 0) {
				myPool.freeMarker(myPool.get(markerId));
			}
		}
		removeElements(idx, size());
	}

	boolean hasErrorsAfter(@NotNull PsiBuilderImpl.StartMarker marker) {
		for (int i = indexOf(marker) + 1; i < size(); ++i) {
			PsiBuilderImpl.ProductionMarker m = getStartMarkerAt(i);
			if (m != null && hasError(m)) return true;
		}
		return false;
	}

	private boolean hasError(PsiBuilderImpl.ProductionMarker marker) {
		return marker instanceof PsiBuilderImpl.ErrorItem || myOptionalData.getDoneError(marker.markerId) != null;
	}

	void dropMarker(@NotNull PsiBuilderImpl.StartMarker marker) {
		if (marker.isDone()) {
			removeInt(lastIndexOf(-marker.markerId));
		}
		removeInt(indexOf(marker));
		myPool.freeMarker(marker);
	}

	void addDone(PsiBuilderImpl.StartMarker marker, @Nullable PsiBuilderImpl.ProductionMarker anchorBefore) {
		add(anchorBefore == null ? size() : indexOf(anchorBefore), -marker.markerId);
//		int xxx = anchorBefore == null ? size() : indexOf(anchorBefore);
//		System.out.println("MarkerProduction.addDone: add "+xxx+", "+(-marker.markerId)+"   "+this);
//		add(xxx, -marker.markerId);
//		Util.IERR();
	}

	@Nullable
	PsiBuilderImpl.ProductionMarker getMarkerAt(int index) {
		int id = getInt(index);
		return myPool.get(id > 0 ? id : -id);
	}

	@Nullable
	PsiBuilderImpl.ProductionMarker getStartMarkerAt(int index) {
		int id = getInt(index);
		return id > 0 ? myPool.get(id) : null;
	}

	@Nullable
	PsiBuilderImpl.StartMarker getDoneMarkerAt(int index) {
		int id = getInt(index);
		return id < 0 ? (PsiBuilderImpl.StartMarker)myPool.get(-id) : null;
	}

	int getLexemeIndexAt(int productionIndex) {
		int id = getInt(productionIndex);
		return myPool.get(Math.abs(id)).getLexemeIndex(id < 0);
	}

	void confineMarkersToMaxLexeme(int markersBefore, int lexemeIndex) {
		for (int k = markersBefore - 1; k > 1; k--) {
			int id = a[k];
			PsiBuilderImpl.ProductionMarker marker = myPool.get(Math.abs(id));
			boolean done = id < 0;
			if (marker.getLexemeIndex(done) < lexemeIndex) break;

			marker.setLexemeIndex(lexemeIndex, done);
		}
	}

	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	void doHeavyChecksOnMarkerDone(@NotNull PsiBuilderImpl.StartMarker doneMarker, @Nullable PsiBuilderImpl.StartMarker anchorBefore) {
		System.out.println("MarkerProduction.doHeavyChecksOnMarkerDone: "+doneMarker);
//		Util.IERR();
		int idx = indexOf(doneMarker);
		System.out.println("MarkerProduction.doHeavyChecksOnMarkerDone: idx="+idx);
		Util.IERR();

		int endIdx = size();
		if (anchorBefore != null) {
			endIdx = indexOf(anchorBefore);
			if (idx > endIdx) {
				LOG.error("'Before' marker precedes this one.");
			}
		}

		for (int i = endIdx - 1; i > idx; i--) {
			PsiBuilderImpl.ProductionMarker item = getStartMarkerAt(i);
			if (item instanceof PsiBuilderImpl.StartMarker) {
				PsiBuilderImpl.StartMarker otherMarker = (PsiBuilderImpl.StartMarker)item;
				if (!otherMarker.isDone()) {
					Throwable debugAllocThis = myOptionalData.getAllocationTrace(doneMarker);
					Throwable currentTrace = new Throwable();
					if (debugAllocThis != null) {
						ExceptionUtil.makeStackTraceRelative(debugAllocThis, currentTrace).printStackTrace(System.err);
					}
					Throwable debugAllocOther = myOptionalData.getAllocationTrace(otherMarker);
					if (debugAllocOther != null) {
						ExceptionUtil.makeStackTraceRelative(debugAllocOther, currentTrace).printStackTrace(System.err);
					}
					LOG.error("Another not done marker added after this one. Must be done before this.");
				}
			}
		}
	}

	void assertNoDoneMarkerAround(@NotNull PsiBuilderImpl.StartMarker pivot) {
		int pivotIndex = indexOf(pivot);
		for (int i = pivotIndex + 1; i < size(); i++) {
			PsiBuilderImpl.StartMarker m = getDoneMarkerAt(i);
			if (m != null && m.myLexemeIndex <= pivot.myLexemeIndex && indexOf(m) < pivotIndex) {
				throw new AssertionError("There's a marker of type '" + m.getTokenType() + "' that starts before and finishes after the current marker. See cause for its allocation trace.", myOptionalData.getAllocationTrace(m));
			}
		}
	}

}