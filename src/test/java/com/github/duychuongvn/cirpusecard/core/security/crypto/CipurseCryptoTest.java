package com.github.duychuongvn.cirpusecard.core.security.crypto;

import com.github.duychuongvn.cirpusecard.core.security.securemessaging.AES;
import com.github.duychuongvn.cirpusecard.core.security.securemessaging.Logger;
import com.github.duychuongvn.cirpusecard.core.util.ByteUtils;
import junit.framework.TestCase;
import org.fest.assertions.Assertions;

/**
 * Created by huynhduychuong on 5/12/2016.
 */
public class CipurseCryptoTest extends TestCase {

    private byte[] key = ByteUtils.fromHexString("73 73 73 73 73 73 73 73 73 73 73 73 73 73 73 73");
    private byte[] RP = ByteUtils.fromHexString("D5 65 C1 99 52 2A F9 14 AE 71 B2 8E 5F 78 7F 6A");
    private byte[] rP = ByteUtils.fromHexString("E6 AB C2 80 08 F0");
    private byte[] RT = ByteUtils.fromHexString("93 A9 7D 9F BF B0 80 BB BE 1F C6 B6 85 67 B9 CC");
    private byte[] rT = ByteUtils.fromHexString("97 47 6C 62 F4 4D");

    public void testGetSessionKey() throws Exception {
        CipurseCrypto cipurseCrypto = new CipurseCrypto(new AES(), new Logger());
    }

    public void testSetSessionKey() throws Exception {

    }

    public void testWrapCommand() throws Exception {

    }

    public void testUnwrapCommand() throws Exception {

    }

    public void testGenerateCT() throws Exception {

    }

    public void testGenerateK0AndGetCp() throws Exception {
        CipurseCrypto cipurseCrypto = new CipurseCrypto(new AES(), new Logger());
        byte[] cP = cipurseCrypto.generateK0AndGetCp(key, RP, rP, RT, rT);
        Assertions.assertThat(cP).isEqualTo(ByteUtils.fromHexString("08 68 43 8B A9 02 33 0F 4A 9A F5 F1 76 04 B1 75"));
    }

    public void testGetRandom() throws Exception {

    }

    public void testVerifyPICCResponse() throws Exception {

    }

    public void testGetPlainSMCommand() throws Exception {

    }

    public void testGetMACedCommand() throws Exception {

    }
}