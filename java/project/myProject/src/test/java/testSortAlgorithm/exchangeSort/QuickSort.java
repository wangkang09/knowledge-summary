package testSortAlgorithm.exchangeSort;

import testSortAlgorithm.BaseUtil;
import testSortAlgorithm.insertSort.StraightInsertionSort;

public class QuickSort {
	
	public static void sort(int[] a){
		doSort(a,0,a.length-1);
	}
	
	private static void doSort(int[] a,int low,int high){
		if(high<=low) return;
		int middle = getMiddle(a, low, high);
		doSort(a, low, middle-1);
		doSort(a, middle+1, high);
	}
	
	private static int getMiddle(int[] a,int low,int high){
		int temp = a[low];//��׼Ԫ��
		
		while(low<high){
			
			while(low<high&&a[high]>=temp) high--;//��high�����ң����high��ֵ���ڻ�׼������ǰ�ң�һֱ�ҵ��Ȼ�׼С�ģ������ŵ���׼Ԫ��λ��
			a[low] = a[high];//һֱ�ҵ��Ȼ�׼С�ģ������ŵ���׼Ԫ��λ��
			while(low<high&&a[low]<=temp) low++;//��low�����ң����lowֵС�ڻ�׼���������ң�һֱ�ҵ��Ȼ�׼��ģ������ŵ�high��λ��
			a[high] = a[low];//һֱ�ҵ��Ȼ�׼��ģ������ŵ�high��λ��
			
		}  //����һ��while�������ұߵ�һ��С�ڻ�׼��ֵ���ŵ��˻�׼��λ�ã�������ߵ�һ�����ڻ�׼��ֵ(low)���ŵ������ұߵ�һ��С�ڻ�׼ֵ��λ��(high)������low��ߺ�high�ұ��Ѿ����������ˡ�ֻҪhigh=low��ȫ���㣡����
		a[low] = temp;//����a[high] = temp
		return low;
	}
	
	//ֻ�Գ��ȴ���dk�ǵ������н����������Ŵ����8
	private static void doSort0(int[] a,int low,int high,int dk){
//		if(high <= low + dk){
//			StraightInsertionSort.sort(a,low,high);
//			return;
//		}
		if(high <= low ){
			return;
		}
		int middle = getMiddle(a, low, high);
		doSort0(a, low, middle-1,dk);
		doSort0(a, middle+1, high,dk);
	}
	
	/**
	 * 
	 * @param a
	 * @param dk ��С�����г���
	 */
	public static void sort0(int[] a,int dk){
		doSort0(a, 0, a.length-1, dk);//�ȵ��øĽ��㷨Qsortʹ֮��������
		//long start = System.currentTimeMillis();
		//StraightInsertionSort.sort(a);//���ò�������Ի��������������� 
		//System.out.println(System.currentTimeMillis()-start);

	}
	
	public static void sort0(int[] a){
		doSort0(a, 0, a.length-1, 8);//�ȵ��øĽ��㷨Qsortʹ֮��������
	}
	
	//ֻ�Գ��ȴ���dk�ǵ������н����������Ŵ����8
	private static void doSort1(int[] a,int low,int high){
		if(low>=high) return;
		
		int[] middles = getMiddle0(a, low, high);
		doSort1(a, low, middles[0]);
		doSort1(a, middles[1], high);
	}
	
	/**
	 * �����淨���أ��´εݹ�ģ���λ�ú���λ��
	 * @param a
	 * @param low
	 * @param high
	 * @return
	 */
	private static int[] getMiddle0(int[] a,int low,int high){
		int lt = low,i = low+1,gt=high;
		int temp = a[low];
		while(i<=gt){
			int flag = a[i] - temp;
			if(flag<0) BaseUtil.swap(a,lt++,i++);
			else if(flag>0) BaseUtil.swap(a,i,gt--);
			else i++;
		}
		return new int[]{lt-1,gt+1};				
	}
	
	/**
	 * �����淨���ţ������ںܶ��ظ�Ԫ�ص�����
	 * @param a
	 */
	public static void sort1(int[] a){
		doSort1(a, 0, a.length-1);
	}
}
