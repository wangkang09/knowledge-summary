package testSortAlgorithm.priorityQueue;

public class Multiway {	
	
	public static void main(String[] args) {
	}
	
	private static void  swap(int[] a,int k,int l){
		int temp = a[k];
		a[k] = a[l];
		a[l] = temp;
	}
	
	
	/**
	 * k不能等于1，否则k/2=0，不能取a[0]
	 * @param a
	 * @param k
	 */
	private static void swim(int[] a,int k){
		while(k>1&&a[k/2]<a[k]){
			swap(a,k/2,k);
			k = k/2;
		}
	}
	
	/**
	 * 从数组第一位开始a[1]
	 * @param a
	 * @param k
	 */
	private static void sink(int[] a,int k){
		int N = a.length - 1;
		while(2*k<=N){
			int j = 2*k;
			if(j<N&&a[j]<a[j+1]) j++;//取最大的子节点
			if(a[k]>a[j]) return;
			swap(a, k, j);
			k = j;
		}
		
	}
	
	
}	
