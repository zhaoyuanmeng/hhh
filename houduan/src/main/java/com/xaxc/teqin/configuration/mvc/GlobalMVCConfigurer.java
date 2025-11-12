package com.xaxc.teqin.configuration.mvc;

import com.xaxc.teqin.component.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class GlobalMVCConfigurer implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    private final List<String> excludePaths = new ArrayList<>(Collections.singletonList("/user/login"));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Intercept for all request interfaces.
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns(excludePaths);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GetSnakeArgumentProcessor(true));
    }
}
