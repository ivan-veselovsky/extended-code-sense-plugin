package edu.jetbrains.codesense;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import edu.jetbrains.util.Debug;

import java.util.Locale;

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

    public PhpTypedHandlerDelegate() {
    }

    public Result checkAutoPopup(char charTyped, final Project project, final Editor editor, final PsiFile file) {
      Debug.out(" ===== checkPopupFor: ["+charTyped+"]");

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
              || (c == '!') || (c == '(') || (c == '?') || (c == ':')
              || (c == '[') /* NB: for array length expression */
              ;
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
          Debug.out(" project: ["+project+"]");
        if (project == null)  { return false; }
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
          Debug.out(" file: ["+file+"]");
        if (file == null) { return false; }
        final int offset = editor.getCaretModel().getOffset();
          //Debug.out(" offset: ["+offset+"]");

        PsiElement lastElement = file.findElementAt(offset - 1);
        if (lastElement == null) {
          return false;
        }

          printAllUpwards(lastElement); // DEBUG

          // some classes from com.intellij.psi. are just absent in PHP Storm product.
          ASTNode node = lastElement.getNode();
          IElementType type = (node == null) ? null : node.getElementType();
          if (type == null) {
              return false;
          }

          PsiElement prevSibling = lastElement.getPrevSibling();
          printPsi("prev sibling", prevSibling);                 // DEBUG

          PsiElement nextSibling = lastElement.getNextSibling();
          printPsi("next sibling", nextSibling);                 // DEBUG

//          if (nextSibling != null) {
//              printPsi("next sibling parent: ", nextSibling.getParent()); // DEBUG
//          }

          final String typeString = type.toString().toLowerCase(Locale.ROOT);
//          if ("class".equals(typeString)) {
//              // do not show auto-popup in class (?)
//              return false;
//          }
          if ("identifier".equals(typeString)
                  || "dollar".equals(typeString)
                  || "variable".equals(typeString) ) {
              // these cases should lead to unconditional true:
              return true;
          }
          // Optionally invoke the auto-popup in expressions:
          if (isAutoExpandInExpressions) {
              boolean inExpression = isInExpression(lastElement);
              if (inExpression) {
                  Debug.out(" *** in expression!"); // DEBUG
                  return true;
              }
          }
          return false;
      }
    }

    private static void printAllUpwards(final PsiElement e) {
        PsiElement parent = e;
        int height = 0;
        while (parent != null) {
            printPsi(String.valueOf(height), parent);
            parent = parent.getParent();
            height++;
        }
    }

    private static void printPsi(String comment, PsiElement psiElement) {
        if (psiElement == null) {
            Debug.out(comment + ": elem: ["+psiElement+"]");
        } else {
            String className = psiElement.getClass().getName();
            ASTNode node = psiElement.getNode();
            IElementType type = (node == null) ? null : node.getElementType();
            Debug.out(comment + ": elem: ["+psiElement+"], class == "+className+", type = "+type);
        }
    }

    /**
     * This is some kind of dirty code: it is not guaranteed to work for any language,
     * however, it does work in most the cases for Php anf Java languages.
     * @param e
     * @return
     */
    private static boolean looksLikeExpression(PsiElement e) {
       if (e == null) {
           return false;
       }
       ASTNode node = e.getNode();
       IElementType type = (node == null) ? null : node.getElementType();
       if (type == null) {
           return false;
       }
       if (e instanceof PsiErrorElement) {
           PsiErrorElement psiErrorElement = (PsiErrorElement)e;
           String err = psiErrorElement.getErrorDescription();
           if (err != null
                   && err.toLowerCase(Locale.ROOT).contains("expression") /* like "Expression expected"*/
                   )
           {
               return true;
           }
       }
       String typeStr = type.toString().toLowerCase(Locale.ROOT);
       boolean looksLikeExpr = typeStr.contains("expression");
       if (looksLikeExpr) {
           return true;
       }
       if ( "multiply".equals(typeStr)
               || "asterisk".equals(typeStr) // java
               || typeStr.contains("less than") // "less than or equal"
               || typeStr.contains("greater than") // "greater than or equal"
               || "equals".equals(typeStr)
               || "eq".equals(typeStr)     // java
               || "eqeq".equals(typeStr)   // java
               || "not equals".equals(typeStr)
               || "ne".equals(typeStr)    // java
               || "le".equals(typeStr)    // java
               || "ge".equals(typeStr)    // java
               || "bit and".equals(typeStr)
               || "bit or".equals(typeStr) 
               || "tilde".equals(typeStr) // java
               || "shift left".equals(typeStr)
               || "shift right".equals(typeStr)
               || "gtgtgt".equals(typeStr) /* java: unsigned right shift */
               || "gtgt".equals(typeStr) // java
               || "ltlt".equals(typeStr) // java
               || "and".equals(typeStr)
               || "andand".equals(typeStr)
               || "or".equals(typeStr)
               || "oror".equals(typeStr)
               || "bit xor".equals(typeStr)
               || "xor".equals(typeStr)
               || "remainder".equals(typeStr)
               || "perc".equals(typeStr)  // java
               || "division".equals(typeStr)
               || "div".equals(typeStr) 
               || "assign".equals(typeStr)
               || "ternary".equals(typeStr) 
               || "quest".equals(typeStr) // java
               || "colon".equals(typeStr)
               || "array index".equals(typeStr) // Php
               ) {
           Debug.out("** type matches.");
           return true;
       }
       return false;
    }

    static boolean isInExpression(PsiElement e) {
        if (looksLikeExpression(e.getParent())
              || looksLikeExpression(e.getPrevSibling())
              || looksLikeExpression(e.getNextSibling()) ) {
            return true;
        }
        return false;
    }
}
