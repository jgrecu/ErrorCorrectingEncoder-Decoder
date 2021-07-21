package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String encodedMessage = encodeMessage(input);
        String scrambledMessage = errorEmulator(encodedMessage);
        String decodedMessage = decodeMessage(scrambledMessage);
        System.out.println(input);
        System.out.println(encodedMessage);
        System.out.println(scrambledMessage);
        System.out.println(decodedMessage);
    }

    public static String errorEmulator(String string) {
        StringBuilder sb = new StringBuilder(string);
        Random r = new Random(3);
        int size = string.length();
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < size - 2; i += 3) {
            int rand1 = r.nextInt(3);
            sb.replace(i + rand1, i + rand1 + 1, String.valueOf(chars.charAt(r.nextInt(chars.length()))));
        }
        return sb.toString();
    }

    public static String encodeMessage(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            sb.append(string.charAt(i));
            sb.append(string.charAt(i));
            sb.append(string.charAt(i));
        }
        return sb.toString();
    }

    public static String decodeMessage(String string) {
        StringBuilder sb = new StringBuilder();
        char[] input = string.toCharArray();

        for (int i = 1; i < input.length - 1; i +=3) {
            if (input[i] == input[i - 1] || input[i - 1] == input[i + 1]) {
                sb.append(input[i - 1]);
            } else {
                sb.append(input[i]);
            }
        }
        return sb.toString();
    }
}
