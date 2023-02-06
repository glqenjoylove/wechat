package com.mywg.base.mybatis.properties;

import lombok.Data;

import java.util.Map;

/**
 * 数据库配置参数
 *
 * @Author: Xusg
 * @Date: 18/08/17
 * @Email: 18620066064@gdyitop.com
 */
@Data
public class DbSourceProperties {

    /**
     * 数据源配置
     */
    private Map<String, DbSourceConfigDetail> dataSources;

    /**
     * 数据库连接配置参数
     */
    @Data
    public static class DbSourceConfigDetail {
        // 数据库连接信息
        private String url;
        private String username;
        private String password;
        private String driverClassName;

        // 配置初始化大小、最小、最大
        private int initialSize;
        private int minIdle;
        private int maxActive;

        // 配置获取连接等待超时的时间
        private int maxWait;

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒（60000:1分钟）
        private int timeBetweenEvictionRunsMillis;

        // 配置一个连接在池中最小生存的时间，单位是毫秒(300000:为5分钟)
        private int minEvictableIdleTimeMillis;

        // 检测连接是否有效的sql
        private String validationQuery;

        // 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。建议配置为true，不影响性能，并且保证安全性
        private boolean testWhileIdle;
        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        private boolean testOnBorrow;
        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        private boolean testOnReturn;

        // 打开PSCache，并且指定每个连接上PSCache的大小
        private boolean poolPreparedStatements;
        private int maxPoolPreparedStatementPerConnectionSize;

        // 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
        private String filters;

        // 通过connectProperties属性来打开mergeSql功能；慢SQL记录
        private String connectionProperties;
    }

}
