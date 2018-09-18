package testSortAlgorithm.insertSort;

import org.junit.Test;
import testSortAlgorithm.BaseUtil;
import testSortAlgorithm.RefUtil;

import java.util.Arrays;
import java.util.List;

public class TestInsertSort {

	@Test
	public void TestStraightInsertionSort(){
		
		List<Integer> aInt = Arrays.asList(1,3,3,2,6,5,11,3,56,35,32);
		StraightInsertionSort.sort(aInt);
		System.out.println(aInt.toString());//�б����ֱ��toString���
		
		List<String> aString = Arrays.asList("b","a","ab","c");
		StraightInsertionSort.sort(aString);
		System.out.println(aString.toString());
		
		List<Double> aDouble = Arrays.asList(1.1,1.2,0.4,0.5);
		StraightInsertionSort.sort(aDouble);
		System.out.println(aDouble.toString());
	}

	@Test
	public void TestShellInsertionSort(){
		
		List<Integer> aInt = Arrays.asList(1,3,3,2,6,5,11,3,56,35,32);
		int[] dk = {5,3,1};
		ShellInsertSort.sort(aInt, dk);
		System.out.println(aInt.toString());
		
		List<String> aString = Arrays.asList("b","a","ab","c");
		int[] dk1 = {2,1};
		ShellInsertSort.sort(aString, dk1);
		System.out.println(aString.toString());
		
		List<Double> aDouble = Arrays.asList(1.1,1.2,0.4,0.5);
		int[] dk2 = {3,1};
		ShellInsertSort.sort(aDouble, dk2);
		System.out.println(aDouble.toString());		
	}
	
	
	@Test
	public void testWrapper(){
		Integer[] integers = {11,2,4,55,6,777,3};
		StraightInsertionSort.sort(integers);

		String[] strings = {"ba","bb","c","dd"};
		StraightInsertionSort.sort(strings);

		Double[] doubles = {12.3,0.5,0.3,22.6,35.56,123.4};
		StraightInsertionSort.sort(doubles);
				
		RefUtil.isSortedAndShow(integers);
		RefUtil.isSortedAndShow(doubles);
		RefUtil.isSortedAndShow(strings);

	}
	
	@Test
	public void testShellWrapper(){
		int[] dk = {7,5,3,1};
		Integer[] integers = {11,2,4,55,6,777,3,123,345,5677,234,12,333};
		ShellInsertSort.sort(integers,dk);
		RefUtil.isSortedAndShow(integers);

		String[] strings = {"ba","bb","c","dd","aaa","dgsd","aaw","hh","et","ghr"};
		ShellInsertSort.sort(strings,dk);
		RefUtil.isSortedAndShow(strings);

		System.out.println("]");
		System.out.print("[");
		Double[] doubles = {12.3,0.5,0.3,22.6,35.56,123.4,1233.4,2345.5,32.6,7634.4};
		ShellInsertSort.sort(doubles,dk);
		RefUtil.isSortedAndShow(doubles);

	}
	
	
	
	@Test
	public void testBasic(){
		int[] ints = BaseUtil.generateIntArray(200, 30000);
		StraightInsertionSort.sort(ints);
		BaseUtil.isSortedAndShow(ints);

	}
	
	@Test
	public void testShellBasic(){
		int[] dk = {7,5,3,1};
		int[] integers = BaseUtil.generateIntArray(20, 300);
		ShellInsertSort.sort(integers,dk);

		String[] strings = {"ba","bb","c","dd","aaa","dgsd","aaw","hh","et","ghr"};
		ShellInsertSort.sort(strings,dk);

		double[] doubles = {12.3,0.5,0.3,22.6,35.56,123.4,1233.4,2345.5,32.6,7634.4};
		ShellInsertSort.sort(doubles,dk);
		
		//BaseUtil.isSortedAndShow(doubles);//����
		RefUtil.isSortedAndShow(strings);
		BaseUtil.isSortedAndShow(dk);


	}
	
}
