package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
//import com.intellij.codeInsight.lookup.LookupElement;
//import com.intellij.codeInsight.lookup.LookupElementBuilder;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.patterns.CharPattern;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
//import com.intellij.patterns.ObjectPattern;
import com.intellij.util.ProcessingContext;
//import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiveTemplatesCompletionContributor extends CompletionContributor {

    private final LiveTemplatesCompletionProvider provider = new LiveTemplatesCompletionProvider();
    
    public LiveTemplatesCompletionContributor() {
        ElementPattern elementPattern = new ElementPattern() {
            public boolean accepts(@Nullable Object o) {
                return true;
            }

            public boolean accepts(@Nullable Object o, ProcessingContext context) {
                return true;
            }

            public ElementPatternCondition getCondition() {
                return null;
            }
        };
       extend(CompletionType.BASIC, elementPattern, provider);
    }

//    @Override
//    public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result) {
//
//        super.fillCompletionVariants(parameters, result);    //To change body of overridden methods use File | Settings | File Templates.
//        // TODO test:
//        LookupElement lookupElement = LookupElementBuilder.create("foo");
//        result.addElement(lookupElement);
//    }

//    @Override
//    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
//        super.beforeCompletion(context);    //To change body of overridden methods use File | Settings | File Templates.
//    }

//    @Override
//    public String handleEmptyLookup(@NotNull CompletionParameters parameters, Editor editor) {
//        return super.handleEmptyLookup(parameters, editor);    //To change body of overridden methods use File | Settings | File Templates.
//    }

//    @Override
//    public AutoCompletionDecision handleAutoCompletionPossibility(AutoCompletionContext context) {
//        return super.handleAutoCompletionPossibility(context);
//    }
}
