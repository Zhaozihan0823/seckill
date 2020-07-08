package nefu.zzh.commons.validator;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern  mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String str){
        if (StringUtils.isEmpty(str)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(str);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("1778700272"));
    }
}
