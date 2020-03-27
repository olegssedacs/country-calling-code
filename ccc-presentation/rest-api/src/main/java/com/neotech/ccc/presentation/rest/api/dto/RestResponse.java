package com.neotech.ccc.presentation.rest.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RestResponse {

    private final Object data;
    private final Object error;

    public static RestResponse withData(@NonNull Object data) {
        return new RestResponse(data, null);
    }

    public static RestResponse withError(@NonNull Object error) {
        return new RestResponse(null, error);
    }

}
