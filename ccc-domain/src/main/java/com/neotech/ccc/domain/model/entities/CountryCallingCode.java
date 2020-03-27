package com.neotech.ccc.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryCallingCode {

    @NotNull
    @Min(1)
    private Long callingCode;

    @NotNull
    @Valid
    private List<Country> countries = new ArrayList<>();

}
