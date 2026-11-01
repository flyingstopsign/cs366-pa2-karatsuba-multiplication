package edu.wne.cs366;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;


class StudentKaratsubaTest {
    private static BigInteger big(String digits) {
        return new BigInteger(digits);
    }
 
    /** Asserts that karatsuba(x, y) equals the expected product, in both argument orders. */
    private static void assertProduct(String expected, String x, String y) {
        assertEquals(big(expected), KaratsubaMultiplier.karatsuba(big(x), big(y)),
                x + " * " + y);
        assertEquals(big(expected), KaratsubaMultiplier.karatsuba(big(y), big(x)),
                y + " * " + x);
    }
     private static String repeat(char digit, int count) {
        return String.valueOf(digit).repeat(count);
    }
    @Test
    void multiplicationByZero() {
        assertProduct("0", "0", "0");
        assertProduct("0", "0", "7");
        assertProduct("0", "0", "-7");
        assertProduct("0", "0", "123456789012345678901234567890");
        assertProduct("0", "0", "-123456789012345678901234567890");

    }
   @Test
    void oneDigitFactorsUseBaseCase() {
        assertProduct("56", "7", "8");
        assertProduct("81", "9", "9");
        assertProduct("5", "1", "5");
        assertProduct("0", "0", "9");
        assertProduct("8641975307", "1234567901", "7");
    }
    //TODO: finish implementing tests 
}
