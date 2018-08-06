## 读取操作 ##
    g_hv_array[thread_id] = g_version;
    ret = g_conf;
    // 读取逻辑代码…
    g_hv_array[thread_id] = INT64_MAX;

* 先读取全局对象 g_version
* 在缓存 g_conf，这时的 g_conf 的 g_version 可能大于 缓存的 g_version
* 操作完后，将此线程的局部变量置 最大值，待到释放的时候，保证大于 全局变量，说明此线程已完成操作，可以成功释放

## 使用新的对象替换旧对象 ##

    set_new_conf(new_conf) {

      retired_ptr = atomic_store_and_fetch_old(&g_conf, new_conf);
      retired_ptr->set_version(atomic_fetch_and_add(&g_version, 1));

      reclaim_version = INT64_MAX;

      for (i = 0; i < g_hv_array.length(); i++) {
	    if (reclaim_version > g_hv_array[i]) {
	      reclaim_version = g_hv_array[i];
	    }
      }

      if (reclaim_version > retired_ptr->get_version()) {
    	free(retired_ptr);
      }
    }

* 先原子替换 g_conf
* 将老的 g_conf 版本加1
* 获取数组中最小的 version,要知道 数组中的 version 很可能都小于实际的值
* 但是当线程执行完了之后，就置最大值了，所有只要所有的线程都执行完，肯定能释放
* **当有一个线程运行很长时间时，就会出现资源不释放的情况**