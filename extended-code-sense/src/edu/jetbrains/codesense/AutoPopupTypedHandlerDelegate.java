package edu.jetbrains.codesense;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.DotAutoLookupHandler;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import edu.jetbrains.options.BeanManager;
import edu.jetbrains.util.Util;

public class AutoPopupTypedHandlerDelegate extends TypedHandlerDelegate {

    public AutoPopupTypedHandlerDelegate() {
    }

    public Result checkAutoPopup(char charTyped, final Project project, final Editor editor, final PsiFile file) {
      final boolean isInWordAutoActivation = BeanManager.storedBean().isInWordAutoActivation();
      final boolean isOutOfWordAutoActivation = BeanManager.storedBean().isOutOfWordAutoActivation();
      boolean isAuto = false;
      if ( isInWordAutoActivation || isOutOfWordAutoActivation ) {
          final boolean inWord = (charTyped == '$' /*identifier start in PHP */
                        || Character.isJavaIdentifierPart(charTyped));
          if (inWord) {
              isAuto = isInWordAutoActivation;
          } else {
              isAuto = isOutOfWordAutoActivation;
          }
      }
      if ( isAuto ) {
          AutoPopupController controller = AutoPopupController.getInstance(project);
          final Runnable request = new Runnable(){
            public void run(){
              if (project.isDisposed()) { return; }
              if (editor.isDisposed()) { return; }
              PsiDocumentManager.getInstance(project).commitAllDocuments();
              new DotAutoLookupHandler().invoke(project, editor, file);
            }
          };
          int delay = getDelay();
          controller.invokeAutoPopupRunnable( request, delay ); 
          return Result.STOP;
      }
      return Result.CONTINUE;
    }

    static int getDelay() {
        String delayString = BeanManager.storedBean().getAutoActivationDelay();
        Integer delay = Util.getInt(delayString);
        return delay;
    }

}
