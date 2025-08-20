package com.example.trpg_writer.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.storage-dir}")
    private String storageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /storage/** というURLパスのリクエストを、外部の物理フォルダにマッピングする
        registry.addResourceHandler("/storage/**")
                .addResourceLocations("file:" + storageDir);
    }
}
