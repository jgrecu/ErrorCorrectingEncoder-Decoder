package correcter;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Write a mode: ");
        String input = scanner.nextLine();
        switch (input) {
            case "encode":
                encode();
                break;
            case "send":
                send();
                break;
            case "decode":
                decode();
                break;
            default:
                break;
        }
    }

    public static int getBit(byte b, int index) {
        return (b & (1 << (7 - index))) == 0 ? 0 : 1;
    }

    public static byte setBit(byte b, int bit, int index) {
        if (bit == 0) {
            if (getBit(b, index) == 0) {
                b = (byte) (b ^ (1 << (7 - index)));
            }
            return (byte) (b ^ (1 << (7 - index)));
        }
        return (byte) (b | (bit << (7 - index)));
    }

    public static void encode() {
        File file = new File("send.txt");

        int numBytes = (int) file.length();
        int numBits = numBytes * 8;
        int numOutBytes = numBits / 3 + (numBits % 3 == 0 ? 0 : 1);

        byte[] in_data = new byte[numBytes];
        byte[] out_data = new byte[numOutBytes];

        try (FileInputStream inputStream = new FileInputStream("send.txt")) {
            inputStream.read(in_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (var i : in_data) {
            System.out.print(i + " ");
        }

        for (int i = 0; i < numOutBytes; ++i) {
            int sum = 0;
            out_data[i] = 0;
            for (int j = 0; j < 3; ++j) {
                int numBit = i * 3 + j;
                int numByte = numBit / 8;
                int bytePos = numBit % 8;
                int bit;
                if (numByte >= in_data.length) {
                    bit = 0;
                } else {
                    bit = getBit(in_data[numByte], bytePos);
                }
                sum ^= bit;
                out_data[i] = setBit(out_data[i], bit, j * 2);
                out_data[i] = setBit(out_data[i], bit, j * 2 + 1);
            }
            out_data[i] = setBit(out_data[i], sum, 6);
            out_data[i] = setBit(out_data[i], sum, 7);
        }

        for (var i : out_data) {
            System.out.print(i + " ");
        }

        try (OutputStream outputStream = new FileOutputStream("encoded.txt", false)) {
            outputStream.write(out_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send() {
        Random r = new Random();
        File file = new File("encoded.txt");
        int numBytes = (int) file.length();
        byte[] data = new byte[numBytes];

        try (FileInputStream inputStream = new FileInputStream("encoded.txt")) {
            inputStream.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numBytes; ++i) {
            int pos = r.nextInt(8);
            data[i] ^= (1 << pos);
        }

        for (var i : data) {
            System.out.print(i + " ");
        }

        try (OutputStream outputStream = new FileOutputStream("received.txt", false)) {
            outputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte decodeBytes(byte input) {
        int xor = 0;
        for (int i = 0; i < 3; i++) {
            if (getBit(input, i * 2) == getBit(input, i * 2 + 1))
                xor ^= getBit(input, i * 2);
        }

        for (int j = 0; j < 3; ++j) {
            if (getBit(input, j * 2) != getBit(input, j * 2 + 1)) {
                input = setBit(input, xor ^ getBit(input, 6), j * 2);
                input = setBit(input, xor ^ getBit(input, 6), j * 2 + 1);
                return input;
            }
        }

        input = setBit(input, xor , 6);
        input = setBit(input, xor , 7);

        return input;
    }

    public static void decode() {
        File file = new File("received.txt");
        int numBytes = (int) file.length();
        int numOutBytes = (numBytes * 3) / 8;
        byte[] data = new byte[numBytes];
        byte[] out_data = new byte[numOutBytes];

        try (FileInputStream inputStream = new FileInputStream("received.txt")) {
            inputStream.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data.length; ++i) {
            data[i] = decodeBytes(data[i]);
            for (int j = 0; j < 3; j++) {
                int bitIndex = i * 3 + j;
                int byteIndex = bitIndex / 8;
                int bytePos = bitIndex % 8;
                int bit = getBit(data[i], j * 2);
                if (byteIndex < numOutBytes) {
                    out_data[byteIndex] = setBit(out_data[byteIndex], bit, bytePos);
                }
            }
        }

        for (var i : out_data) {
            System.out.print(i + " ");
        }

        try (OutputStream outputStream = new FileOutputStream("decoded.txt", false)) {
            outputStream.write(out_data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
