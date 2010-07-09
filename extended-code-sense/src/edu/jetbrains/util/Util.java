package edu.jetbrains.util;

public class Util {

    private Util() {
    }

    public static Integer getInt(String s) {
        Integer i = null;
        try {
            i = Integer.parseInt(s);
        } catch (RuntimeException e) {
        }
        return i;
    }

}
