package edu.jetbrains.util;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 04.07.2010
 * Time: 2:39:40
 * To change this template use File | Settings | File Templates.
 */
public class Debug {

    private static final Logger LOG = Logger.getInstance("#"+Debug.class.getName());

    public static void out(Object x) {
        System.err.println(x);
        // No need to contaminate the log since the console output is enough:
        //LOG.info(String.valueOf(x));
    }
}
