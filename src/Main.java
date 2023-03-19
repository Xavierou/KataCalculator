import com.sun.jdi.InvalidTypeException;

import java.util.Scanner;
public class Main {
    static final Scanner in = new Scanner(System.in);
    // Объявление массивов tens и ones для облегчения перевода из арабской записи
    // в римскую
    static final String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    static final String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
    static String to_roman(int num) {
        return tens[num / 10] + ones[num % 10];
    }

    /**
     * Рекурсивная функция для перевода из римской записи в арабскую
     * @param num - римское число
     * @return переведенное в арабскую запись число
     */
    static int to_arabic(String num) {
        if (num.isEmpty()) return 0;
        if (num.startsWith("XC")) return 90 + to_arabic(num.replace("XC", ""));
        if (num.startsWith("L")) return 50 + to_arabic(num.replace("L", ""));
        if (num.startsWith("XL")) return 40 + to_arabic(num.replace("XL", ""));
        if (num.startsWith("X")) return 10 + to_arabic(num.replaceFirst("X", ""));
        if (num.startsWith("IX")) return 9 + to_arabic(num.replace("IX", ""));
        if (num.startsWith("V")) return 5 + to_arabic(num.replace("V", ""));
        if (num.startsWith("IV")) return 4 + to_arabic(num.replace("IV", ""));
        if (num.startsWith("I")) return 1 + to_arabic(num.replaceFirst("I", ""));
        return 0;
    }

    /**
     * Основная функция; проверяет ввод и с помощью метода compute производит вычисления
     * @param input - вводимое выражение
     * @return - результат вычисления
     * @throws InvalidTypeException - в случае, если формат ввода не подхоодит
     */
    public static String calc(String input) throws InvalidTypeException {
        String romanStr = "([iIvVxX])+\\s?[-+*/]\\s?([iIvVxX])+";
        String arabStr = "^\\d?\\s?[-+/*]\\s?\\d?";
        String out;
        if (input.matches(romanStr)) {
            out = compute(input, false);
        }
        else if (input.matches(arabStr)) {
            out = compute(input, true);
        }
        else throw new InvalidTypeException("Cannot match pattern");

        return out;
    }

    /**
     * Функция для расчета выражения
     * @param str - вводимое выражение
     * @param isArabic - флаг, указывающий, в каком формате записано выражение
     * @return - результат вычислений в формате String
     * @throws ArithmeticException - в случае, если римское число выходит за рамки значений
     */
    static String compute(String str, boolean isArabic) throws ArithmeticException {
        String[] expr = str.split(" ");
        int result = 0;
        int num1;
        int num2;
        if (isArabic) {
            num1 = Integer.parseInt(expr[0]);
            num2 = Integer.parseInt(expr[2]);
        }
        else {
            num1 = to_arabic(expr[0].toUpperCase());
            num2 = to_arabic(expr[2].toUpperCase());
        }

        switch (expr[1]) {
            case "+" -> result = num1 + num2;
            case "-" -> result = num1 - num2;
            case "/" -> result = num1 / num2;
            case "*" -> result = num1 * num2;
        }

        if (isArabic) {
            return Integer.toString(result);
        }
        else {
            // Так как в римской записи число находится в рамках от 1 до 3999
            // (в контексте задачи - от 1 до 100), нужно проверить,
            // не вышел ли итоговый результат за эти рамки
            if ((result >= 1) && (result < 100)) {
                return to_roman(result);
            }
            else throw new ArithmeticException("Roman number out of range");
        }
    }
    public static void main(String[] args) {
        String expression = in.nextLine();
        try {
            System.out.println(calc(expression));
        }
        catch (InvalidTypeException | ArithmeticException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error");
        }
    }
}