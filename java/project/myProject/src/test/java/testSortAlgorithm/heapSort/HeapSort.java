package testSortAlgorithm.heapSort;

public class HeapSort {
	
	public static void sort(int[] a){
		int N = a.length - 1;
		for(int k=N/2;k>=1;k--) sink(a,k,N);
		while(N>1){
			swap(a,1,N--);
			sink(a,1,N);//N在变化，所以要传参到sink中
		}
	}
	
	private static void sink(int[] a,int k,int N){
		while(2*k<=N){
			int j = 2*k;
			if(j<N&&a[j]<a[j+1]) j++;
			if(a[k]>=a[j]) return;
			swap(a, k, j);
			k = j;
		}
	}
	
	private static void swap(int[] a,int k,int N){
		int temp = a[k];
		a[k] = a[N];
		a[N] = temp;
	}
}
