package org.sitenv.xdrmessagesender.configuration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.sitenv.xdrmessagesender.services.enums.XdrMessageTypeEnumConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan("org.sitenv")
@PropertySource(value = "/META-INF/maven/org.sitenv/xdrmessagesender/pom.properties", ignoreResourceNotFound=true)
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		return multipartResolver;
	}

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public XdrMessageTypeEnumConverter xdrMessageTypeEnumConverter(){
        return new XdrMessageTypeEnumConverter();
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        formatterRegistry.addConverter(xdrMessageTypeEnumConverter());
    }

    @Bean
    public static final List<String> ccdaFileList(final Environment environment){
        List<String> fileNames = new ArrayList<>();
        try {
            Collection<File> filesb =
                    FileUtils.listFiles(new File(environment.getProperty("sampleCcdaDir")), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : filesb) {
                fileNames.add(file.getCanonicalPath());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileNames;
    }
}
