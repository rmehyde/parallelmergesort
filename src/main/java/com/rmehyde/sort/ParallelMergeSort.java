package com.rmehyde.sort;
/** 
* A parallelized implementation of the merge sort algorithm. Special thanks to Brett Bernstein.
* 
* @author Reese Hyde
* @version 0.0.1
* @since 12 August, 2016
*/

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {
	
	private static int MAX_THREADS = Runtime.getRuntime().availableProcessors();
	private static ForkJoinPool pool = new ForkJoinPool(MAX_THREADS);
	private static int THRESHOLD = 4096/Math.min(8, MAX_THREADS);
	
	private static class MergeWorker<T> extends RecursiveAction {
		
		private static final long serialVersionUID = 5125713593164694541L;
		private int L, R;
		private List<T> list;
		private Comparator<? super T> comp;
		private Object[] temp;
		
		@SuppressWarnings("unchecked")
		private void merge(int L, int M, int R) {
			int i=L, j=M+1;
			for(int k=L; k<=R; k++) {
				if(i>M)
					temp[k] = list.get(j++);
				else if(j>R || comp.compare(list.get(i), list.get(j))<=0)
					temp[k] = list.get(i++);
				else
					temp[k] = list.get(j++);
			}
			for(int k=L;k<=R; k++)
				list.set(k,(T)temp[k]);
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
				invokeAll(new MergeWorker<T>(L, M, list, comp, temp), new MergeWorker<T>(M+1, R, list, comp, temp));
				merge(L, M, R);
			}
		}
		
		public MergeWorker(int L, int R, List<T> list, Comparator<?super T> comp, Object[] temp) {
			this.L = L;
			this.R = R;
			this.list = list;
			this.comp = comp;
			this.temp = temp;
		}
	}
	
	public static <T extends Comparable<? super T>> void sort(List<T> list) {
		Comparator<T> comp = new Comparator<T>() { @Override public int compare(T a, T b) { return a.compareTo(b); }};
		Object[] temp = new Object[list.size()];
		MergeWorker<T> fw = new MergeWorker<T>(0, list.size()-1, list, comp, temp);
		pool.invoke(fw);
	}
	
	public static <T> void sort(List<T> list, Comparator<?super T> comp) {
		Object[] temp = new Object[list.size()];
		MergeWorker<T> fw = new MergeWorker<T>(0, list.size()-1, list, comp, temp);
		pool.invoke(fw);
	}	
}
