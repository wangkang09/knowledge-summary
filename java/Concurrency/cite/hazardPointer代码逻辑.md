## 读取操作 ##

    while(true) {

      ret = g_conf;
      g_hp_array[thread_id] = ret;

      if (g_conf == ref){ //相比无锁计数因为有引用+1的操作，所以只能通过seq_id来共同形成一个原子操作，而不能通过比对地址
    	break;
      } else {
    	g_hp_array[thread_id] = NULL;
      }
    }
    // 读取后的操作…
    g_hp_array[thread_id] = NULL;//操作完之后设为null


* g_hp_array[thread_id] = ret
	* 保存每个线程局部信息的数组，长度为线程数
* 通过循环，获取最新的g_conf
* 线程完成工作后，将线程的局部信息设为null

## 使用新的对象替换旧对象 ##

    set_new_conf(new_conf) {

      retired_ptr = atomic_store_and_fetch_old(&g_conf, new_conf);
      found = false;

      for (i = 0; i < g_hp_array.length(); i++) {
    	if (retired_ptr == g_hp_array[i]) {
	      found = true;
	      break;
	    }
      }

      if (!found) {
    	free(retired_ptr);
      }
    }
