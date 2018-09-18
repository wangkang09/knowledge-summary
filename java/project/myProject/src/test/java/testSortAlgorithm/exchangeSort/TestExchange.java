package testSortAlgorithm.exchangeSort;

import org.junit.Test;
import testSortAlgorithm.BaseUtil;
import testSortAlgorithm.insertSort.ShellInsertSort;
import testSortAlgorithm.selectSort.SimpleSelectSort;

public class TestExchange {
	
	@Test
	public void testBubble(){
		int[] a = BaseUtil.generateIntArray(2000, 30000);
		//BubbleSort.sort(a);
		//BubbleSort.flagSort(a);
		BubbleSort.ShakerSort(a);

	}
	
	@Test
	public void testQuick(){
		int[] a = BaseUtil.generateIntArray(20, 30);
		QuickSort.sort0(a);
		BaseUtil.isSortedAndShow(a);

	}
	
	@Test
	public void testQuick0(){
		int[] a = BaseUtil.generateIntArray(1000000, 100000000);
		int[] b = new int[a.length];

		System.arraycopy(a, 0, b, 0, a.length);
		long start = System.currentTimeMillis();
		QuickSort.sort0(a, 4);
		System.out.println(System.currentTimeMillis()-start);
		
		BaseUtil.isSorted(a);


		long start0 = System.currentTimeMillis();
		QuickSort.sort(b);

		System.out.println(System.currentTimeMillis()-start0);
		
		BaseUtil.isSorted(b);

		
	}
	
	@Test
	public void testShakerSort(){
		int[] a = BaseUtil.generateIntArray(2000, 30000);
		BubbleSort.ShakerSort(a);
		BaseUtil.isSortedAndShow(a);
	}
	
	
	
	@Test
	public void testAll(){
		int[] a = BaseUtil.generateIntArray(20, 1000);
		int[] b = new int[a.length];
		int[] simpleIn = new int[a.length];
		int[] shakerSort = new int[a.length];
		int[] shell = new int[a.length];
		int[] simpleSe0 = new int[a.length];
		int[] simpleSe = new int[a.length];
		
		System.arraycopy(a, 0, simpleIn, 0, a.length);
		System.arraycopy(a, 0, shakerSort, 0, a.length);
		System.arraycopy(a, 0, shell, 0, a.length);
		System.arraycopy(a, 0, simpleSe, 0, a.length);
		System.arraycopy(a, 0, simpleSe0, 0, a.length);

		System.arraycopy(a, 0, b, 0, a.length);
		long start = System.currentTimeMillis();
		QuickSort.sort0(a, 8);

		System.out.println((System.currentTimeMillis()-start)+"-->�Ż�����");
		

		long start0 = System.currentTimeMillis();
		QuickSort.sort(b);

		System.out.println((System.currentTimeMillis()-start0)+"-->��ͨ����");
		
//		long start1 = System.currentTimeMillis();
//		StraightInsertionSort.sort(simpleIn);
//		System.out.println((System.currentTimeMillis()-start1)+"-->�򵥲���");
		
		long start2 = System.currentTimeMillis();
		int[] dk = {51,31,29,23,19,17,13,7,3,1};
		ShellInsertSort.sort(shell,dk);
		System.out.println((System.currentTimeMillis()-start2)+"-->ϣ������");
		
		long start3 = System.currentTimeMillis();
		SimpleSelectSort.sortMinMax(simpleSe);
		System.out.println((System.currentTimeMillis()-start3)+"-->��ѡ���Ż�");
		
		long start6 = System.currentTimeMillis();
		SimpleSelectSort.sort(simpleSe0);
		System.out.println((System.currentTimeMillis()-start6)+"-->��ѡ��");

		
//		long start4 = System.currentTimeMillis();
//		BubbleSort.ShakerSort(shakerSort);
//		System.out.println((System.currentTimeMillis()-start4)+"-->��β������");

		BaseUtil.isSortedAndShow(b);

		BaseUtil.isSortedAndShow(a);
		BaseUtil.isSortedAndShow(shell);
		BaseUtil.isSortedAndShow(simpleSe);
		BaseUtil.isSortedAndShow(simpleSe0);

		//RefUtil.isSortedAndShow(b);��������û��ʵ��comparable�ӿ�

		
	}
	
	@Test
	public void testSort1(){
		int[] a = BaseUtil.generateIntArray(20, 5);
		int[] b = new int[a.length];

		System.arraycopy(a, 0, b, 0, a.length);
		long start = System.currentTimeMillis();
		QuickSort.sort(a);
		System.out.println(System.currentTimeMillis()-start);
		
		BaseUtil.isSortedAndShow(a);


		long start0 = System.currentTimeMillis();
		QuickSort.sort1(b);

		System.out.println(System.currentTimeMillis()-start0);
		
		BaseUtil.isSortedAndShow(b);
	}
}
