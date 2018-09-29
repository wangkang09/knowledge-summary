## TOP K 及中位数问题

1、只有2G内存的pc机，在一个存有10G个整数的文件，从中找到中位数，写一个算法 

```
答：先将10G的文件，顺序读入，将每个整数a，通过取最高10位，将10G的文件分成1024份小文件
file[a>>21].append(a);
int[a>>21]++;//每个文件的整数数量
long count = for(int);//统计总数
long middle;
if(count%2==1) {
    middle = (count+1)/2;//如果总数为奇数，则直接取middle
} else {
    middle = count/2;//如果总数为偶数，要取middle和middle+1的中数
}
//假设为奇数
for(int++){
    if(sum>=middle) 得到中位数在第 i 个文件中，int weizhi = middle-sum[i-1];得到第几个
}
如果文件还是很大，但i很大或很小，直接维护一个大顶堆（可先切片，再做大顶堆），做top k,k=weizhi，取其中的最大的
如果i在中间，再将这个文件切分成小文件，直到，文件可以直接放入内存，或i很小或很大

如果文件可以直接放入内存，直接做快排

```

**如果i在中间，再将这个文件切分成小文件，直到，文件可以直接放入内存，或i很小或很大**

```java
/*
3 7
2 5 3 77 5 23 11
 */
public class kKuaiPai {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        int count = sc.nextInt();
        int[] src = new int[count];
        for (int i = 0; i < count; i++) {
            src[i] = sc.nextInt();
        }
        sc.close();
        int middle = new SolutionKKuaiPai().solution(k,src);
        System.out.println(src[middle]);

    }
}

class SolutionKKuaiPai {
    int[] target;
    int k;
    public int solution(int k,int[] a) {
        target = a;
        this.k = k;
        return resolve(0,target.length-1);
    }

    private int resolve(int begin,int end) {
        int middle = getMiddle(begin,end);
        if(middle==k-1) return middle;
        else if(middle<k-1) {
            return resolve(middle+1,end);
        } else {
            return resolve(begin,middle-1);
        }
    }

    private int getMiddle(int begin, int end) {
        int temp = target[begin];
        while (begin<end) {
            while(begin<end&&temp<target[end]) end--;
            target[begin] = target[end];
            while (begin<end&&temp>=target[begin]) begin++;
            target[end] = target[begin];
        }
        target[begin] = temp;
        return begin;
    }
}

```

