package edu.jetbrains.codesense;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import edu.jetbrains.options.BeanManager;
import edu.jetbrains.options.ExtendedCodeSenseOptionsComponent;
import edu.jetbrains.options.MockApplication;
import edu.jetbrains.options.OptionsBean;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class AutoPopupTypedHandlerDelegateTest {

    final boolean[] scheduled = new boolean[] { false };
    CharSequence editorSequence = null;
    int typedOffset = -1;

    private AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate() {

        @Override
        protected boolean isIdentifierOrKeywordInEditorContext(char charTyped, Editor editor) {
            return isIdentifierInEditorContext(charTyped, editorSequence, typedOffset);
        }

        @Override
        protected boolean isAfterConfiguredTrigger(char charTyped, Editor editor) {
            return isAfterConfiguredTrigger(charTyped, editorSequence, typedOffset);
        }

        @Override
        protected void schedulePopupImpl(Project project, Editor editor, PsiFile file) {
            scheduled[0] = true;
        }
    };

    @Test
    public void instantiate() throws Exception {
        // must have a public non-arg constructor:
        Constructor constructor = AutoPopupTypedHandlerDelegate.class.getConstructor(new Class[0]);
        Assert.assertTrue(Modifier.isPublic(constructor.getModifiers()));
        // instantiation is ok:
        AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate();
        Assert.assertTrue(delegate instanceof TypedHandlerDelegate);
    }

    @Test
    public void getInt() {
        AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate();
        // the default:
        int delay = delegate.getDelay();
        Assert.assertEquals(BeanManager.DEFAULT_DELAY, delay);

        // custom:
        MockApplication mockApplication = new MockApplication();
        BeanManager.setMockApplication(mockApplication);
        OptionsBean bean = mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();
        bean.setAutoActivationDelay("984");

        delay = delegate.getDelay();
        Assert.assertEquals(984, delay);
    }

    /**
     * check cases around the options and character typed.
     */
    @Test
    public void checkAutoPopupWithOptions() {
        MockApplication mockApplication = new MockApplication();
        BeanManager.setMockApplication(mockApplication);
        OptionsBean defaultBean = BeanManager.defaultBean();
        mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).loadState(defaultBean);
        OptionsBean bean = mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();

        // default options:
        checkAutoActivationImpl("", 0, '$', true);
        checkAutoActivationImpl("", 0, 'A', true);
        checkAutoActivationImpl("", 0, 'a', true);
        checkAutoActivationImpl("", 0, '5', false);
        checkAutoActivationImpl("a", 1, '5', true);
        checkAutoActivationImpl("", 0, ' ', false);
        checkAutoActivationImpl("a", 1, ' ', false);

        // custom options:
        bean.setInWordAutoActivation(false);
        bean.setOutOfWordAutoActivation(true);
        checkAutoActivationImpl("", 0, 'a', false);
        checkAutoActivationImpl("1", 1, '-', true);

        bean.setInWordAutoActivation(true);
        bean.setOutOfWordAutoActivation(false);
        checkAutoActivationImpl("a", 1, 'b', true);
        checkAutoActivationImpl("1", 1, '*', false);

        bean.setInWordAutoActivation(true);
        bean.setOutOfWordAutoActivation(true);
        checkAutoActivationImpl("a", 1, 'b', true);
        checkAutoActivationImpl("1", 1, '*', true);

        bean.setInWordAutoActivation(false);
        bean.setOutOfWordAutoActivation(false);
        checkAutoActivationImpl("a", 1, 'b', false);
        checkAutoActivationImpl("1", 1, '*', false);
    }

    private void checkAutoActivationImpl(CharSequence seq, int offset, char typedChar, boolean autoPopupExpected) {
        scheduled[0] = false;
        editorSequence = seq;
        typedOffset = offset;
        delegate.checkAutoPopup( typedChar, null, null, null);
        Assert.assertTrue(autoPopupExpected == scheduled[0]);
    }

    @Test
    public void checkInWordAutoPopup() {
        MockApplication mockApplication = new MockApplication();
        BeanManager.setMockApplication(mockApplication);
        OptionsBean defaultBean = BeanManager.defaultBean();
        mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).loadState(defaultBean);
        OptionsBean bean = mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();

        // positive:
        checkAutoActivationImpl("", 0, 'a', true);
        checkAutoActivationImpl("ab", 2, 'c', true);
        checkAutoActivationImpl("a ", 2, 'b', true);
        checkAutoActivationImpl("", 0, '\u044b'/* russian "Y" letter*/, true);
        checkAutoActivationImpl("", 0, '$', true);
        checkAutoActivationImpl("c", 1, '$', true);
        checkAutoActivationImpl(" publ", 4, 'i', true);
        checkAutoActivationImpl(" (xxx", 4, 'y', true);
        checkAutoActivationImpl(" { xxx", 5, 'y', true);
        checkAutoActivationImpl(" { xxx", 2, 'a', true);
        checkAutoActivationImpl(" {xxx", 1, 'a', true);

        // negative:
        checkAutoActivationImpl(" 5ubl", 4, 'i', false);
        checkAutoActivationImpl("", 1, '-', false);
        checkAutoActivationImpl("", 1, '%', false);
    }


    /**
     * check cases around the options and character typed.
     */
    @Test
    public void checkOutOfWordAutoPopup() {
        MockApplication mockApplication = new MockApplication();
        BeanManager.setMockApplication(mockApplication);
        OptionsBean defaultBean = BeanManager.defaultBean();
        mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).loadState(defaultBean);
        OptionsBean bean = mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();

        // default options, nagative:
        checkAutoActivationImpl("(a ", 2, '>', false);
        checkAutoActivationImpl("(A>", 2, ' ', false);
        checkAutoActivationImpl("", 0, '(', false);
        checkAutoActivationImpl("", 0, '{', false);
        checkAutoActivationImpl("(", 1, ' ', false);
        checkAutoActivationImpl("{", 1, ' ', false);

        // custom options, positive:
        bean.setOutOfWordAutoActivation(true);
        bean.setInWordAutoActivation(false);

        checkAutoActivationImpl("(a", 2, '>', true);
        checkAutoActivationImpl("a", 1, '>', true);
        checkAutoActivationImpl("", 0, '>', true);
        checkAutoActivationImpl("a =", 3, ' ', true);
        checkAutoActivationImpl("(A ", 3, '%', true);
        checkAutoActivationImpl("(A *", 4, ' ', true);

        checkAutoActivationImpl("a b", 3, 'c', false);
        checkAutoActivationImpl("a ", 2, 'b', false);
        checkAutoActivationImpl("a", 1, 'b', false);

        bean.setOutOfWordActivationCharacters("@");

        checkAutoActivationImpl("(A ", 3, '%', false);

        checkAutoActivationImpl("(A ", 3, '@', true);
        checkAutoActivationImpl("(A @", 4, ' ', true);

    }

}
