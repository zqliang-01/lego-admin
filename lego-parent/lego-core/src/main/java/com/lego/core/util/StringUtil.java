package com.lego.core.util;

import cn.hutool.core.io.IoUtil;
import com.lego.core.common.Constants;
import com.lego.core.exception.CoreException;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final int STRING_BUILDER_SIZE = 256;
    private static final int DEFAULT_SCALE = 2;
    private static final String NUMBER_PATTERN = "##0.00";
    private static final String NUMBER_PATTERN_CENT = "##0";

    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    public static boolean isBlank(Collection<String> strings) {
        return strings == null || strings.size() == 0;
    }

    public static boolean isNotBlank(Collection<String> strings) {
        return strings != null;
    }

    public static boolean isBlank(String[] str) {
        return str == null || str.length == 0;
    }

    public static boolean isNotBlank(String[] str) {
        return !isBlank(str);
    }

    public static String toString(Object obj) {
        if (obj instanceof InputStream) {
            return IoUtil.readUtf8((InputStream) obj);
        }
        if (obj instanceof byte[]) {
            return new String((byte[]) obj, Constants.DEFAULT_CHARSET);
        }
        if (obj instanceof BigDecimal) {
            BigDecimal bg = (BigDecimal) obj;
            return new DecimalFormat(NUMBER_PATTERN).format(bg.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP));
        }
        return obj == null ? "" : String.valueOf(obj);
    }

    public static String encodeUrl(String value) {
        try {
            if (isBlank(value)) {
                return value;
            }
            return URLEncoder.encode(value, Constants.DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new CoreException("Url编码异常！", e);
        }
    }

    public static String encodeUrl(String value, String charset) {
        try {
            if (isBlank(value)) {
                return value;
            }
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new CoreException("Url编码异常！", e);
        }
    }

    public static String decodeUrl(String value) {
        try {
            if (isBlank(value)) {
                return value;
            }
            return URLDecoder.decode(value, Constants.DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new CoreException("Url解码异常！", e);
        }
    }

    public static String decodeUrl(String value, String charset) {
        try {
            if (isBlank(value)) {
                return value;
            }
            return URLDecoder.decode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new CoreException("Url解码异常！", e);
        }
    }

    public static String encodeBase64(byte[] value) {
        return new String(Base64.encodeBase64(value));
    }

    public static String encodeBase64(String value) {
        return new String(Base64.encodeBase64(value.getBytes()));
    }

    public static String decodeBase64(String value) {
        return new String(Base64.decodeBase64(value.getBytes()), Constants.DEFAULT_CHARSET);
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getMD5(String value) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new CoreException("MD5编码出错", e);
        }
        byte[] b = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * Clob转为String
     */
    public static String clobToString(Clob clob) {
        String reString = "";
        Reader is = null;
        try {
            is = clob.getCharacterStream();
        } catch (SQLException e) {
            throw new CoreException("Clob转String异常！", e);
        }
        BufferedReader br = new BufferedReader(is);
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
            throw new CoreException("Clob转为String出错", e);
        }
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            sb.append(s);
            try {
                s = br.readLine();
            } catch (IOException e) {
                throw new CoreException("Clob转为String出错", e);
            }
        }
        reString = sb.toString();
        return reString;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String replaceSubString(String param, int start, int end, String replaceStr) {
        param = param.trim();
        if (isBlank(param)) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        char[] arr = param.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (start <= i && i < end) {
                str.append(replaceStr);
            } else {
                str.append(arr[i]);
            }
        }
        return str.toString();
    }

    public static String trim(String str) {
        if (isBlank(str)) {
            return "";
        }
        return str.trim();
    }

    public static String trimStart(String str, String replaceStr) {
        if (str != null && str.startsWith(replaceStr)) {
            return str.substring(str.indexOf(replaceStr) + replaceStr.length());
        }
        return str;
    }

    public static String trimEnd(String str, String replaceStr) {
        if (str != null && str.endsWith(replaceStr)) {
            return str.substring(0, str.lastIndexOf(replaceStr));
        }
        return str;
    }

    public static boolean equals(String originalStr, String newStr) {
        if (isBlank(originalStr)) {
            return isBlank(newStr);
        }
        return originalStr.equals(newStr);
    }

    public static String getFirstPartOfPath(String url) {
        String[] urls = url.split("/", 3);
        if (urls.length == 3) {
            return "/" + urls[2];
        }
        return url;
    }

    public static String getMaxLength(String str, int maxLength) {
        if (str == null) {
            return str;
        }
        if (str.length() > maxLength) {
            return str.substring(0, maxLength);
        }
        return str;
    }

    public static String format(String pattern, Object... arguments) {
        MessageFormat temp = new MessageFormat(pattern);
        return temp.format(arguments);
    }

    public static String format(double d) {
        BigDecimal bd = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return bd.toString();
    }

    public static String formatCent(BigDecimal amount) {
        BigDecimal centAmount = amount.multiply(new BigDecimal(100));
        return new DecimalFormat(NUMBER_PATTERN_CENT).format(centAmount);
    }

    public static String trimToNull(Object str) {
        String ts = trim(str.toString());
        return isBlank(ts) ? null : ts;
    }

    /**
     * 空值返回-1
     */
    public static int toInt(String str) {
        if (isBlank(str)) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    public static String toNumberString(String prefix, long number, int length) {
        String str = toString(number);
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < length) {
            sb = new StringBuffer();
            sb.append("0").append(str);// 左补0
            str = sb.toString();
            strLen = str.length();
        }
        return prefix + str;
    }

    public static byte[] getBytes(String string, Charset charset) {
        return string == null ? new byte[0] : string.getBytes(charset);
    }

    public static String substringAfter(String str, String separator) {
        if (isBlank(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBefore(String str, String separator) {
        if (isBlank(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(0, pos);
    }

    public static String substringBetween(String str, String open, String close) {
        if (isBlank(str) || isBlank(open) || isBlank(close)) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != -1) {
            final int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    public static String stripStart(final String str, final String stripChars) {
        int strLen = str == null ? 0 : str.length();
        if (strLen == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                start++;
            }
        }
        return str.substring(start);
    }

    public static String join(Collection<String> values, String separator) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        Iterator<String> iterator = values.iterator();
        String first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, "");
        }
        StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String toCamelCase(String name, boolean isFirstUpper) {
        if (name == null) {
            return null;
        }
        name = name.toLowerCase();
        StringBuilder sb = new StringBuilder(name.length());
        boolean upperCase = isFirstUpper;
        for (int i = 0; i < name.length(); i++) {
            char k = name.charAt(i);
            if ('_' == k) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(k));
                upperCase = false;
            } else {
                sb.append(k);
            }
        }
        return sb.toString();
    }

    public static String toFirstUpper(String name) {
        if (name == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char k = name.charAt(i);
            if (i == 0) {
                sb.append(Character.toUpperCase(k));
                continue;
            }
            sb.append(k);
        }
        return sb.toString();
    }

    public static int indexOf(String seq, String searchChar) {
        if (isBlank(seq)) {
            return -1;
        }
        return seq.indexOf(searchChar);
    }

    public static String substring(String content, int maxLength) {
        if (isBlank(content)) {
            return "";
        }
        if (content.length() < maxLength) {
            return content;
        }
        return content.substring(0, maxLength);
    }

    /**
     * 匹配并替换字符串中的${param}
     */
    public static String replaceBrace(String content, Map<String, String> params) {
        String pattern = "(\\$\\{(.+?)\\})";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(2);
            if (StringUtil.isNotBlank(key)) {
                String value = params.get(key);
                m.appendReplacement(sb, value == null ? "" : value);
            }
        }
        return m.appendTail(sb).toString();
    }
}
