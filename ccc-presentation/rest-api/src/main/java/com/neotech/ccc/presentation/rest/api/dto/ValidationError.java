package com.neotech.ccc.presentation.rest.api.dto;

import lombok.Value;

@Value
public class ValidationError {

    private final String objectName;
    private final String message;
    private final Object rejectedValue;

}
