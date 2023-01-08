package cc.ussu.modules.files.config;

import cc.ussu.modules.files.properties.LocalUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LocalUploadProperties localUploadProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(localUploadProperties.getLocalFilePrefix() + "/**")
                .addResourceLocations("file:" + localUploadProperties.getLocalFilePath() + File.separator);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(localUploadProperties.getLocalFilePrefix() + "/**")
                .allowedMethods("GET")
                .allowedOrigins("*");
    }
}
