package com.rmehyde.sort;
import java.util.ArrayList;
import java.util.Comparator;

public class MergeSorter {
	
	private ArrayList<String> data;
	private Comparator<String> comp;
	private String[] temp;
	
	private class MergeWorker implements Runnable {
		
		private int L, R;
		
		private void merge(int L, int M, int R) {
			int i=L, j=M+1;
			for(int k=L; k<=R; k++) {
				if(i>M)
					temp[k] = data.get(j++);
				else if(j>R || comp.compare(data.get(i), data.get(j))<=0)
					temp[k] = data.get(i++);
				else
					temp[k] = data.get(j++);
			}
			for(int k=L;k<=R; k++)
				data.set(k,temp[k]);
		}
		
		private void mergeSort(int L, int R) {
			if(L==R) return;
			int M = (R+L)/2;
			mergeSort(L, M);
			mergeSort(M+1,R);
			merge(L,M,R);
		}
		
		public void run() {
			mergeSort(L,R);
		}
		
		public MergeWorker(int L, int R) {
			this.L = L;
			this.R = R;
		}
	}
	
	
	public void mergeSort() {
		new MergeWorker(0, data.size()-1).run();
	}
	
	public MergeSorter(ArrayList<String> data) {
		this.data = data;
		this.comp = new Comparator<String>() { @Override public int compare(String a, String b) { return a.compareTo(b); }};
		temp = new String[data.size()];
	}
	
	public MergeSorter(ArrayList<String> data, Comparator<String> comp) {
		this.data = data;
		this.comp = comp;
		temp = new String[data.size()];
	}
		
}
