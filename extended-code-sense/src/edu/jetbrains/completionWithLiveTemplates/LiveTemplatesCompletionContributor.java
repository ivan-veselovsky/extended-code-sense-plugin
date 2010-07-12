package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.Nullable;

public class LiveTemplatesCompletionContributor extends CompletionContributor {

    private final LiveTemplatesCompletionProvider provider = new LiveTemplatesCompletionProvider();
    
    LiveTemplatesCompletionContributor() {
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
}
