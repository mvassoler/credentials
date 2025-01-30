package br.com.mvassoler.credentials.core.handlers.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class GenericErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -345743864301452379L;

    private final Object[] objects;

    private Integer code = 400;

    public GenericErrorException(Object[] objects) {
        super(String.valueOf(objects[0]));
        this.objects = objects;
    }

    public String getProperty() {
        return "task";
    }

    public Object[] getObjects() {
        return objects;
    }
}
