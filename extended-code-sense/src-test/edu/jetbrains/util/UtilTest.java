package edu.jetbrains.util;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest 
{

    @Test
    public void getInt() {
        Assert.assertEquals(null, Util.getInt(null) );
        Assert.assertEquals(null, Util.getInt("") );
        Assert.assertEquals(null, Util.getInt("abc") );
        Assert.assertEquals(null, Util.getInt("abc") );
        Assert.assertEquals(null, Util.getInt("0xfa") );
        Assert.assertEquals(null, Util.getInt("+1") );
        Assert.assertEquals(null, Util.getInt(" ") );

        Assert.assertEquals(new Integer(0), Util.getInt("0") );
        Assert.assertEquals(new Integer(-5), Util.getInt("-5") );
        Assert.assertEquals(new Integer(5), Util.getInt("5") );
        Assert.assertEquals(new Integer(Integer.MAX_VALUE), Util.getInt(String.valueOf(Integer.MAX_VALUE)) );
        Assert.assertEquals(new Integer(Integer.MIN_VALUE), Util.getInt(String.valueOf(Integer.MIN_VALUE)) );
    }
}
