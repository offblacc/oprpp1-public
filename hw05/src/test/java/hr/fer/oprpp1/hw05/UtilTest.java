package hr.fer.oprpp1.hw05;

import hr.fer.oprpp1.hw05.crypto.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class UtilTest {

    @Test
    public void hextobyte() {
        String keyText = "01aE22";
        byte[] bytearray = Util.hextobyte(keyText);
        assertEquals(3, bytearray.length);
        assertEquals(1, bytearray[0]);
        assertEquals(-82, bytearray[1]);
        assertEquals(34, bytearray[2]);
    }

    @Test
    public void bytetohex() {
        byte[] bytearray = new byte[] {1, -82, 34};
        String keyText = Util.bytetohex(bytearray);
        assertEquals("01ae22", keyText);
    }
}