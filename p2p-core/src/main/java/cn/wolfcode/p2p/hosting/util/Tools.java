package cn.wolfcode.p2p.hosting.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class Tools {
    //签名版本
    public static final String SIGN_VERSION_NAME = "sign_version";

    //签名类型
    public static final String SIGN_TYPE_NAME = "sign_type";

    //签名值
    public static final String SIGN_NAME = "sign";

   
    /**
     *  创建http post发送数据的url连接
     *  请求参数拼接的字符串和 待签名的字符串都使用这个方法来创建
     *  encode : 参数值是否需要 URLEncoder.encode编码
     *  isFilter :参数值是否需要过滤掉 sign、sign_type、sign_version
     */
    public static String createLinkString(Map<String, String> params, boolean encode,boolean isFilter) {
        List<String> keys = new ArrayList<String>(params.keySet());
        //ascii排序
        Collections.sort(keys);
		//编码
        String charset = params.get("_input_charset");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if(isFilter){
                //做签名:过滤掉 sign、sign_type、sign_version
                if(key.equals("sign") || key.equals("sign_type") || key.equals("sign_version")){
                    continue;
                }
            }
            String value = params.get(key);
            if (encode) {
				//请求参数需要对参数值进行URLEncoder.encode编码
                try {
                    value = URLEncoder.encode(URLEncoder.encode(value, charset), charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
			//拼接参数:key=value&key=value 效果
            sb.append("&").append(key).append("=").append(value);
        }

        return sb.substring(1);
    }

    public static Map getParameterMap(HttpServletRequest request, boolean isFilter) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            if (isFilter) {
                if (!name.equals("sign") && !name.equals("sign_type") && !name.equals("sign_version")) {
                    Object valueObj = entry.getValue();
                    if (null == valueObj) {
                        value = "";
                    } else if (valueObj instanceof String[]) {
                        String[] values = (String[]) valueObj;
                        for (int i = 0; i < values.length; i++) {
                            value = values[i] + ",";
                        }
                        value = value.substring(0, value.length() - 1);
                    } else {
                        value = valueObj.toString();
                    }
                    returnMap.put(name, value);
                }
            } else {
                Object valueObj = entry.getValue();
                if (null == valueObj) {
                    value = "";
                } else if (valueObj instanceof String[]) {
                    String[] values = (String[]) valueObj;
                    for (int i = 0; i < values.length; i++) {
                        value = values[i] + ",";
                    }
                    value = value.substring(0, value.length() - 1);
                } else {
                    value = valueObj.toString();
                }
                returnMap.put(name, value);
            }
        }
        return returnMap;
    }

    /**
     * 计算文件的MD5摘要值
     *
     * @param file 文件路劲
     * @return 32位的MD5摘要
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String bytes2hex03 = bytes2hex03(digest.digest());
        return bytes2hex03;

    }

    public static String bytes2hex03(byte[] bytes) {
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数  
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数  
            sb.append(HEX.charAt(b & 0x0f));
        }

        return sb.toString();
    }

    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }


}
