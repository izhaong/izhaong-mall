package com.izhaong.mall.util;

import com.izhaong.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author izhaong
 */
public class MD5Utils {
    public static String getMD5Str(String strVal) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((strVal + Constant.SAIT).getBytes()));
    }
}
