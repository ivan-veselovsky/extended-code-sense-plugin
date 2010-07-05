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
        // some platforms like to run w/o console, so for them the only possibility to view the
        // output is to see the idea.log:
        LOG.info(String.valueOf(x));
    }
}
