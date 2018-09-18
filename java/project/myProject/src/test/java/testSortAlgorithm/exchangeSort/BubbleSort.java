package testSortAlgorithm.exchangeSort;

import testSortAlgorithm.BaseUtil;

public class BubbleSort {
	
	private static void doSort(int[] a){
		for(int i=0,iLength=a.length-1;i<iLength;i++){
			for(int j=0,jLength=iLength-i-1;j<jLength;j++){
				if(a[j]>a[j+1]) BaseUtil.swap(a,j,j+1);//����ð�ݣ�����С�����ǽ���
			}
		}
	}
		
	public static void sort(int[] a){
		doSort(a);
	}
	
	/**
	 * ��������ɱ�־λ�����޽���ʱ���˳�����
	 * @param a
	 */
	public static void flagSort(int[] a){
		int i = a.length-1;
		while(i>0){
			int flag = 0;
			for(int j=0;j<i;j++){
				if(a[j]>a[j+1]){
					BaseUtil.swap(a,j,j+1);
					flag = j;//flagΪ��󽻻���λ�ã������û�н�������ʾ�Ѿ��������
				} 
			}
			i = flag;//��û�н�����ʱ����˳���˵�������Ѿ���Ҫ�����кã��������������򣬱��ⲻ��Ҫ�ıȽϹ��̡�
		}
	}
		
	public static void ShakerSort(int[] a) {
	    int low = 0, high = a.length-1, shift = 0;
	    int i;
	     
	    while (low < high) {
	        for (i = low; i < high; i++) { //��Ϊi<high������high = shift -1;���У�Ҫ��Ȼ�����ж�
	            if (a[i] > a[i+1]){
	            	BaseUtil.swap(a,i, i+1);
	                shift = i;//��¼��󽻻���λ�ã�i=4,4��5���ˣ����Ժ�û����
	            }
	        }
	        high = shift ; //high�����Ѿ������ˣ���
	        for (i = high - 1; i >= low; i--) {//����i=high-1����Ϊ[high]�϶�С��[high+1],���ǲ���high = shift-1,//��Ϊi<high������high = shift -1;���У�Ҫ��Ȼ�����ж�
	            if (a[i] > a[i+1]) {
	            	BaseUtil.swap(a,i, i+1);
	                shift = i + 1;  //5��6���ˣ�֤��5ǰ��������ģ���Ϊ5��6���ˣ�����5��6Ҳ������ģ�����6ǰ���������
	            }					//��6��7��һ���������´δ�6��ʼ
	        }
	        low = shift;//lowǰ���Ѿ������ˣ���
	    }
	}
}


///**
//* ˫�����򣬼�¼���ֵ��Сֵ
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