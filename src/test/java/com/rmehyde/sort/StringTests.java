package com.rmehyde.sort;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class StringTests {

	private int N = 10000000;
	private ArrayList<String> origList = makeRandomStrings(N);
	private ArrayList<String> petList = new ArrayList<String>(origList);
	private ArrayList<String> builtList = new ArrayList<String>(origList);
	
	
	private ArrayList<String> makeRandomStrings(int N) {
		ArrayList<String> res = new ArrayList<>(N);
		for(int i=0; i<N; i++) {
			int stringLength = (int)(11*Math.random()) + 1;
			StringBuilder builder = new StringBuilder();
			for(int j=0;j<stringLength;j++)
				builder.append((char)(65 + (26*Math.random())));
			res.add(new String(builder));
		}
		return res;
	}
	
	@Test
	public void sortBuiltin() {
		Collections.sort(builtList);
	}
	
	@Test
	public void sortPet() {
		ParallelMergeSort.sort(petList);
	}
	
	
}
