package testSortAlgorithm;

public class RefUtil {

	/**
	 * 判断是否是正序数组
	 * @param a
	 * @return
	 */
	public static boolean isPositiveSequence(Comparable[] a){	
		for(int i=0,iMax=a.length-1;i<iMax;i++){
			if(isLess(a[i+1],a[i])) return false;
		}
		return true;		
	}
	
	/**
	 * 判断是否为逆序数组
	 * @param a
	 * @return
	 */
	public static boolean isNegativeSequence(Comparable[] a){		
		for(int i=0,iMax=a.length-1;i<iMax;i++){
			if(isLess(a[i],a[i+1])) return false;
		}
		return true;		
	}
	
	/**
	 * 判断是否为有序数组，并打印数组，注：若是逆序要遍历两次
	 * @param a
	 */
	public static void isSortedAndShow(Comparable[] a){
		isSorted(a);
		show(a);
	}
	
	/**
	 * 数组是否排序
	 * @param a
	 */
	public static void isSorted(Comparable[] a){
		boolean isPos = isPositiveSequence(a);
		System.out.println("是否正序："+isPos);
		if(!isPos){
			isPos = isNegativeSequence(a);
			System.out.println("是否逆序：" + isPos);
		}
	}
	
	/**
	 * 比较引用类型大小
	 * @param a
	 * @param b
	 * @return
	 */
	public static <E> boolean isLess(E  a,E b){		
		return ((Comparable) a).compareTo(b)<0;		
	}
	/**
	 * 打印数组
	 * @param data
	 */
    public static void show(Comparable[] a) {    
        for (int i = 0; i < a.length; i++) {    
            System.out.print(a[i] + ",");    
        }    
        System.out.println();    
    }   
    
    /**
     * 交换数组位置
     * @param a
     * @param i
     * @param j
     */
    public static void swap(Comparable[] a,int i,int j){
    	Comparable temp = a[i];
    	a[i] = a[j];
    	a[j] = temp;
    }
}
