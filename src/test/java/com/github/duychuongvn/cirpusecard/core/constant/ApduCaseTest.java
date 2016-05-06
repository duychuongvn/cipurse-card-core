package com.github.duychuongvn.cirpusecard.core.constant;

import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by huynhduychuong on 4/27/2016.
 */
public class ApduCaseTest {
    @Test
    public void shouldReturnCase1WhenSelect() {

        ApduCase apduCase = ApduCase.getCase(ByteUtils.fromHexString("00 A4 00 00"));
        Assertions.assertThat(apduCase).isEqualTo(ApduCase.CASE1);
    }
}
