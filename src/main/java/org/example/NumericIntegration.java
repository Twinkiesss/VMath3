package org.example;

import java.util.function.DoubleUnaryOperator;

public class NumericIntegration {

    public static class Result {

        public double value;
        public int n;

        public Result(double value, int n) {
            this.value = value;
            this.n = n;
        }
    }

    //обычные функции
    public static Result integrate(int method,
                                   double a,
                                   double b,
                                   double eps,
                                   int funcId) {

        return integrate(
                method,
                a,
                b,
                eps,
                x -> IntegrandFunction.f(funcId, x)
        );
    }

    public static Result integrate(int method,
                                   double a,
                                   double b,
                                   double eps,
                                   DoubleUnaryOperator f) {

        int n = 4;

        double i1;//знач при n
        double i2;//2n

        do {
            i1 = applyMethod(method, a, b, n, f);
            i2 = applyMethod(method, a, b, n * 2, f);
            n *= 2;

        } while (rungeError(i1, i2, method) > eps);

        return new Result(i2, n);
    }

    private static double rungeError(double i1,
                                     double i2,
                                     int method) {

        switch (method) {

            // левые/правые прямоугольники
            case 1:
            case 2:
                return Math.abs(i2 - i1);

            // средние/трапеции
            case 3:
            case 4:
                return Math.abs(i2 - i1) / 3.0;

            // Симпсон
            case 5:
                return Math.abs(i2 - i1) / 15.0;

            default:
                return Double.MAX_VALUE;
        }
    }

    private static double safeValue(DoubleUnaryOperator f,
                                    double x) {

        double val = f.applyAsDouble(x);

        if (Double.isNaN(val) || Double.isInfinite(val)) {
            throw new ArithmeticException("Разрыв функции");
        }

        return val;
    }

    private static double applyMethod(int method,
                                      double a,
                                      double b,
                                      int n,
                                      DoubleUnaryOperator f) {

        switch (method) {

            case 1:
                return rectangleLeft(a, b, n, f);

            case 2:
                return rectangleRight(a, b, n, f);

            case 3:
                return rectangleMiddle(a, b, n, f);

            case 4:
                return trapezoid(a, b, n, f);

            case 5:
                return simpson(a, b, n, f);

            default:
                throw new IllegalArgumentException("Некорректный метод");
        }
    }

    private static double rectangleLeft(double a,
                                        double b,
                                        int n,
                                        DoubleUnaryOperator f) {

        double h = (b - a) / n;
        double sum = 0;

        for (int i = 0; i < n; i++) {
            double x = a + i * h;
            sum += safeValue(f, x);
        }

        return sum * h;
    }

    private static double rectangleRight(double a,
                                         double b,
                                         int n,
                                         DoubleUnaryOperator f) {

        double h = (b - a) / n;
        double sum = 0;

        for (int i = 1; i <= n; i++) {
            double x = a + i * h;
            sum += safeValue(f, x);
        }

        return sum * h;
    }

    private static double rectangleMiddle(double a,
                                          double b,
                                          int n,
                                          DoubleUnaryOperator f) {

        double h = (b - a) / n;
        double sum = 0;

        for (int i = 0; i < n; i++) {
            double x = a + (i + 0.5) * h;
            sum += safeValue(f, x);
        }

        return sum * h;
    }

    private static double trapezoid(double a,
                                    double b,
                                    int n,
                                    DoubleUnaryOperator f) {

        double h = (b - a) / n;
        double sum = (safeValue(f, a) + safeValue(f, b)) / 2.0;

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += safeValue(f, x);
        }

        return sum * h;
    }

    private static double simpson(double a,
                                  double b,
                                  int n,
                                  DoubleUnaryOperator f) {

        if (n % 2 != 0) {
            n++;
        }

        double h = (b - a) / n;
        double sum = safeValue(f, a) + safeValue(f, b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;

            if (i % 2 == 0) {
                sum += 2 * safeValue(f, x);
            } else {
                sum += 4 * safeValue(f, x);
            }
        }

        return sum * h / 3.0;
    }
}