package com.yellowhao.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * @PACKAGE_NAME: com.yrp.util
 * @NAME: RedisUtil
 * @AUTHOR: 如意郎君
 * @DATE: 2020/10/1
 * @TIME: 13:19
 * @DAY_NAME_SHORT: 星期四
 * @VERSION: 1.0
 */
public class RedisUtil {


    public static <T> Object gteCache(String key){

        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        RedisTemplate redisTemplate = (RedisTemplate) wac.getBean("redisTemplate");

        //设置redisTemplate的key序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //从redis中获取
        return redisTemplate.opsForValue().get("key");

    }


}
