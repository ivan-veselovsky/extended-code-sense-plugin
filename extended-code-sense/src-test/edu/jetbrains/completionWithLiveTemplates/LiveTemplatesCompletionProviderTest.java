package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.mock.MockDocument;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.Consumer;
import edu.jetbrains.options.BeanManager;
import edu.jetbrains.options.MockApplication;
import edu.jetbrains.util.Debug;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class LiveTemplatesCompletionProviderTest {

    final MockApplication application = new MockApplication();
    MockCompletionResultSet resultSet;
    MockCaretModel caretModel;
    TemplateSettings settings1;
    MockEditor editor;
    Document document;
    LiveTemplatesCompletionProvider provider;
    PsiFile file;
    MockPsiElement element;
    CompletionParameters parameters;

    @Test
    public void testCreate() throws Exception {
        LiveTemplatesCompletionProvider p = new LiveTemplatesCompletionProvider(); 
    }


    private static class MockCompletionResultSet extends CompletionResultSet {

        int count = 0;

        private MockCompletionResultSet(PrefixMatcher prefixMatcher, Consumer<LookupElement> consumer, CompletionContributor contributor) {
            super(prefixMatcher, consumer, contributor);
        }

        @Override
        public void addElement(@NotNull LookupElement element) {
            count++;
        }

        @NotNull
        @Override
        public CompletionResultSet withPrefixMatcher(@NotNull PrefixMatcher matcher) {
            return null;
        }

        @NotNull
        @Override
        public CompletionResultSet withPrefixMatcher(@NotNull String prefix) {
            return null;
        }

        @NotNull
        @Override
        public CompletionResultSet caseInsensitive() {
            return null;
        }
    }

    private static class TemplateImpl2 extends TemplateImpl {
        private TemplateImpl2(@NotNull String key, String group) {
            super(key, group);
        }

        @Override
        public String getDescription() {
            return "descr";
        }
    }

    private void setUp (String text, int offset) throws InterruptedException {

        final TemplateImpl[] templateImpls = new TemplateImpl[] {
            new TemplateImpl2("itar", "user"),
            new TemplateImpl2("it", "user"),
            new TemplateImpl2("lazy", "user"),
            new TemplateImpl2("busy", "user"),
            new TemplateImpl2("xArg", "user") {
                @Override
                public boolean hasArgument() {
                    return true;
                }
            },
        };

        caretModel = new MockCaretModel();
        caretModel.offset = offset;

        this.document = new MockDocument(text);

        MockSchemesManagerFactory schemesManagerFactory = new MockSchemesManagerFactory();
        schemesManagerFactory.schemesManager = new MockSchemesManager();

        final TemplateSettings[] settings0 = new TemplateSettings[] { null };
        try {
            settings0[0] = new MockTemplateSettings( schemesManagerFactory ) {
                public void finalize() {
                    // the first time in my life i do suck a f..cky ...k:
                    settings0[0] = this;
                }

                @Override
                public TemplateImpl[] getTemplates() {
                    return templateImpls;
                }

                @Override
                public int getMaxKeyLength() {
                    return 4;
                }
            };
        } catch (AssertionError ae) {

        }

        System.gc();
        System.runFinalization();

        while (settings0[0] == null) {
            Thread.sleep(10);
        }

        settings1 = settings0[0];

        editor = new MockEditor() {
            @NotNull
            @Override
            public CaretModel getCaretModel() {
                return caretModel;
            }
        } ;
        editor.document = this.document;
        provider = new LiveTemplatesCompletionProvider() {
            @Override
            protected Editor getEditor(Project project) {
                return editor;
            }

            @Override
            protected TemplateSettings getTemplateSettings() {
                return settings1;
            }

            @Override
            protected void handleCustomTemplates(PsiFile psiFile, int offset, Editor editor,
                Map<TemplateImpl, String> template2argument, Project project, CompletionResultSet result) {
                // skip
            }

            @Override
            protected void commitDocument(PsiFile file, Document document) {
                // skip
            }

            @Override
            protected List<TemplateImpl> filterApplicableCandidates(PsiFile file, int caretOffset, List<TemplateImpl> candidates) {
                // simplify:
                return candidates;
            }
        };
        file = new MockPsiFile();
        element = new MockPsiElement();
        element.psiFile = file;
        int count = 0;
        parameters = new CompletionParameters(element, file, CompletionType.BASIC, offset, count);
        CompletionContributor contributor = new LiveTemplatesCompletionContributor();
        ApplicationManager m = new ApplicationManager() {
            {
                ourApplication = application;
            }
        };
        BeanManager.setMockApplication(application);
        PrefixMatcher matcher = new MockPrefixMatcher("myPrefix");
        Consumer consumer = Consumer.EMPTY_CONSUMER;
        resultSet = new MockCompletionResultSet(matcher, consumer, contributor);
    }

    @Test
    public void testOptions() throws InterruptedException {

        BeanManager.storedBean().setShowLiveTemplates(false);
        setUp("abcd foo", 8);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        int matching = resultSet.count;
        Assert.assertEquals(0, matching);

        BeanManager.storedBean().setShowLiveTemplates(true);
        BeanManager.storedBean().setShowLiveTemplatesOnEmptySpace(false);
        setUp("abcd it", 7);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(2, matching);

        setUp("abcd b", 6);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(1, matching);

        setUp("abcd ita", 8);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(1, matching);

        setUp("abcd itar", 9);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(1, matching);

        setUp("abcd itar ", 10);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(0, matching);

        BeanManager.storedBean().setShowLiveTemplatesOnEmptySpace(true);
        setUp("abcd itar ", 10);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(5, matching);

        BeanManager.storedBean().setShowLiveTemplatesOnEmptySpace(false);
        setUp("    xArg x", 10);
        provider.addCompletions(
                parameters,
                null,
                resultSet
        );
        matching = resultSet.count;
        Assert.assertEquals(1, matching);

    }

}
