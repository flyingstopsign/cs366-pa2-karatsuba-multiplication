package edu.wne.cs366;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
     @Test
    void unequalDigitCountsMultiplyCorrectly() {
        assertProduct("827115", "12345", "67");
        assertProduct("12000000", "1000000", "12");
        assertProduct("1219326311126352690", "123456789", "9876543210");
        assertProduct("99990000", "9999", "10000");
    }
 
    @Test
    void oneNegativeFactorGivesNegativeProduct() {
        assertProduct("-7006652", "-1234", "5678");
        assertProduct("-7006652", "1234", "-5678");
        assertProduct("-56", "-7", "8");
        assertProduct("-827115", "12345", "-67");
    }
 
    @Test
    void twoNegativeFactorsGivePositiveProduct() {
        assertProduct("7006652", "-1234", "-5678");
        assertProduct("56", "-7", "-8");
        assertProduct("827115", "-12345", "-67");
    }
 
    @Test
    void largeValuesRequireSeveralRecursiveLevels() {
        // (10^64 - 1)^2 = 10^128 - 2*10^64 + 1, i.e. 63 nines, an 8, 63 zeros, a 1.
        // 64-digit factors recurse through roughly six levels of splitting.
        String nines = repeat('9', 64);
        String expected = repeat('9', 63) + "8" + repeat('0', 63) + "1";
        assertProduct(expected, nines, nines);
        assertProduct("-" + expected, "-" + nines, nines);
 
        // 10^100 * 10^57 = 10^157 (odd total length, unequal lengths).
        String tenTo100 = "1" + repeat('0', 100);
        String tenTo57 = "1" + repeat('0', 57);
        assertProduct("1" + repeat('0', 157), tenTo100, tenTo57);
    }
 
    @Test
    void matchesBigIntegerMultiplyForChosenPairs() {
        String[][] pairs = {
            {"3412", "1523"},
            {"5678", "1234"},
            {"99", "99"},
            {"100", "101"},
            {"12345", "6789"},
            {"-98765432109876543210", "12345678901234567890"},
            {"3141592653589793238462643383279502884197169399375105820974944592",
             "2718281828459045235360287471352662497757247093699959574966967627"},
            {"-271828182845904523536028747135", "-314159265358979323846"},
        };
        for (String[] pair : pairs) {
            BigInteger x = big(pair[0]);
            BigInteger y = big(pair[1]);
            assertEquals(x.multiply(y), KaratsubaMultiplier.karatsuba(x, y),
                    pair[0] + " * " + pair[1]);
        }
    }
 
    @Test
    void inputValuesAreUnchanged() {
        BigInteger x = big("-123456789123456789");
        BigInteger y = big("987654321");
        KaratsubaMultiplier.karatsuba(x, y);
        assertEquals(big("-123456789123456789"), x);
        assertEquals(big("987654321"), y);
    }
 
    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> KaratsubaMultiplier.karatsuba(null, BigInteger.ONE));
        assertThrows(NullPointerException.class,
                () -> KaratsubaMultiplier.karatsuba(BigInteger.ONE, null));
    }
}

