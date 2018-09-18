package testSortAlgorithm.exchangeSort;

import testSortAlgorithm.BaseUtil;

public class BubbleSort {
	
	private static void doSort(int[] a){
		for(int i=0,iLength=a.length-1;i<iLength;i++){
			for(int j=0,jLength=iLength-i-1;j<jLength;j++){
				if(a[j]>a[j+1]) BaseUtil.swap(a,j,j+1);//升序冒泡，若是小于则是降序
			}
		}
	}
		
	public static void sort(int[] a){
		doSort(a);
	}
	
	/**
	 * 加排序完成标志位，当无交换时，退出排序
	 * @param a
	 */
	public static void flagSort(int[] a){
		int i = a.length-1;
		while(i>0){
			int flag = 0;
			for(int j=0;j<i;j++){
				if(a[j]>a[j+1]){
					BaseUtil.swap(a,j,j+1);
					flag = j;//flag为最后交换的位置，其后面没有交换，表示已经排序好了
				} 
			}
			i = flag;//但没有交换的时候才退出，说明数据已经按要求排列好，可立即结束排序，避免不必要的比较过程。
		}
	}
		
	public static void ShakerSort(int[] a) {
	    int low = 0, high = a.length-1, shift = 0;
	    int i;
	     
	    while (low < high) {
	        for (i = low; i < high; i++) { //因为i<high，所以high = shift -1;不行，要不然会少判断
	            if (a[i] > a[i+1]){
	            	BaseUtil.swap(a,i, i+1);
	                shift = i;//记录最后交换的位置，i=4,4、5换了，但以后都没换了
	            }
	        }
	        high = shift ; //high后面已经有序了！！
	        for (i = high - 1; i >= low; i--) {//这里i=high-1是因为[high]肯定小于[high+1],但是不能high = shift-1,//因为i<high，所以high = shift -1;不行，要不然会少判断
	            if (a[i] > a[i+1]) {
	            	BaseUtil.swap(a,i, i+1);
	                shift = i + 1;  //5和6换了，证明5前面是有序的，因为5和6换了，所以5、6也是有序的，所以6前面是有序的
	            }					//但6、7不一定，所以下次从6开始
	        }
	        low = shift;//low前面已经有序了！！
	    }
	}
}


///**
//* 双向排序，记录最大值最小值
//* @param a
//*/
//public static void doubleBubble(int[] a){
//	int low=0,high=a.length-1;
//	while(low<high){
//		for(int j=low;j<high;j++){
//			if(a[j]>a[j+1]){
//				swap(a,j,j+1);
//			} 
//		}
//		high--;
//		for(int j=high;j>low;j--){
//			if(a[j]<a[j-1]){
//				swap(a,j,j-1);
//			} 
//		}
//		low++;
//	}
//}