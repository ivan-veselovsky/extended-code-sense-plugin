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
        OptionsBean bean = BeanManager.defaultBean();
        
        // check defaults:
        Assert.assertEquals( Integer.toString(BeanManager.DEFAULT_DELAY), bean.getAutoActivationDelay() );

        Assert.assertEquals( true, bean.isInWordAutoActivation() );
        Assert.assertEquals( false, bean.isOutOfWordAutoActivation() );
        Assert.assertEquals( BeanManager.DEFAULT_OUT_OF_WORD_TRIGGERS, bean.getOutOfWordActivationCharacters());

        Assert.assertEquals( true, bean.isShowLiveTemplates() );
        Assert.assertEquals( false, bean.isShowLiveTemplatesOnEmptySpace() );
    }

    @Test
    public void readWriteValues() {
        OptionsBean bean = BeanManager.storedBean();
        
        // check set-read operations:
        bean.setOutOfWordActivationCharacters("@/-");
        Assert.assertEquals( "@/-", bean.getOutOfWordActivationCharacters() );
        bean.setOutOfWordActivationCharacters("f..k");
        Assert.assertEquals( "f..k", bean.getOutOfWordActivationCharacters() );

        bean.setOutOfWordAutoActivation(true);
        Assert.assertEquals( true, bean.isOutOfWordAutoActivation() );
        bean.setOutOfWordAutoActivation(false);
        Assert.assertEquals( false, bean.isOutOfWordAutoActivation() );

        bean.setInWordAutoActivation(true);
        Assert.assertEquals( true, bean.isInWordAutoActivation() );
        bean.setInWordAutoActivation(false);
        Assert.assertEquals( false, bean.isInWordAutoActivation() );

        bean.setShowLiveTemplates(false);
        Assert.assertEquals( false, bean.isShowLiveTemplates() );
        bean.setShowLiveTemplates(true);
        Assert.assertEquals( true, bean.isShowLiveTemplates() );

        bean.setShowLiveTemplatesOnEmptySpace(true);
        Assert.assertEquals( true, bean.isShowLiveTemplatesOnEmptySpace() );
        bean.setShowLiveTemplatesOnEmptySpace(false);
        Assert.assertEquals( false, bean.isShowLiveTemplatesOnEmptySpace() );
    }
}
