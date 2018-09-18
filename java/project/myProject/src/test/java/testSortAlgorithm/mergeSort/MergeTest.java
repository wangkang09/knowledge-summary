package testSortAlgorithm.mergeSort;

import org.junit.Test;
import testSortAlgorithm.BaseUtil;

public class MergeTest {
	@Test
	public void mergeTest(){
		int[] a = BaseUtil.generateIntArray(10, 109);
		//Print.out(a);
		long ss = System.currentTimeMillis();
		MergeSort.sort(a);
		System.out.println(System.currentTimeMillis()-ss);
		BaseUtil.isSortedAndShow(a);
	}
}
