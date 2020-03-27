package com.neotech.ccc.domain.model.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SensitiveInfoUtilsTest {

    private String input = "some text";
    private char replacement = '*';

    @Test
    void shouldHideLast4WhenItPossible() {
        String result = SensitiveInfoUtils.hideLast4(input);
        assertEquals("some ****", result);
    }

    @Test
    void shouldNotHideLast4WhenLengthIsLessThan5() {
        String result = SensitiveInfoUtils.hideLast4("text");
        assertEquals("text", result);
    }

    @Test
    void shouldNotHideLast4WhenStringIsNull() {
        String result = SensitiveInfoUtils.hideLast4(null);
        assertNull(result);
    }

    @Test
    public void replaceBetweenShouldReplaceNothingWhenBoardersAreTooBig() {
        String result = SensitiveInfoUtils.replaceBetween(input, 6, 6, replacement);
        assertEquals(input, result);
    }

    @Test
    public void replaceBetweenShouldReplaceNothingWhenLeftIsNegative() {
        String result = SensitiveInfoUtils.replaceBetween(input, -6, 6, replacement);
        assertEquals(input, result);
    }

    @Test
    public void replaceBetweenShouldReplaceNothingWhenRightIsNegative() {
        String result = SensitiveInfoUtils.replaceBetween(input, 6, -6, replacement);
        assertEquals(input, result);
    }

    @Test
    public void replaceBetweenShouldReplaceNothingWhenBoardersAreSameAsString() {
        String result = SensitiveInfoUtils.replaceBetween(input, 4, 5, replacement);
        assertEquals(input, result);
    }

    @Test
    public void replaceBetweenShouldReplaceFromSecondToLast() {
        String result = SensitiveInfoUtils.replaceBetween(input, 1, 1, replacement);
        assertEquals("s*******t", result);
    }

    @Test
    public void replaceBetweenShouldReplaceFullText() {
        String result = SensitiveInfoUtils.replaceBetween(input, 0, 0, replacement);
        assertEquals("*********", result);
    }

}