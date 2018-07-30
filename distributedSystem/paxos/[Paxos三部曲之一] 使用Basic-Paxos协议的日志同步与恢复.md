## [Paxos三部曲之一] 使用Basic-Paxos协议的日志同步与恢复 ##

### 传统模式 ###
业界常见MySQL/Oracle的1主N备的方案面临的问题是“最大可用（Maximum Availability）”和“最大保护（Maximum Protection）”模式间的艰难抉择：  

* 最大可用
	* 主机尽力将数据同步到备机之后才返回成功
	* 如果备机宕机或网络中断那么主机则单独提供服务
	* 这意味着主备都宕机情况下可能的数据丢失

* 最大保护
	* 主机一定要将数据同步到备机后才能返回成功
	* 意味着在任意备机宕机或网络中断情况下主机不得不停服务等待备机或网络恢复

* 可见传统主备方式下，如果要求数据不丢，那么基本放弃了服务的持续可用能力


### Paxos 模式 ###
* Paxos只需任意超过半数的副本在线且相互通信正常
* 就可以保证服务的持续可用，且数据不丢失

### 原理 ###
* 将每条日志的持久化流程都看作一个“Paxos Instance”
* 不同的logID代表不同的Paxos Instance形成的“决议（decision）”
* 即每一个logID标识着一轮完整paxos协议流程的执行，最后形成decision
* 机群内的每个server同时作为paxos的acceptor和proposer

#### 获取LogID ####
* 这里并不能保证并发提交的两条日志一定被分配到不同的logID，而是依靠后续的paxos协议流程来达到对一个logID形成唯一的decision的目的

#### 产生ProposalID ####
* proposalID要满足全局唯一和递增序
* 即对同一个server来说后产生的proposalID一定大于之前产生的
* 这里我们使用server的timestamp联合ip作为proposalID，其中timestamp在高位，ip在低位，只要时钟的误差范围小于server重启的时间，就可以满足“同一个server后产生的proposalID一定大于之前产生的”

#### Prepare阶段 ####
* 将proposalID作为 “提案（proposal）”发送给所有的acceptor
* 根据Paxos协议P1b的约束，这个阶段发送的proposal并不需要携带日志内容，而只需要发送proposalID
* 对于分配到相同logID的不同日志，由于他们的proposalID不同，acceptor在response一个较小proposalID后，是允许继续response后来的较大的proposalID的



