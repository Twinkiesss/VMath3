package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Лабораторная работа №3. Численное интегрирование");

        System.out.println("\nОбычные функции:");
        System.out.println("0) f(x) = -2x^3 - 3x^2 + x + 5");
        System.out.println("1) f(x) = x^2");
        System.out.println("2) f(x) = e^x");
        System.out.println("3) f(x) = sin(x)");
        System.out.println("4) f(x) = 1/(1+x^2)");

        System.out.println("\nНесобственные функции:");
        System.out.println("6) f(x) = 1/(1-x)^(2/3)");
        System.out.println("8) f(x) = 1/|x-1|");
        System.out.println("9) f(x) = 1/x");

        int funcId = readFunction(sc);

        System.out.println("\nВыберите метод:");
        System.out.println("1 - Левые прямоугольники");
        System.out.println("2 - Правые прямоугольники");
        System.out.println("3 - Средние прямоугольники");
        System.out.println("4 - Трапеции");
        System.out.println("5 - Симпсон");

        int method = readIntInRange(
                sc,
                "Введите метод: ",
                1,
                5
        );

        double eps;

        while (true) {

            eps = readDouble(sc, "Введите точность eps: ");
            if (eps > 0) {
                break;
            }
            System.out.println("Ошибка: eps должен быть > 0");
        }

        // обычные функции
        if (funcId >= 0 && funcId <= 4) {
            double[] ab = readTwoDoubles(
                    sc,
                    "Введите a и b: "
            );

            double a = ab[0];
            double b = ab[1];

            try {
                NumericIntegration.Result result =
                        NumericIntegration.integrate(
                                method,
                                a,
                                b,
                                eps,
                                funcId
                        );
                print(method, result);

            } catch (ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        // несобственные функции
        else {
            NumericIntegration.Result result;

            switch (funcId) {

                // 1/(1-x)^(2/3)
                case 6:
                    double a6 = readDouble(sc, "Введите a: ");
                    double b6 = readDouble(sc, "Введите b: ");

                    try {

                        // разрыв в левом конце
                        if (Math.abs(a6 - 1) < 1e-8) {

                            result =
                                    ImproperIntegral2ndKind.integrateFromA(
                                            b6,
                                            eps,
                                            method
                                    );

                            print(method, result);
                            break;
                        }

                        // разрыв в правом конце
                        if (Math.abs(b6 - 1) < 1e-8) {

                            result = ImproperIntegral2ndKind.integrateAtB(
                                            a6,
                                            eps,
                                            method
                                    );
                            print(method, result);
                            break;
                        }

                        // разрыв внутри интервала
                        if (a6 < 1 && b6 > 1) {
                            result = ImproperIntegral2ndKind
                                            .integrateAroundPoint(
                                                    a6,
                                                    b6,
                                                    eps,
                                                    method
                                            );
                            print(method, result);
                            break;
                        }

                        // обычный случай
                        result = NumericIntegration.integrate(
                                method,
                                a6,
                                b6,
                                eps,
                                IntegrandFunction::f6
                        );
                        print(method, result);

                    } catch (ArithmeticException e) {
                        System.out.println("Интеграл не существует.");
                    }
                    break;

                // 1/|x-1|
                case 8:
                    double a8 = readDouble(sc, "Введите a: ");
                    double b8 = readDouble(sc, "Введите b: ");

                    // разрыв внутри интервала
                    if (a8 <= 1 && b8 >= 1) {
                        System.out.println("Интеграл не существует");

                    } else {
                        try {
                            result = NumericIntegration.integrate(
                                    method,
                                    a8,
                                    b8,
                                    eps,
                                    x -> 1.0 / Math.abs(x - 1)
                            );
                            print(method, result);

                        } catch (ArithmeticException e) {
                            System.out.println("Интеграл не существует.");
                        }
                    }

                    break;

                // 1/x
                case 9:
                    double a9 = readDouble(sc, "Введите a: ");
                    double b9 = readDouble(sc, "Введите b: ");

                    try {

                        if (a9 < 0 && b9 > 0) {
                            result = ImproperIntegral2ndKind
                                            .principalValue1OverX(
                                                    a9,
                                                    b9,
                                                    eps,
                                                    method
                                            );

                            print(method, result);

                        } else {

                            result = NumericIntegration.integrate(
                                    method,
                                    a9,
                                    b9,
                                    eps,
                                    IntegrandFunction::f9
                            );

                            print(method, result);
                        }

                    } catch (ArithmeticException e) {
                        System.out.println(
                                "Интеграл не существует."
                        );
                    }
                    break;

                default:
                    System.out.println(
                            "Некорректный выбор функции."
                    );
            }
        }
        sc.close();
    }

    private static int readFunction(Scanner sc) {

        while (true) {
            int value = readIntInRange(
                    sc,
                    "\nВыберите функцию: ",
                    0,
                    9
            );

            if (value == 5 || value == 7) {
                System.out.println("Ошибка: такой функции нет.");
                continue;
            }
            return value;
        }
    }

    private static int readIntInRange(
            Scanner sc,
            String message,
            int min,
            int max
    ) {

        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(
                        sc.nextLine()
                );

                if (value < min || value > max) {
                    System.out.println(
                            "Ошибка: число должно быть от "
                                    + min + " до " + max
                    );
                    continue;
                }
                return value;

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    private static double readDouble(
            Scanner sc,
            String message
    ) {

        while (true) {
            System.out.print(message);

            try {
                return Double.parseDouble(
                        sc.nextLine().replace(',', '.')
                );

            } catch (NumberFormatException e) {

                System.out.println("Ошибка: введите число.");
            }
        }
    }

    private static double[] readTwoDoubles(
            Scanner sc,
            String message
    ) {

        while (true) {
            System.out.print(message);

            try {
                String[] parts = sc.nextLine()
                        .replace(',', '.')
                        .trim()
                        .split("\\s+");

                if (parts.length != 2) {
                    System.out.println("Ошибка: нужно ввести два числа.");
                    continue;
                }

                double a = Double.parseDouble(parts[0]);
                double b = Double.parseDouble(parts[1]);
                return new double[]{a, b};

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены некорректные числа.");
            }
        }
    }

    private static void print(
            int method,
            NumericIntegration.Result r
    ) {
        printMethodName(method);
        System.out.printf("%.10f\n", r.value);
        System.out.println("Число разбиений n = " + r.n);
    }
    private static void printMethodName(int method) {
        switch (method) {
            case 1 -> System.out.print("Левые прямоугольники: ");
            case 2 -> System.out.print("Правые прямоугольники: ");
            case 3 -> System.out.print("Средние прямоугольники: ");
            case 4 -> System.out.print("Метод трапеций: ");
            case 5 -> System.out.print( "Метод Симпсона: ");
        }
    }
}