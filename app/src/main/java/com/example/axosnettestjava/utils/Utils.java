package com.example.axosnettestjava.utils;

public class Utils {
    public static String removeFirstAndLastChar(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.deleteCharAt(str.length() - 1);
        sb.deleteCharAt(0);
        return sb.toString();
    }
}