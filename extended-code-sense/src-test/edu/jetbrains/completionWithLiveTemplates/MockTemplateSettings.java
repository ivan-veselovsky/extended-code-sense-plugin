package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.options.SchemesManagerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 11.07.2010
 * Time: 21:11:21
 * To change this template use File | Settings | File Templates.
 */
public class MockTemplateSettings extends TemplateSettings {

    public MockTemplateSettings(SchemesManagerFactory schemesManagerFactory) {
       super(schemesManagerFactory);
    }

}
