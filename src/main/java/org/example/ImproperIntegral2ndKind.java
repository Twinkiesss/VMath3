package org.example;

public class ImproperIntegral2ndKind {
    // Разрыв в правом конце x -> 1-
    public static NumericIntegration.Result integrateAtB(
            double a,
            double eps,
            int method
    ) {

        double delta = eps;

        return NumericIntegration.integrate(
                method,
                a,
                1 - delta,
                eps,
                IntegrandFunction::f6
        );
    }
    // Разрыв в левом конце x -> 1+
    public static NumericIntegration.Result integrateFromA(
            double b,
            double eps,
            int method
    ) {

        double delta = eps;

        return NumericIntegration.integrate(
                method,
                1 + delta,
                b,
                eps,
                IntegrandFunction::f6
        );
    }
    // Разрыв внутри интервала
    public static NumericIntegration.Result integrateAroundPoint(
            double a,
            double b,
            double eps,
            int method
    ) {

        double delta = eps;

        NumericIntegration.Result left =
                NumericIntegration.integrate(
                        method,
                        a,
                        1 - delta,
                        eps,
                        IntegrandFunction::f6
                );

        NumericIntegration.Result right =
                NumericIntegration.integrate(
                        method,
                        1 + delta,
                        b,
                        eps,
                        IntegrandFunction::f6
                );

        return new NumericIntegration.Result(
                left.value + right.value,
                Math.max(left.n, right.n)
        );
    }
    public static NumericIntegration.Result principalValue1OverX(
            double a,
            double b,
            double eps,
            int method
    ) {

        double symmetric =
                Math.min(Math.abs(a), Math.abs(b));

        double result = 0;
        int n = 0;

        if (Math.abs(a) > symmetric) {

            var r1 = NumericIntegration.integrate(
                    method,
                    a,
                    -symmetric,
                    eps,
                    IntegrandFunction::f9
            );

            result += r1.value;
            n = Math.max(n, r1.n);
        }

        if (Math.abs(b) > symmetric) {

            var r2 = NumericIntegration.integrate(
                    method,
                    symmetric,
                    b,
                    eps,
                    IntegrandFunction::f9
            );

            result += r2.value;
            n = Math.max(n, r2.n);
        }

        return new NumericIntegration.Result(result, n);
    }
}