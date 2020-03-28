package com.neotech.ccc.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.neotech.ccc.domain.model.utils.SensitiveInfoUtils.hideLast4;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {

    @NotNull
    @Pattern(regexp = "[1-9][0-9]{6,14}", message = "must begin with 1-9 and have a length of 7-15 digits")
    private String value;

    @Override
    public String toString() {
        return getMaskedValue();
    }

    public String getMaskedValue() {
        return value == null ? null : hideLast4(value);
    }
}
