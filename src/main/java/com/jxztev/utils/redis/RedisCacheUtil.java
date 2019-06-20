package com.jxztev.utils.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
* @author winnerlbm
* @date 2018年5月7日
* @desc Redis Hash处理工具类
*/
@Component("redisCache")
public class RedisCacheUtil {

@Resource
private StringRedisTemplate stringRedisTemplate;

/**设置Hash表、字段、值
* @param key : redis hash表名
* @param field : hash表字段
* @param value : hash表字段值
*/
public void hashSet(String key, String field, String value) {
if (key == null || "".equals(key)) {
return;
}
stringRedisTemplate.opsForHash().put(key, field, value);
}

/**
* @param key : redis hash表名
* @param field : hash表字段
* @return hash表字段值
*/
public String hashGet(String key, String field) {
if (key == null || "".equals(key)) {
return null;
}
return (String) stringRedisTemplate.opsForHash().get(key, field);
}

public boolean hashExists(String key, String field) {
if (key == null || "".equals(key)) {
return false;
}
return stringRedisTemplate.opsForHash().hasKey(key, field);
}

public long hashSize(String key) {
if (key == null || "".equals(key)) {
return 0L;
}
return stringRedisTemplate.opsForHash().size(key);
}

public void hashDel(String key, String field) {
if (key == null || "".equals(key)) {
return;
}
stringRedisTemplate.opsForHash().delete(key, field);
}
}
