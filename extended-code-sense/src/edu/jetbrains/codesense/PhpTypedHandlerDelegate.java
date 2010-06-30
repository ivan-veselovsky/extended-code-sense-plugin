package edu.jetbrains.codesense;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 25.06.2010
 * Time: 7:41:46
 * To change this template use File | Settings | File Templates.
 */
public class PhpTypedHandlerDelegate extends TypedHandlerDelegate {

  private final Condition<Editor> memberAutoLookupCondition = new MemberAutoLookupCondition();

    // TODO: take the value from the config option.
  private static boolean isAutoExpandInExpressions = true;

  public Result checkAutoPopup(char charTyped, final Project project, final Editor editor, final PsiFile file) {
      System.out.println("checkPopupFor: ["+charTyped+"]");

      if ( (isAutoExpandInExpressions
              && mayPrecedeAnIdentifierInExpression(charTyped))
              || charTyped == '$' /*identifier start in PHP */
              || Character.isJavaIdentifierPart(charTyped) ) {
        AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, memberAutoLookupCondition);
        return Result.STOP;
      }

      return Result.CONTINUE;
  }

  private static boolean mayPrecedeAnIdentifierInExpression(char c) {
      boolean result = (c == '>') || (c == '<') || (c == '*') || (c == '/')
          || (c == '+') || (c == '-')
              || (c == ' ') || (c == '\t') || (c == '\n')
              || (c == '~') || (c == '^') || (c == '&') || (c == '|') || (c == '=') 
              || (c == '&') || (c == '!') || (c == '(') || (c == '?') || (c == ':');
      return result;
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
          //System.out.println(" project: ["+project+"]");
        if (project == null)  { return false; }
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
          //System.out.println(" file: ["+file+"]");
        if (file == null) { return false; }
        final int offset = editor.getCaretModel().getOffset();
          //System.out.println(" offset: ["+offset+"]");

        PsiElement lastElement = file.findElementAt(offset - 1);
          //System.out.println(" last elem: ["+lastElement+"]");
        if (lastElement == null) {
          return false;
        }
          // TODO: it looks like psi support is only possible for Java.
          // some classes from com.intellij.psi. are just absent in PHP Storm product. 
//         //IElementType type = lastElement.getNode().getElementType();
//         if (  (lastElement instanceof PsiIdentifier)
//               || (lastElement instanceof PsiLocalVariable)
//               || (lastElement instanceof PsiMember)
//                 ) {
//             return true;
//         } else {
//             if (isAutoExpandInExpressions) {
//                final PsiElement parent = lastElement.getParent();
//                if ( true
////                        || (lastElement instanceof PsiExpression)
////                        || (parent instanceof PsiExpression/*direct part of expr*/)
////                        || (parent instanceof PsiStatement/*for, while, etc.*/)
////                        || (parent instanceof PsiLocalVariable/*local var init*/)
////                        || (parent instanceof PsiMember) /*member init*/
////                        || (parent instanceof PsiCodeBlock)
//                        ) {
//                  return true;
//                } else {
//                    System.err.println("debug: expr: psi elem == "+lastElement+", parent = "+parent);
//                    return false;
//                }
//             }
             System.err.println("psi elem == "+lastElement);
             return true;
      }
    }

}
