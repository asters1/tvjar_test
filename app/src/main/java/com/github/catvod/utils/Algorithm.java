package com.github.catvod.utils;

import java.security.MessageDigest;

public class Algorithm {
    public static String getMd5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] data = md.digest(str.getBytes("utf-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {
                sb.append(Integer.toHexString(b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("MD5运行错误!!");
        }
        return "";
    }
}
