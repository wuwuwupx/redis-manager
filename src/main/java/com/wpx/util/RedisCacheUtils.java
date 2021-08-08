package com.wpx.util;

import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: 不会飞的小鹏
 */
public class RedisCacheUtils {

    private StringRedisTemplate stringRedisTemplate;

    public RedisCacheUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     *  缓存3天
     */
    private static final Long TTL = 3L * 24 * 60 * 60;

    /**
     * 获取缓存的过期时间   单位为秒
     *
     * @param key
     * @return Long
     */
    public Long getTtl(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /** =======================================  通过StringRedisTemplate操作Redis  ===================================== */

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
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取redis value中的值
     *
     * @param    key  Redis Key
     * @return   String  从value中获取的数据
     */
    public String getForValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 将数据存放到redis value
     *
     * @param    key  Redis key
     * @param    value  需要存放的数值
     */
    public void setForValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * redis value 赋值并添加过期时间
     * 默认3天
     *
     * @param key  Redis key
     * @param value  需要存放的值
     */
    public void setForValueTtl(String key, Integer value) {
        setForValueTtl(key, value.toString(), TTL, TimeUnit.SECONDS);
    }

    /**
     * redis value 赋值并添加过期时间
     *
     * @param key  Redis key
     * @param value  需要存放的值
     * @param timeout  过期时间  默认单位为秒
     */
    public void setForValueTtl(String key, Integer value, Long timeout) {
        setForValueTtl(key, value.toString(), timeout, TimeUnit.SECONDS);
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
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 对Redis的key进行自增
     *
     * @param    key  Redis key
     * @param    delta  自增的数值  Long
     * @return   Long  自增后的数值
     */
    public Long incrementForValue(String key, Long delta){
        return stringRedisTemplate.opsForValue().increment(key, delta);
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
     * @param    delta  自增的值  Integer
     * @return   Long  自增后的值
     */
    public Long incrementForValue(String key, Integer delta){
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 从redis hash中获取信息
     *
     * @param    key  Redis key
     * @param    hashKey  hash中的 键值对key
     * @return   String
     */
    public String getForHash(String key, String hashKey) {
        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 将数据保存到redis hash
     *
     * @param    key  Redis key
     * @param    hashKey  hash中的 键值对key
     * @param    value  hash中的 键值对value
     */
    public void putForHash(String key, String hashKey, String value){
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * redis hash 每次累加 1
     *
     * @param    key  Redis key
     * @param    hashKey  hash中的 要对value累加的键值对key
     * @return   Long  对应的键值对累加后的value
     */
    public Long incrementForHash(String key, String hashKey){
        return incrementForHash(key, hashKey, 1);
    }

    /**
     * redis hash 累加
     *
     * @param    key  Redis key
     * @param    hashKey  hash中的 要对value累加的键值对key
     * @param    delta  累加的数值 Integer
     * @return   Long  对应的键值对累加后的value
     */
    public Long incrementForHash(String key, String hashKey, Integer delta){
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * redis hash 累加
     *
     * @param    key  Redis key
     * @param    hashKey  hash中的 要对value累加的键值对key
     * @param    delta  累加的数值 Long
     * @return   Long  对应的键值对累加后的value
     */
    public Long incrementForHash(String key, String hashKey, Long delta){
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 从redis hash中获取多个信息
     *
     * @param key
     * @param hashKeySet
     */
    public List<String> multiGetForHash(String key, Set<String> hashKeySet) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        return operations.multiGet(key, hashKeySet);
    }

    /**
     * 将多个键值对数据保存到redis hash
     *
     * @param key      Redis key
     * @param valueMap hash 键值, value
     */
    public void multiPutForHash(String key, Map<String, String> valueMap) {
        stringRedisTemplate.opsForHash().putAll(key, valueMap);
    }

    /**
     * 从Redis hash 获取对应key下的所有key
     *
     * @param    key Redis key
     * @return Set<String> 对应的redis key下的所有key -- 键值对的key
     */
    public Set<String> keysForHash(String key) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.keys(key);
    }

    /**
     * 从redis hash 获取对应key下的所有value
     *
     * @param    key  Redis key
     * @return   List<Object>  对应的redis key下的所有value -- 键值对的value
     */
    public List<String> valuesForHash(String key) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.values(key);
    }

    /**
     * 从redis hash 获取对应key下的所有键值对
     *
     * @param    key  Redis key
     * @return Cursor<Map.Entry < String, String>>  对应的redis key下的所有键值对  链式结构
     */
    public Map<String, String> entriesForHash(String key) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.entries(key);
    }

    /**
     * 从redis hash 获取对应key下的所有键值对
     *
     * @param    key  Redis key
     * @return Cursor<Map.Entry < String, String>>  对应的redis key下的所有键值对  链式结构
     */
    public Cursor<Map.Entry<String, String>> getAllForHash(String key) {
        HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
        return ops.scan(key, ScanOptions.NONE);
    }

    /**
     * 从redis hash 中删除元素
     *
     * @param    key  Redis key
     * @param    hashKey  hash中的 键值对key
     */
    public void deleteForHash(String key, String hashKey){
        stringRedisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 从redis hash 中删除元素
     *
     * @param    key  Redis key
     * @param    hashKeys  hash中的 键值对key
     */
    public void deleteForHash(String key, Object[] hashKeys){
        stringRedisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 从Redis list中pop数据
     *
     * @param    key  Redis Key
     * @return   String  对应的key下获取的值 从list左端pop
     */
    public String leftPopForList(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从Redis list中pop数据
     *
     * @param    key  Redis Key
     * @return   String  对应的key下获取的值 从list右端pop
     */
    public String rightPopForList(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从Redis list中push数据
     *
     * @param    key  Redis key
     * @param    value  存入list中的值  从list左端push
     */
    public void leftPushForList(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从Redis list中push数据
     *
     * @param    key  Redis key
     * @param    value  存入list中的值  从list右端push
     */
    public void rightPushForList(String key, String value) {
        stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     *
     * 将集合导入redis list
     *
     * @param    key  Redis Key
     * @param    list  传入的数据集合  从redis左端传入
     */
    public void lestPushAllForList(String key, List<String> list){
        stringRedisTemplate.opsForList().leftPushAll(key, list);
    }

    /**
     * 将集合导入redis list
     *
     * @param    key  Redis Key
     * @param    list  传入的数据集合  从redis右端传入
     */
    public void rightPushAllForList(String key, List<String> list){
        stringRedisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     *
     * 获取redis list的size
     *
     * @param    key  Redis key
     * @return   Long  list的size
     */
    public Long sizeForList(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * 从redis list中range区间数据
     *
     * @param    key  Redis key
     * @param    start  区间的开始
     * @param    end  区间的结束
     * @return   List<String>  从区间获取的数据集合
     */
    public List<String> rangeForList(String key, Long start, Long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 向Redis Set中添加元素
     *
     * @param    key
     * @param    value
     */
    public void addForSet(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    /**
     * 向Redis Set中批量添加元素
     *
     * @param    key
     * @param    values
     */
    public void addForSet(String key, String[] values) {
        stringRedisTemplate.opsForSet().add(key, values);
    }

    /**
     * 从 Redis Set 中pop元素
     *
     * @param    key
     */
    public String popForSet(String key) {
        return stringRedisTemplate.opsForSet().pop(key);
    }

    /**
     * 从 Redis Set 中pop元素
     *
     * @param    key
     * @param    count
     */
    public List<String> popForSet(String key, Long count) {
        return stringRedisTemplate.opsForSet().pop(key, count);
    }

    /**
     * 获取Redis Set中的所有元素
     *
     * @param key
     */
    public Set<String> membersForSet(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * set中是否存在对应的成员
     *
     * @param key
     * @param value
     */
    public Boolean existMemberInSet(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取两个set的交集
     *
     * @param keyAlpha
     * @param keyBeta
     * @return
     */
    public Set<String> getSetsIntersect(String keyAlpha, String keyBeta) {
        return stringRedisTemplate.opsForSet().intersect(keyAlpha, keyBeta);
    }

    /**
     * 从 Redis set 中移除元素
     *
     * @param key
     * @param value
     */
    public Long removeForSet(String key, String value) {
        return stringRedisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 从 Redis set 中批量移除元素
     *
     * @param key
     * @param values
     */
    public Long removesForSet(String key, Object[] values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取set成员数量
     *
     * @param key
     */
    public Long sizeForSet(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 累加redis sort set 的数值
     *
     * @param    key  Redis key
     * @param    value  需要累加的value -- 累加项
     * @param    score  累加的数值 -- 分数  Double
     */
    public void incrementScoreForZSet(String key, String value, Double score){
        stringRedisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 累加redis sort set 的数值
     *
     * @param    key  Redis key
     * @param    value  需要累加的value -- 累加项
     * @param    score  累加的数值 -- 分数  Integer
     */
    public Double incrementScoreForZSet(String key, String value, Integer score){
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 累加redis sort set 的数值
     *
     * @param    key  Redis key
     * @param    value  需要累加的value -- 累加项
     * @param    score  累加的数值 -- 分数  Integer
     */
    public Double incrementScoreForZSet(String key, String value, Long score){
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 将 tupleSet 存入 redis sort set
     *
     * @param key  Redis key
     * @param tuples  需要存入的Set<Tuple>
     */
    public void addTupleSetForZSet(String key, Set<ZSetOperations.TypedTuple<String>> tuples) {
        stringRedisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * 将 Map 存入 redis sort set
     *
     * @param key  Redis key
     * @param map  需要存入的map
     */
    public void addMapForZSet(String key, Map<String, Double> map) {
        Set<ZSetOperations.TypedTuple<String>> tuples = map.entrySet().stream()
                .map(s -> new DefaultTypedTuple<>(s.getKey(), s.getValue())).collect(Collectors.toSet());
        addTupleSetForZSet(key, tuples);
    }

    /**
     *  获取redis sort set的分值
     *
     * @param    key  Redis key
     * @param    value  需要获取分数的value
     * @return   Double  value对应的score -- 分数
     */
    public Double getScoreForZSet(String key, String value){
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    /**
     *  获取ZSet的 降序排名 -- 从大到小
     *
     * @param    key  Redis
     * @param    value
     * @return   Long
     */
    public Long getRankForZSet(String key, String value){
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 从 Redis ZSet 中获取 start -- end
     *
     * @param    key  Redis key
     * @param    start  区间开始  0开始
     * @param    end  区间结束
     * @return   Set<TypedTuple<String>> value和score
     */
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScoresForZSet(String key, Long start, Long end){
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取一定排名的value
     *
     * @param key  Redis key
     * @param start  区间开始 0开始
     * @param end  区间结束
     * @return: Set<String>  返回的value
     */
    public Set<String> reverseRangeForZSet(String key, Long start, Long end){
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 从 Redis ZSet 中获取集合元素个数
     *
     * @param key
     */
    public Long sizeForZSet(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * 匹配按照规则指定的前缀的key
     *
     * @param    key  需要匹配的key
     * @return   Set<String>  匹配的key集合
     */
    public Set<String> getKeys(String key) {
        return stringRedisTemplate.keys(key);
    }

    /**
     * 从Redis 中删除某个key
     *
     * @param    key  删除的Redis key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除指定的所有key
     *
     * @param keys
     */
    public void deleteForAll(Set<String> keys) {
        stringRedisTemplate.delete(keys);
    }

}
