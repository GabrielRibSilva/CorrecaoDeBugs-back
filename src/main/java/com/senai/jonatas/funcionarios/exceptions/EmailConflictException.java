package com.senai.jonatas.funcionarios.exceptions;

public class EmailConflictException extends RuntimeException {
    public EmailConflictException(String message) { super(message); }
}
