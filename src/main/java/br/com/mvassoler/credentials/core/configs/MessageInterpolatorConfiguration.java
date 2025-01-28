package br.com.mvassoler.credentials.core.configs;

import jakarta.validation.MessageInterpolator;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.internal.util.CollectionHelper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageInterpolatorConfiguration implements MessageInterpolator {

    private final MessageInterpolator defaultInterpolator;

    public MessageInterpolatorConfiguration(MessageInterpolator interpolator) {
        this.defaultInterpolator = interpolator;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return defaultInterpolator.interpolate(messageTemplate, context);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        MessageInterpolatorContextExtended messageInterpolatorContext = new MessageInterpolatorContextExtended((MessageInterpolatorContext) context);
        setInterpolationParameters(messageInterpolatorContext);
        return defaultInterpolator.interpolate(messageTemplate, messageInterpolatorContext, locale);
    }

    private void setInterpolationParameters(MessageInterpolatorContextExtended messageInterpolatorContext) {
        Map<String, Object> params = new HashMap<>();
        setFieldParameter(params, messageInterpolatorContext);
        messageInterpolatorContext.setMessageParameters(CollectionHelper.toImmutableMap(params));
    }

    private void setFieldParameter(Map<String, Object> params, MessageInterpolatorContextExtended messageInterpolatorContext) {
        params.put("field", messageInterpolatorContext.getPropertyPath());
    }

}