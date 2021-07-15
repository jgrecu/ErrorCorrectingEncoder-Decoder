package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(errorEmulator(input));
    }

    public static String errorEmulator(String string) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random(3);
        final String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        char[] input = string.toCharArray();
        int N = chars.length();
        for(int i = 2; i < input.length; i += 3) {
            input[i] = chars.charAt(r.nextInt(N));
        }
        for (char c : input) {
            sb.append(c);
        }
        return sb.toString();
    }
}
