package testSortAlgorithm.heapSort;

public class MaxPQ {
	private int[] pq;
	private int N=0;
	
	public MaxPQ(int maxN){
		pq = new int[maxN+1];
	}
	
	public boolean isEmpty(){
		return N == 0;
	}
	
	public int size(){
		return N;
	}
	
	public void insert(int v){
		pq[++N] = v;
		swim(N);
	}
	
	public int delMax(){
		int max = pq[1];
		swap(1,N--);
		//pq[N+1] = null;����Ҳ����Ҫ������Ϊ������������
		sink(1);
		return max;
	}
	
	private void swap(int i,int j){
		int temp = pq[i];
		pq[i] = pq[j];
		pq[j] = temp;
	}
	
	private void swim(int k){
		while(k>1&&pq[k/2]<pq[k]){
			swap(k/2, k);
			k = k/2;
		}
	}
	
	private void sink(int k){
		while(2*k<=N){
			int j = 2*k;
			if(j<N&&less(j, j+1)) j++;//�ҵ��ϴ�Ľڵ�
			if(!less(k, j)) break;//���pq[k]>pq[j],�����ڵ���������ӽڵ�ʱ���Ѿ���������ˡ�
			swap(k, j);
			k = j;
		}
	}
	
	private boolean less(int i,int j){
		return pq[i]<pq[j];
	}
}
