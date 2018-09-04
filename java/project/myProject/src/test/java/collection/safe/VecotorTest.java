package collection.safe;

import java.util.Vector;

/**
 * @Description: 所有的关键方法，都加了synchronized保证数据一致性
 * @Author: wangkang
 * @Date: Created in 16:17 2018/9/3
 * @Modified By:
 */
public class VecotorTest {
    public static void main(String[] args) {
        Vector<Integer> v = new Vector<>();
        v.size();
        v.add(1);
        v.get(0);
        v.remove(0);
        v.iterator();
        v.lastIndexOf(1);
    }
}
