package edu.jetbrains.codesense;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.filters.ClassFilter;
import com.intellij.psi.filters.position.SuperParentFilter;
import com.intellij.psi.javadoc.PsiDocComment;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 25.06.2010
 * Time: 7:41:46
 * To change this template use File | Settings | File Templates.
 */
public class PhpTypedHandlerDelegate extends TypedHandlerDelegate {

  public Result checkAutoPopup(char charTyped, final Project project, final Editor editor, final PsiFile file) {
      System.out.println("checkPopupFor: ["+charTyped+"]");

      // TODO: investigation code:
      if ( (charTyped > 'a' && charTyped < 'z') || (charTyped > 'A' && charTyped < 'Z') || charTyped == '$' || charTyped == '>') {
        AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, new MemberAutoLookupCondition());
        return Result.STOP;
      }

      return Result.CONTINUE;
  }

  public Result beforeCharTyped(char c, final Project project, final Editor editor, final PsiFile file, final FileType fileType) {
    //System.out.println("before typed: ["+c+"]");
    return Result.CONTINUE;
  }

  public Result charTyped(char c, final Project project, final Editor editor, final PsiFile file) {
      //System.out.println(" typed: ["+c+"]");
    return Result.CONTINUE;
  }

    private static class MemberAutoLookupCondition implements Condition<Editor> {

      public boolean value(final Editor editor) {
        final Project project = editor.getProject();
          System.out.println(" project: ["+project+"]");
        if (project == null)  { return false; }
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
          System.out.println(" file: ["+file+"]");
        if (file == null) { return false; }
        final int offset = editor.getCaretModel().getOffset();
          System.out.println(" offset: ["+offset+"]");

        PsiElement lastElement = file.findElementAt(offset - 1);
          System.out.println(" last elem: ["+lastElement+"]");
        if (lastElement == null) {
          return false;
        }
         return true;
      }
    }

}
