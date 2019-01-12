package com.example.goldenman.myapplication;

public class FPE_Algo {

    static byte[] initv = {24, -24, 67, 9, 47, -12, -32, 123, -46, 12, 45, 67, -12, -98, 20, -15};
    static AES aes;

    public void init() throws Exception {
        aes = new AES();
        aes.initCipher();
    }

    public static byte[] rotateinit(byte[] a, byte b) {
        for (int i = 0; i < a.length - 1; i++) {
            byte a1 = a[i];
            byte a2 = (byte) (a1 << 4);// lower nibble to higher
            byte a3 = a[i + 1];
            byte a4 = (byte) ((a3 & 0xf0) >> 4); //extracting higher nibble of next byte
            byte a5 = (byte) (a2 + a4);
            a[i] = a5;
        }
        int l = a.length - 1;
        byte b1 = a[l];
        byte b2 = (byte) (b1 << 4);
        byte b3 = (byte) (b2 + b);
        a[l] = b3;
        return a;
    }

    public static byte byte_encrypt(byte[] c, byte[] plain) throws Exception {
        byte[] ab = aes.encrypt(c);
        byte message = plain[0];
        byte d1 = ab[0];
        byte d2 = (byte) ((d1 & 0xf0) >> 4);
//         System.out.println(Integer.toBinaryString(d2));
        byte d3 = (byte) ((d2 + message) % 10);

        byte k4 = d3;

        //byte k4=(byte) (pt ^ d2);
//        System.out.println("cipher="+k4+"\n");
        return k4;
    }

    public static byte byte_decrypt(byte[] iv, byte c) throws Exception {
        byte result;
//        System.out.println("the init vector is:");
//         for(int i=0;i<iv.length;i++)
//        {
//            System.out.print(iv[i]+",");
//
//        }
//         System.out.println();
        byte[] cd = aes.encrypt(iv);
//         for(int i=0;i<cd.length;i++)
//        {
//            System.out.print(cd[i]+",");
//
//        }
//
//         System.out.println();
        //System.out.println(Integer.toBinaryString(cd[0]));
        byte f1 = cd[0];
        byte f2 = (byte) ((f1 & 0xf0) >> 4);
//         System.out.println(Integer.toBinaryString(f2));
        byte f3;
        if (c < f2) {
            f3 = (byte) (10 + (c - f2));
        } else {
            f3 = (byte) ((c - f2) % 10);
        }
        if (f3 < 0) {
            f3 = (byte) (10 + f3);
        }

//         System.out.println("ans="+f3);

        result = f3;
        return result;
    }

    public static String fpe_encrypt(String ip) throws Exception {
        byte[] cix = new byte[ip.length()];
        byte[] iv = new byte[initv.length];
        System.arraycopy(initv, 0, iv, 0, initv.length);
        for (int i = 0; i < ip.length(); i++) {
            Character c = ip.charAt(i);
            String a = c.toString();
            byte[] pt = new byte[1];

            pt[0] = (byte) Integer.parseInt(a);

            cix[i] = byte_encrypt(iv, pt);
            iv = rotateinit(iv, cix[i]);
        }
        String result = "";
        for (int j = 0; j < ip.length(); j++) {
            int b = cix[j];
            result = result + Integer.toString(b);
        }
        return result;
    }

    public static String fpe_decrypt(String ip) throws Exception {
        byte[] pix = new byte[ip.length()];
        byte[] iv = new byte[initv.length];
        System.arraycopy(initv, 0, iv, 0, initv.length);
        for (int i = 0; i < ip.length(); i++) {
            Character c = ip.charAt(i);
            String a = c.toString();
            byte pt = (byte) Integer.parseInt(a);
            byte opdecrypt;
            if (i == 0) {
                opdecrypt = byte_decrypt(iv, pt);
            } else {
                opdecrypt = byte_decrypt(iv, pt);
            }

            iv = rotateinit(iv, pt);
            pix[i] = opdecrypt;
        }
        String result = "";
        for (int j = 0; j < ip.length(); j++) {
            int b = pix[j];
            result = result + Integer.toString(b);
        }
        return result;
    }
}
