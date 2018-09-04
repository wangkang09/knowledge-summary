package findWork.chubao.xiecheng;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 20:00 2018/9/4
 * @Modified By:
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
public class Main2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int cache = in.nextInt();
        LRU<Integer,Integer> lru = new LRU<>(cache);

        while(in.hasNext()) {
            String s = in.nextLine();
            String[] ss = s.split(" ");
            if(ss[0].equals("p")){
                lru.put(Integer.valueOf(ss[1]),Integer.valueOf(ss[2]));
            } else if(ss[0].equals("g")){
                if(lru.get(Integer.valueOf(ss[1]))==null){
                    System.out.println(-1);
                }else {
                    System.out.println(lru.get(Integer.valueOf(ss[1])));

                }

            }
        }
    }
}

 class LRU<K,V> {

    private static final float hashLoadFactory = 0.75f;
    private LinkedHashMap<K,V> map;
    private int cacheSize;

    public LRU(int cacheSize) {
        this.cacheSize = cacheSize;
        int capacity = (int)Math.ceil(cacheSize / hashLoadFactory) + 1;
        map = new LinkedHashMap<K,V>(capacity, hashLoadFactory, true){
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRU.this.cacheSize;
            }
        };
    }

    public  V get(K key) {
        return map.get(key);
    }

    public  void put(K key, V value) {
        map.put(key, value);
    }

    public  void clear() {
        map.clear();
    }

    public  int usedSize() {
        return map.size();
    }

}

