package edu.jetbrains.options;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PluginOptionsConfigurable implements Configurable, ApplicationComponent /*, Place.Navigator */ {

    private OptionsUIJPanel optionsUIJPanel;

    @NotNull
    public String getComponentName() {
        return this.getClass().getName();
    }

    public void initComponent() {
    }

    public void disposeComponent() {
        optionsUIJPanel = null;
    }

    @Nls
    public String getDisplayName() {
        return "Extended Code Sense";
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return null;// "extended.codesense.options";
    }

    public JComponent createComponent() {
        if (optionsUIJPanel == null) {
            optionsUIJPanel = new OptionsUIJPanel();
        }
        return optionsUIJPanel.getRootComponent();
    }

    public boolean isModified() {
        OptionsBean bean = BeanManager.storedBean();
        return optionsUIJPanel.isModified(bean);
    }

    public void apply() throws ConfigurationException {
        OptionsBean bean = BeanManager.storedBean();
        optionsUIJPanel.getData(bean);
    }

    public void reset() {
        OptionsBean bean = BeanManager.storedBean();
        optionsUIJPanel.setData(bean);
    }

    public void disposeUIResources() {
        optionsUIJPanel = null;
    }

}
