package testSortAlgorithm.selectSort;

import org.junit.Test;
import testSortAlgorithm.BaseUtil;

public class TestSelectSort {
	
	@Test
	public void testSimpleSelectSort(){
		int[] a = BaseUtil.generateIntArray(50, 300);
		SimpleSelectSort.sort(a);
		BaseUtil.isSortedAndShow(a);
		
//		double[] b = {2.1,1.5,4.3,2.4,0,-1.4,34.2,3.2,-89};
//		SimpleSelectSort.sort(b);
//		for (double i : b) {
//			System.out.println(i);
//		}
//		
//		char[] c = {'2','1','4','2','0','a','c','3','b'};
//		SimpleSelectSort.sort(c);
//		for (char i : c) {
//			System.out.println(i);
//		}
//		
//		String[] s = {"ad","ddg","dg","zz"};
//		SimpleSelectSort.sort(s);
//		for (String i : s) {
//			System.out.println(i);
//		}
		
	}
	
	@Test
	public void test(){
		int[] a = BaseUtil.generateIntArray(50, 300);
		SimpleSelectSort.sortMinMax(a);
		BaseUtil.isSortedAndShow(a);
	}
}
