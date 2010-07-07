package edu.jetbrains.options;

import java.io.Serializable;

public class OptionsBean implements Serializable {

    private boolean showLiveTemplates;
    private boolean showLiveTemplatesOnEmptySpace;
    
    private boolean enableAutoActivationInExpressions;
    private String autoActivationDelay;
    private boolean enableAutoActivation;

    public OptionsBean() {
    }

    public boolean isShowLiveTemplates() {
        return showLiveTemplates;
    }

    public void setShowLiveTemplates(final boolean showLiveTemplates) {
        this.showLiveTemplates = showLiveTemplates;
    }

    public boolean isShowLiveTemplatesOnEmptySpace() {
        return showLiveTemplatesOnEmptySpace;
    }

    public void setShowLiveTemplatesOnEmptySpace(final boolean showLiveTemplatesOnEmptySpace) {
        this.showLiveTemplatesOnEmptySpace = showLiveTemplatesOnEmptySpace;
    }

    public boolean isEnableAutoActivationInExpressions() {
        return enableAutoActivationInExpressions;
    }

    public void setEnableAutoActivationInExpressions(final boolean enableAutoActivationInExpressions) {
        this.enableAutoActivationInExpressions = enableAutoActivationInExpressions;
    }

    public String getAutoActivationDelay() {
        return autoActivationDelay;
    }

    public void setAutoActivationDelay(final String autoActivationDelay) {
        this.autoActivationDelay = autoActivationDelay;
    }

    public boolean isEnableAutoActivation() {
        return enableAutoActivation;
    }

    public void setEnableAutoActivation(final boolean enableAutoActivation) {
        this.enableAutoActivation = enableAutoActivation;
    }
}