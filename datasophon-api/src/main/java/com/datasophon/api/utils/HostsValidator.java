package com.datasophon.api.utils;

import com.datasophon.api.annotation.Hosts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Check host list roughly.
 *
 * @author rwj
 * @date 2022/11/20
 */
public class HostsValidator implements ConstraintValidator<Hosts, String> {

    private static final Pattern ALPHABET_AND_NUMBER = Pattern.compile("[a-zA_Z0-9,]+");
    private static final Pattern BASIC = Pattern.compile("[a-zA-Z0-9_.\\[\\-\\],]+");
    private static final Pattern ALPHABET = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern IP = Pattern.compile("[0-9.\\[\\]\\-]+");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //1.只有字母和数字
        if (ALPHABET_AND_NUMBER.matcher(value).matches()) {
            return true;
        }
        //2.不能有特殊字符
        if (!BASIC.matcher(value).matches()) {
            return false;
        }
        //3.字母和[-]任一字符不能同时出现
        if (ALPHABET.matcher(value).matches() && (value.contains("[") || value.contains("]") || value.contains("-"))) {
            return false;
        }
        //4. [ - ] 三个字符顺序不能颠倒
        int left = value.indexOf("[");
        int mid = value.indexOf("-");
        int right = value.indexOf("]");
        if (left > mid || left > right || mid > right) {
            return false;
        }
        //5.如果是数值类型ip地址
        String[] ipStrs = value.split(",");
        for (String ipStr : ipStrs) {
            if (IP.matcher(ipStr).matches()) { //IP
                String[] ipItems = ipStr.split("\\.");
                int splitLen = ipItems.length;
                if (splitLen != 4) {    //每个ip地址 .splitLength == 4
                    return false;
                }
                for (int i = 0; i < splitLen; i++) {
                    String curNumStr = ipItems[i];
                    if (curNumStr.contains("[") || curNumStr.contains("-") || curNumStr.contains("]")) {  //粗筛，这里直接不判断这种
                        continue;
                    }
                    if (ALPHABET.matcher(curNumStr).matches()) {
                        return false;
                    }
                    int curLen = ipItems[i].length();
                    if ((i != 0 && ipItems[i].startsWith("0")) || (curLen > 1 && ipItems[i].startsWith("0"))) {   //不是第一个不能以0开头 || 以 0 开头却大于0
                        return false;
                    }
                    int splitInt = Integer.parseInt(ipItems[i]);
                    if (curLen < 1 || curLen > 3 || splitInt > 255 || splitInt < 0) {   //1 < 位数 <= 3 , 0 < 值 < 255
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
