package com.github.duychuongvn.cirpusecard.core.exception;

import com.github.duychuongvn.cirpusecard.core.constant.SwEnum;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class Iso7816ExceptionTest {

    @Test
    public void shouldExposeExceptionInBytes() {
        try {
            throw  new Iso7816Exception(SwEnum.SW_DATA_INVALID);
        } catch (Iso7816Exception ex) {
            Assertions.assertThat(ex.getResponseStatus()).isEqualTo(new byte[]{(byte)0x69, (byte)0x84});
        }
    }
}
