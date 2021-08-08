package com.wpx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wpx.util.CollectionUtils;
import com.wpx.util.RedisCacheUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: 不会飞的小鹏
 * @Description: 在RedisCacheUtils的基础上封装，将数据序列化
 */
public class RedisBaseService {

    private RedisCacheUtils redisCacheUtils;

    public RedisBaseService(RedisCacheUtils redisCacheUtils) {
        this.redisCacheUtils = redisCacheUtils;
    }

    /**
     * 缓存3天
     */
    private static final Long TTL = 3L * 24 * 60 * 60;

    /**
     * 字符串转换为对象
     *
     * @param str
     * @param target
     */
    private <T> T strToObject(String str, Class<T> target) {
        try {
            return StringUtils.isEmpty(str) ? target.newInstance() : JSON.parseObject(str, target);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转换为list
     *
     * @param str
     * @param target
     */
    private <T> List<T> strToList(String str, Class<T> target) {
        return StringUtils.isEmpty(str) ? new ArrayList<>() : JSONArray.parseArray(str, target);
    }

    /**
     * set转换，将字符串set集合转换为对应的对象集合
     *
     * @param strList
     * @param target
     */
    private <T> Set<T> setStrToSet(Set<String> strList, Class<T> target) {
        return CollectionUtils.isEmpty(strList) ? new HashSet<>()
                : strList.stream().map(s -> strToObject(s, target)).collect(Collectors.toSet());
    }

    /**
     * list转换,将字符串list集合转换为对应的对象集合
     *
     * @param strList
     * @param target
     */
    private <T> List<T> listStrToList(List<String> strList, Class<T> target) {
        return CollectionUtils.isEmpty(strList) ? new ArrayList<>()
                : strList.stream().map(s -> strToObject(s, target)).collect(Collectors.toList());
    }

    /**
     * 转换map，将map的value转换为对应的对象
     *
     * @param map
     * @param target
     */
    private <T> Map<String, T> conversionMap(Map<String, String> map, Class<T> target) {
        Map<String, T> resultMap = new HashMap<>();
        if (CollectionUtils.nonEmpty(map)) {
            map.forEach((key, value) -> resultMap.put(key, strToObject(value, target)));
        }
        return resultMap;
    }


    /**
     * 为key设置过期时间
     * 默认3天  3600 * 24 * 3
     * 单位默认为秒
     *
     * @param    key
     */
    public void expire(String key) {
        expire(key, TTL);
    }

    /**
     * 为key设置过期时间
     * 单位默认为秒
     *
     * @param    key
     * @param    timeout
     */
    public void expire(String key, long timeout) {
        expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 为key设置过期时间
     * 单位默认为秒
     *
     * @param    key
     * @param    timeout
     */
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisCacheUtils.expire(key, timeout, timeUnit);
    }


    /**
     * 将数据存放到redis value
     *
     * @param key
     * @param value
     */
    public void setForValue(String key, String value) {
        redisCacheUtils.setForValue(key, value);
    }

    /**
     * 将数据存放到redis value
     *
     * @param key
     * @param data
     */
    public <T> void setForValue(String key, T data) {
        String value = Objects.isNull(data) ? "" : JSON.toJSONString(data);
        redisCacheUtils.setForValue(key, value);
    }

    /**
     * redis value 赋值并添加过期时间
     * 默认3天
     *
     * @param key  Redis key
     * @param value  需要存放的值
     */
    public void setForValueTtl(String key, String value) {
        setForValueTtl(key, value, TTL);
    }

    /**
     * redis value 赋值并添加过期时间
     *
     * @param key  Redis key
     * @param value  需要存放的值
     * @param timeout  过期时间  默认单位为秒
     */
    public void setForValueTtl(String key, String value, Long timeout) {
        setForValueTtl(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * redis value 赋值并添加过期时间
     *
     * @param key  Redis key
     * @param value  需要存放的值
     * @param timeout  过期时间
     * @param timeUnit  时间单位
     */
    public void setForValueTtl(String key, String value, Long timeout, TimeUnit timeUnit) {
        redisCacheUtils.setForValueTtl(key, value, timeout, timeUnit);
    }

    /**
     * 对Redis的key进行自增
     * 无指定数值  默认为 1
     *
     * @param    key  Redis key
     * @return   Long   自增后的数值
     */
    public Long incrementForValue(String key){
        return incrementForValue(key, 1);
    }

    /**
     * 对Redis的key进行自增
     *
     * @param    key  Redis key
     * @param    delta  自增的数值  Long
     * @return   Long  自增后的数值
     */
    public Long incrementForValue(String key, Long delta){
        return redisCacheUtils.incrementForValue(key, delta);
    }

    /**
     * 对Redis的key进行自增
     *
     * @param    key  Redis key
     * @param    delta  自增的值  Integer
     * @return   Long  自增后的值
     */
    public Long incrementForValue(String key, Integer delta){
        return redisCacheUtils.incrementForValue(key, delta);
    }

    /**
     * 从Redis value中获取值
     *
     * @param key
     */
    public String getForValue(String key) {
        return redisCacheUtils.getForValue(key);
    }

    /**
     * 从Redis value中获取值并转换为对应对象
     *
     * @param key
     * @param target
     */
    public <T> T getForValue(String key, Class<T> target) {
        return strToObject(redisCacheUtils.getForValue(key), target);
    }

    /**
     * 从Redis value中获取值并转换为对应的对象list
     *
     * @param key
     * @param target
     */
    public <T> List<T> listForValue(String key, Class<T> target) {
        return strToList(redisCacheUtils.getForValue(key), target);
    }

    /**
     * 从Redis hash中获取值并转换为对应对象
     *
     * @param key
     * @param hashKey
     */
    public String getForHash(String key, String hashKey) {
        return redisCacheUtils.getForHash(key, hashKey);
    }

    /**
     * 从Redis hash中获取值并转换为对应对象
     *
     * @param key
     * @param hashKey
     * @param target
     */
    public <T> T getForHash(String key, String hashKey, Class<T> target) {
        return strToObject(redisCacheUtils.getForHash(key, hashKey), target);
    }

    /**
     * 从Redis hash中批量获取值
     *
     * @param key
     * @param hashKeySet
     */
    public List<String> multiGetForHash(String key, Set<String> hashKeySet) {
        return redisCacheUtils.multiGetForHash(key, hashKeySet);
    }

    /**
     * 从Redis hash中批量获取值并转换为对应对象集合
     *
     * @param key
     * @param hashKeySet
     * @param target
     */
    public <T> List<T> multiGetForHash(String key, Set<String> hashKeySet, Class<T> target) {
        return listStrToList(redisCacheUtils.multiGetForHash(key, hashKeySet), target);
    }

    /**
     * 从Redis hash中获取所有的key
     *
     * @param key
     */
    public Set<String> keysForHash(String key) {
        return redisCacheUtils.keysForHash(key);
    }

    /**
     * 从Redis hash中获取所有的value
     *
     * @param key
     */
    public List<String> valuesForHash(String key) {
        return redisCacheUtils.valuesForHash(key);
    }

    /**
     * 从Redis hash中获取所有的value并转换为对应对象集合
     *
     * @param key
     * @param target
     */
    public <T> List<T> valuesForHash(String key, Class<T> target) {
        return listStrToList(redisCacheUtils.valuesForHash(key), target);
    }

    /**
     * 从Redis hash中获取所有的entry
     *
     * @param key
     */
    public Map<String, String> entriesForHash(String key) {
        return redisCacheUtils.entriesForHash(key);
    }

    /**
     * 从Redis hash中获取所有的entry并将value转换为对应对象
     *
     * @param key
     * @param target
     */
    public <T> Map<String, T> entriesForHash(String key, Class<T> target) {
        return conversionMap(redisCacheUtils.entriesForHash(key), target);
    }

    /**
     * 从Redis list 左端pop元素
     *
     * @param key
     */
    public String leftPopForList(String key) {
        return redisCacheUtils.leftPopForList(key);
    }

    /**
     * 从Redis list 左端pop元素并转换为对应对象
     *
     * @param key
     * @param target
     */
    public <T> T leftPopForList(String key, Class<T> target) {
        return strToObject(redisCacheUtils.leftPopForList(key), target);
    }

    /**
     * 从Redis list 右端pop元素
     *
     * @param key
     */
    public String rightPopForList(String key) {
        return redisCacheUtils.rightPopForList(key);
    }

    /**
     * 从Redis list 右端pop元素并转换为对应对象
     *
     * @param key
     * @param target
     */
    public <T> T rightPopForList(String key, Class<T> target) {
        return strToObject(redisCacheUtils.rightPopForList(key), target);
    }

    /**
     * 从Redis list 批量获取元素
     *
     * @param key
     * @param start
     * @param end
     */
    public List<String> rangeForList(String key, long start, long end) {
        return redisCacheUtils.rangeForList(key, start, end);
    }

    /**
     * 从Redis list 批量获取元素并转换为对应对象集合
     *
     * @param key
     * @param start
     * @param end
     * @param target
     */
    public <T> List<T> rangeForList(String key, long start, long end, Class<T> target) {
        return listStrToList(redisCacheUtils.rangeForList(key, start, end), target);
    }

    /**
     * 从Redis set pop元素
     *
     * @param key
     */
    public String popForSet(String key) {
        return redisCacheUtils.popForSet(key);
    }

    /**
     * 从Redis set pop元素并转换为对应对象
     *
     * @param key
     * @param target
     */
    public <T> T popForSet(String key, Class<T> target) {
        return strToObject(redisCacheUtils.popForSet(key), target);
    }

    /**
     * 从Redis set 批量pop元素
     *
     * @param key
     * @param count
     */
    public List<String> popForSet(String key, long count) {
        return redisCacheUtils.popForSet(key, count);
    }

    /**
     * 从Redis set 批量pop元素并转换为对应对象
     *
     * @param key
     * @param count
     * @param target
     */
    public <T> List<T> popForSet(String key, long count, Class<T> target) {
        return listStrToList(redisCacheUtils.popForSet(key, count), target);
    }

    /**
     * 从Redis set 获取所有的元素
     *
     * @param key
     */
    public Set<String> membersForSet(String key) {
        return redisCacheUtils.membersForSet(key);
    }

    /**
     * 从Redis set 获取所有的元素并转换为对应的对象
     *
     * @param key
     * @param target
     */
    public <T> Set<T> membersForSet(String key, Class<T> target) {
        return setStrToSet(redisCacheUtils.membersForSet(key), target);
    }

}
