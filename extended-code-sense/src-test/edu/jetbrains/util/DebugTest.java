package edu.jetbrains.util;

import org.junit.Test;

public class DebugTest {

    @Test
    public void testDebug() {
        // Just check that no exception happens:
        Debug.out(null);
        Debug.out("");
        Debug.out("f..k");
        Debug.out(new Object());
        Debug.out(new Throwable());
        Debug.out(new char[] { 'a', 'b'} );
        Debug.out( (char[]) null );
    }
}
