package testSortAlgorithm.insertSort;

import testSortAlgorithm.RefUtil;

import java.util.List;

public class StraightInsertionSort {
	/******************�����б�ʽ****************************/
	/**
	 * ϣ������ѭ�����ôκ���
	 * @param a
	 * @param dk ���򲽳�
	 */
	public static <E>  void sort(List<E> a,int dk){//dk�ǱȽϲ�����Ϊ1ʱ��ֱ�Ӳ�������
		for(int i=dk,iLength=a.size();i<iLength;i++){
			E temp = a.get(i);
			int j=i;
			for(;(j>dk-1)&&RefUtil.isLess(temp, a.get(j-dk));j=j-dk){//���j>0��tempС�ڱȽϵ�ֵ�Ž�����ǰ�Ƚϡ�
				a.set(j, a.get(j-dk));//ǰ��Ĵ��ں���ģ����԰�ǰ�渳ֵ������
			}
			a.set(j, temp);//��Ϊ��ǰ���û�и�ֵ�����԰Ѵ�����Ĳ�����ȷ��λ��
		}		
	}
	
	/**
	 * Ĭ�ϲ���Ϊ1��һ��ϣ������
	 * @param a
	 */
	public static <E>  void sort(List<E> a){//dk�ǱȽϲ�����Ϊ1ʱ��ֱ�Ӳ�������
		sort(a,1);//Ĭ�ϵ��ò���Ϊ1������
	}
	/******************************************************************/

	
	
	/******************�������鷽ʽ������������ǰ�װ��****************************/
	public static <E> void sort(E a[] ,int dk){
		for(int i=dk,iLength=a.length;i<iLength;i++){
			E temp = a[i];
			int j = i;
			for(;(j>dk-1)&&RefUtil.isLess(temp,a[j-dk]);j=j-dk){
				a[j] = a[j-dk];				
			}
			a[j] = temp;
		}
	}
	public static <E> void sort(E a[]){
		sort(a, 1);
	}
	/******************************************************************/

	
	
	
	
	/*******************�����������ط�ʽ����********************************/
	public static void sort(int[] a){
		sort(a, 1);
	}
	
	public static void sort(String[] a){
		sort(a, 1);
	}
	
	public static void sort(double[] a){
		sort(a, 1);
	}
	
	public static void sort(int[] a,int low,int high){
		for(int i=low+1,iLength=high;i<=iLength;i++){
			if(a[i]<a[i-1]){
				int temp = a[i];
				int j = i;
				//for(;(j>dk-1)&&isTempSmall(temp,a[j-dk]);j=j-dk){
				for(;(j>low)&&(temp<a[j-1]);j=j-1){
					a[j] = a[j-1];				
				}
				a[j] = temp;
			}

		}
	}
	
	public static  void sort(int[] a,int dk){
		for(int i=dk,iLength=a.length;i<iLength;i++){
			if(a[i]<a[i-dk]){
				int temp = a[i];
				int j = i;
				//for(;(j>dk-1)&&isTempSmall(temp,a[j-dk]);j=j-dk){
				for(;(j>dk-1)&&(temp<a[j-dk]);j=j-dk){
					a[j] = a[j-dk];				
				}
				a[j] = temp;
			}

		}
	} 
	
	public static  void sort(String[] a,int dk){
		for(int i=dk,iLength=a.length;i<iLength;i++){
			String temp = a[i];
			int j = i;
			for(;(j>dk-1)&&RefUtil.isLess(temp,a[j-dk]);j=j-dk){
				a[j] = a[j-dk];				
			}
			a[j] = temp;
		}
	} 
	
	public static  void sort(double[] a,int dk){
		for(int i=dk,iLength=a.length;i<iLength;i++){
			double temp = a[i];
			int j = i;
			for(;(j>dk-1)&&(temp<a[j-dk]);j=j-dk){
				a[j] = a[j-dk];				
			}
			a[j] = temp;
		}
	}
	/******************************************************************/

	
	/**
	 * ����ǽ����С�ڸĳɴ��ڼ���	
	 * @param temp
	 * @param a
	 * @return
	 */
//	private static <E> boolean isTempSmall(E temp,E a){
//
//		if(temp instanceof String){
//			
//			String bTemp = (String)temp;
//			String b = (String)a;
//			if( bTemp.compareTo(b)<0) return true;
//			
//		}else if(temp instanceof Integer){
//			
//			int bTemp = (int)temp;
//			int b = (int)a;
//			if(bTemp<b) return true; 
//			
//		}else if(temp instanceof Double){
//			
//			double bTemp = (double)temp;
//			double b = (double)a;
//			if(bTemp<b) return true; 
//			
//		}
//		return false;
//		
//	}
}
