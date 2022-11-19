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

    private static final Pattern BASIC = Pattern.compile("[a-zA-Z0-9_.\\[\\-\\],]+");
    private static final Pattern ALPHABET = Pattern.compile(".*[a-zA-Z].*");
    private static final Pattern IP = Pattern.compile("[0-9.]");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //1.不能有特殊字符
        if(!BASIC.matcher(value).matches()) {
            return false;
        }
        //2.字母和[-]任一字符不能同时出现
        if (ALPHABET.matcher(value).matches() && (value.contains("[") || value.contains("]") || value.contains("-"))) {
            return false;
        }
        //3.如果是ip地址，那每个ip地址 .splitLength == 4
        if(IP.matcher(value).matches() && value.split("\\.").length != 4) {
            return false;
        }
        //4. [ - ] 三个字符顺序不能颠倒
        int left = value.indexOf("[");
        int mid = value.indexOf("-");
        int right = value.indexOf("]");
        return left <= mid && left <= right && mid <= right;
    }

}
