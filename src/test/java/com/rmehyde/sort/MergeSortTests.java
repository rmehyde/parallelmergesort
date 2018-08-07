package com.rmehyde.sort;
import static org.junit.Assert.assertArrayEquals;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MergeSortTests {
	
	private int N = 10000000;
	private float[] origArr= makeRandomFloats(N);
	private Float[] petArr = ArrayUtils.toObject(Arrays.copyOf(origArr, origArr.length));
	private Float[] builtArr = Arrays.copyOf(petArr, N);
	
	private float[] makeRandomFloats(int N) {
		float[] res = new float[N];
		java.util.Random rng = new java.util.Random();
		for(int i=0;i<N;i++) {
			res[i] = rng.nextFloat();
		}
		return res;
	}
	
	@Test
	public void SortBuiltin() {
		java.util.Arrays.parallelSort(builtArr);
	}
	
	@Test
	public void SortPet() {
		ArrayParallelMergeSort.sort(petArr);
	}
	
	@Test
	public void ZCheckCorrectness() {
		java.util.Arrays.parallelSort(builtArr);
		ArrayParallelMergeSort.sort(petArr);
		assertArrayEquals(builtArr, petArr);
	}
	
}
