package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.CustomLiveTemplate;
import com.intellij.codeInsight.template.CustomTemplateCallback;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.ListTemplatesHandler;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.PairProcessor;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 26.06.2010
 * Time: 21:14:04
 * To change this template use File | Settings | File Templates.
 */
public class LiveTemplatesCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
        final PsiElement element = parameters.getPosition();
        final Project project = element.getProject();
        final PsiFile psiFile = element.getContainingFile();
        //VirtualFile virtualFile = psiFile.getVirtualFile();
        // TODO: see if these is a better way to get the editor; Also check if the editor is needed at all there.
        // !! Use the editor that taken from LookupElement#handleInsert(InsertionContext c)
        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            // fallback:
            return;
        }
        //TemplateManager templateManager0 = TemplateManager.getInstance(project);
        //TemplateManagerImpl templateManager = (TemplateManagerImpl) TemplateManagerImpl.getInstance(project);

        Map<Object, String> m = startTemplate(project, editor);
        if (m == null || m.size() < 1) {
            return;
        }
        Object firstKey = m.keySet().iterator().next();
        if (firstKey instanceof CustomLiveTemplate) {
            addLiveTemplateToCompletionList((CustomLiveTemplate)firstKey, m.get(firstKey), result);
        } else if (firstKey instanceof TemplateImpl) {
            Map<TemplateImpl, String> map = (Map)m;
            for (Map.Entry<TemplateImpl,String> e: map.entrySet()) {
                addLiveTemplateToCompletionList(e.getKey(), e.getValue(), result);
            }
        }
    }

    private static void addLiveTemplateToCompletionList(TemplateImpl templateImpl, String argument, CompletionResultSet result) {
        String key = templateImpl.getKey();
        //String id = templateImpl.getId();
        String description = templateImpl.getDescription();
        //String string = templateImpl.getString(); // template text
        LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(description, key);
        //lookupElementBuilder.setTailText(description, false);
        lookupElementBuilder = lookupElementBuilder.setInsertHandler(new InsertHandler() {
            public void handleInsert(InsertionContext context, LookupElement item) {
                // TODO: correctly apply the live template here.
                System.out.println("inserted lookup element: ["+item+"], context = ["+context+"]");
            }
        });
        // TODO: add icon for live templates. Maybe borrow from eclipse.
        lookupElementBuilder = lookupElementBuilder.setPresentableText(key);
        lookupElementBuilder = lookupElementBuilder.setTypeText(description);
        //lookupElementBuilder = lookupElementBuilder.setTailText(" - ");
        lookupElementBuilder.setPrefixMatcher(new PrefixMatcher("matcher") {
            @Override
            public boolean prefixMatches(@NotNull LookupElement element) {
                return true; //super.prefixMatches(element);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public boolean prefixMatches(@NotNull String name) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull
            @Override
            public PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
                return this;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        result.addElement(lookupElementBuilder);
    }

    private static void addLiveTemplateToCompletionList(CustomLiveTemplate customLiveTemplate, String key, CompletionResultSet result) {
        String title = customLiveTemplate.getTitle();
        LookupElementBuilder lookupElement = LookupElementBuilder.create(title, key);
        lookupElement.setTailText(title, false);
        lookupElement.setPrefixMatcher(new PrefixMatcher("") {
            @Override
            public boolean prefixMatches(@NotNull LookupElement element) {
                return true; //super.prefixMatches(element);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public boolean prefixMatches(@NotNull String name) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull
            @Override
            public PrefixMatcher cloneWithPrefix(@NotNull String prefix) {
                return this;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        result.addElement(lookupElement);
    }

    public Map<Object,String> startTemplate(Project project, final Editor editor) {
      PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
      if (file == null) return null;
      TemplateSettings templateSettings = TemplateSettings.getInstance();

      Map<TemplateImpl, String> template2argument = findMatchingTemplates(file, editor, templateSettings);

      for (final CustomLiveTemplate customLiveTemplate : CustomLiveTemplate.EP_NAME.getExtensions()) {
        //if (shortcutChar == customLiveTemplate.getShortcut()) {
          int caretOffset = editor.getCaretModel().getOffset();
          if (customLiveTemplate.isApplicable(file, caretOffset)) {
            final CustomTemplateCallback callback = new CustomTemplateCallback(editor, file);
            String key = customLiveTemplate.computeTemplateKey(callback);
            if (key != null) {
              int offsetBeforeKey = caretOffset - key.length();
              CharSequence text = editor.getDocument().getCharsSequence();
              if (template2argument == null || !containsTemplateStartingBefore(template2argument, offsetBeforeKey, caretOffset, text)) {
                //customLiveTemplate.expand(key, callback);
                //callback.startAllExpandedTemplates();
                return Collections.<Object, String>singletonMap(customLiveTemplate, key) ; //true;
              }
            }
          }
        //}
      }
      //return startNonCustomTemplates(template2argument, editor, processor);
        return (Map)template2argument;
    }

    private static boolean containsTemplateStartingBefore(Map<TemplateImpl, String> template2argument,
                                                          int offset,
                                                          int caretOffset,
                                                          CharSequence text) {
      for (TemplateImpl template : template2argument.keySet()) {
        String argument = template2argument.get(template);
        int templateStart = getTemplateStart(template, argument, caretOffset, text);
        if (templateStart < offset) {
          return true;
        }
      }
      return false;
    }

    private static int getArgumentOffset(int caretOffset, String argument, CharSequence text) {
      int argumentOffset = caretOffset - argument.length();
      if (argumentOffset > 0 && text.charAt(argumentOffset - 1) == ' ') {
        if (argumentOffset - 2 >= 0 && Character.isJavaIdentifierPart(text.charAt(argumentOffset - 2))) {
          argumentOffset--;
        }
      }
      return argumentOffset;
    }

    private static int getTemplateStart(TemplateImpl template, String argument, int caretOffset, CharSequence text) {
      int templateStart;
      if (argument == null) {
        templateStart = caretOffset - template.getKey().length();
      }
      else {
        int argOffset = getArgumentOffset(caretOffset, argument, text);
        templateStart = argOffset - template.getKey().length();
      }
      return templateStart;
    }

    private Map<TemplateImpl, String> findMatchingTemplates(final PsiFile file,
                                                            Editor editor,
                                                            //Character shortcutChar,
                                                            TemplateSettings templateSettings) {
      final Document document = editor.getDocument();
      CharSequence text = document.getCharsSequence();
      final int caretOffset = editor.getCaretModel().getOffset();

      List<TemplateImpl> candidatesWithoutArgument = findMatchingTemplates(text, caretOffset, templateSettings, false);

      int argumentOffset = passArgumentBack(text, caretOffset);
      String argument = null;
      if (argumentOffset >= 0) {
        argument = text.subSequence(argumentOffset, caretOffset).toString();
        if (argumentOffset > 0 && text.charAt(argumentOffset - 1) == ' ') {
          if (argumentOffset - 2 >= 0 && Character.isJavaIdentifierPart(text.charAt(argumentOffset - 2))) {
            argumentOffset--;
          }
        }
      }
      List<TemplateImpl> candidatesWithArgument = findMatchingTemplates(text, argumentOffset, templateSettings, true);

      if (candidatesWithArgument.isEmpty() && candidatesWithoutArgument.isEmpty()) {
        return null;
      }

//      CommandProcessor.getInstance().executeCommand(myProject, new Runnable() {
//        public void run() {
//          PsiDocumentManager.getInstance(myProject).commitDocument(document);
//        }
//      }, "", null);

      candidatesWithoutArgument = filterApplicableCandidates(file, caretOffset, candidatesWithoutArgument);
      candidatesWithArgument = filterApplicableCandidates(file, argumentOffset, candidatesWithArgument);
      Map<TemplateImpl, String> candidate2Argument = new com.intellij.util.containers.HashMap<TemplateImpl, String>();
      addToMap(candidate2Argument, candidatesWithoutArgument, null);
      addToMap(candidate2Argument, candidatesWithArgument, argument);
      return candidate2Argument;
    }

    public static List<TemplateImpl> filterApplicableCandidates(PsiFile file, int caretOffset, List<TemplateImpl> candidates) {
      List<TemplateImpl> result = new ArrayList<TemplateImpl>();
      for (TemplateImpl candidate : candidates) {
        if (TemplateManagerImpl.isApplicable(file, caretOffset - candidate.getKey().length(), candidate)) {
          result.add(candidate);
        }
      }
      return result;
    }

    private static <T, U> void addToMap(@NotNull Map<T, U> map, @NotNull Collection<? extends T> keys, U value) {
      for (T key : keys) {
        map.put(key, value);
      }
    }

    private static int passArgumentBack(CharSequence text, int caretOffset) {
      int i = caretOffset - 1;
      for (; i >= 0; i--) {
        char c = text.charAt(i);
        if (isDelimiter(c)) {
          break;
        }
      }
      return i + 1;
    }
    
    private static boolean isDelimiter(char c) {
      return !Character.isJavaIdentifierPart(c);
    }

    private static List<TemplateImpl> findMatchingTemplates(CharSequence text,
                                                           int caretOffset,
                                                          // Character shortcutChar,
                                                           TemplateSettings settings,
                                                           boolean hasArgument) {
      String key;
      List<TemplateImpl> candidates = Collections.emptyList();
      for (int i = settings.getMaxKeyLength(); i >= 1; i--) {
        int wordStart = caretOffset - i;
        if (wordStart < 0) {
          continue;
        }
        key = text.subSequence(wordStart, caretOffset).toString();
        if (Character.isJavaIdentifierStart(key.charAt(0))) {
          if (wordStart > 0 && Character.isJavaIdentifierPart(text.charAt(wordStart - 1))) {
            continue;
          }
        }

        candidates = collectMatchingCandidates(settings, key, hasArgument);
        if (!candidates.isEmpty()) break;
      }
      return candidates;
    }

    private static List<TemplateImpl> collectMatchingCandidates(TemplateSettings settings, String key, boolean hasArgument) {
      final Collection<TemplateImpl> templates = settings.getTemplates(key);
      List<TemplateImpl> candidates = new ArrayList<TemplateImpl>();
      for (TemplateImpl template : templates) {
        if (template.isDeactivated()) {
          continue;
        }
          // iv: we don;'t need to check up the shortcut char:
//        if (shortcutChar != null && getShortcutChar(template) != shortcutChar) {
//          continue;
//        }
        if (template.isSelectionTemplate()) {
          continue;
        }
        if (hasArgument && !template.hasArgument()) {
          continue;
        }
        candidates.add(template);
      }
      return candidates;
    }

//    public boolean startNonCustomTemplates(Map<TemplateImpl, String> template2argument,
//                                            Editor editor,
//                                            PairProcessor<String, String> processor) {
//      final int caretOffset = editor.getCaretModel().getOffset();
//      final Document document = editor.getDocument();
//      CharSequence text = document.getCharsSequence();
//
//      if (template2argument == null || template2argument.size() == 0) {
//        return false;
//      }
//      if (!FileDocumentManager.getInstance().requestWriting(editor.getDocument(), myProject)) {
//        return false;
//      }
//
//      if (template2argument.size() == 1) {
//        TemplateImpl template = template2argument.keySet().iterator().next();
//        String argument = template2argument.get(template);
//        int templateStart = getTemplateStart(template, argument, caretOffset, text);
//        startTemplateWithPrefix(editor, template, templateStart, processor, argument);
//      }
//      else {
//        ListTemplatesHandler.showTemplatesLookup(myProject, editor, template2argument);
//      }
//      return true;
//    }

}
