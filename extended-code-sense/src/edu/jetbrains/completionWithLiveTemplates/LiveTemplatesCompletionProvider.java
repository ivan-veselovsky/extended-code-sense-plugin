package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.CustomLiveTemplate;
import com.intellij.codeInsight.template.CustomTemplateCallback;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import edu.jetbrains.options.BeanManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LiveTemplatesCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
        final boolean isShowLiveTemplates = BeanManager.storedBean().isShowLiveTemplates();
        if (!isShowLiveTemplates) {
            return;
        }

        final PsiElement element = parameters.getPosition();
        final Project project = element.getProject();
        final PsiFile psiFile = element.getContainingFile();
        if (psiFile == null) {
            return; // XXX: ever happens?
        }
        //VirtualFile virtualFile = psiFile.getVirtualFile();
        
        // TODO: see if these is a better way to get the editor; Also check if the editor is needed at all there.
        // ! Normally we should use the editor that taken from LookupElement#handleInsert(InsertionContext c)
        // Actually there are 2 obstacles:
        // 1) CustomTemplateCallback
        // 2) editor.getDocument().getCharsSequence()
        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            // fallback:
            return;
        }
        final int offset = parameters.getOffset();
        final TemplateSettings templateSettings = TemplateSettings.getInstance();
        final Map<TemplateImpl, String> template2argument = findMatchingTemplates(psiFile, editor, templateSettings);
        // TODO: revise this: why return?
        for (final CustomLiveTemplate customLiveTemplate : CustomLiveTemplate.EP_NAME.getExtensions()) {
            //int caretOffset = editor.getCaretModel().getOffset();
            if (customLiveTemplate.isApplicable(psiFile, offset)) {
              final CustomTemplateCallback callback = new CustomTemplateCallback(editor, psiFile);
              final String key = customLiveTemplate.computeTemplateKey(callback);
              if (key != null) {
                final int offsetBeforeKey = offset - key.length();
                CharSequence text = editor.getDocument().getCharsSequence();
                if (template2argument == null || !containsTemplateStartingBefore(template2argument, offsetBeforeKey, offset, text)) {
                    final String description = customLiveTemplate.getTitle();
                    LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(description, key);
                    lookupElementBuilder = lookupElementBuilder.setInsertHandler(new InsertHandler<LookupElement>() {
                        public void handleInsert(InsertionContext context, LookupElement item) {
                            Editor editor2 = context.getEditor();
                            if (!FileDocumentManager.getInstance().requestWriting(editor2.getDocument(), project)) {
                              return;
                            }
                            customLiveTemplate.expand(key, callback);
                            callback.startAllExpandedTemplates();
                        }
                    });
                    // TODO: add an icon for Custom Live templates. 
                    //lookupElementBuilder = lookupElementBuilder.setIcon(....);
                    lookupElementBuilder = lookupElementBuilder.setPresentableText(key);
                    lookupElementBuilder = lookupElementBuilder.setTypeText(description);
                    result.addElement(lookupElementBuilder);
                    return;
                }
              }
            }
        }

        if (template2argument != null) {
            for (Map.Entry<TemplateImpl,String> e: template2argument.entrySet()) {
                addLiveTemplateToCompletionList(project, e.getKey(), e.getValue(), result);
            }
        }
    }

    private static void addLiveTemplateToCompletionList(final Project project,
                                                        final TemplateImpl templateImpl,
                                                        final String argument,
                                                        CompletionResultSet result) {
        String key = templateImpl.getKey();
        String description = templateImpl.getDescription();
        LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(description, key);
        lookupElementBuilder = lookupElementBuilder.setInsertHandler(new InsertHandler<LookupElement>() {
            public void handleInsert(InsertionContext context, LookupElement item) {
                //System.out.println("inserted lookup element: ["+item+"], context = ["+context+"]");
                final Editor editor2 = context.getEditor();
                final int caretOffset = editor2.getCaretModel().getOffset();
                final Document document = editor2.getDocument();
                final CharSequence text = document.getCharsSequence();
                if (!FileDocumentManager.getInstance().requestWriting(editor2.getDocument(), project)) {
                  return;
                }
                TemplateManagerImpl templateManager = (TemplateManagerImpl)TemplateManagerImpl.getInstance(project);
                int templateStart = getTemplateStart(templateImpl, argument, caretOffset, text);
                templateManager.startTemplateWithPrefix(editor2, templateImpl, templateStart, null, argument);
            }
        });
        // TODO: add icon for live templates. 
        //lookupElementBuilder = lookupElementBuilder.setIcon(....);
        lookupElementBuilder = lookupElementBuilder.setPresentableText(key);
        lookupElementBuilder = lookupElementBuilder.setTypeText(description);
        result.addElement(lookupElementBuilder);
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

    private static Map<TemplateImpl, String> findMatchingTemplates(final PsiFile file,
                                                            Editor editor,
                                                            TemplateSettings templateSettings) {
      final Document document = editor.getDocument();
      final CharSequence text = document.getCharsSequence();
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

      candidatesWithoutArgument = TemplateManagerImpl.filterApplicableCandidates(file, caretOffset, candidatesWithoutArgument);
      candidatesWithArgument = TemplateManagerImpl.filterApplicableCandidates(file, argumentOffset, candidatesWithArgument);
      Map<TemplateImpl, String> candidate2Argument = new com.intellij.util.containers.HashMap<TemplateImpl, String>();
      addToMap(candidate2Argument, candidatesWithoutArgument, null);
      addToMap(candidate2Argument, candidatesWithArgument, argument);
      return candidate2Argument;
    }

//    public static List<TemplateImpl> filterApplicableCandidates(PsiFile file, int caretOffset, List<TemplateImpl> candidates) {
//      List<TemplateImpl> result = new ArrayList<TemplateImpl>();
//      for (TemplateImpl candidate : candidates) {
//        if (TemplateManagerImpl.isApplicable(file, caretOffset - candidate.getKey().length(), candidate)) {
//          result.add(candidate);
//        }
//      }
//      return result;
//    }

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
      final boolean isShowingOnEmptySpace = BeanManager.storedBean().isShowLiveTemplatesOnEmptySpace();
      final int minKeyLength = isShowingOnEmptySpace ? 0 : 1;
      for (int i = settings.getMaxKeyLength(); i >= minKeyLength; i--) {
        int wordStart = caretOffset - i;
        if (wordStart < 0) {
          continue;
        }
        key = text.subSequence(wordStart, caretOffset).toString();
        if (key.length() > 0 && Character.isJavaIdentifierStart(key.charAt(0))) {
          if (wordStart > 0 && Character.isJavaIdentifierPart(text.charAt(wordStart - 1))) {
            continue;
          }
        }
        candidates = collectMatchingCandidates(settings, key, hasArgument);
        if (!candidates.isEmpty()) {
            break;
        }
      }
      return candidates;
    }

    private static Collection<TemplateImpl> getSemiMatchingTemplates(TemplateSettings settings, String startingWith) {
        //final Collection<TemplateImpl> templates = settings.getTemplates(key);
        final Collection<TemplateImpl> coll = new ArrayList<TemplateImpl>();
        TemplateImpl[] templates = settings.getTemplates();
        for (TemplateImpl impl: templates) {
            if (impl.getKey().startsWith(startingWith)) {
                 coll.add(impl);
            }
        }
        return coll;       
    }

    private static List<TemplateImpl> collectMatchingCandidates(TemplateSettings settings, String key, boolean hasArgument) {
        //final Collection<TemplateImpl> templates = settings.getTemplates(key);
        final Collection<TemplateImpl> templates = getSemiMatchingTemplates(settings, key);
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

}
