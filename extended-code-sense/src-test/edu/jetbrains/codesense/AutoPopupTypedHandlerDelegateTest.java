package edu.jetbrains.codesense;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import org.junit.Assert;
import org.junit.Test;

public class AutoPopupTypedHandlerDelegateTest {

    @Test
    public void instantiate() {
        AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate();
        Assert.assertTrue(delegate instanceof TypedHandlerDelegate);
    }

    @Test
    public void getInt() {
        AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate();
        // the default:
        int delay = delegate.getDelay();
        Assert.assertEquals(200, delay);
    }

    @Test
    public void checkAutoPopup() {
        AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate();

        // TODO: check various cases around options and character typed.
        //delegate.checkAutoPopup('o', );

    }
}
