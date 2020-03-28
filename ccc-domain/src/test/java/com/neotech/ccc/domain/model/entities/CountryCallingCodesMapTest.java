package com.neotech.ccc.domain.model.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountryCallingCodesMapTest {

    private CountryCallingCodesMap codesMap;

    private CountryCallingCode caUs = new CountryCallingCode(1L, asList(new Country("Canada", "CA"), new Country("United States", "US")));
    private CountryCallingCode vg = new CountryCallingCode(1284L, singletonList(new Country("British Virgin Islands", "VG")));
    private CountryCallingCode gd = new CountryCallingCode(1473L, singletonList(new Country("Grenada", "GD")));
    private CountryCallingCode eg = new CountryCallingCode(20L, singletonList(new Country("Egypt", "EG")));
    private CountryCallingCode yt = new CountryCallingCode(262269L, singletonList(new Country("Mayotte", "YT")));
    private CountryCallingCode uk = new CountryCallingCode(44L, singletonList(new Country("United Kingdom", "UK")));
    private CountryCallingCode im = new CountryCallingCode(447524L, singletonList(new Country("Isle of Man", "IM")));
    private CountryCallingCode pl = new CountryCallingCode(48L, singletonList(new Country("Poland", "PL")));
    private CountryCallingCode ru = new CountryCallingCode(7L, singletonList(new Country("Russia", "RU")));
    private CountryCallingCode kz = new CountryCallingCode(76L, singletonList(new Country("Kazakhstan", "KZ")));

    @BeforeEach
    void setUp() {
        List<CountryCallingCode> codes = asList(caUs, vg, gd, eg, yt, uk, im, ru, kz, pl);
        codesMap = new CountryCallingCodesMap(codes);
    }

    @Test
    void verifySize() {
        assertEquals(10, codesMap.size());
    }

    @Test
    void test0xx() {
        assertAbsent("01");
    }

    @Test
    void test1xx() {
        assertPresent("101111111", caUs);
        assertPresent("111111111", caUs);
        assertPresent("121111111", caUs);
        assertPresent("128511111", caUs);
        assertPresent("147011111", caUs);
        assertPresent("147499111", caUs);
        assertPresent("190011111", caUs);

        assertPresent("128419111", vg);
        assertPresent("147399111", gd);

    }

    @Test
    void test2xx() {
        assertAbsent("211111111");
        assertAbsent("299111111");
        assertAbsent("233111111");
        assertAbsent("262268111");

        assertPresent("20999111", eg);
        assertPresent("26226911", yt);
    }

    @Test
    void test3xx() {
        assertAbsent("301111111");
    }

    @Test
    void test4xx() {
        assertAbsent("400111111");
        assertAbsent("420984484484");
        assertAbsent("451111111");
        assertPresent("44011111", uk);
        assertPresent("44752311", uk);
        assertPresent("44752411", im);
    }

    @Test
    void test5xx() {
        assertAbsent("501111111");
    }

    @Test
    void test6xx() {
        assertAbsent("601111111");
    }

    @Test
    void test7xx() {
        assertPresent("70111111", ru);
        assertPresent("72111111", ru);
        assertPresent("78111111", ru);
        assertPresent("79911111", ru);

        assertPresent("76111111", kz);
        assertPresent("76911111", kz);
    }

    @Test
    void test8xx() {
        assertAbsent("811111111");
    }

    @Test
    void test9xx() {
        assertAbsent("911111111");
    }

    private void assertAbsent(String number) {
        PhoneNumber phoneNumber = new PhoneNumber(number);
        assertFalse(codesMap.get(phoneNumber).isPresent());
    }

    private void assertPresent(String number, CountryCallingCode expectation) {
        PhoneNumber phoneNumber = new PhoneNumber(number);
        Optional<CountryCallingCode> result = codesMap.get(phoneNumber);
        assertTrue(result.isPresent());
        assertEquals(expectation, result.get());
    }

}