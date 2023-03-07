package hr.fer.oprpp1.hw05.crypto;

/**
 * Utilities for crypto class.
 */
public class Util {
    /**
     * Converts hex string to byte array.
     * @param keyText - hex string
     * @return byte array
     */
    public static byte[] hextobyte(String keyText) {
        if (keyText == null) {
            throw new IllegalArgumentException("Key text cannot be null");
        }
        int keyTextLen = keyText.length();
        if (keyTextLen % 2 != 0) {
            throw new IllegalArgumentException("keyText parameter must be of even length, but is " + keyTextLen);
        }
        keyText = keyText.toLowerCase();
        byte[] bytearray = new byte[keyTextLen / 2];
        int[] offset = new int[] {0, 1};
        for (int i = 0; i < keyTextLen; i += 2) {
            for (int o : offset) {
                char c = keyText.charAt(i + o);
                if ((int) c >= 48 && (int) c <= 57 || (int) c >= 97 && (int) c <= 102) {
                    bytearray[i / 2] += (byte) ((int) c - (c >= 97 ? 87 : 48)) * (o == 0 ? 16 : 1);
                } else {
                    throw new IllegalArgumentException("Key text must be in hexadecimal format");
                }
            }
        }
        return bytearray;
    }

    /**
     * Converts byte array to hex string.
     * @param bytearray - byte array
     * @return hex string
     */
    public static String bytetohex(byte[] bytearray) {
        if (bytearray == null) {
            throw new IllegalArgumentException("Byte array cannot be null");
        }
        if (bytearray.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int[] offset = new int[] {0, 1};
        for (byte b : bytearray) {
            for (int o : offset) {
                byte byt = (byte) ((b >> (o == 0 ? 4 : 0)) & 0xf);
                sb.append((char) (byt + (byt >= 10 ? 87 : 48)));
            }
        }
        return sb.toString();
    }
}
