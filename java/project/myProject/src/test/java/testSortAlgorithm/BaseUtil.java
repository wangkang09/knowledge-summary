package testSortAlgorithm;

import java.util.Random;

public class BaseUtil {
	/**
	 * 产生长度为 length,最大数为 max 的数组
	 * @param length 数组长度
	 * @param max    数组最大值
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
	 * 打印数组
	 * @param data
	 */
    public static void show(int[] a) {    
        for (int i = 0; i < a.length; i++) {    
            System.out.print(a[i] + ",");    
        }    
        System.out.println();    
    } 
    
	/**
	 * 判断是否为有序数组
	 * @param a
	 */
	public static void isSorted(int[] a){
		boolean isPos = isPositiveSequence(a);
		System.out.println("是否正序："+isPos);
		if(!isPos){
			isPos = isNegativeSequence(a);
			System.out.println("是否逆序：" + isPos);
		}
	}
	
	/**
	 * 判断是否为有序数组，并打印数组，注：若是逆序要遍历两次
	 * @param a
	 */
	public static void isSortedAndShow(int[] a){
		isSorted(a);
		show(a);
	}
	
	/**
	 * 判断是否是正序数组
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
	 * 判断是否为逆序数组
	 * @param a
	 * @return
	 */
	public static boolean isNegativeSequence(int[] a){		
		for(int i=0,iMax=a.length-1;i<iMax;i++){
			if(a[i]<a[i+1]) return false;
		}
		return true;		
	}
	
	public static void swap(int[] a,int i,int j ){//只能用于int和long型
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void swap(double[] a,int i,int j ){//只能用于int和long型
		double temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public static void swap(char[] a,int i,int j ){//只能用于int和long型
		char temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
}
