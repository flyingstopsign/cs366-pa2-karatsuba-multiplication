package edu.wne.cs366;
 
import java.math.BigInteger;
import java.util.Objects;
 
/**
 * CS366 Programming Assignment 02: Karatsuba Multiplication.
 *
 * @author Brady Paiva
 */
public class KaratsubaMultiplier {
 
    private static final BigInteger TEN = BigInteger.TEN;
 
    /**
     * Multiplies two signed integers using recursive Karatsuba multiplication.
     *
     * @param x first factor
     * @param y second factor
     * @return x multiplied by y
     * @throws NullPointerException when either argument is null
     */
    public static BigInteger karatsuba(BigInteger x, BigInteger y) {
        Objects.requireNonNull(x, "x can't be null");
        Objects.requireNonNull(y, "y can't null");
 
        // Record the result's sign, then work with absolute values.
        int sign = x.signum() * y.signum();
        BigInteger product = karatsubaNonnegative(x.abs(), y.abs());
 
        // Restore the sign only after computing the nonnegative product.
        return sign < 0 ? product.negate() : product;
    }


        private static BigInteger karatsubaNonnegative(BigInteger x, BigInteger y) {
        int xDigits = decimalDigits(x);
        int yDigits = decimalDigits(y);
 
        //base case
        if (xDigits == 1 || yDigits == 1) {
            return x.multiply(y);
        }
 
        //n = greater digit count, m = n / 2, split at 10^m
        int n = Math.max(xDigits, yDigits);
        int m = n / 2;
        BigInteger splitter = TEN.pow(m);
 
        //x = x_high * 10^m + x_low
        BigInteger[] xParts = x.divideAndRemainder(splitter);
        BigInteger xHigh = xParts[0];
        BigInteger xLow = xParts[1];
 
        //y = y_high * 10^m + y_low
        BigInteger[] yParts = y.divideAndRemainder(splitter);
        BigInteger yHigh = yParts[0];
        BigInteger yLow = yParts[1];
 
        BigInteger highProduct = karatsubaNonnegative(xHigh, yHigh);
        BigInteger lowProduct = karatsubaNonnegative(xLow, yLow);
        BigInteger sumProduct = karatsubaNonnegative(xHigh.add(xLow), yHigh.add(yLow));
 
        BigInteger crossProduct = sumProduct.subtract(highProduct).subtract(lowProduct);
 
        // result = high_product * 10^(2m) + cross_product * 10^m + low_product
        return highProduct.multiply(TEN.pow(2 * m))
                .add(crossProduct.multiply(splitter))
                .add(lowProduct);

    }

    private static int decimalDigits(BigInteger value) {
        if (value.signum() == 0) {
            return 1;
        }
 
        //estimate from the bit length log10(2), compare against powers of 10
        int digits = (int) (value.bitLength() * 0.3010299) + 1;
        if (value.compareTo(TEN.pow(digits - 1)) < 0) {
            digits--;//estimate was one too high
        } else if (value.compareTo(TEN.pow(digits)) >= 0) {
            digits++;//estimate was one too low
        }
        return digits;
    }

    public static void main(String[] args) {
        BigInteger x = new BigInteger("3412");
        BigInteger y = new BigInteger("1523");
        System.out.printf("%s × %s = %s%n", x, y, karatsuba(x, y));
    }
}
