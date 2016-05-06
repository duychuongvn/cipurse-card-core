package com.github.duychuongvn.cirpusecard.core.exception;

import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/27/2016.
 */
public class EnumParserExceptionTest {
    @Test
    public void shouldFormatMessage() {
        try{
            throw new EnumParserException(258, "CommandEnum");
        } catch (EnumParserException exception) {
            Assertions.assertThat(exception.getMessage()).isEqualTo("Cannot parse enum for class: CommandEnum - Bytes Id: 00 00 01 02");
        }
    }
}
