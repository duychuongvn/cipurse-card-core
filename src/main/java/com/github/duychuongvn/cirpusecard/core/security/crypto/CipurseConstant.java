package com.github.duychuongvn.cirpusecard.core.security.crypto;

/**
 * Constants used in CIPURSE communication
 */
public class CipurseConstant {
	/**   **/
	public static final int OFFSET_CLA = 0x00;
	/**   **/
	public static final int OFFSET_INS = 0x01;
	/**   **/
	public static final int OFFSET_P1 = 0x02;
	/**   **/
	public static final int OFFSET_P2 = 0x03;
	/**   **/
	public static final int OFFSET_LC = 0x04;
	/**   **/
	public static final int AES_BLOCK_LENGTH = 16;
	/**   **/
	public static final int CIPURSE_SECURITY_PARAM_N = 6;
	/**   **/
	public static final int MIC_LENGH = 4;
	/**   **/
	public static final byte SM_BIT = 0x04;
	/**   **/
	public static final byte OFFSET_CMD_DATA = 0x05;
	/**   **/
	public static final byte SM_COMMAND_RESPONSE_PLAIN = 0x00;
	/**   **/
	public static final byte BITMAP_SMI_FOR_RESPONSE = 0x0C;
	/**   **/
	public static final int OSPT_MAC_LENGTH = 8;
	/**   **/
	public static final byte LE_DASH = 0x00;
	/**   **/
	public static final int PLAIN_MAX_LC_WITHLE = 253;
	/**   **/
	public static final int PLAIN_MAX_LC_WITHOUTLE = 254;
	/**   **/
	public static final int MAC_MAX_LC_WITHLE = 245;
	/**   **/
	public static final int MAC_MAX_LC_WITHOUTLE = 246;
	/**   **/
	public static final int ENC_MAX_LC = 231;
	/**   **/
	public  static final int CIPURSE_KVV_LENGTH = 3;

	public static final int OSPT_RESPONSE_STATUS_LENGTH = 2;


	/// <summary>
    /// Indicates value of SMI for PLAIN command
    /// 00-plain, 01-MAC, 10-ENC, 11-RFU
    /// </summary>
	/**   **/
	public static final  byte SM_COMMAND_PLAIN = 0x00;

	/// <summary>
    /// Indicates value of SMI for MECed command
    /// 00-plain, 01-MAC, 10-ENC, 11-RFU
    /// </summary>
	/**   **/
	public static final  byte SM_COMMAND_MACED = 0x40;

    /// <summary>
    /// Indicates value of SMI for ENCed response
    /// 00-plain, 01-MAC, 10-ENC, 11-RFU
    /// </summary>
	/**   **/
	public static final byte SM_COMMAND_ENCED = (byte)0x80;

    /// <summary>
    /// Bits which indicate SM format for command in SMI
    /// Bit1 : XX-- ----
    /// </summary>
	/**   **/
	public static final byte BITMAP_SMI_COMMAND = (byte)0xC0;

    /// <summary>
    /// Bits which indicate SM format for Response in SMI
    /// Bit1 : XX-- ----
    /// </summary>
	/**   **/
	public static final byte BITMAP_SMI_RESPONSE = (byte)0x0C;


    /// <summary>
    /// Indicates value of SMI for MECed response
    /// 00-plain, 01-MAC, 10-ENC, 11-RFU
    /// </summary>
	/**   **/
	public static final byte SM_RESPONSE_MACED = 0x04;

    /// <summary>
    /// Indicates value of SMI for ENCed response
    /// 00-plain, 01-MAC, 10-ENC, 11-RFU
    /// </summary>
	/**   **/
	public static final byte SM_RESPONSE_ENCED = 0x08;

    /// <summary>
    /// Const q used for ENCed commands
    /// </summary>
	/**   **/
	public static final byte[] qConstant = new byte[] {
        0x74, 0x74, 0x74, 0x74, 0x74, 0x74, 0x74, 0x74,
        0x74, 0x74, 0x74, 0x74, 0x74, 0x74, 0x74, 0x74
    };



	// Exception Message
	/**   **/
	public static final String CARD_LOADER_INSTANCE = "Card Loader Script facade instance cannot be null";
	/**   **/
	public static final String INVALID_SMI = "Invalid SMI value";
	/**   **/
	public static final String INVALID_PARAMS = "Invalid parameters";
	/**   **/
	public static final String LC_GRT_ALLOWED = "Lc > allowed";
	/**   **/
	public static final String RESP_LESS_THAN_MAC_RESP_LEN = "Response is less than min MACed response length(=8)";
	/**   **/
	public static final String RESP_MAC_FAILED = "Response MAC verification failed";
	/**   **/
	public static final String RESP_NOT_MUL_AES_BLOCK = "Response is not multiple of AES Blok";
	/**   **/
	public static final String RESP_LESS_THAN_MIN_ENC_RESP = "Response is less than min ENCed response length(=6)";
	/**   **/
	public static final String MISSMATCHED_SW = "SW in ENCed response != actual SW";
	/**   **/
	public static final String MIC_VER_FAIL = "MIC verify failed";
	/**   **/
	public static final String INVALID_MAC_BLOCK = "MAC Data not block aligned";
	/**   **/
	public static final String INVALID_CASE = "Command with invalid case";
	/**   **/
	public static final String INVALID_PAD_LEN = "Data to pad length > 128 bits";
	/**   **/
	public static final String INVALID_2PAD_LEN = "Length of data to pad * 2 > 128 bits";
	/**   **/
	public static final String INVALID_GETCHALLENGE_RES_LEN = "Get Challenge response is != 16H";
	/**   **/
	public static final String INVALID_MUTUAL_AUTH_RES_LEN = "Mutual Authentication response length != 10H";
	/**   **/
	public static final String INVALID_KEY_LEN = "Not a valid key length";
	/**   **/
	public static final String KEY_NOT_INIT = "Key value is not initialized";
	/**   **/
	public static final String INVALID_RESET_TYPE = "Invalid reset type, COLD_RESET = 0, WARM_RESET = 1";

}

