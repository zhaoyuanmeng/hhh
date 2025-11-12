package com.xaxc.teqin.component.mybatis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.xaxc.teqin.handler.JsonbType2ArrayHandler;
import com.xaxc.teqin.handler.JsonbTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sean
 * @version 0.3
 * @date 2021/12/22
 */
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // select database
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            // 注册自定义 TypeHandler
            configuration.getTypeHandlerRegistry().register(JSONObject.class, JdbcType.VARCHAR, new JsonbTypeHandler());
            configuration.getTypeHandlerRegistry().register(JSONArray.class, JdbcType.VARCHAR, new JsonbType2ArrayHandler());
            //MAP字段自动转驼峰
            //configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
        };
    }
}
