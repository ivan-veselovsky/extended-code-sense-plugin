package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.patterns.CharPattern;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
import com.intellij.patterns.ObjectPattern;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 26.06.2010
 * Time: 19:55:23
 * To change this template use File | Settings | File Templates.
 */
public class LiveTemplatesCompletionContributor extends CompletionContributor {

    LiveTemplatesCompletionProvider provider = new LiveTemplatesCompletionProvider();
    public LiveTemplatesCompletionContributor() {
         //CompletionType completionType =
        // TODO: remake for PHP:
        //CharPattern charPattern = new CharPattern() {};
        //ElementPattern elementPattern = charPattern.javaIdentifierPart();
        ElementPattern elementPattern = new ElementPattern() {
            public boolean accepts(@Nullable Object o) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean accepts(@Nullable Object o, ProcessingContext context) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public ElementPatternCondition getCondition() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
       extend(CompletionType.BASIC, elementPattern, provider);
       // extend(CompletionType.SMART, elementPattern, provider);
        // TODO: will use it also for CompletionType.SMART ?
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
