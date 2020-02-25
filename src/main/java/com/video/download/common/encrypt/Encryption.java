package com.video.download.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xiaom
 * @Date 2020/1/9 14:53
 * @Version 1.0.0
 * @Description <91加密/解密工具>
 **/
public class Encryption {

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
        System.out.println("sign1:" + sign);
        System.out.println("data2:" + encrypt);
        System.out.println("time3:" + timestamp);
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

    public static byte[][] a(int var0, int var1, byte[] var2, byte[] var3, int var4) throws Exception {
        MessageDigest var12 = MessageDigest.getInstance("md5");
        byte[][] var13 = new byte[2][];
        byte[] var14 = new byte[var0];
        byte[] var15 = new byte[var1];
        var13[0] = var14;
        var13[1] = var15;
        if (var3 == null) {
            return var13;
        } else {
            byte[] var11 = null;
            int var6 = var1;
            byte var7 = 0;
            var1 = 0;
            int var5 = var0;
            int var8 = 0;
            var0 = var6;
            var6 = var7;

            while (true) {
                var12.reset();
                if (var8 > 0) {
                    var12.update(var11);
                }

                var12.update(var3);
                if (var2 != null) {
                    var12.update(var2, 0, 8);
                }

                var11 = var12.digest();

                int var16;
                for (var16 = 1; var16 < var4; ++var16) {
                    var12.reset();
                    var12.update(var11);
                    var11 = var12.digest();
                }

                if (var5 > 0) {
                    for (var16 = 0; var5 != 0 && var16 != var11.length; ++var6) {
                        var14[var6] = var11[var16];
                        --var5;
                        ++var16;
                    }
                } else {
                    var16 = 0;
                }

                int var9 = var0;
                int var10 = var1;
                if (var0 > 0) {
                    var9 = var0;
                    var10 = var1;
                    if (var16 != var11.length) {
                        label90:
                        {
                            while (var0 != 0) {
                                if (var16 == var11.length) {
                                    var9 = var0;
                                    var10 = var1;
                                    break label90;
                                }

                                var15[var1] = var11[var16];
                                --var0;
                                ++var16;
                                ++var1;
                            }

                            var9 = var0;
                            var10 = var1;
                        }
                    }
                }

                if (var5 == 0 && var9 == 0) {
                    for (var0 = 0; var0 < var11.length; ++var0) {
                        var11[var0] = 0;
                    }

                    return var13;
                }

                ++var8;
                var0 = var9;
                var1 = var10;
            }
        }
    }

    public static byte[] a(byte[] var0, byte[] var1) {
        byte[] var2 = new byte[var0.length + var1.length];
        System.arraycopy(var0, 0, var2, 0, var0.length);
        System.arraycopy(var1, 0, var2, var0.length, var1.length);
        return var2;
    }

    private static String b(byte[] var0) {
        String var2 = "";

        for (int var1 = 0; var1 < var0.length; ++var1) {
            String var3 = Integer.toHexString(var0[var1] & 255);
            StringBuilder var4;
            if (var3.length() == 1) {
                var4 = new StringBuilder();
                var4.append(var2);
                var4.append("0");
                var4.append(var3);
                var2 = var4.toString();
            } else {
                var4 = new StringBuilder();
                var4.append(var2);
                var4.append(var3);
                var2 = var4.toString();
            }
        }

        return var2.toUpperCase();
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
