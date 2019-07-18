package collection.safe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 22:06 2018/9/3
 * @Modified By:
 */
public class concurrentHashMapTest {
//
//
//    public  void test(String[] args) {
//        ConcurrentHashMap<Integer,Integer> map = new ConcurrentHashMap<>();
//        map.put(1,1);
//        map.get(1);
//        map.remove(1);
//        map.remove(1,1);
//        map.containsKey(1);
//        map.containsValue(1);
//        map.contains(1);
//        map.elements();
//        map.entrySet();
//        map.keys();
//        map.keySet();
//        map.size();
//        map.values();
//
//        String a = "a";
//
//        String id;
//        String biz_tag = "leaf1";
//        Queue<String>[] q = new Queue[10];
//        int idx = getIdx();
//
//        synchronized (q[idx] ) {
//            id = q[1].poll();
//            if (q[idx].size()<=0) {
//                q[idx].addAll(leaf_serverClient(biz_tag));
//            }
//        }
//
//
//    }
//
//    private static int getIdx() {
//        return 0;
//    }
//
//    private  Queue<String> leaf_serverClient(String biz_tag ,boolean luanxu, int step) {
//        get(step,luanxu);
//        return null;
//    }
//    long lastTimestamp = -1;
//    Queue<String> sequences;
//    int sequenceIdx = 1000;
//
//    public  synchronized Result get(int step,boolean luanxu {
//        long timestamp = timeGen();
//        if (timestamp < lastTimestamp) {
//            long offset = lastTimestamp - timestamp;
//            if (offset <= 5) {
//                try {
//                    wait(offset << 1);
//                    timestamp = timeGen();
//                    if (timestamp < lastTimestamp) {
//                        return new Result(-1,null, Status.EXCEPTION);
//                    }
//                } catch (InterruptedException e) {
//                    //LOGGER.error("wait interrupted");
//                    return new Result(-2,null, Status.EXCEPTION);
//                }
//            } else {
//                return new Result(-3, null,Status.EXCEPTION);
//            }
//        }
//        if (lastTimestamp == timestamp) {
//            sequences = genSequence(sequenceIdx,step);
//        } else {
//            sequences = genSequence(step);
//        }
//
//        lastTimestamp = timestamp;
//
//        return new Result(timestamp,sequences, Status.SUCCESS);
//
//    }
//
//    public Queue<String> genSequence(int step) {
//        return genSequence(0,step,false);
//    }
//
//    public Queue<String> genSequence(int start, int step) {
//        return genSequence(0,step,false);
//    }
//
//    //返回从缓存中的start开始的offset个数据
//    public Queue<String> genSequence(int start, int offset, boolean luanxu) {
//
//        List<String> result = new ArrayList<>();
//        if(luanxu) {
//            disorder(result);
//        }
//        return null;
//    }
//
//    private void disorder(List<String> result) {
//    }
//
//    private long timeGen() {
//        return System.currentTimeMillis();
//    }

}
