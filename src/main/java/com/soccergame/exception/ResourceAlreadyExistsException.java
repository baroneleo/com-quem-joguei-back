package com.soccergame.exception;

/**
 * Exceções customizadas usadas pelo backend.
 * Colocadas em um único arquivo por conveniência — você pode separar em arquivos distintos se preferir.
 */

/** Exceção para recurso já existente (ex.: username duplicado) */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException() { super(); }
    public ResourceAlreadyExistsException(String message) { super(message); }
    public ResourceAlreadyExistsException(String message, Throwable cause) { super(message, cause); }
}

/** Exceção para recurso não encontrado */
public class NotFoundException extends RuntimeException {
    public NotFoundException() { super(); }
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}

/** Exceção para requisição inválida / regras de negócio */
public class BadRequestException extends RuntimeException {
    public BadRequestException() { super(); }
    public BadRequestException(String message) { super(message); }
    public BadRequestException(String message, Throwable cause) { super(message, cause); }
}

/** Exceção para acesso não autorizado */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() { super(); }
    public UnauthorizedException(String message) { super(message); }
    public UnauthorizedException(String message, Throwable cause) { super(message, cause); }
}
