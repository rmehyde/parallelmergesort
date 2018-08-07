package com.rmehyde.sort;
/** 
* A parallelized implementation of the merge sort algorithm. Special thanks to Brett Bernstein. 
* Used as alternative to java.util.Arrays.parallelSort(...).
* MergeSort utilized on all levels.
* 
* Future ideas:
* 1. Add insertion sort threshold.
* 
* @author Reese Hyde
* @version 1.0
* @since 12 August, 2016
*/

import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ArrayParallelMergeSort {
	
	private static int MAX_THREADS = Runtime.getRuntime().availableProcessors();
	private static ForkJoinPool pool = new ForkJoinPool(MAX_THREADS);
	private static int THRESHOLD = 4096/Math.min(8, MAX_THREADS);
	
	private static class MergeWorker<T> extends RecursiveAction {
		
		private static final long serialVersionUID = 5125713593164694541L;
		private int L, R;
		private T[] arr;
		private Comparator<? super T> comp;
		private Object[] temp;
		
		@SuppressWarnings("unchecked")
		private void merge(int L, int M, int R) {
			int i=L, j=M+1;
			for(int k=L; k<=R; k++) {
				if(i>M)
					temp[k] = arr[j++];
				else if(j>R || comp.compare(arr[i], arr[j])<=0)
					temp[k] = arr[i++];
				else
					temp[k] = arr[j++];
			}
			for(int k=L;k<=R; k++)
				arr[k] = (T)temp[k];
		}
		
		private void mergeSort(int L, int R) {
			if(L==R) return;
			int M = (R+L)/2;
			mergeSort(L, M);
			mergeSort(M+1, R);
			merge(L,M,R);
		}
		
		public void compute() {
			if(R-L <= THRESHOLD) {
				int M = (R+L)/2;
				mergeSort(L, M);
				mergeSort(M+1, R);
				merge(L,M,R);
			}
			else {
				int M = (R+L)/2;
				invokeAll(new MergeWorker<T>(L, M, arr, comp, temp), new MergeWorker<T>(M+1, R, arr, comp, temp));
				merge(L, M, R);
			}
		}
		
		public MergeWorker(int L, int R, T[] arr, Comparator<?super T> comp, Object[] temp) {
			this.L = L;
			this.R = R;
			this.arr = arr;
			this.comp = comp;
			this.temp = temp;
		}
	}
	
	public static <T extends Comparable<? super T>> void sort(T[] arr) {
		Comparator<T> comp = new Comparator<T>() { @Override public int compare(T a, T b) { return a.compareTo(b); }};
		Object[] temp = new Object[arr.length];
		MergeWorker<T> fw = new MergeWorker<T>(0, arr.length-1, arr, comp, temp);
		pool.invoke(fw);
	}
	
	public static <T> void sort(T[] arr, Comparator<?super T> comp) {
		Object[] temp = new Object[arr.length];
		MergeWorker<T> fw = new MergeWorker<T>(0, arr.length-1, arr, comp, temp);
		pool.invoke(fw);
	}	
}
