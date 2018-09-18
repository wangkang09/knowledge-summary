package testSortAlgorithm.insertSort;

import java.util.List;

public class ShellInsertSort {
	/**
	 * 
	 * @param list �������б�
	 * @param dk   �������飬�ܹؼ�
	 * 
	 */
	public static <E> void sort(List<E> list,int[] dk){//dk��ÿ������Ĳ�������ͬdkЧ�����ܴ�
		for(int i=0,iLength=dk.length;i<iLength;i++){
			StraightInsertionSort.sort(list, dk[i]);//����ֱ�Ӳ�������
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
