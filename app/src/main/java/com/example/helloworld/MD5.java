package com.example.helloworld;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/13.
 */

public class MD5 {
    public static String getMD5(String val) {
        try {
            //定义一个名为MD5的哈希值
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //传入val转化为byte二进制的摘要
            md5.update(val.getBytes());
            //计算哈希值，并返回
            byte[] m = md5.digest();
            return getString(m);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i =0;i< b.length ;i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
