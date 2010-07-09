package edu.jetbrains.options;

import org.junit.Assert;
import org.junit.Test;

public class BeanManagerTest {

    @Test
    public void getDefault() {
        OptionsBean bean = BeanManager.defaultBean();
        Assert.assertNotNull(bean);
    }

    @Test
    public void getStored() {
        OptionsBean bean = BeanManager.storedBean();
        Assert.assertNotNull(bean);
    }

    @Test
    public void getDefaultValues() {
        OptionsBean bean = BeanManager.storedBean();
        
        // check defaults:
        Assert.assertEquals( "200", bean.getAutoActivationDelay() );

        Assert.assertEquals( true, bean.isInWordAutoActivation() );
        Assert.assertEquals( false, bean.isOutOfWordAutoActivation() );

        Assert.assertEquals( true, bean.isShowLiveTemplates() );
        Assert.assertEquals( false, bean.isShowLiveTemplatesOnEmptySpace() );

    }
}
