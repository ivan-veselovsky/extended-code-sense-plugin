package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionContributorEP;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 26.06.2010
 * Time: 19:50:23
 * To change this template use File | Settings | File Templates.
 */
public class AddLiveTemplatesCompletionContributorEP extends CompletionContributorEP {

    public AddLiveTemplatesCompletionContributorEP() {
    }

    public CompletionContributor getInstance() {
               return new LiveTemplatesCompletionContributor();
    }

    @Override
    public String getKey() {
        return super.getKey();
    }
}
