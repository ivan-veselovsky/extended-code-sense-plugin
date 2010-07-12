package edu.jetbrains.options;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public class BeanManager {

    public static final int DEFAULT_DELAY = 700; // ms

    // defaults are the operands for C-like languages: 
    public static final String DEFAULT_OUT_OF_WORD_TRIGGERS = "><*/+-~^&|%=!?:[";

    private static Application mockApplication;

    /* 4junit */
    public static void setMockApplication(Application m) {
        mockApplication = m;
    }

    @NotNull
    public static OptionsBean storedBean() {
        /* 4junit: */
        Application application = mockApplication;
        if (application == null) {
            application = ApplicationManager.getApplication();
        }

        OptionsBean bean = null;
        if (application != null) {
            bean = application.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();
            String chars = bean.getOutOfWordActivationCharacters();
            /* usability: restore default for those who uses previous version config: */
            if (chars == null) {
                bean.setOutOfWordActivationCharacters(DEFAULT_OUT_OF_WORD_TRIGGERS);
            }
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
        bean.setAutoActivationDelay(Integer.toString(DEFAULT_DELAY));
        bean.setInWordAutoActivation(true);
        bean.setOutOfWordAutoActivation(false);
        bean.setOutOfWordActivationCharacters(DEFAULT_OUT_OF_WORD_TRIGGERS);

        bean.setShowLiveTemplates(true);
        bean.setShowLiveTemplatesOnEmptySpace(false);

        return bean;
    }

}
