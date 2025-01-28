package br.com.mvassoler.credentials.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    public static final String PATH_RESOURCES_PATTERN = "/resources/**";

    private final ObjectMapper objectMapper;
    private final org.springframework.validation.Validator validator;

    public WebConfig(ObjectMapper objectMapper,
                     org.springframework.validation.Validator validator
    ) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PATH_RESOURCES_PATTERN)
                .addResourceLocations("/resources/")
                .setCacheControl(CacheControl.maxAge(730L, TimeUnit.DAYS));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
        jacksonMessageConverter.setObjectMapper(objectMapper);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(jacksonMessageConverter);
    }

    @Override
    public org.springframework.validation.Validator getValidator() {
        return validator;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        registry.addInterceptor(localeInterceptor);
    }


}
