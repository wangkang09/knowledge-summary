package testSortAlgorithm.mergeSort;

public class MergeSort {
	
	private static int[] tempArray;
	
	public static void sort(int[] a){
		
		tempArray = new int[a.length];		
		doSort(a,0,a.length-1);
		
	}
	private static void doSort(int[] a,int left,int right){
		
		if(left>=right) return;
		
		int center = (right+left)/2;//找出中间索引    
		doSort(a, left, center);// 对左边数组进行递归 
		doSort(a, center+1, right);// 对右边边数组进行递归 		
		merge(a, left, center, right);//对递归拆分的数组，进行递归合并
		
	}
	
	private static void merge(int[] a,int left,int center,int right) {
		
		int second = center+1;//右边数组第一位
		int first = left;//左边数组第一位
		
//		for(int k=left;k<=right;k++){
//			tempArray[k] = a[k];
//		}
		
		System.arraycopy(a, left, tempArray, left, right-left+1);
		
		for(int k=left;k<=right;k++){
			if(first>center) a[k] = tempArray[second++];//当第一个数组使用完后，直接使用第二个数组即可
			else if(second>right) a[k] = tempArray[first++];//当第二个数组使用完后，直接使用第一个数组即可
			else if(tempArray[second]>=tempArray[first]) a[k] = tempArray[first++];//第二个数组数值大于等于第一个，则使用第一个
			else a[k] = tempArray[second++];//否则使用第二个
		}

	}
}

/*public static void merge(int[] a,int left,int center,int right) {
	
	int second = center+1;//右边数组第一位
	int begin = left;//左边数组第一位
	int head = left;//缓存首位，最后用来在这一位开始复制
	
	//遍历左右数组，将小的值插入缓存数组
	while(left<=center&&second<=right){
		
		if(a[left]<=a[second]) {
			tempArray[begin++] = a[left++];
		} else{
			tempArray[begin++] = a[second++];
		} 
	}
	
	// 剩余部分依次放入临时数组（实际上两个while只会执行其中一个）	
    while (left <= center) {    
    	tempArray[begin++] = a[left++];    
    }             
    while (mid <= right) {    
    	tempArray[begin++] = a[mid++];    
    }
    
    // 将临时数组中的内容拷贝回原数组中    
    // （原left-right范围的内容被复制回原数组） 
    while (head<=right) {
    	a[head] = tempArray[head++];
	}

}*/

/*
	//自底向上，可以对 merge进行处理，使插入减半
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