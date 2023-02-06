package com.mywg.base.mybatis.properties;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Mybatis配置参数
 *
 * @Author: Xusg
 * @Date: 18/10/01
 * @Email: 18620066064@gdyitop.com
 */
@Data
public class MybatisProperties {

    /**
     * Mybatis配置
     */
    private Map<String, MybatisConfigDetail> mbConfigs;


    /**
     * Mybatis配置参数
     */
    @Data
    public static class MybatisConfigDetail {
        // 数据源名称
        private String dataSourceName;
        // 别名包
        private String typeAliasesPackage;
        // 映射文件路径
        private String mapperLocations;
        // Mapper路径
        private String basePackage;
    }

}
