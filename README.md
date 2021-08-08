### 当前仅支持lettuce连接池
- redis配置示例如下
```yaml
spring:
  #默认的redis配置
  redis:
    port: 6379
    host: 127.0.0.1
    password: 123456
    database: 2
    timeout: 20000ms
    lettuce:
      pool:
        # 最大活跃链接数 默认8
        max-active: 3
        # 最大空闲连接数 默认8
        max-idle: 3
        # 最小空闲连接数 默认0
        min-idle: 0
    client-type: lettuce
  # 多数据源配置
  data-source:
    # redis1将作为 StringRedisTemplate、RedisCacheUtils和RedisBaseService的名称前缀
    redis1:
      port: 6379
      host: 127.0.0.1
      password: 123456
      database: 3
      timeout: 20000ms
      lettuce:
        pool:
          # 最大活跃链接数 默认8
          max-active: 3
          # 最大空闲连接数 默认8
          max-idle: 3
          # 最小空闲连接数 默认0
          min-idle: 0
    # redis2将作为 StringRedisTemplate、RedisCacheUtils和RedisBaseService的名称前缀
    redis2:
      port: 6379
      host: 127.0.0.1
      password: 123456
      database: 4
      timeout: 20000ms
      lettuce:
        pool:
          # 最大活跃链接数 默认8
          max-active: 3
          # 最大空闲连接数 默认8
          max-idle: 3
          # 最小空闲连接数 默认0
          min-idle: 0
```
- 在上面的配置中，默认的redis配置将生成默认的 StringRedisTemplate、RedisCacheUtils 和 RedisBaseService
- 多数据源配置的信息，将生成自定义的 StringRedisTemplate("redis1Template")、RedisCacheUtils("redis1CacheUtils") 和 RedisBaseService("redis1BaseService")
- 对redis配置的引用如下

```java
import com.wpx.service.RedisBaseService;
import com.wpx.util.RedisCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisService {

    /**
     * 默认配置的 StringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 使用redis1配置的 StringRedisTemplate
     */
    @Autowired
    @Qualifier("redis1Template")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 使用redis2配置的 StringRedisTemplate
     */
    @Autowired
    @Qualifier("redis2Template")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 默认配置的 RedisCacheUtils
     */
    @Autowired
    private RedisCacheUtils redisCacheUtils;

    /**
     * 使用redis1配置的 StringRedisTemplate
     */
    @Autowired
    @Qualifier("redis1CacheUtils")
    private RedisCacheUtils redisCacheUtils;

    /**
     * 使用redis2配置的 StringRedisTemplate
     */
    @Autowired
    @Qualifier("redis2CacheUtils")
    private RedisCacheUtils redisCacheUtils;

    /**
     * 默认配置的 RedisBaseService
     */
    @Autowired
    private RedisBaseService redisBaseService;

    /**
     * 使用redis1配置的 RedisBaseService
     */
    @Autowired
    @Qualifier("redis1BaseService")
    private RedisBaseService redisBaseService;

    /**
     * 使用redis2配置的 RedisBaseService
     */
    @Autowired
    @Qualifier("redis2BaseService")
    private RedisBaseService redisBaseService;

}
```