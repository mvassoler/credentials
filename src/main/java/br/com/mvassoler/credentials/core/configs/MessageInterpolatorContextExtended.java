package br.com.mvassoler.credentials.core.configs;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;

import java.util.Map;

@Getter
@Setter
public class MessageInterpolatorContextExtended extends MessageInterpolatorContext {

    private Map<String, Object> messageParameters;

    public MessageInterpolatorContextExtended(MessageInterpolatorContext messageInterpolatorContext) {
        super(messageInterpolatorContext.getConstraintDescriptor(),
                messageInterpolatorContext.getValidatedValue(),
                messageInterpolatorContext.getRootBeanType(),
                messageInterpolatorContext.getPropertyPath(),
                messageInterpolatorContext.getMessageParameters(),
                messageInterpolatorContext.getExpressionVariables(),
                messageInterpolatorContext.getExpressionLanguageFeatureLevel(),
                messageInterpolatorContext.isCustomViolation());
    }

    @Override
    public Map<String, Object> getMessageParameters() {
        return this.messageParameters;
    }

    public void setMessageParameters(Map<String, Object> messageParameters) {
        this.messageParameters = messageParameters;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
