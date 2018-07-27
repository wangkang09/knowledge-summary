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

