# Redis 入门 #
## Redis 基本语法 ##

基本类型 
---
set name wang `set key value` `设置键值对`  
get name `get key` `获取key的value值`
getset name kang `getset key newValue` `想获取key的旧值，再设置该key的新值`  

del name `del key` `删除key值`  
del name1 name2 

exists name `存在返回1，否则0`  
rename name namm `重命名`  

incr num `incr key` `自增key的value，value必须是integer，若key不存在，创建此key，且默认是0，自增后成1`  
decr num `同incr key`  
incrby num 7 `在num基础上+7`  
decrby num 7 `同incrby`  

keys * `查询所有的键值`  
keys n* `模糊匹配`  

expire name 1000 `给name键值设置超时时间为1000s`  
ttl name `查看name的剩余时间`  

append name kang `appen key value` `字符串append，返回的是字符串的长度`  

type mylist `获取类型`  

哈希类型 
---
hset myhash username jack  `在myhash中存 key value`
hset myhash age 18  
hmset myhash2 username rose age 21  `存多个key value`  
hget myhash username  `获取单个key的值`  
hmget myhash username age  `获取多个key的值`  
hgetall myhash `获取所有的 key value`  
hdel myhash2 username age `删除多个属性`  
del myhash2 `删除整个集合`  
hincrby myhash age 7 `增加值`  
hexists myhash username `查看username是否存在`  
hlen myhash `获得myhash集合的长度`  
hkeys myhash `获得所有键值`  
hvalues myhash  

list类型：是链表结构，在头部/尾部操作，速度快
---
lpush mylist a b c   `c最后进去，所有是在第一位`
lpush mylist 1 2 3  
rpush mylist2 a b c  
rpush mylist2 1 2 3  

lrange mylist 0 5  
lrange mylist 0 -1 `和上一个效果一样`  

lpop mylist `返回并弹出第一个值`  
rpop mylist 

llen mylist `获取长度`  

lpushx mylist x `如果mylist存在，才插入`  
rpushx mylist x `同理`  

lrem mylist 2 3 `从头到尾删除2个3`  
lrem mylist -2 1 `从尾到头删除2个1`  
lrem mylist 0 2 `删除所有的2`  

lset mylist 3 mmm `将第三个角标的值换成mmm`  
lpust mylist before b 11  `在第一个b之前插入11`  
lpust mylist after b 11  `在第一个b之前插入11`  


rpoplpush mylist1 mylist2 `将mylist1的最后一位压出到mylist2的最前面`  

set类型</font>
---
sadd myset a b c  
srem myset 1 2  
smembers myset  

sismember myset a  `查看a是不是在myset集合中，在返回1，否则返回0`  

sdiff mya myb `输出两个集合的不同元素`  
sinter mya myb `输出两个集合都有的`  
sunion mya myb `并集`  

scard myset `返回集合的长度`  
srandmember myset `随机返回集合里的一个数`  

sdiffstore my1 mya myb `将mya myb相差的集合存到my1中`   
sinterstore my1 mya myb `将mya myb的交集存到my1中`  
sunion my1 mya myb `将mya myb的并集存到my1中`  

sorted-set类型：按成员的分数排序，分数可以有一样的，元素还是不行
---
在sorted-set中 增删改 都很快，时间复杂度为logn  
因为在集合中是有序的，所以访问任何一个元素都很快  

场景：游戏排名，微博热点  

zadd mysort 70 zs 80 ls 90 ww  `为每个元素添加分数，返回的是添加的个数`  
zadd mysort 100 zs `如果有zs，那么用新的分数覆盖旧的分数`  
zscore mysort zs `获得 zs 的分数`  

zcard mysort `返回数量`  
zrem mysort zs ww `删除`  

zrange mysort 0 -1 `返回第一个到最后一个元素`  
zrange mysort 0 -1 withscores `返回带分数，默认排序asc`  
zrevrange mysort 0 -1 withscores `desc`  

zremrangebyrank mysort 0 4 `按分数排名删除0-4元素`  
zremrangebyscore mysort 80 100 `删除分数在80-100之间的元素`  

zrangebyscore mysort 0 100 withscores limit 0 2 `返回带分数的分数为0-100之间的元素，从第一0个开始，返回2个`  

zincrby mysort 3 ls `将ls加3分`  
zscore mysort ls `获取ls的分数`  

zcount mysort 80 -90 `获取80-90之间的数量`  

## Redis 特性 ##
* 多数据库  
* 事务  
* 持久化

多数据库 
---
最多是16个数据库  
select 1 `选择1号数据库`  
move myset 1 `将myset移到1号数据库中`  

事务 
---
redis是串行化机制  
某一个事务失败，后面的事务正常运行  

multi `相当于开启一个事务，这个语句之后的命令都是在一个事务里，知道遇到exec或discard`    
exec `相当与提交`  
discard `放弃当前事务，相当于回滚`  

持久化 
---
* RDB方式：在指定的时间间隔，把数据集快照存储到磁盘一次。默认。  
* AOF方式：以日志的形式，记录服务器的每一个操作，每次服务器开启，都要读取该文件，重新构建数据库，保证启动后，数据时完整的。
* 无持久化：
* 同时使用RDB和AOF

RDB   
优势：  







