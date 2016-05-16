package com.gongsibao.common.util;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by luqingrun on 16/3/21.
 */

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private StringUtils(){
    }
    public static boolean isEmail(String str) throws NullArgumentException{
        String regEx = "^\\w+@\\w+\\.\\w+$";
        return isMatches(str,regEx);
    }
    public static boolean isPhone(String str) throws NullArgumentException{
        String regEx = "^(1)[0-9]{10}$";
        return isMatches(str,regEx);
    }
    public static boolean isOrganizationNumber(String str) throws NullArgumentException{
        String regEx = "^[a-zA-Z0-9]{9}([a-zA-Z0-9]{9})?$";
        return isMatches(str,regEx);
    }
    public static boolean isIdCard(String str) throws NullArgumentException{
        String regEx = "^(\\d{15}$)|(^\\d{17}([0-9]|X|x)$)";
        return isMatches(str,regEx);
    }
    public static boolean isContractNumber(String str) throws NullArgumentException{
        String regEx = "(^(\\d{3,4}-)?\\d{7,8})$|(1[3,4,5,8][0-9]{9})";
        return isMatches(str,regEx);
    }
    public static boolean isBankCardNumber(String str) throws NullArgumentException{
        String regEx = "^[0-9]*$";
        return isMatches(str,regEx);
    }
    public static boolean isValidPassword(String str) throws NullArgumentException{
        if(isEmpty(str)){
            return false;
        }
        if(str.length()<6){
            return false;
        }
        String regExL = "^[a-zA-z]+$";
        if(isMatches(str,regExL)){
            return false;
        }
        String regExD = "^d+$";
        return !isMatches(str, regExD);
    }
    private static boolean isMatches(String str,String regEx) throws NullArgumentException{
        if(isEmpty(regEx)){
            throw new NullArgumentException("Argument \"regEx\" can not be null");
        }
        if(isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String trimToEmpty(Object obj) {
        if (null == obj) {
            return "";
        }
        return trimToEmpty(obj.toString());
    }
}
