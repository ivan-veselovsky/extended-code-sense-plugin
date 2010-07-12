package edu.jetbrains.options;

import java.io.Serializable;

public class OptionsBean implements Serializable {

    private boolean showLiveTemplates;
    private boolean showLiveTemplatesOnEmptySpace;
    
    private boolean outOfWordAutoActivation;
    private boolean inWordAutoActivation;

    private String autoActivationDelay;
    private String outOfWordActivationCharacters;

    public OptionsBean() {
    }

    public boolean isShowLiveTemplates() {
        return showLiveTemplates;
    }

    public void setShowLiveTemplates(final boolean showLiveTemplates1) {
        showLiveTemplates = showLiveTemplates1;
    }

    public boolean isShowLiveTemplatesOnEmptySpace() {
        return showLiveTemplatesOnEmptySpace;
    }

    public void setShowLiveTemplatesOnEmptySpace(final boolean showLiveTemplatesOnEmptySpace1) {
        showLiveTemplatesOnEmptySpace = showLiveTemplatesOnEmptySpace1;
    }

    public boolean isOutOfWordAutoActivation() {
        return outOfWordAutoActivation;
    }

    public void setOutOfWordAutoActivation(final boolean outOfWordAutoActivation1) {
        outOfWordAutoActivation = outOfWordAutoActivation1;
    }

    public String getAutoActivationDelay() {
        return autoActivationDelay;
    }

    public void setAutoActivationDelay(final String autoActivationDelay1) {
        autoActivationDelay = autoActivationDelay1;
    }

    public boolean isInWordAutoActivation() {
        return inWordAutoActivation;
    }

    public void setInWordAutoActivation(final boolean inWordAutoActivation1) {
        inWordAutoActivation = inWordAutoActivation1;
    }

    public String getOutOfWordActivationCharacters() {
        return outOfWordActivationCharacters;
    }

    public void setOutOfWordActivationCharacters(final String outOfWordActivationCharacters) {
        this.outOfWordActivationCharacters = outOfWordActivationCharacters;
    }
}