package br.com.mvassoler.credentials.configurations;


import br.com.mvassoler.credentials.core.configs.MessageInterpolatorConfiguration;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfig {

    private final MessageSource messageSource;

    public ValidatorConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    public org.springframework.validation.Validator validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        bean.setMessageInterpolator(new MessageInterpolatorConfiguration(new ParameterMessageInterpolator()));
        return bean;
    }

}
