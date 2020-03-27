package com.neotech.ccc.domain.model.entities;

import com.neotech.ccc.domain.model.utils.SensitiveInfoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {

    @NotNull
    @Pattern(regexp = "[0-9]{1,15}")
    private String value;

    @Override
    public String toString() {
        return getMaskedValue();
    }

    public String getMaskedValue(){
        return value == null ? null : SensitiveInfoUtils.hideLast4(value);
    }
}
