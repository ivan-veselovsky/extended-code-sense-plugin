package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.PrefixMatcher;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 11.07.2010
 * Time: 20:34:28
 * To change this template use File | Settings | File Templates.
 */
public class MockPrefixMatcher extends PrefixMatcher {

    public MockPrefixMatcher(String prefix) {
        super(prefix);
    }

    @Override
    public boolean prefixMatches(@NotNull String name) {

        return name != null && name.startsWith(myPrefix);
    }

    @NotNull
    @Override
    public PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
        return this;  
    }
}
