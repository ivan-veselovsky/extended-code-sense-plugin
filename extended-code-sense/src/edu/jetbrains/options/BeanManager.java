package edu.jetbrains.options;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public class BeanManager {

    @NotNull
    public static OptionsBean storedBean() {
        Application application = ApplicationManager.getApplication();
        OptionsBean bean = null;
        if (application != null) {
            bean = application.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();
        }
        if (bean == null) {
            bean = defaultBean();
        }
        return bean;
    }

    @NotNull
    public static OptionsBean defaultBean() {
        OptionsBean bean = new OptionsBean();
        // default options:
        bean.setAutoActivationDelay("200");
        bean.setInWordAutoActivation(true);
        bean.setOutOfWordAutoActivation(false);

        bean.setShowLiveTemplates(true);
        bean.setShowLiveTemplatesOnEmptySpace(false);

        return bean;
    }

}
