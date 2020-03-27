package com.neotech.ccc.presentation.rest.api.detections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotech.ccc.domain.model.entities.PhoneNumber;
import lombok.Value;

@Value
public class PhoneNumberRequest {

    private PhoneNumber phoneNumber;

    @JsonCreator
    public static PhoneNumberRequest fromString(@JsonProperty("phoneNumber") String value) {
        var phoneNumber = value != null
                          ? new PhoneNumber(value)
                          : new PhoneNumber("");
        return new PhoneNumberRequest(phoneNumber);
    }

}
