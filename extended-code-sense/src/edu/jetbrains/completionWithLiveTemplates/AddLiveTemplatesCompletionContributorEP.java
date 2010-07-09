package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionContributorEP;

public class AddLiveTemplatesCompletionContributorEP extends CompletionContributorEP {

    private final CompletionContributor instance = new LiveTemplatesCompletionContributor();  

    public AddLiveTemplatesCompletionContributorEP() {
    }

    public CompletionContributor getInstance() {
        return instance;
    }

}
