package com.wpx.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author: 不会飞的小鹏
 * @Deprecated: redis多数据源配置
 */
@Configuration
@ConfigurationProperties(prefix = RedisDataSourceProperties.PREFIX)
public class RedisDataSourceProperties {

    public static final String PREFIX = "spring.redis";

    private Map<String, RedisMessageProperties> dataSource;

    public Map<String, RedisMessageProperties> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, RedisMessageProperties> dataSource) {
        this.dataSource = dataSource;
    }

}
