package logic.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StringUtil {
    private static final String sep = ",";

    private static final byte[] ENCRYPTION_KEYS = {12, 45, 110, 98, 77, 63, 28, 90, 100, 44};

    @SuppressWarnings("unchecked")
    public static <E> List<E> split(String str, Class<E> clazz) {
        var ret = new ArrayList();

        if (!StringUtils.isEmpty(str)) {
            var arr = str.split(sep);
            for (var i : arr) {
                if (clazz == Integer.class)
                    ret.add(Integer.valueOf(i));
                else if (clazz == Long.class)
                    ret.add(Long.valueOf(i));
                else
                    ret.add(i);
            }
        }

        return ret;
    }

    public static <E> String join(Collection<E> collection, String sep) {
        if (collection == null)
            return null;

        var builder = new StringBuilder();
        for (var i : collection) {
            if (i != null) {
                builder.append(i.toString());
                builder.append(sep);
            }
        }

        var str = builder.toString();
        if (str.endsWith(sep)) {
            str = str.substring(0, str.length() - sep.length());
        }

        return str;
    }

    public static String ltrim(String str, String del) {
        do {
            if (str.startsWith(del))
                str = str.substring(del.length());
            else
                break;
        } while (true);

        return str;
    }

    public static String rtrim(String str, String del) {
        do {
            if (str.endsWith(del))
                str = str.substring(0, str.length() - del.length());
            else
                break;
        } while (true);

        return str;
    }

    public static String trim(String str, String del) {
        str = ltrim(str, del);
        str = rtrim(str, del);

        return str;
    }

    public static byte[] encryption(byte[] bytes, byte[] keys) {
        for (int i = 0; i < bytes.length; i++) {
            if (i < keys.length) {
                bytes[i] ^= keys[i];
            } else {
                bytes[i] ^= keys[i % keys.length];
            }
        }
        return bytes;
    }

    public static byte[] decryption(byte[] bytes, byte[] keys) {
        return encryption(bytes, keys);
    }


    public static String decryptMsg(String message) {
        return new String(decryption(Base64.decode(message), ENCRYPTION_KEYS));
    }

    public static boolean equals(String a, String b) {
        return a == null && b == null || a != null && a.equals(b);
    }

    /**
     * 获取文件名(可指定忽略扩展名)
     * 模拟 php basename()
     *
     * @param filePath
     * @param ext
     * @return
     */
    public static String basename(String filePath, String ext) {
        if (filePath == null)
            return null;

        if (ext != null && ext.length() > 0) {
            filePath = rtrim(filePath, ext);
        }

        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件扩展名
     *
     * @param filePath
     * @return
     */
    public static String fileExt(String filePath) {
        if (filePath == null || filePath.lastIndexOf(".") == -1)
            return "";

        return filePath.substring(filePath.lastIndexOf("."));
    }

    public static String left(String str, int length) {
        if (str == null) return null;
        else if (length <= str.length()) return str.substring(0, length - 1);
        else return str;
    }

    public static String right(String str, int length) {
        if (str == null) return null;
        else if (length <= str.length()) return str.substring(str.length() - length);
        else return str;
    }

    /**
     * 获取url中参数
     *
     * @param url  ex:http://192.168.20.2/x6mc/index.html?type=2&userid=1000248&name=steven
     * @param name ex:userid
     * @return
     */
    public static String getUrlParam(String url, String name) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(name)) return null;

        String params = url.substring(url.indexOf("?") + 1);
        if (StringUtils.isEmpty(params)) return null;

        Map<String, String> split = new HashMap<>();
        String[] keyVals = params.split("&");
        for (String keyVal : keyVals) {
            String[] item = keyVal.split("=", 2);
            if (item.length == 2) {
                split.put(item[0].trim(), item[1].trim());
            }
        }

        return split.get(name);
    }

    /**
     * 如果参数为null，返回""，否则原样返回
     *
     * @param str
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }

    /**
     * 如果字符串中包含了回车、和换行符，则替换成空字符
     * 去掉base64编码时出现的回车和换行符
     *
     * @param arg
     */
    public static String replaceBaseStr(String arg) {
        return getString(arg).replaceAll("[\\n\\r]", "");
    }
}
