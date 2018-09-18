package otherAlgorithm;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:26 2018/9/14
 * @Modified By:
 */
public class MinStack {
    private Deque<Integer> stack = new LinkedList<>();
    private Deque<Integer> stackMin = new LinkedList<>();

    public boolean push(Integer o) {
        stack.add(o);
        Integer last = stackMin.peekLast();
        if(last==null) {
            stackMin.add(o);
        } else {
            if(last<=o) stackMin.add(last);
            else stackMin.add(o);
        }
        return true;
    }

    public Integer pop() {
        stackMin.pollLast();
        return stack.pollLast();
    }

    public Integer getMin() {
        return stackMin.peekLast();
    }
}

class Main {
    public static void main(String[] args) {
        MinStack min = new MinStack();
        min.push(1);
        min.push(3);
        min.push(2);
        min.push(-1);
        System.out.println(min.getMin());
        min.pop();
        System.out.println(min.getMin());
        min.pop();
        System.out.println(min.getMin());
        min.pop();
        System.out.println(min.getMin());
    }
}