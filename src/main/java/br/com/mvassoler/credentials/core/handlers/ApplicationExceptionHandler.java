package br.com.mvassoler.credentials.core.handlers;


import br.com.mvassoler.credentials.core.dtos.ErrorDetailsDTO;
import br.com.mvassoler.credentials.core.handlers.exception.BadRequestException;
import br.com.mvassoler.credentials.core.handlers.exception.BusinessException;
import br.com.mvassoler.credentials.core.handlers.exception.EntityNotFoundException;
import br.com.mvassoler.credentials.core.handlers.exception.ForbidenException;
import br.com.mvassoler.credentials.core.handlers.exception.GenericErrorException;
import br.com.mvassoler.credentials.core.handlers.exception.InternalServerErrorException;
import br.com.mvassoler.credentials.core.handlers.exception.ServiceUnavailableException;
import br.com.mvassoler.credentials.core.handlers.exception.UnauthorizedAccessException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;


    public ApplicationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;

    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(@NonNull HttpMediaTypeNotAcceptableException ex,
                                                                      @NonNull HttpHeaders headers,
                                                                      @NonNull HttpStatusCode status,
                                                                      @NonNull WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }


    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        log.info("M=Exception", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.ERRO_DE_SISTEMA, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatusCode status,
                                                                   @NonNull WebRequest request) {
        log.info("M=NoHandlerFoundException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.RECURSO_NAO_ENCONTRADO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatusCode status,
                                                        @NonNull WebRequest request) {
        log.info("M=TypeMismatchException", ex);
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        log.info("M=HttpMessageNotReadableException", ex);
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(invalidFormatException, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBinding(propertyBindingException, headers, status, request);
        }
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.MENSAGEM_INCOMPREENSIVEL, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

  /*  @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.info("M=AccessDeniedException", ex);
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.ACESSO_NEGADO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }*/

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleAccessDeniedException(BusinessException ex, WebRequest request) {
        log.info("M=BussinessExcpetion", ex);
        HttpStatus status = BAD_REQUEST;
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex, WebRequest request) {
        log.info("M=EntityNotFoundException", ex);
        HttpStatus status = NOT_FOUND;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.ENTIDADE_NAO_ENCONTRADA, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServiceUnavailableException(final EntityNotFoundException ex, WebRequest request) {
        log.info("M=EntityNotFoundException", ex);
        HttpStatus status = SERVICE_UNAVAILABLE;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.ERRO_DE_SISTEMA, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(GenericErrorException.class)
    public ResponseEntity<Object> handleGenericErrorException(final GenericErrorException ex, WebRequest request) {
        log.info("M=GenericErrorException", ex);
        HttpStatus status = UNPROCESSABLE_ENTITY;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(final UnauthorizedAccessException ex, WebRequest request) {
        log.info("M=UnauthorizedAccessException", ex);
        HttpStatus status = UNAUTHORIZED;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.SEM_AUTORIZACAO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(final InternalServerErrorException ex, WebRequest request) {
        log.info("M=InternalServerErrorException", ex);
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.SERVER_ERROR, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ForbidenException.class)
    public ResponseEntity<Object> handleForbidenException(final ForbidenException ex, WebRequest request) {
        log.info("M=ForbidenException", ex);
        HttpStatus status = FORBIDDEN;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.SEM_AUTORIZACAO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex, WebRequest request) {
        log.info("M=BadRequestException", ex);
        HttpStatus status = BAD_REQUEST;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleExceptionsConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatusCode status,
                                                            WebRequest request,
                                                            BindingResult bindingResult) {
        String detail = "Um ou mais campos estão inválidos.";
        List<ErrorDetailsDTO.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message;
                    try {
                        //message = messageSource.getMessage(Objects.requireNonNull(objectError.getDefaultMessage()), null, Util.getLocaleDefault());

                        message = messageSource.getMessage(Objects.requireNonNull(objectError.getDefaultMessage()), null, Locale.getDefault());

                        //Util.getLocaleUsuarioAtual()
                    } catch (NoSuchMessageException e) {
                        message = objectError.getDefaultMessage();
                    }
                    String name = objectError.getObjectName();
                    if (objectError instanceof FieldError fieldError) {
                        name = fieldError.getField();
                    }
                    return ErrorDetailsDTO.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                }).toList();
        ErrorDetailsDTO problem = createProblemBuilder(status,
                TitleExceptionsConstants.DADOS_INVALIDOS,
                detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        problem.setObjects(problemObjects);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ErrorDetailsDTO createProblemBuilder(HttpStatusCode status, String title, String detail, String contextPah) {
        return ErrorDetailsDTO.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(contextPah)
                .title(title)
                .detail(detail).build();
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.PARAMETRO_INVALIDO, detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers,
                                                       HttpStatusCode status,
                                                       WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.MENSAGEM_INCOMPREENSIVEL, detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers,
                                                         HttpStatusCode status,
                                                         WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("A propriedade '%s' não existe. "
                + "Corrija ou remova essa propriedade e tente novamente.", path);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleExceptionsConstants.MENSAGEM_INCOMPREENSIVEL, detail,
                ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }


}
