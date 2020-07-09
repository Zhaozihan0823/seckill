package nefu.zzh.commons.Util;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String salt = "1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     * 将输入的密码加密一次
     * @param inputPass
     * @return
     */
    public static String inputPassFormPass(String inputPass){
        String str = "" + salt.charAt(1) + salt.charAt(4) + inputPass + salt.charAt(5) + salt.charAt(2);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt){
        String str = "" + salt.charAt(1) + salt.charAt(4) + formPass + salt.charAt(5) + salt.charAt(2);
        return md5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB){
        return formPassToDBPass(inputPassFormPass(input), saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassFormPass("123456"));
        System.out.println(formPassToDBPass(inputPassFormPass("123456"), "1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
    }

}
