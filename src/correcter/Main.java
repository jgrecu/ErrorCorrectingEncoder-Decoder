/*
 * Copyright (c) 2021. Jeremy Grecu
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package correcter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
        scanner.close();
    }


    private static int getBitAt(byte b, int index) {
        int shift = 7 - index;
        return (b & (1 << shift)) >> shift;
    }

    private static byte setBitAt(byte b, int index, int bit) {
        if (bit == 1) {
            return (byte) (b | (1 << (7 - index)));
        } else {
            return (byte) (b & ~(1 << (7 - index)));
        }
    }

    private static byte encodeData(final int[] data) {
        /*
        use 'Hamming code' to encode
         */
        byte encodedData = 0;
        encodedData = setBitAt(encodedData, 0, data[0] ^ data[1] ^ data[3]);
        encodedData = setBitAt(encodedData, 2 - 1, data[0] ^ data[2] ^ data[3]);
        encodedData = setBitAt(encodedData, 3 - 1, data[0]);
        encodedData = setBitAt(encodedData, 4 - 1, data[1] ^ data[2] ^ data[3]);
        encodedData = setBitAt(encodedData, 5 - 1, data[1]);
        encodedData = setBitAt(encodedData, 6 - 1, data[2]);
        encodedData = setBitAt(encodedData, 7 - 1, data[3]);
        return encodedData;
    }

    public static void encode() {
        File file = new File("send.txt");

        int numBytes = (int) file.length();
        int numOutBytes = numBytes * 2;

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

        for (int i = 0; i < in_data.length; i++) {
            for (int j = 0; j < 2; j++) {
                int[] data = new int[4];
                for (int k = 0; k < 4; k++)
                    data[k] = getBitAt(in_data[i], j * 4 + k);
                out_data[2 * i + j] = encodeData(data);
            }
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
        byte[] inputData = new byte[numBytes];
        byte[] outputData = new byte[numBytes];

        try (FileInputStream inputStream = new FileInputStream("encoded.txt")) {
            inputStream.read(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (var i : inputData) {
            System.out.print(i + " ");
        }

        for (int i = 0; i < inputData.length; ++i) {
            int num = 1 << r.nextInt(8);
            outputData[i] = (byte) (inputData[i] ^ num);
        }

        for (var i : outputData) {
            System.out.print(i + " ");
        }

        try (OutputStream outputStream = new FileOutputStream("received.txt", false)) {
            outputStream.write(outputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int[] decodeData(byte data) {
        /*
        use 'Hamming code' to decode
         */
        int p1 = getBitAt(data, 0);
        int p2 = getBitAt(data, 2 - 1);
        int p4 = getBitAt(data, 4 - 1);
        int a = getBitAt(data, 3 - 1);
        int b = getBitAt(data, 5 - 1);
        int c = getBitAt(data, 6 - 1);
        int d = getBitAt(data, 7 - 1);
        boolean c1 = (p1 == (a ^ b ^ d));
        boolean c2 = (p2 == (a ^ c ^ d));
        boolean c4 = (p4 == (b ^ c ^ d));
        if (!(c1 && c2 || c2 && c4 || c4 && c1)) {
            if (c1) {
                c = shiftInteger(c);
            } else if (c2) {
                b = shiftInteger(b);
            } else if (c4) {
                a = shiftInteger(a);
            } else {
                d = shiftInteger(d);
            }
        }
        return new int[] { a, b, c, d };
    }

    private static int shiftInteger(int number) {
        return number == 1 ? 0 : 1;
    }

    public static void decode() {
        File file = new File("received.txt");
        int numBytes = (int) file.length();
        int numOutBytes = numBytes / 2;
        byte[] in_data = new byte[numBytes];
        byte[] out_data = new byte[numOutBytes];
        int decodedDataCount = 0;
        byte bitCount = 0;
        byte outData = 0;

        try (FileInputStream inputStream = new FileInputStream("received.txt")) {
            inputStream.read(in_data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (final byte data : in_data) {
            int[] bits = decodeData(data);
            for (byte i = 0; i < 4; i++) {
                outData = setBitAt(outData, bitCount, bits[bitCount % 4]);
                bitCount++;
            }
            if (bitCount == 8) {
                out_data[decodedDataCount++] = outData;
                bitCount = 0;
                outData = 0;
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
