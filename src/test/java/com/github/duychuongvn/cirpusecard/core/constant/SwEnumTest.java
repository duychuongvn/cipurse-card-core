package com.github.duychuongvn.cirpusecard.core.constant;

import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/26/2016.
 */
public class SwEnumTest {

    @Test
    public void shouldReturnBytesAndDescription(){
        byte[] actualStatus = SwEnum.SW_NO_ERROR.toBytes();
        String actualDescription = SwEnum.SW_NO_ERROR.getDescription();
        Assertions.assertThat(actualStatus).isEqualTo(new byte[]{(byte)0x90, (byte)0x00});
        Assertions.assertThat(actualDescription).isEqualTo("No Error = (short)0x9000");
    }
}
