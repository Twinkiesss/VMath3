import java.util.Scanner;

public class NumericIntegration {

    // Функция f(x) = -2x^3 - 3x^2 + x + 5
    static double f(double x) {
        return -2.0 * x*x*x - 3.0 * x*x + x + 5.0;
    }

    // Левые прямоугольники
    static double rectangleLeft(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            double xi = a + i * h;
            sum += f(xi);
        }
        return h * sum;
    }

    // Правые прямоугольники
    static double rectangleRight(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.0;
        for (int i = 1; i <= n; i++) {
            double xi = a + i * h;
            sum += f(xi);
        }
        return h * sum;
    }

    // Средние прямоугольники
    static double rectangleMiddle(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            double xi = a + (i + 0.5) * h;
            sum += f(xi);
        }
        return h * sum;
    }

    // Метод трапеций
    static double trapezoid(double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.5 * (f(a) + f(b));
        for (int i = 1; i < n; i++) {
            sum += f(a + i * h);
        }
        return h * sum;
    }

    // Метод Симпсона (n чётное)
    static double simpson(double a, double b, int n) {
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n должно быть чётным");
        }
        double h = (b - a) / n;
        double sum = f(a) + f(b);
        for (int i = 1; i < n; i++) {
            double xi = a + i * h;
            sum += (i % 2 == 1) ? 4.0 * f(xi) : 2.0 * f(xi);
        }
        return h * sum / 3.0;
    }

    // Правило Рунге (для метода трапеций)
    static double rungeTrap(double a, double b, double eps) {
        int n = 4;
        double h = (b - a) / n;
        double I1, I2;
        do {
            I1 = trapezoid(a, b, n);
            n *= 2;
            h = (b - a) / n;
            I2 = trapezoid(a, b, n);
            double error = Math.abs(I2 - I1) / 3.0; // p=2
        } while (Math.abs(I2 - I1) > 3.0 * eps);
        return I2;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Лабораторная работа №3. Численное интегрирование");
        System.out.println("Функция: f(x) = -2*x^3 - 3*x^2 + x + 5");
        System.out.print("Введите пределы интегрирования a и b: ");
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        System.out.print("Введите требуемую точность (eps): ");
        double eps = sc.nextDouble();

        int n = 10;
        double I_mid   = rectangleMiddle(a, b, n);
        double I_trap  = trapezoid(a, b, n);
        double I_simp  = simpson(a, b, n);

        System.out.printf("\nРезультаты при n=%d:\n", n);
        System.out.printf("Средние прямоугольники: %.6f\n", I_mid);
        System.out.printf("Метод трапеций:        %.6f\n", I_trap);
        System.out.printf("Метод Симпсона:        %.6f\n", I_simp);

        double I_runge = rungeTrap(a, b, eps);
        System.out.printf("По правилу Рунге (трапеции): %.6f (eps=%.2e)\n",
                I_runge, eps);

        sc.close();
    }
}