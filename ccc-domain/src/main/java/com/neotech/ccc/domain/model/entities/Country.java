package com.neotech.ccc.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @NotBlank
    private String name;

    @NotNull
    @Pattern(regexp = "[A-Z]{2}")
    private String alpha2Code;

}
