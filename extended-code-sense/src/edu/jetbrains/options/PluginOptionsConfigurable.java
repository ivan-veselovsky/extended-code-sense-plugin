package edu.jetbrains.options;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.navigation.Place;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 04.07.2010
 * Time: 21:18:11
 * To change this template use File | Settings | File Templates.
 */
public class PluginOptionsConfigurable implements Configurable, ApplicationComponent /*, Place.Navigator */ {

    private OptionsUIJPanel optionsUIJPanel;

    @NotNull
    public String getComponentName() {
        return this.getClass().getName();  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;
    }

    public JComponent createComponent() {
        if (optionsUIJPanel == null) {
            optionsUIJPanel = new OptionsUIJPanel();
        }
        return optionsUIJPanel.getRootComponent();
    }

    public boolean isModified() {
        //optionsUIJPanel.isModified(); TODO
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void apply() throws ConfigurationException {
        //optionsUIJPanel.apply();     TODO
    }

    public void reset() {
        //optionsUIJPanel.rese     TODO
    }

    public void disposeUIResources() {
        optionsUIJPanel = null;
    }


}
