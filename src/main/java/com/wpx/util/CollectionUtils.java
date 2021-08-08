package com.wpx.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author wpx
 * Created on 2020/11/18 16:17
 * @description: 集合的工具类
 */
public class CollectionUtils {

    private static final String TYPE_NOT_APPOINT = "type_not_appoint";

    public enum ComparableType {

        /**
         * key重复时取最大
         */
        MAX,

        /**
         * key重复时取最小
         */
        MIN,
        ;

    }

    public enum OrderType {

        /**
         * key重复时取最大
         */
        DESC,

        /**
         * key重复时取最小
         */
        ASC,
        ;

    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否为空
     *
     * @param map
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 集合是否包含元素
     *
     * @param collection
     * @param obj
     */
    public static boolean contains(Collection<?> collection, Object obj) {
        return nonEmpty(collection) && collection.contains(obj);
    }

    /**
     * 对list分组
     *
     * @param    list  需要分组的list
     * @param    groupFunction  分组的function
     * @return   Map<R,.List<C>>
     */
    public static <T, R> Map<R, List<T>> groupByList(List<T> list, Function<T, R> groupFunction) {
        return list.stream().collect(Collectors.groupingBy(groupFunction));
    }

    /**
     * 对list分组后的元素取某个字段
     *
     * @param    list  需要分组的list
     * @param    groupFunction  分组的function
     * @param    mappingFunction  分组后的list取某个字段的function
     * @return   Map<R,.List<C>>
     */
    public static <T, R, C> Map<R, List<C>> groupByListMapping(List<T> list, Function<T, R> groupFunction,
                                                               Function<T, C> mappingFunction) {
        return list.stream().collect(Collectors.groupingBy(groupFunction, Collectors.mapping(mappingFunction,
                Collectors.toList())));
    }

    /**
     * 将集合内的元素类型转换，并将集合的类型转换为list
     *
     * @param    collection 传入的集合
     * @param    function <T, C> T传入类型 C 返回类型
     * @return   List<C> 转换后的list
     */
    public static <T, C> List<C> conversionList(Collection<T> collection, Function<T, C> function) {
        return collection.stream().map(function).collect(Collectors.toList());
    }

    /**
     * 将集合内的元素类型转换，并将集合的类型转换为set
     *
     * @param    collection 传入的list
     * @param    function <T, C> T传入类型 C 返回类型
     * @return   Set<C> 转换后的set
     */
    public static <T, C> Set<C> conversionSet(Collection<T> collection, Function<T, C> function) {
        return collection.stream().map(function).collect(Collectors.toSet());
    }

    /**
     * 筛选集合后，返回list
     *
     * @param    collection 传入的list
     * @param    predicate <T> T传入类型
     * @return   Set<C> 转换后的set
     */
    public static <T> List<T> filterList(Collection<T> collection, Predicate<? super T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 筛选集合后，返回set
     *
     * @param    collection 传入的list
     * @param    predicate <T> T传入类型
     * @return   Set<C> 转换后的set
     */
    public static <T> Set<T> filterSet(Collection<T> collection, Predicate<? super T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toSet());
    }

    /**
     * list转化map
     * key重复情况下，比较器的类型默认为MAX最大值
     * 转换后map的value类型默认为T，即传入的list类型
     *
     * @param    list  传入的list
     * @param    keyFunction  获取map的key的function <T, R> T传入的类型，R转换后map的key类型
     * @param    comparableFunction  key重复的情况下获取比较器的function <T, U> T传入的类型，U生成比较器的类型
     * @return   Map<R,T>
     */
    public static <T, R, U extends Comparable<U>> Map<R, T> listToMap(List<T> list, Function<T, R> keyFunction,
                                                                      Function<T, U> comparableFunction) {
        return listToMap(list, keyFunction, comparableFunction, t -> t, ComparableType.MAX);
    }

    /**
     * list 转换map
     * key重复情况下，比较器的类型默认为MAX最大值
     *
     * @param    list  需要转换的list
     * @param    keyFunction  获取map的key的function <T, R> T传入的类型，R转换后map的key类型
     * @param    comparableFunction  key重复的情况下获取比较器的function <T, U> T传入的类型，U生成比较器的类型
     * @param    valueFunction  valueFunction  获取map的value的function <T, C> T传入类型，C转换后map的value类型
     * @return   Map<R,C>
     */
    public static <T, R, U extends Comparable<U>, C> Map<R, C> listToMap(List<T> list, Function<T, R> keyFunction,
                                                                         Function<T, U> comparableFunction,
                                                                         Function<T, C> valueFunction) {
        return listToMap(list, keyFunction, comparableFunction, valueFunction, ComparableType.MAX);
    }

    /**
     * 将list转换为map
     * 转换后map的value类型默认为T，即传入的list类型
     *
     * @param    list  需要转换的list
     * @param    keyFunction  获取map的key的function <T, R> T传入的类型，R转换后map的key类型
     * @param    comparableFunction  key重复的情况下获取比较器的function <T, U> T传入的类型，U生成比较器的类型
     * @param    type  key重复情况下，比较器的类型，MAX最大值或MIN最小值
     * @return   Map<R,T> 转换后的map
     */
    public static <T, R, U extends Comparable<U>> Map<R, T> listToMap(List<T> list, Function<T, R> keyFunction,
                                                                      Function<T, U> comparableFunction,
                                                                      ComparableType type) {
        return listToMap(list, keyFunction, comparableFunction, t -> t, type);
    }

    /**
     * list 转换为map
     *
     * @param    list  需要转换的list
     * @param    keyFunction  获取map的key的function <T, R> T传入的类型，R转换后map的key类型
     * @param    comparableFunction  key重复的情况下获取比较器的function <T, U> T传入的类型，U生成比较器的类型
     * @param    valueFunction  获取map的value的function <T, C> T传入类型，C转换后map的value类型
     * @param    type  key重复情况下，比较器的类型，MAX最大值或MIN最小值
     * @return   Map<R,C>
     */
    public static <T, C, R, U extends Comparable<U>> Map<R, C> listToMap(List<T> list, Function<T, R> keyFunction,
                                                                         Function<T, U> comparableFunction,
                                                                         Function<T, C> valueFunction,
                                                                         ComparableType type) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        Set<R> rSet = conversionSet(list, keyFunction);
        return rSet.size() == list.size() ? toMap(list, keyFunction, valueFunction)
                : toMap(list, keyFunction, comparableFunction, valueFunction, type);
    }

    /**
     * list 转换为map
     * key不重复的情况将list转换为map的方法
     *
     * @param    list  需要转换的list
     * @param    keyFunction  获取map的key的function <T, R> T传入的类型，R转换后map的key类型
     * @param    valueFunction  获取map的value的function <T, C> T传入类型，C转换后map的value类型
     * @return   Map<R,C>
     */
    private static <T, C, R> Map<R, C> toMap(List<T> list, Function<T, R> keyFunction, Function<T, C> valueFunction) {
        return list.stream().collect(Collectors.toMap(keyFunction, valueFunction));
    }

    /**
     * list转换为map
     * key重复的情况下将list转换为map的方法
     *
     * @param    list  需要转换的list
     * @param    keyFunction  获取map的key的function <T, R> T传入的类型，R转换后map的key类型
     * @param    comparableFunction  key重复的情况下获取构造器的function <T, U> T传入的类型，U生成比较器的类型
     * @param    valueFunction  获取map的value的function <T, C> T传入类型，C转换后map的value类型
     * @param    type  key重复情况下，比较器的类型，MAX最大值或MIN最小值
     * @return   Map<R,C>
     */
    private static <T, C, R, U extends Comparable<U>> Map<R, C> toMap(List<T> list, Function<T, R> keyFunction,
                                                                      Function<T, U> comparableFunction,
                                                                      Function<T, C> valueFunction,
                                                                      ComparableType type) {
        switch (type) {
            case MAX:
                return list.stream().collect(Collectors.groupingBy(keyFunction,
                        Collectors.collectingAndThen(
                                Collectors.collectingAndThen(
                                        maxCollector(comparableFunction), Optional::get), valueFunction)
                ));
            case MIN:
                return list.stream().collect(Collectors.groupingBy(keyFunction,
                        Collectors.collectingAndThen(
                                Collectors.collectingAndThen(
                                        minCollector(comparableFunction), Optional::get), valueFunction)
                ));
        }
        throw new RuntimeException(TYPE_NOT_APPOINT);
    }

    /**
     * 获取集合的第一个元素
     * 不排序
     *
     * @param    collection  集合
     * @return   Optional<T>
     */
    public static <T, C extends Comparable<C>> Optional<T> limitOne(Collection<T> collection) {
        return limitOne(collection, 0L);
    }

    /**
     * 获取跳过指定个数元素后的第一个元素
     * 不排序
     *
     * @param    collection  集合
     * @param    skip  跳过元素的个数
     * @return   Optional<T>
     */
    public static <T, C extends Comparable<C>> Optional<T> limitOne(Collection<T> collection, long skip) {
        return collection.stream().skip(skip).limit(1L).findAny();
    }

    /**
     * 获取指定方式排序后的第一个元素
     * 默认不跳过元素
     * 默认降序排序
     *
     * @param    collection  传入的集合
     * @param    function  排序元素
     * @return   Optional<T>
     */
    public static <T, C extends Comparable<C>> Optional<T> limitOne(Collection<T> collection, Function<T, C> function) {
        return limitOne(collection, function, 0L, OrderType.DESC);
    }

    /**
     * 获取指定方式排序后  跳过一定数量元素后  的第一个元素
     * 默认降序排序
     *
     * @param    collection  传入的集合
     * @param    function  排序元素
     * @param    skip 跳过元素个数
     * @return   Optional<T>
     */
    public static <T, C extends Comparable<C>> Optional<T> limitOne(Collection<T> collection, Function<T, C> function,
                                                                    long skip) {
        return limitOne(collection, function, skip, OrderType.DESC);
    }

    /**
     *  获取 通过指定方式，指定元素排序后 的第一个元素
     *
     * @param    list  传入集合
     * @param    function  排序元素
     * @param    orderType  排序方式
     * @return   Optional<T>
     */
    public static <T, C extends Comparable<C>> Optional<T> limitOne(List<T> list, Function<T, C> function,
                                                                    OrderType orderType) {
        return limitOne(list, function, 0L, orderType);
    }

    /**
     *  获取 通过指定方式，指定元素排序后 跳过一定数量元素后 的第一个元素
     *
     * @param    collection  传入集合
     * @param    function  排序方法
     * @param    skip  跳过元素个数
     * @param    orderType  排序方式
     * @return   Optional<T>
     */
    public static <T, C extends Comparable<C>> Optional<T> limitOne(Collection<T> collection, Function<T, C> function,
                                                                    long skip, OrderType orderType) {
        switch (orderType) {
            case DESC: {
                return collection.stream().sorted(getComparator(function).reversed()).skip(skip).limit(1).findAny();
            }
            case ASC: {
                return collection.stream().sorted(getComparator(function)).skip(skip).limit(1).findAny();
            }
            default: {
                throw new RuntimeException(TYPE_NOT_APPOINT);
            }
        }
    }

    /**
     * 截取 list的前 {limit} 个元素
     * 不排序，不跳过
     *
     * @param    collection  传入的集合
     * @param    limit  获取元素的个数
     * @return   List<T>
     */
    public static <T> List<T> limitList(Collection<T> collection, long limit) {
        return limitList(collection, 0L, limit);
    }

    /**
     * 截取 list的前 {limit} 个元素
     * 不排序，不跳过
     *
     * @param    collection  传入的集合
     * @param    limit  获取元素的个数
     * @return   Set<T>
     */
    public static <T> Set<T> limitSet(Collection<T> collection, long limit) {
        return limitSet(collection, 0L, limit);
    }

    /**
     * 截取集合的前 {limit} 个元素
     * 不排序
     *
     * @param    collection  传入的集合
     * @param    skip  跳过元素的个数
     * @param    limit  获取元素的个数
     * @return   List<T>
     */
    public static <T> List<T> limitList(Collection<T> collection, long skip, long limit) {
        return collection.stream().skip(skip).limit(limit).collect(Collectors.toList());
    }

    /**
     * 截取集合的前 {limit} 个元素
     * 不排序
     *
     * @param    collection  传入的集合
     * @param    skip  跳过元素的个数
     * @param    limit  获取元素的个数
     * @return   Set<T>
     */
    public static <T> Set<T> limitSet(Collection<T> collection, long skip, long limit) {
        return collection.stream().skip(skip).limit(limit).collect(Collectors.toSet());
    }

    /**
     * 获取 指定元素排序后的前 {limit} 个元素
     * 默认不跳过
     * 默认降序排序
     *
     * @param    list  需要截取的list
     * @param    function  排序元素
     * @param    limit  截取元素个数
     * @return   List<T>
     */
    public static <T, C extends Comparable<C>> List<T> limitList(List<T> list, Function<T, C> function, long limit) {
        return limitList(list, function, 0, limit, OrderType.DESC);
    }

    /**
     * 获取 指定元素，指定方式排序后的前 {limit} 个元素
     * 默认不跳过
     *
     * @param    list  需要截取的list
     * @param    function  排序元素
     * @param    limit  截取元素个数
     * @param    orderType  排序方式
     * @return   List<T>
     */
    public static <T, C extends Comparable<C>> List<T> limitList(List<T> list, Function<T, C> function, long limit,
                                                                 OrderType orderType) {
        return limitList(list, function, 0, limit, orderType);
    }

    /**
     * 通过指定元素，指定方式 排序后，跳过skip个元素后，截取list中的前limit个元素
     *
     * @param    list  需要截取的list
     * @param    function  排序元素
     * @param    skip  跳过元素个数
     * @param    limit  截取元素个数
     * @param    orderType  排序方式
     * @return   List<T>
     */
    public static <T, C extends Comparable<C>> List<T> limitList(List<T> list, Function<T, C> function, long skip,
                                                                 long limit, OrderType orderType) {
        switch (orderType) {
            case DESC:
                return list.stream().sorted(getComparator(function).reversed()).skip(skip).limit(limit).collect(Collectors.toList());
            case ASC:
                return list.stream().sorted(getComparator(function)).skip(skip).limit(limit).collect(Collectors.toList());
        }
        throw new RuntimeException(TYPE_NOT_APPOINT);
    }

    /**
     * 截取 跳过 {skip} 个元素后的所有元素
     * 不排序
     *
     * @param    list  需要截取的list
     * @param    skip  跳过元素个数
     * @return   List<T>
     */
    public static <T, C extends Comparable<C>> List<T> skipList(List<T> list, long skip) {
        return list.stream().skip(skip).collect(Collectors.toList());
    }

    /**
     * 截取 指定元素排序后 跳过 {skip} 个元素后的所有元素
     * 默认降序排序
     *
     * @param    list  需要截取的list
     * @param    function  排序元素
     * @param    skip  跳过元素个数
     * @return   List<T>
     */
    public static <T, C extends Comparable<C>> List<T> skipList(List<T> list, Function<T, C> function, long skip) {
        return skipList(list, function, skip, OrderType.DESC);
    }

    /**
     * 获取 指定元素，指定方式排序后 跳过{skip}个元素后的所有元素
     *
     * @param    list  需要截取的list
     * @param    function  排序元素
     * @param    skip  跳过的元素个数
     * @param    orderType  排序方式
     * @return   List<T>
     */
    public static <T, C extends Comparable<C>> List<T> skipList(List<T> list, Function<T, C> function, long skip, OrderType orderType) {
        switch (orderType) {
            case DESC:
                return list.stream().sorted(getComparator(function).reversed()).skip(skip).collect(Collectors.toList());
            case ASC:
                return list.stream().sorted(getComparator(function)).skip(skip).collect(Collectors.toList());
        }
        throw new RuntimeException(TYPE_NOT_APPOINT);
    }

    /**
     * 获取比较器
     *
     * @param    function  生成比较器的function
     * @return   Comparator<T> T类型的比较器
     */
    private static <T, C extends Comparable<C>> Comparator<T> getComparator(Function<T, C> function) {
        return Comparator.comparing(function);
    }

    /**
     * 获取最大值
     *
     * @param    function
     * @return   Collector<T,?,Optional<T>>
     */
    private static <T, C extends Comparable<C>> Collector<T, ?, Optional<T>> maxCollector(Function<T, C> function) {
        return Collectors.maxBy(getComparator(function));
    }

    /**
     * 获取最小值
     *
     * @param    function
     * @return   Collector<T,?,Optional<T>>
     */
    private static <T, C extends Comparable<C>> Collector<T, ?, Optional<T>> minCollector(Function<T, C> function) {
        return Collectors.minBy(getComparator(function));
    }

    /**
     * 判断集合是否不为空
     *
     * @param    collection  判断的集合
     * @return   boolean true：不为空，false：为空
     */
    public static boolean nonEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断map是否不为空
     *
     * @param    map  判断的集合
     * @return   boolean true：不为空，false：为空
     */
    public static boolean nonEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

}
