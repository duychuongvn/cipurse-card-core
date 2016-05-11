package com.github.duychuongvn.cirpusecard.core.security.crypto;

import java.util.Arrays;

class Padding {

    public static final byte M2_PAD_BYTE = (byte) 0x80;

    /// <summary>
    /// This method implements the ISO9797 M2 Padding Scheme.
    /// </summary>
    /// <param name="inputData">Input Data</param>
    /// <param name="blockLen">block size</param>
    /// <returns>Padded Data</returns>
    public static byte[] schemeISO9797M2(final byte[] inputData, int blockLen) {
        int paddingLength = 0;
        int inputDataLen = inputData.length;
        byte[] paddedData = null;

        //Padding Length
        paddingLength = blockLen - (inputDataLen % blockLen);
        paddedData = new byte[inputDataLen + paddingLength];
        Arrays.fill(paddedData, (byte) 0x00);

        //Copy input data
        System.arraycopy(inputData, 0, paddedData, 0, inputDataLen);
        //Padding byte
        paddedData[inputDataLen] = M2_PAD_BYTE;

        return paddedData;
    }

    /// <summary>
    /// This method removes the ISO9797 M2 Padding bytes.
    /// </summary>
    /// <param name="inputData">Decrypted Data</param>
    /// <returns>Actual Data</returns>
    public static byte[] removeISO9797M2(final byte[] inputData) {
        int actualDataLength = 0;
        byte[] actualData = null;
        //Get the offset from where the padding starts
        for (int i = inputData.length - 1; i >= 0; --i) {
            if (inputData[i] == M2_PAD_BYTE) {
                actualDataLength = i;
                break;
            }
        }
        if (actualDataLength == 0) {
            return null;
        } else {
            //Allocate and copy the data excluding the padding bytes
            actualData = new byte[actualDataLength];
            System.arraycopy(inputData, 0, actualData, 0, actualDataLength);

            return actualData;
        }
    }


}

