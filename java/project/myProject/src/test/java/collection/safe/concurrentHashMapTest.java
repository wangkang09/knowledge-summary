package collection.safe;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:06 2018/9/3
 * @Modified By:
 */
public class concurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer,Integer> map = new ConcurrentHashMap<>();
        map.put(1,1);
        map.get(1);
        map.remove(1);
        map.remove(1,1);
        map.containsKey(1);
        map.containsValue(1);
        map.contains(1);
        map.elements();
        map.entrySet();
        map.keys();
        map.keySet();
        map.size();
        map.values();

    }
}
