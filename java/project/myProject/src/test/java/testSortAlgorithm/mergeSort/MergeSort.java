package testSortAlgorithm.mergeSort;

public class MergeSort {
	
	private static int[] tempArray;
	
	public static void sort(int[] a){
		
		tempArray = new int[a.length];		
		doSort(a,0,a.length-1);
		
	}
	private static void doSort(int[] a,int left,int right){
		
		if(left>=right) return;
		
		int center = (right+left)/2;//�ҳ��м�����    
		doSort(a, left, center);// �����������еݹ� 
		doSort(a, center+1, right);// ���ұ߱�������еݹ� 		
		merge(a, left, center, right);//�Եݹ��ֵ����飬���еݹ�ϲ�
		
	}
	
	private static void merge(int[] a,int left,int center,int right) {
		
		int second = center+1;//�ұ������һλ
		int first = left;//��������һλ
		
//		for(int k=left;k<=right;k++){
//			tempArray[k] = a[k];
//		}
		
		System.arraycopy(a, left, tempArray, left, right-left+1);
		
		for(int k=left;k<=right;k++){
			if(first>center) a[k] = tempArray[second++];//����һ������ʹ�����ֱ��ʹ�õڶ������鼴��
			else if(second>right) a[k] = tempArray[first++];//���ڶ�������ʹ�����ֱ��ʹ�õ�һ�����鼴��
			else if(tempArray[second]>=tempArray[first]) a[k] = tempArray[first++];//�ڶ���������ֵ���ڵ��ڵ�һ������ʹ�õ�һ��
			else a[k] = tempArray[second++];//����ʹ�õڶ���
		}

	}
}

/*public static void merge(int[] a,int left,int center,int right) {
	
	int second = center+1;//�ұ������һλ
	int begin = left;//��������һλ
	int head = left;//������λ�������������һλ��ʼ����
	
	//�����������飬��С��ֵ���뻺������
	while(left<=center&&second<=right){
		
		if(a[left]<=a[second]) {
			tempArray[begin++] = a[left++];
		} else{
			tempArray[begin++] = a[second++];
		} 
	}
	
	// ʣ�ಿ�����η�����ʱ���飨ʵ��������whileֻ��ִ������һ����	
    while (left <= center) {    
    	tempArray[begin++] = a[left++];    
    }             
    while (mid <= right) {    
    	tempArray[begin++] = a[mid++];    
    }
    
    // ����ʱ�����е����ݿ�����ԭ������    
    // ��ԭleft-right��Χ�����ݱ����ƻ�ԭ���飩 
    while (head<=right) {
    	a[head] = tempArray[head++];
	}

}*/

/*
	//�Ե����ϣ����Զ� merge���д���ʹ�������
	public static void sort(int[] a){
		int N = a.length;
		int[] temp = new int[N];
		for(int i=1;i<N;i=2*i){
			for(int j=0; j<N-i;j += 2*i){
				merge(a,j,j+i-1;Math.min(j+2*i-1,N-1));
			}
		}
	}

*/