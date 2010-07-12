package edu.jetbrains.completionWithLiveTemplates;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Assert;

import org.junit.Test;

public class AddLiveTemplatesCompletionContributorEPTest {

    @Test
    public void testCreate() throws Exception {
        Constructor constructor =  AddLiveTemplatesCompletionContributorEP.class.getConstructor(new Class[0]);
        Assert.assertTrue(Modifier.isPublic(constructor.getModifiers()));
        AddLiveTemplatesCompletionContributorEP ep = new AddLiveTemplatesCompletionContributorEP(); 
    }
}
