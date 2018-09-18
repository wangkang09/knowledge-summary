package testSortAlgorithm;

public class RefUtil {

	/**
	 * �ж��Ƿ�����������
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
	 * �ж��Ƿ�Ϊ��������
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
	 * �ж��Ƿ�Ϊ�������飬����ӡ���飬ע����������Ҫ��������
	 * @param a
	 */
	public static void isSortedAndShow(Comparable[] a){
		isSorted(a);
		show(a);
	}
	
	/**
	 * �����Ƿ�����
	 * @param a
	 */
	public static void isSorted(Comparable[] a){
		boolean isPos = isPositiveSequence(a);
		System.out.println("�Ƿ�����"+isPos);
		if(!isPos){
			isPos = isNegativeSequence(a);
			System.out.println("�Ƿ�����" + isPos);
		}
	}
	
	/**
	 * �Ƚ��������ʹ�С
	 * @param a
	 * @param b
	 * @return
	 */
	public static <E> boolean isLess(E  a,E b){		
		return ((Comparable) a).compareTo(b)<0;		
	}
	/**
	 * ��ӡ����
	 * @param data
	 */
    public static void show(Comparable[] a) {    
        for (int i = 0; i < a.length; i++) {    
            System.out.print(a[i] + ",");    
        }    
        System.out.println();    
    }   
    
    /**
     * ��������λ��
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
