package com.video.download.common.encrypt;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * @Author xiaom
 * @Date 2020/2/18 14:57
 * @Version 1.0.0
 * @Description <>
 **/
public class EncryptionV2 {

    private static String key = "e79465cfbb39cjdusimcuekd3b066a6e";
    private static String salt = "132f1537f85sjdpcm59f7e318b9epa51";


    public static String encrypt(String data) {
        return b(key, data);
    }

    public static String decrypt(String data) {
        return a(key, data);
    }

    public static String sign(String timestamp, String encrypt) {
        StringBuilder sb = new StringBuilder("data=");
        sb.append(encrypt);
        sb.append("&timestamp=");
        sb.append(timestamp);
        sb.append(salt);

        String temp = a(sb.toString());
        String sign = null;
        try {
            sign = a(MessageDigest.getInstance("MD5").digest(temp.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String b(String var0, String var1) {
        try {
            Cipher var2 = Cipher.getInstance("AES/CFB/NoPadding");
            byte[][] var4 = a(32, 16, (byte[]) null, var0.getBytes("UTF-8"), 0);
            var2.init(1, new SecretKeySpec(var4[0], "AES"), new IvParameterSpec(var4[1]));
            var0 = b(a(var2.getIV(), var2.doFinal(var1.getBytes("UTF-8"))));
            return var0;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static byte[][] a(int i, int i2, byte[] bArr, byte[] bArr2, int i3) throws Exception {
        byte[] digest;
        int i4;
        byte[] bArr3 = bArr;
        byte[] bArr4 = bArr2;
        MessageDigest instance = MessageDigest.getInstance("md5");
        int i5 = i;
        byte[] bArr5 = new byte[i5];
        int i6 = i2;
        byte[] bArr6 = new byte[i6];
        byte[][] bArr7 = {bArr5, bArr6};
        if (bArr4 == null) {
            return bArr7;
        }
        byte[] bArr8 = null;
        int i7 = i6;
        int i8 = 0;
        int i9 = 0;
        int i10 = i5;
        int i11 = 0;
        while (true) {
            instance.reset();
            int i12 = i11 + 1;
            if (i11 > 0) {
                instance.update(bArr8);
            }
            instance.update(bArr4);
            if (bArr3 != null) {
                instance.update(bArr3, 0, 8);
            }
            digest = instance.digest();
            int i13 = i3;
            for (int i14 = 1; i14 < i13; i14++) {
                instance.reset();
                instance.update(digest);
                digest = instance.digest();
            }
            if (i10 > 0) {
                i4 = 0;
                while (i10 != 0 && i4 != digest.length) {
                    bArr5[i8] = digest[i4];
                    i10--;
                    i4++;
                    i8++;
                }
            } else {
                i4 = 0;
            }
            if (i7 > 0 && i4 != digest.length) {
                while (i7 != 0 && i4 != digest.length) {
                    bArr6[i9] = digest[i4];
                    i7--;
                    i4++;
                    i9++;
                }
            }
            if (i10 == 0 && i7 == 0) {
                break;
            }
            i11 = i12;
            bArr8 = digest;
        }
        for (int i15 = 0; i15 < digest.length; i15++) {
            digest[i15] = 0;
        }
        return bArr7;
    }

    public static byte[] a(byte[] var0, byte[] var1) {
        byte[] var2 = new byte[var0.length + var1.length];
        System.arraycopy(var0, 0, var2, 0, var0.length);
        System.arraycopy(var1, 0, var2, var0.length, var1.length);
        return var2;
    }

    private static String b(byte[] var0) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : var0) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
    }


    public static String a(String var0, String var1) {
        try {
            byte[] var6 = b(var1);
            byte[] var2 = Arrays.copyOfRange(var6, 0, 16);
            var6 = Arrays.copyOfRange(var6, 16, var6.length);
            byte[][] var3 = a(32, 16, (byte[]) null, var0.getBytes("UTF-8"), 0);
            IvParameterSpec var5 = new IvParameterSpec(var2);
            SecretKeySpec var7 = new SecretKeySpec(var3[0], "AES");
            Cipher var8 = Cipher.getInstance("AES/CFB/NoPadding");
            var8.init(2, var7, var5);
            var0 = new String(var8.doFinal(var6), "UTF-8");
            return var0;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private static byte[] b(String var0) {
        if (var0 == null) {
            return null;
        } else {
            int var1 = var0.length();
            if (var1 % 2 == 1) {
                return null;
            } else {
                int var2 = var1 / 2;
                byte[] var4 = new byte[var2];

                for (var1 = 0; var1 != var2; ++var1) {
                    int var3 = var1 * 2;
                    var4[var1] = (byte) Integer.parseInt(var0.substring(var3, var3 + 2), 16);
                }

                return var4;
            }
        }
    }

    public static String a(String var0) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("SHA-256");
            var1.update(var0.getBytes("UTF-8"));
            var0 = bb(var1.digest());
            return var0;
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return "";
    }

    private static String bb(byte[] var0) {
        StringBuffer var2 = new StringBuffer();

        for (int var1 = 0; var1 < var0.length; ++var1) {
            String var3 = Integer.toHexString(var0[var1] & 255);
            if (var3.length() == 1) {
                var2.append("0");
            }

            var2.append(var3);
        }

        return var2.toString();
    }

    private static String a(byte[] var0) {
        StringBuilder var5 = new StringBuilder();
        int var4 = var0.length;

        for (int var1 = 0; var1 < var4; ++var1) {
            byte var3 = var0[var1];
            int var2 = var3;
            if (var3 < 0) {
                var2 = var3 + 256;
            }

            if (var2 < 16) {
                var5.append("0");
            }

            var5.append(Integer.toHexString(var2));
        }

        return var5.toString();
    }
    public static void main(String[] args) {
        String encrypt = "";
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("输入请求体data：");
            if (in.hasNextLine()) {
                encrypt = in.nextLine();
                System.out.println("请求体data decrypt：" + decrypt(encrypt));
            }
        }

    }

}
