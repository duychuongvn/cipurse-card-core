package com.github.duychuongvn.cirpusecard.core.security.securemessaging;

import org.osptalliance.cipurse.ILogger;

/**
 * Created by caoky on 12/2/2015.
 */
public class Logger implements ILogger {
    private static char[] hexChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static StringBuilder logContent = new StringBuilder();

    public Logger() {
    }


    public static String getLogContent() {
        return logContent.toString();
    }

    public static void clearLogContent() {
        logContent = new StringBuilder();
    }

    public void log(int type, String message) {
        switch (type) {
            case 1:
                System.err.println("Error Message := " + message);
                logContent.append("Error Message := " + message).append("\n");
                break;
            case 2:
            default:
                System.out.println("Info Message := " + message);
                logContent.append("Info Message := " + message).append("\n");
                break;
            case 3:
                System.out.println("Warning Message := " + message);
                logContent.append("Warning Message := " + message).append("\n");
                break;
            case 4:
                System.out.println("Command := " + message);
                logContent.append("Command := " + message).append("\n");
                break;
            case 5:
                System.out.println("Response := " + message);
                logContent.append("Response := " + message).append("\n");
                break;
            case 6:
                System.out.println("Wrapped Command := " + message);
                logContent.append("Wrapped Command := " + message).append("\n");
                break;
            case 7:
                System.out.println("Wrapped Command := " + message);
                logContent.append("Wrapped Command := " + message).append("\n");
        }

    }

    public void log(Throwable object) {
        object.printStackTrace();
    }

    public void log(String message) {
        this.log(2, (String) message);
    }

    public void log(int type, byte[] data) {
        this.log(type, byteArrayToHexString(data));
    }

    public void log(int type, String message, byte[] data) {
        this.log(type, message + " : " + byteArrayToHexString(data));
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer spacedHexString = new StringBuffer(byteArray.length * 5);
        byte[] var5 = byteArray;
        int var4 = byteArray.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            byte lastIndex = var5[var3];
            spacedHexString.append(hexChar[(lastIndex & 240) >> 4]);
            spacedHexString.append(hexChar[lastIndex & 15]);
            spacedHexString.append(" ");
        }

        int var6 = spacedHexString.toString().lastIndexOf(" ");
        return spacedHexString.substring(0, var6);
    }
}
