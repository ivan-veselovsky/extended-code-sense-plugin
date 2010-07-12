package edu.jetbrains.util;

import com.intellij.openapi.diagnostic.Logger;

public class Debug {

    private static final Logger LOG = Logger.getInstance("#"+Debug.class.getName());

    private static final boolean logEnabled = false;

    public static void out(Object x) {
        if (logEnabled) {
            System.err.println(x);
            // some platforms like to run w/o console, so for them the only possibility to view the
            // output is to see the idea.log:
            LOG.info(String.valueOf(x));
        }
    }
}
