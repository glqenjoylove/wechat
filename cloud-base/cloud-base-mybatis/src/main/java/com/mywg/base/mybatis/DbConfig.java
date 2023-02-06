package com.mywg.base.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.mywg.base.common.util.JackSonUtils;
import com.mywg.base.mybatis.properties.DbSourceProperties;
import com.mywg.base.mybatis.properties.MybatisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Map;
import java.util.Properties;

/**
 * 数据库初始化
 *
 * @Author: Xusg
 * @Date: 18/10/01
 * @Email: 18620066064@gdyitop.com
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, DruidDataSource.class})
public class DbConfig implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {
    /**
     * DbSource、Mybatis参数前缀
     */
    private static final String DB_CONFIG_PREFIX = "dbconfig";
    private static final String MB_CONFIG_PREFIX = "mybatis";

    /**
     * DbSource、Mybatis参数对象
     */
    private DbSourceProperties dbSourceProperties;
    private MybatisProperties mybatisProperties;

    /**
     * 注册数据源
     *
     * @param registry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            for (Map.Entry<String, DbSourceProperties.DbSourceConfigDetail> entry : dbSourceProperties.getDataSources().entrySet()) {
                String dbSourceName = entry.getKey();

                MybatisProperties.MybatisConfigDetail mybatisConfigDetail = mybatisProperties.getMbConfigs().get(dbSourceName + "_dataSource");
                if (mybatisConfigDetail == null) {
                    throw new RuntimeException("Mybatis缺少[" + dbSourceName + "_dataSource" + "]数据源配置");
                }

                /*================================ Start 创建、注册HikariDataSource对象：key(配置文件读取)_dataSource ================================*/
                log.info("[数据库初始化]-注册DruidDataSource对象：{}", dbSourceName + "_dataSource");
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(DruidDataSource.class);
                beanDefinition.setInitMethodName("init");
                beanDefinition.setDestroyMethodName("close");
                MutablePropertyValues mpv = beanDefinition.getPropertyValues();
                Map<String, Object> dbConfigDetailMap = JackSonUtils.beanToMap(entry.getValue());
                mpv.addPropertyValues(dbConfigDetailMap);
                registry.registerBeanDefinition(entry.getKey() + "_dataSource", beanDefinition);
                /*================================ End 创建、注册HikariDataSource对象：Bean对象名称：key(配置文件读取)_dataSource ================================*/

                // 数据源对象引用依赖
                RuntimeBeanReference dbRuntimeBeanReference = new RuntimeBeanReference(dbSourceName + "_dataSource");

                /*================================ Start 创建、注册Mybatis-SqlSessionFactoryBean对象：key(配置文件读取)_sqlSessionFactoryBean ================================*/
                log.info("[数据库初始化]-注册SqlSessionFactoryBean对象：{}", dbSourceName + "_sqlSessionFactoryBean");
                GenericBeanDefinition sqlSessionFactoryBeanDefinition = new GenericBeanDefinition();
                sqlSessionFactoryBeanDefinition.setBeanClass(SqlSessionFactoryBean.class);
                MutablePropertyValues sqlSessionFactoryProperty = sqlSessionFactoryBeanDefinition.getPropertyValues();
                sqlSessionFactoryProperty.addPropertyValue("dataSource", dbRuntimeBeanReference);

                // Mybatis全局配置
                org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
                configuration.setCallSettersOnNulls(true);
                configuration.setJdbcTypeForNull(JdbcType.NULL);
                sqlSessionFactoryProperty.addPropertyValue("configuration", configuration);

                // 分页插件配置
                Properties properties = new Properties();
                properties.setProperty("reasonable", "true");
                properties.setProperty("supportMethodsArguments", "true");
                properties.setProperty("returnPageInfo", "check");
                properties.setProperty("params", "count=countSql");
                Interceptor interceptor = new PageInterceptor();
                interceptor.setProperties(properties);
                sqlSessionFactoryProperty.addPropertyValue("plugins", new Interceptor[]{interceptor});

                // 加载mapper.xml文件
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                sqlSessionFactoryProperty.addPropertyValue("mapperLocations", resolver.getResources(mybatisConfigDetail.getMapperLocations()));
                if (StringUtils.isNotBlank(mybatisConfigDetail.getTypeAliasesPackage())) {
                    sqlSessionFactoryProperty.addPropertyValue("typeAliasesPackage", mybatisConfigDetail.getTypeAliasesPackage());
                }
                registry.registerBeanDefinition(dbSourceName + "_sqlSessionFactory", sqlSessionFactoryBeanDefinition);
                /*================================ End 创建、注册Mybatis-SqlSessionFactoryBean对象：key(配置文件读取)_sqlSessionFactoryBean ================================*/

                /*================================ Start 创建、注册Mybatis-MapperScannerConfigurer对象：key(配置文件读取)_mapperScannerConfigurer ================================*/
                log.info("[数据库初始化]-注册MapperScannerConfigurer对象：{}", dbSourceName + "_mapperScannerConfigurer");
                GenericBeanDefinition mapperScannerConfigurerBeanDefinition = new GenericBeanDefinition();
                mapperScannerConfigurerBeanDefinition.setBeanClass(MapperScannerConfigurer.class);
                MutablePropertyValues mapperScannerConfigurerProperty = mapperScannerConfigurerBeanDefinition.getPropertyValues();
                mapperScannerConfigurerProperty.addPropertyValue("basePackage", mybatisConfigDetail.getBasePackage());
                mapperScannerConfigurerProperty.addPropertyValue("sqlSessionFactoryBeanName", dbSourceName + "_sqlSessionFactory");
                registry.registerBeanDefinition(dbSourceName + "_mapperScannerConfigurer", mapperScannerConfigurerBeanDefinition);
                /*================================ End 创建、注册Mybatis-MapperScannerConfigurer对象：key(配置文件读取)_mapperScannerConfigurer ================================*/

                /*================================ Start 创建、注册DataSourceTransactionManager对象：key(配置文件读取)_transactionManager ================================*/
                log.info("[数据库初始化]-注册DataSourceTransactionManager对象：{}", entry.getKey() + "_transactionManager");
                GenericBeanDefinition dataSourceTransactionManagerBeanDefinition = new GenericBeanDefinition();
                dataSourceTransactionManagerBeanDefinition.setBeanClass(DataSourceTransactionManager.class);
                MutablePropertyValues dataSourceTransactionManagerProperty = dataSourceTransactionManagerBeanDefinition.getPropertyValues();
                dataSourceTransactionManagerProperty.addPropertyValue("dataSource", dbRuntimeBeanReference);
                registry.registerBeanDefinition(dbSourceName + "_transactionManager", dataSourceTransactionManagerBeanDefinition);
                /*================================ End 创建、注册DataSourceTransactionManager对象：key(配置文件读取)_transactionManager ================================*/
            }
        } catch (Exception e) {
            throw new RuntimeException("[数据库初始化]-注册[DbSource、Mybatis]异常", e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    /**
     * 初始化数据库配置参数
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        log.info("[数据库初始化]-加载[DbSource、Mybatis]配置参数");
        dbSourceProperties = Binder.get(environment).bind(DB_CONFIG_PREFIX, DbSourceProperties.class).orElse(null);
        if (dbSourceProperties == null || dbSourceProperties.getDataSources() == null || dbSourceProperties.getDataSources().size() <= 0) {
            throw new RuntimeException("[数据库初始化]-加载[DbSource、Mybatis]配置参数失败：缺少DbSource配置数据");
        }
        mybatisProperties = Binder.get(environment).bind(MB_CONFIG_PREFIX, MybatisProperties.class).orElse(null);
        if (mybatisProperties == null || mybatisProperties.getMbConfigs() == null || mybatisProperties.getMbConfigs().size() <= 0) {
            throw new RuntimeException("[数据库初始化]-加载[DbSource、Mybatis]配置参数失败：缺少Mybatis配置数据");
        }
    }
}
