package testSortAlgorithm.insertSort;

import java.util.List;

public class ShellInsertSort {
	/**
	 * 
	 * @param list 待排序列表
	 * @param dk   增量数组，很关键
	 * 
	 */
	public static <E> void sort(List<E> list,int[] dk){//dk是每次排序的步长，不同dk效率相差很大
		for(int i=0,iLength=dk.length;i<iLength;i++){
			StraightInsertionSort.sort(list, dk[i]);//调用直接插入排序
		}
	}
	
	
	public static <E> void  sort(E[] array,int[] dk) {
		for(int i=0,iLength=dk.length;i<iLength;i++){	
			StraightInsertionSort.sort(array, dk[i]);
		}
	}
	
	public static void sort(int[] array,int[] dk){
		for(int i=0,iLength=dk.length;i<iLength;i++){	
			StraightInsertionSort.sort(array, dk[i]);
		}
	}

	public static void sort(String[] array,int[] dk){
		for(int i=0,iLength=dk.length;i<iLength;i++){	
			StraightInsertionSort.sort(array, dk[i]);
		}
	}
	
	public static void sort(double[] array,int[] dk){
		for(int i=0,iLength=dk.length;i<iLength;i++){	
			StraightInsertionSort.sort(array, dk[i]);
		}
	}
}
