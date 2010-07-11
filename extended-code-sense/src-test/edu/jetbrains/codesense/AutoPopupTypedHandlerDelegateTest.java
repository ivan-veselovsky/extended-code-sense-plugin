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

    private final boolean[] scheduled = new boolean[] { false };
    private AutoPopupTypedHandlerDelegate delegate = new AutoPopupTypedHandlerDelegate() {

        @Override
        protected boolean isIdentifierInEditorContext(char charTyped, Editor editor) {
            CharSequence seq = "a";  // TODO: test various texts and offsets.
            int offset = 1;
            return isIdentifierInEditorContext(charTyped, seq, offset);
        }

        @Override
        protected void schedulePopupImpl(Project project, Editor editor, PsiFile file) {
            scheduled[0] = true;
        }
//        @Override
//        protected String getPrecedingElementText(Project project, Editor editor, PsiFile file) {
//            return "foo"; // TODO
//        }
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
        Assert.assertEquals(200, delay);

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
    public void checkAutoPopup() {
        // default options:
        checkAutoActivationImpl('$', true);
        checkAutoActivationImpl('A', true);
        checkAutoActivationImpl('a', true);
        checkAutoActivationImpl('5', true);
        checkAutoActivationImpl(' ', false);

        // custom options:
        MockApplication mockApplication = new MockApplication();
        BeanManager.setMockApplication(mockApplication);
        OptionsBean bean = mockApplication.getComponent(ExtendedCodeSenseOptionsComponent.class).getState();

        bean.setInWordAutoActivation(false);
        bean.setOutOfWordAutoActivation(true);
        checkAutoActivationImpl('a', false);
        checkAutoActivationImpl(' ', true);

        bean.setInWordAutoActivation(true);
        bean.setOutOfWordAutoActivation(false);
        checkAutoActivationImpl('a', true);
        checkAutoActivationImpl(' ', false);

        bean.setInWordAutoActivation(true);
        bean.setOutOfWordAutoActivation(true);
        checkAutoActivationImpl('a', true);
        checkAutoActivationImpl(' ', true);

        bean.setInWordAutoActivation(false);
        bean.setOutOfWordAutoActivation(false);
        checkAutoActivationImpl('a', false);
        checkAutoActivationImpl(' ', false);
    }

    private void checkAutoActivationImpl(char typedChar, boolean autoPopupExpected) {
        scheduled[0] = false;
        delegate.checkAutoPopup( typedChar, null, null, null);
        Assert.assertTrue(autoPopupExpected == scheduled[0]);
    }

//    @Test
//    public void test1() {
//        Assert.assertTrue(Character.isJavaIdentifierPart('$'));
//        Assert.assertTrue(Character.isJavaIdentifierStart('È'));
//        Assert.assertTrue(Character.isJavaIdentifierStart('è'));
//    }
}
