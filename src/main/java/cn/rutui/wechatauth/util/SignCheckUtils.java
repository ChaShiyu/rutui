package cn.rutui.wechatauth.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 签名验证工具类
 */
@Slf4j
public class SignCheckUtils {

    //设置秘钥, 和微信公众平台一致
    public static final String TOKEN = "sjyx";

    /**
     * @param timestamp timestamp
     * @param nonce     nonce
     * @param signature signature
     * @return bool
     */
    public static boolean check(String timestamp, String nonce, String signature) {
        //排序
        String[] arr = {TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }

        //sha1Hex 加密
        MessageDigest md;
        String temp = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            temp = byteToStr(digest);
            log.info("param sign={}, encrypt sign={}", signature, temp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return temp != null && (temp.toLowerCase()).equals(signature);
    }

    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }

}