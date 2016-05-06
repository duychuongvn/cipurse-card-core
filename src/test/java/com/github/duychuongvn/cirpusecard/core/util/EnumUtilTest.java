package com.github.duychuongvn.cirpusecard.core.util;

import com.github.duychuongvn.cirpusecard.core.constant.CommandEnum;
import com.github.duychuongvn.cirpusecard.core.constant.EnumKey;
import com.github.duychuongvn.cirpusecard.core.exception.EnumParserException;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/27/2016.
 */
public class EnumUtilTest {

    @Test
    public void shouldReturnSelectWhenIdIs9000() {
        int id = ByteUtils.byteArrayToInt(new byte[]{(byte) 0x00, (byte) 0xA4});
        CommandEnum select = EnumUtil.getEnum(id, CommandEnum.class);
        Assertions.assertThat(select).isEqualTo(CommandEnum.SELECT);
    }

    @Test(expected = EnumParserException.class)
    public void shouldThrowExceptionIfIdNotValid() {
        EnumUtil.getEnum(1, CommandEnum.class);
    }
}
