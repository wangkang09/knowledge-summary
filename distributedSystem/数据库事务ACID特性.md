## Atomicity ##
* 原子性，整个事务中的所有操作，要么全部完成，要么全部不完成
* 事务在执行过程中发生错误，会被回滚（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样


## Consistency ##
* 一个事务完成后，数据的状态应该和期望的一致，即不能被其他事务改变

## Isolation ##
* 当前事务看不到，另一个未完成的事务修改的结果

## Durability ##
* 在事务完成以后，该事务对数据库所作的更改便持久的保存在数据库之中，并不会被回滚。若系统故障，已提交的事务，会被恢复

## 实现方式 ##
* Write ahead logging:is a more popular solution that uses in-place updates
* Shadow paging: is a copy-on-write technique for avoiding in-place updates of pages。多版本控制

### WAL ###
* all modifications are written to a log before they are applied. Usually both redo and undo information is stored in the log
* 这样数据库出现问题，也可以通过日志来恢复。因为日志已经持久化了

#### redo ####
* 在修改数据块之前，立即将一个redo change vector 写入redo log buffer（大部分，有些不写redo）,最终写入 redo log，这个写入时间越快，系统恢复数据月完整
* 做undo的目的是使系统恢复到系统崩溃前(关机前)的状态,再进行redo是保证系统的一致性。不做undo,系统就不会知道之前的状态,redo就无从谈起

#### undo ####
* 数据修改前的备份，记录旧值，保证用户的读一致性
* 在事务修改数据时产生
* 至少保存到事务结束，直到undo失效，被其他undo覆盖
* 回滚操作，从失败的事务中还原数据
* 非正常停机后的实例恢复，**恢复到系统奔溃前**
* **undo 日志可以看成普通的更新数据，也要记录到redo中**，也有data buffer


1. oracle 数据库会记录数据两次
	1. 在 datafiles中记录最新的数据（可能有更新的数据在缓存中没有刷新到 datafiles中）
	2. 在 redo log files中，当datafiles发生故障时，用于恢复数据
	3. data包含索引和数据，在某些场合还包含undo

2. 数据变化前的准备
	1. 创建一个redo变化向量，来描述如何将undo记录插入undo块
	2. 创建一个redo变化向量，来描述数据的变化
	3. 合并以上两个redo信息到一个redo记录中，并将此记录写入log buffer
	4. 将undo记录插入undo块
	5. 改变数据块
