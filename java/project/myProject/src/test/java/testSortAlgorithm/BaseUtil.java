package testSortAlgorithm;

import java.util.Random;

public class BaseUtil {
	/**
	 * ��������Ϊ length,�����Ϊ max ������
	 * @param length ���鳤��
	 * @param max    �������ֵ
	 * @return
	 */
	public static int[] generateIntArray(int length,int max){
		int[] a = new int[length];
		Random random = new Random();
//		for(int i=0;i<length;i++){
//			if(i%3==0)
//				a[i] = - random.nextInt(max);
//			else 
//				a[i] =  random.nextInt(max);
//		}	
		for(int i=0;i<length;i++){
			a[i] =  random.nextInt(max);
		}

		return a;		
	}
	
	/**
	 * ��ӡ����
	 * @param data
	 */
    public static void show(int[] a) {    
        for (int i = 0; i < a.length; i++) {    
            System.out.print(a[i] + ",");    
        }    
        System.out.println();    
    } 
    
	/**
	 * �ж��Ƿ�Ϊ��������
	 * @param a
	 */
	public static void isSorted(int[] a){
		boolean isPos = isPositiveSequence(a);
		System.out.println("�Ƿ�����"+isPos);
		if(!isPos){
			isPos = isNegativeSequence(a);
			System.out.println("�Ƿ�����" + isPos);
		}
	}
	
	/**
	 * �ж��Ƿ�Ϊ�������飬����ӡ���飬ע����������Ҫ��������
	 * @param a
	 */
	public static void isSortedAndShow(int[] a){
		isSorted(a);
		show(a);
	}
	
	/**
	 * �ж��Ƿ�����������
	 * @param a
	 * @return
	 */
	public static boolean isPositiveSequence(int[] a){	
		for(int i=0,iMax=a.length-1;i<iMax;i++){
			if(a[i]>a[i+1]) return false;
		}
		return true;		
	}
	
	/**
	 * �ж��Ƿ�Ϊ��������
	 * @param a
	 * @return
	 */
	public static boolean isNegativeSequence(int[] a){		
		for(int i=0,iMax=a.length-1;i<iMax;i++){
			if(a[i]<a[i+1]) return false;
		}
		return true;		
	}
	
	public static void swap(int[] a,int i,int j ){//ֻ������int��long��
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void swap(double[] a,int i,int j ){//ֻ������int��long��
		double temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void swap(char[] a,int i,int j ){//ֻ������int��long��
		char temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
}
