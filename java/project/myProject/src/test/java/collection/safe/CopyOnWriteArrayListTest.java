package collection.safe;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:22 2018/9/3
 * @Modified By:
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        CopyOnWriteArrayList l = new CopyOnWriteArrayList();
        l.add(1);
        l.contains(1);
        l.get(0);
        l.remove(0);
        l.remove(1);
        l.indexOf(1);
        l.iterator();
        l.lastIndexOf(1);
    }
}
