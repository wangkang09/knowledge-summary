## 使用Hazard Version实现的无锁Stack与Queue ##

### ABA问题，使用CAS对Stack进行push/pop ###
    void push(Node *node) {
    	Node *curr = top;
    	Node *old = curr;
    	node→next = curr;
    	while (old != (curr = CAS(&top, old, node))) {
    		old = curr;
    		node→next = curr;		
    	}
    }
    Node *pop() {
    	Node *curr = top;
    	Node *old = curr;
    	while (NULL != curr
    		old != (curr = CAS(&top, old, curr→next))) {
    		old = curr;
    	}
    	return curr
    }
PUSH操作图
---
<div align=center>
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/%E5%8D%95%E9%93%BE%E8%A1%A8%E6%8C%87%E9%92%88%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" />  
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/CAS_PUSH%E5%89%8D_%E5%8D%95%E9%93%BE%E8%A1%A8%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" />  
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/CAS_PUSH%E5%90%8E_%E5%8D%95%E9%93%BE%E8%A1%A8%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" /> 
</div>

* 第一张图，表示一个完整的单链表Stack
* 第二张图，表示进行CAS操作前的Stack
* 第三张图，表示进行CAS后的Stack
* 因为通过while循环CAS，保证了curr肯定是最新的头结点，后面线程取到的头结点就是这个node3了
* node3->next=node0，这一操作，延迟一点没关系

POP操作图
---
