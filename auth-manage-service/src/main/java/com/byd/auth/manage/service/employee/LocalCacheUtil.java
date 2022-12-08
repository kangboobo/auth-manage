package com.byd.auth.manage.service.employee;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zhu.changgen
 * @version 1.0
 * @date 2022/12/2 15:57
 * @title
 * @description
 */
public class LocalCacheUtil {

    // 缓存定时认为执行器
    private static ScheduledExecutorService refreshExecutor =
            new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory());

    // 缓存
    private static volatile ConcurrentHashMap<String, CacheElement> cacheMap = new ConcurrentHashMap<>();

    // 缓存对象
    static class CacheElement {
        private Object value;

        private Long expire;

        public CacheElement(Object value, Long timeout) {
            this.value = value;
            this.expire = System.currentTimeMillis() + timeout;
        }

        public boolean isOvertime() {
            return expire < System.currentTimeMillis();
        }
    }

    public static Object load(String key) {
        CacheElement cacheElement = cacheMap.get(key);
        if (cacheElement == null || cacheElement.isOvertime()) {
            return null;
        }
        return cacheElement.value;
    }

    public static void save(String key, Object value, Long timeout) {
        cacheMap.put(key, new CacheElement(value, timeout));
    }

    public static void clearCache(String key) {
        cacheMap.remove(key);
    }

    static {
        refreshExecutor.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, CacheElement> elementEntry : cacheMap.entrySet()) {
                if(elementEntry.getValue().isOvertime()){
                    cacheMap.remove(elementEntry.getKey());
                }
            }
        }, 10, (long) (0.5 * 60),TimeUnit.SECONDS);
    }
}
