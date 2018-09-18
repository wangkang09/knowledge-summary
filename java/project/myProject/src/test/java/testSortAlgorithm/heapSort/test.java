package testSortAlgorithm.heapSort;

import testSortAlgorithm.BaseUtil;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] a = BaseUtil.generateIntArray(12, 30);
		HeapSort.sort(a);
		BaseUtil.isSortedAndShow(a);//a的第一位不算在排序中！！！

	}

}
