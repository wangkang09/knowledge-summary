package sortAlgorithm;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 14:51 2018/9/12
 * @Modified By:
 */
public class BinarySearch {
    public static void main(String[] args) {
        int[] temp = {0,1,2,6,12,45,67};
        int target = 67;
        int re = new SolutionBin().binarySearch(temp,target);
        System.out.println(re);
    }
}

class SolutionBin {

    int[] array;
    int index;
    int target;
    public int binarySearch(int[] a,int target) {
        array = a;
        this.target = target;
        index = resolve(0,a.length-1);
        return  index;
    }

    private int resolve(int begin, int end) {
        if(begin>end) return -1;
        int middle = (end + begin)/2;
        if(array[middle] == target) return middle;
        else if(array[middle]<target) {
            return resolve(middle+1,end);
        } else return resolve(begin,middle-1);

    }
}