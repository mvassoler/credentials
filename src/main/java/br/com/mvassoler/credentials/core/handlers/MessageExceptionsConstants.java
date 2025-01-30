package br.com.mvassoler.credentials.core.handlers;

public interface MessageExceptionsConstants {

    //MENSAGENS GERAIS
    String LOGIN_MESSAGE_ERRO = "login.message.erro";
    String REGISTER_NOT_FOUND = "register.not.found.custom";
    String USUARIO_ACESSO_LOG_TOKEN_VENCIDO = "usuario.acesso.log.token.vencido";
    String USUARIO_ACESSO_LOG_TEMPO_COMUNICACAO = "usuario.acesso.log.tempo.comunicacao";
    String EXCEPTION_UNPROCESSABLE_ENTITY_INVALID_LOGIN_EXCEPTION = "exception.unprocessableEntity.InvalidLoginException";
    String CREATE_USER_AFTER_AUTHENTICATION_FAILURE = "create.user.after.authentication.failure";
    String PROBLEM_JPA_USUARIO = "problem.jpa.usuario";

}
