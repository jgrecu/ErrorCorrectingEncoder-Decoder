package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String encodedMessage = encodeMessage(input);
        String receivedMessage = errorEmulator(encodedMessage);
        String decodedMessage = decodeMessage(receivedMessage);
        System.out.println(input);
        System.out.println(encodedMessage);
        System.out.println(receivedMessage);
        System.out.println(decodedMessage);
    }

    public static String errorEmulator(String string) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random(3);
        final String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        char[] input = string.toCharArray();
        int N = chars.length();
        for(int i = 2; i < input.length; i += 3) {
            char replace = chars.charAt(r.nextInt(N));
            if (input[i] == replace) {
                i -=3;
            } else {
                input[i] = chars.charAt(r.nextInt(N));
            }

        }
        for (char c : input) {
            sb.append(c);
        }
        return sb.toString();
    }

//    public static String errorEmulator(String string) {
//        StringBuilder sb = new StringBuilder();
//        Random r = new Random(3);
//        int size = string.length();
//        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        for (int i = 0; i < size; i++) {
//            if (i % 3 == 0) {
//                if (size - i >= 3){
//                    String subString = string.substring(i, i + 3);
//                    System.out.println("Initial substring: " + subString);
//                    int rand1 = r.nextInt(3);
//                    char replaceChar = subString.charAt(rand1);
//                    System.out.println("replace char: " + replaceChar);
//                    subString = subString.replace(replaceChar, chars.charAt(r.nextInt(62)));
//                    System.out.println("Final substring: " + subString);
//                    sb.append(subString);
//                } else if (size - i < 3) {
//                    String subString = string.substring(i, i + size - i);
//                    int rand1 = r.nextInt(subString.length());
//                    char replaceChar = subString.charAt(rand1);
//                    subString = subString.replace(replaceChar, chars.charAt(r.nextInt(62)));
//                    sb.append(subString);
//                }
//            }
//        }
//        return sb.toString();
//    }

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
            if (input[i] == input[i - 1]) {
                sb.append(input[i]);
            } else if (input[i] == input[i + 1]) {
                sb.append(input[i]);
            } else if (input[i - 1] == input[i + 1]) {
                sb.append(input[i + 1]);
            }
        }
        return sb.toString();
    }
}
