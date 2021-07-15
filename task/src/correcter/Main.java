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

//        for (int i = 0; i < size; i++) {
//            if (i % 3 == 0) {
//                if (size - i >= 3){
//                    String subString = string.substring(i, i + 3);
//                    int rand1 = r.nextInt(3);
//                    char replaceChar = subString.charAt(rand1);
//                    subString = subString.replace(replaceChar, chars.charAt(r.nextInt(62)));
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
        return sb.toString();
    }
}
