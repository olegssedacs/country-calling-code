package com.neotech.ccc.presentation.rest.api.exceptionhandling;

public interface ExceptionConverter {

    ApiErrorDetails convert(Throwable t);

    Class<? extends Throwable> supports();
}
