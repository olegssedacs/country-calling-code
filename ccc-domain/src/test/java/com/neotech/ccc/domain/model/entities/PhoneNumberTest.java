package com.neotech.ccc.domain.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    @Test
    void shouldPrintMaskedPhone() {
        String print = new PhoneNumber("123456789").toString();
        assertEquals("12345****", print);
    }
}