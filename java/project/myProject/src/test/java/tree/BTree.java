package tree;

import java.util.List;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:05 2018/9/3
 * @Modified By:
 */
public class BTree {
    private BTreeNode node;
    private List<BTreeNode> NodeLists;

    public static class BTreeNode {
        private int address;//物理地址
        private BTreeNode next;
        private String keyWord;
        private Boolean leaf;//是否是叶子节点
    }
}
