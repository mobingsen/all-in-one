package com.drop.leaves.agent;

/**
 * @author by mobingsen on 2021/6/20 10:14
 */
public class BitStringUtil {

    public String addString(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (char) (i % 26 + 'a');
        }
        return result;
    }

    public String buildString(int length) {
        StringBuilder inst = new StringBuilder();
        for (int i = 0; i < length; i++) {
            inst.append((char) (i % 26 + 'a'));
        }
        return inst.toString();
    }
}
