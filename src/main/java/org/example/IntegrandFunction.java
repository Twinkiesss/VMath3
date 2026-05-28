package org.example;

public class IntegrandFunction {

    public static double f(int funcId, double x) {

        switch (funcId) {

            case 0:
                return -2 * x * x * x - 3 * x * x + x + 5;

            case 1:
                return x * x;

            case 2:
                return Math.exp(x);

            case 3:
                return Math.sin(x);

            case 4:
                return 1.0 / (1 + x * x);

            default:
                throw new IllegalArgumentException("Нет функции");
        }
    }

    public static double f6(double x) {
        return 1.0 / Math.pow(Math.abs(1 - x), 2.0 / 3.0);
    }

    public static double f9(double x) {
        return 1.0 / x;
    }
}