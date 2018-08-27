```java
package sortAlgorithm;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

//测试遍历Trie，得到重复字符串算法
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
```

```java
package sortAlgorithm;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 18:17 2018/8/25
 * @Modified By:
 * 加入了existCount表示，插入单词的重复次数
 */
public class Trie {

    private TrieNode root;

    public TrieNode getTrie() {
        return root;
    }

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNode node = root;
        int length = word.length();
        int position ;
        char c;
        for (int i = 0; i < length; i++) {
            c = word.charAt(i);
            position = c-'a';
            if (node.trieNodes[position] == null) {
                node.trieNodes[position] = new TrieNode();
            }
            node = node.trieNodes[position];
            node.setCount(node.getCount()+1);
        }
        if(node.getExist()) node.setExistCount(node.getExistCount()+1);
        else node.setExist(true);
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        boolean result = false;
        TrieNode node = root;
        int length = word.length();
        int position ;
        char c;
        for (int i = 0; i < length; i++) {
            c = word.charAt(i);
            position = c - 'a';
            node = node.trieNodes[position];
            if (node == null) {
                break;
            }
        }
        if (node != null && node.getExist()) {
            result = true;
        }
        return result;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        int length = prefix.length();
        int position ;
        char c;
        for (int i = 0; i < length; i++) {
            c = prefix.charAt(i);
            position = c - 'a';
            node = node.trieNodes[position];
            if (node == null) {
                return false;
            }
        }
        return true;
    }

    // delete if the word is in the trie.
    public boolean doDelete(String word, TrieNode node) {
        //树中已匹配的字符串比传入字符串短
        if (node == null) {
            return false;
        }

        //树中已匹配的字符串比传入字符串不短
        if (word.length()  > 1){
            char c = word.charAt(0);
            int position = c - 'a';
            TrieNode trieNode = node.trieNodes[position];
            boolean b = doDelete(word.substring(1), trieNode);
            if (b) {
                node.setCount(node.getCount() - 1);
                if (trieNode.getCount() == 0) {
                    node.trieNodes[position] = null;
                }
                return true;
            }
        }

        if (word.length() == 1) {
            char c = word.charAt(0);
            int position = c - 'a';
            TrieNode trieNode = node.trieNodes[position];
            //只删除单词 如果是前缀不删除
            if (trieNode != null && trieNode.getExist()) {
                return true;
            }
        }
        return false;
    }

    // delete if the word is in the trie.
    public boolean delete(String word) {
        return this.doDelete(word,root);
    }


    class TrieNode {
        // Initialize your data structure here.
        int count = 0;//前缀个数

        public int getExistCount() {
            return existCount;
        }

        public void setExistCount(int existCount) {
            this.existCount = existCount;
        }

        int existCount = 0;//插入重复的单词个数
        TrieNode[] trieNodes = new TrieNode[26];
        Boolean exist = false;
        public TrieNode() {
        }

        public TrieNode(int count, Boolean exist,int existCount) {
            this.count = count;
            this.exist = exist;
            this.existCount = existCount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public TrieNode[] getTrieNodes() {
            return trieNodes;
        }

        public void setTrieNodes(TrieNode[] trieNodes) {
            this.trieNodes = trieNodes;
        }

        public Boolean getExist() {
            return exist;
        }

        public void setExist(Boolean exist) {
            this.exist = exist;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.search("lintcode");
        trie.startsWith("lint");
        trie.insert("lint");
        trie.startsWith("lint");

        boolean lint = trie.delete("lin");
        System.out.println("lint = " + lint);
        lint = trie.delete("lint");
        System.out.println("lint = " + lint);
    }
}
```

