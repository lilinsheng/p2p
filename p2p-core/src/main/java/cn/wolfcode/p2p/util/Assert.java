package cn.wolfcode.p2p.util;

import cn.wolfcode.p2p.base.exception.CustomException;
import cn.wolfcode.p2p.base.service.ILoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class Assert {

    /**
     * 断言不为空
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new CustomException(message);
        }
    }

    /**
     * 断言字符串不为空
     */
    public static void notNull(String str, String message) {
        if (!StringUtils.hasLength(str)) {
            throw new CustomException(message);
        }
    }

    /**
     * 断言字符串的长度范围
     * @param str 字符串
     * @param min 最小长度
     * @param max 最大长度
     * @param message 异常信息
     */
    public static void range(String str,int min,int max,String message) {
        notNull(str,message);
        if (str.length()<min||str.length()>max){
            throw new CustomException(message);
        }
    }

    /**
     * 断言字符串长度
     * @param str 字符串
     * @param length 字符串长度
     * @param message 异常信息
     */
    public static void length(String str,int length,String message) {
        notNull(str,message);
        if (str.length()!=length){
            throw new CustomException(message);
        }
    }

    /**
     * 断言手机号格式
     * @param str 字符串
     * @param regex 格式
     * @param message 异常信息
     */
    public static void phonePattern(String str, String regex, String message) {
        if (!Pattern.matches(regex,str)){
            throw new CustomException(message);
        }
    }


    /**
     * 断言密码与确认密码一致
     * @param str1 密码
     * @param str2 确认密码
     * @param message 异常信息
     */
    public static void isEquals(String str1, String str2, String message) {
        if (!str1.equals(str2)){
            throw new CustomException(message);
        }
    }

    /**
     * 断言为false
     * @param flag
     * @param message
     */
    public static void isFalse(boolean flag,String message) {
        if (flag){
            throw new CustomException(message);
        }
    }
}
