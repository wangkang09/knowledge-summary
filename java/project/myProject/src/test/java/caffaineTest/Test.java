package caffaineTest;




import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 16:13 2018/12/5
 * @Modified By:
 */
public class Test {
//    public static void main(String[] args) {
//        Cache<String, Object> manualCache = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(10_000)
//                .build();
//
//        String key = "name1";
//// 根据key查询一个缓存，如果没有返回NULL
//        Object graph = manualCache.getIfPresent(key);
//// 根据Key查询一个缓存，如果没有调用createExpensiveGraph方法，并将返回值保存到缓存。
//// 如果该方法返回Null则manualCache.get返回null，如果该方法抛出异常则manualCache.get抛出异常
//        graph = manualCache.get(key, (a) -> null);
//// 将一个值放入缓存，如果以前有值就覆盖以前的值
//        manualCache.put(key, graph);
//// 删除一个缓存
//        manualCache.invalidate(key);
//
//        ConcurrentMap<String, Object> map = manualCache.asMap();
//        manualCache.invalidate(key);
//    }
}
