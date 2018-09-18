package testSortAlgorithm.selectSort;

import testSortAlgorithm.BaseUtil;
import testSortAlgorithm.RefUtil;

/**
 * ��ѡ������
 * @author Administrator
 *
 */
public class SimpleSelectSort {
	
	public static void sort(int[] a){
		doSort(a);
	}
	public static void sort(double[] a){
		doSort(a);
	}
	public static void sort(char[] a){
		doSort(a);
	}
	public static void sort(String[] a){
		doSort(a);
	}
	private static void doSort(int[] a){
		for(int i=0,iLength=a.length-1;i<iLength;i++){
			int min = i;
			for(int temp=i+1,tLength=a.length;temp<tLength;temp++){
				if(a[temp]<a[min]) min = temp;
			}
			if(i!=min) BaseUtil.swap(a,i,min);
		}
	}
	
	private static void doSort(double[] a){
		for(int i=0,iLength=a.length;i<iLength;i++){
			int min = i;
			for(int temp=i+1;temp<iLength;temp++){
				if(a[temp]< a[min]) min = temp;
			}
			if(i!=min) BaseUtil.swap(a,i,min);
		}
	}
	
	private static void doSort(char[] a){
		for(int i=0,iLength=a.length;i<iLength;i++){
			int min = i;
			for(int temp=i+1;temp<iLength;temp++){
				if(a[temp]<a[min]) min = temp;
			}
			if(i!=min) BaseUtil.swap(a,i,min);
		}
	}
	
	private static void doSort(String[] a){
		for(int i=0,iLength=a.length;i<iLength;i++){
			int min = i;
			for(int temp=i+1;temp<iLength;temp++){
				if(RefUtil.isLess(a[temp], a[min])) min = temp;
			}
			if(i!=min) RefUtil.swap(a,i,min);
		}
	}
	
	/**
	 * ��Ԫѡ������<br>
	 *����������������ֵ����Сλ�û���Сֵ�����λ��ʱ������Ϊ���ε���ͬһ����������
	 *
	 * 
	 * @param a ����������
	 */
	public static void sortMinMax(int[] a){
		for(int i=0,iLength=a.length/2;i<iLength;i++){
			int min = i,max =i;
			for(int j=i+1,jLength=a.length-i;j<jLength;j++){
				if(a[j]>a[max]) {max = j;continue;}
				if(a[j]<a[min]) min = j;				
			}
			int aEnd = a.length-i-1;
			//if(min==i&&max==aEnd) return;//����Сֵ�����ֵ
			if(max==i&&min==aEnd){
				if(i!=min) BaseUtil.swap(a, i, min);
				continue;
			}
			if(max==i&&min!=aEnd){
				BaseUtil.swap(a, aEnd, min);
				if(i!=aEnd) BaseUtil.swap(a, aEnd, i);
				continue;
			}
			if(max!=i&&min==aEnd){
				BaseUtil.swap(a, max, i);
				if(i!=aEnd) BaseUtil.swap(a, aEnd, i);
				continue;
			}
			//����Сֵ���ֵ���ò��ƶ�ʱ������������
			if(i!=min) BaseUtil.swap(a, i, min);
			if(max!=aEnd) BaseUtil.swap(a, aEnd, max);
					
		}
	}
}
