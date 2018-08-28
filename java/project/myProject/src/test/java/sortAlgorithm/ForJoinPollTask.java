package sortAlgorithm;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ForJoinPollTask {

    public static void main(String[] args) throws Exception {
        Trie trie = new Trie();
        trie.insert("a");
        trie.insert("a");
        trie.insert("a");
        trie.insert("bcef");
        trie.insert("ab");
        trie.insert("b");
        trie.insert("bcd");
        trie.insert("bcef");
        ForkJoinPool pool = ForkJoinPool.commonPool();
        SumTask task = new SumTask(trie.getTrie(),"");
        pool.invoke(task);
        pool.shutdownNow();
        pool.awaitTermination(1,TimeUnit.DAYS);
    }
}
    class SumTask extends RecursiveAction {
        private Trie.TrieNode trieNode;
        private String old;

        public SumTask(Trie.TrieNode trieNode,String old) {
            super();
            this.trieNode = trieNode;
            this.old = old;
        }


        @Override
        protected void compute() {
            List<SumTask> list = new ArrayList<>();

            for(int i=0;i<26;i++) {
                Trie.TrieNode node = trieNode.trieNodes[i];//取得trie的第一个子节点
                if(node!=null) {
                    String oo = old+(char)(97+i);
                    if(node.getExist()) {
                        System.out.println(oo+node.getExistCount()+node.getCount());
                        //这里完全可以将字符串和重复次数包装成对象，插入排序队列（小顶堆），得到TOP10的字符串数
                    }
                    list.add(new SumTask(node,oo));
                }
            }
            if(list.size()!=0) invokeAll(list);

        }

    }
