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
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/%E5%8D%95%E9%93%BE%E8%A1%A8%E6%8C%87%E9%92%88%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" />  
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/CAS_PUSH%E5%89%8D_%E5%8D%95%E9%93%BE%E8%A1%A8%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" />  
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/CAS_PUSH%E5%90%8E_%E5%8D%95%E9%93%BE%E8%A1%A8%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" /> 
第一张图，表示一个完整的单链表Stack

* 第二张图，表示进行CAS操作前的Stack
* 第三张图，表示进行CAS后的Stack
* 因为通过while循环CAS，保证了curr肯定是最新的头结点，后面线程取到的头结点就是这个node3了
* node3->next=node0，这一操作，延迟一点没关系

POP操作图
---
<img src="https://raw.githubusercontent.com/wangkang09/knowledge-summary/master/c/img/CAS_POP_%E5%8D%95%E9%93%BE%E8%A1%A8%E5%9B%BE%E7%A4%BA.png"  alt="不见了" width="200" height="200" />

*  Stack状态是 n0->n1->n2,top指向no
*  线程I将top（即节点A的地址）赋值给curr，并取得curr→next指针（为B）放入寄存器
*  线程II执行完整pop流程，Stack状态变为B→C，top指向B，节点A被弹出后释放
*  线程II执行完整pop流程，Stack状态变为C，top指向C，节点B被弹出后释放
*  线程II执行完整push流程，新申请节点为被复用的A，Stack状态变为A→C，top指向A
*  线程I继续执行，CAS判断top值仍为A，则原子替换top为B，链表被改坏…

### 关键

* 如果是尾插头出，就不会出现这个问题了

## 无锁引用解决ABA问题

### 产生ABA问题的根源

CAS(&top, old, curr→next)这一步不是原子操作  

* curr->next为第一步，取得curr->next引用到寄存器中
* CAS操作为第二步

### 解决方法

通过无锁引用，引入版本号，来解决原子性问题

