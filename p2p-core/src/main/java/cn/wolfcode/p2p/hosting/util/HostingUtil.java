package cn.wolfcode.p2p.hosting.util;

import cn.wolfcode.p2p.base.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 托管工具类
 */
public class HostingUtil {
    //会员网关
    public static final String URL_MGS="http://localhost:1111/mgs/gateway.do";
    //2．收单网关
    public static final String URL_MAS="http://localhost:1111/mas/gateway.do";
    //签名工具
    public static final String SIGNTYPE_RSA = "RSA";
    //私钥
    public static final String SIGNKEY_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJJqxunyHgxWN2aTSZp2jAYTOwGDeuS/Dkolk3g6/svnhVYAYn8iyjJ5Fo6YQcCz3F1+3YGjq1I1mIKRi3GaRoN3859vuCdo9nWFowF7BgepNSoGZY7uyYVc6EO3+TdeAlvdCceEng/a3hRoDcjLbsXdV9KwSfoCzdoRegy+yGf1AgMBAAECgYAHaRoaOyPkMfTp3yiJhdh+cji5UeNFcpoRjKdTHYFJ5rTr7mcN0j/zoAdRPkmNrEzJ+qLRbew84/ONIDqzjEBnUMRyWbq9rCr/yodld6uY8yQvXONGukv0mDArnsB+0HswkZ4oIMdHyw8EiCjcO+nBE63nc3ejw9XjBjwuUP/PwQJBAPtwxlwu9ORvn5LInQx/xEw6LWRn1kHuzU2V8QPuaZ1u/0MJ0Z1MqtEdWd4cUkox5etxpjAK8Ok7XUgfwcPdEjECQQCVEnep8vpDBxvokuz0bHARzGjUuZpMysBiTmbaUEwsAeCDj+rfQohISCF7GEDOargqKdlZ9XeBU5kZ/PEhf50FAkEA4r9/0o/x/rN/ByLtJeFux4NLfhl6Cblt4YLPd8kf6362qeEH7D/AZ5Z9faSyvQAkpN+3i+nB+cK0S59/4L7TcQJAeedtNew93Xw9xVYJMeRPTS7Ed1kEJlITOxD0KQlLER3D1LJnFoXY3osl3fy6WlKJIemxFVCXlomfhNIE+ijaZQJBANIxZSIC1qD9CA/RzPgxvpu0tjBN0u8Gak4lnUKtmBoBAyILoKkJ4ctElwqCpG4SAMLz6keWUYTUxXjrsR9sXiE=";
    //公钥
    public static final String SIGNKEY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSasbp8h4MVjdmk0madowGEzsBg3rkvw5KJZN4Ov7L54VWAGJ/IsoyeRaOmEHAs9xdft2Bo6tSNZiCkYtxmkaDd/Ofb7gnaPZ1haMBewYHqTUqBmWO7smFXOhDt/k3XgJb3QnHhJ4P2t4UaA3Iy27F3VfSsEn6As3aEXoMvshn9QIDAQAB";
    //字符
    public static final String CHARSET = "UTF-8";

    public static String getSignLinkString(Map<String,String> params){
        return Tools.createLinkString(params,false,true);
    }

    //获取签名
    public static String getSign(Map<String,String> params){
        String linkString = getSignLinkString(params);
        try {
            return SignUtil.sign(linkString,SIGNTYPE_RSA,SIGNKEY_PRIVATE,CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("签名失败");
        }
    }

    public static String getRequestUrl(Map<String, String> params) {
        return Tools.createLinkString(params,true,false);
    }

    //回签校验
    public static boolean checkSign(Map<String, String> params) {
        String linkString = getSignLinkString(params);
        try {
            return SignUtil.Check_sign(linkString,params.get("sign"),SIGNTYPE_RSA,SIGNKEY_PUBLIC,CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("签名失败");
        }
    }

    public static Map<String, String> getBaseParams() {
        Map<String, String> params = new HashMap<>();
        params.put("version","1.0");
        params.put("request_time",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        params.put("partner_id","200004595271");
        params.put("_input_charset","UTF-8");
        params.put("sign_type","RSA");
        params.put("sign_version","1.0");
        params.put("encrypt_version","1.0");
        return params;
    }

    public static Map<String, String> getMap(Enumeration<String> names, HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            String param = request.getParameter(name);
            map.put(name,param);
        }
        return map;
    }
}
