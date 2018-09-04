package collection.unsafe;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:00 2018/9/3
 * @Modified By:
 */
public class MapTest {

    public static void main(String[] args) {
        Map<Integer,Integer> m = new HashMap<>();
        m.get(1);
        m.put(1,1);

        m.containsKey(1);
        m.containsValue(1);
        m.entrySet();
        m.keySet();
        m.remove(1);
        m.remove(1,1);
        m.values();
        m.size();

        Map<Integer,Integer> t = new TreeMap<>();
        t.get(1);
        t.put(1,1);
        t.size();
        t.remove(1);
        t.remove(1,1);
        t.entrySet();


    }
}
