package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 26.06.2010
 * Time: 21:14:04
 * To change this template use File | Settings | File Templates.
 */
public class LiveTemplatesCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        // TODO: the live templates should be taken there instead of "foo!!!":
        LookupElement lookupElement = LookupElementBuilder.create("foo!!!");
        lookupElement.setPrefixMatcher(new PrefixMatcher("") {
            @Override
            public boolean prefixMatches(@NotNull LookupElement element) {
                return true; //super.prefixMatches(element);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public boolean prefixMatches(@NotNull String name) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull
            @Override
            public PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
                return this;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        result.addElement(lookupElement);
    }
}
