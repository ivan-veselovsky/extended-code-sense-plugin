package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.CustomLiveTemplate;
import com.intellij.codeInsight.template.CustomTemplateCallback;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import edu.jetbrains.codesense.AutoPopupTypedHandlerDelegate;
import edu.jetbrains.options.BeanManager;
import edu.jetbrains.util.Debug;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public class LiveTemplatesCompletionProvider extends CompletionProvider<CompletionParameters> {


    private static final String IS_APPLICABLE_NAME = "isApplicable";
    private static final Class[] IS_APPLICABLE_SIG_1 = new Class[] { PsiFile.class, int.class };
    private static final Class[] IS_APPLICABLE_SIG_2 = new Class[] { PsiFile.class, int.class, boolean.class };

    private static Method m = null;
    private static Class[] sig = null;

    static {
        try {
            sig = IS_APPLICABLE_SIG_1;
            m = CustomLiveTemplate.class.getMethod(IS_APPLICABLE_NAME, sig );
            Debug.out("sig1 ok.");
        } catch (Exception e) {
        }
        if (m == null) {
            try {
                sig = IS_APPLICABLE_SIG_2;
                m = CustomLiveTemplate.class.getMethod(IS_APPLICABLE_NAME, sig );
                Debug.out("sig2 ok.");
            } catch (Exception e) {
            }
        }
        if (m == null) {
            // failed to get the method:
            Debug.out("warn: method not found!");
        }
    }

    LiveTemplatesCompletionProvider() {
        super();
    }

    /* junit */
    protected Editor getEditor(Project project) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        return editor;
    }

    /* juint*/
    protected TemplateSettings getTemplateSettings() {
        return TemplateSettings.getInstance();
    }

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
            return;
        }

        // TODO: check to see if these is a better way to get the editor; Also check if the editor is needed at all there.
        // ! Normally we should use the editor that taken from LookupElement#handleInsert(InsertionContext c)
        // Actually there are 2 obstacles:
        // 1) CustomTemplateCallback
        // 2) editor.getDocument().getCharsSequence()
        final Editor editor = getEditor(project);
        if (editor == null) {
            return;
        }
        final int offset = parameters.getOffset();
        final TemplateSettings templateSettings = getTemplateSettings();
        final Map<TemplateImpl, String> template2argument = findMatchingTemplates(psiFile, editor, templateSettings);

        handleCustomTemplates(psiFile, offset, editor, template2argument, project, result);

        if (template2argument != null) {
            for (Map.Entry<TemplateImpl,String> e: template2argument.entrySet()) {
                addLiveTemplateToCompletionList(project, e.getKey(), e.getValue(), result);
            }
        }
    }

    /*
    The following commit changed the signature of the method com.intellij.codeInsight.template.CustomLiveTemplate.isApplicable():

    commit 4a792404273affca368c32a35c1ede1f62170ddd
    Author: Eugene Kudelevsky <Eugene.Kudelevsky@jetbrains.com>
    Date:   Thu Apr 22 20:49:09 2010 +0400
    refactoring, support css zen-coding selectors
     */
    private static boolean isApplicable(CustomLiveTemplate customLiveTemplate, PsiFile psiFile, int offset) {
        if (m == null) {
            return false;
        }
        try {
            Boolean result = null;
            if (sig == IS_APPLICABLE_SIG_1) {
                result = (Boolean)m.invoke(customLiveTemplate, new Object[] { psiFile, new Integer(offset) } );
            } else {
                result = (Boolean)m.invoke(customLiveTemplate, new Object[] { psiFile, new Integer(offset), Boolean.FALSE } );
            }
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    /* NB: this operation is suitable for so called Zen-Coding,
     * a special kind of live template completion in xml documents, like
     * "abc|" -> "<abc></abc>".
     */
    protected void handleCustomTemplates(PsiFile psiFile, int offset, Editor editor,
                            Map<TemplateImpl, String> template2argument, final Project project,
                            CompletionResultSet result) {
        for (final CustomLiveTemplate customLiveTemplate : CustomLiveTemplate.EP_NAME.getExtensions()) {
            if ( //customLiveTemplate.isApplicable(psiFile, offset)
                    isApplicable(customLiveTemplate, psiFile, offset)
                    ) {
              final CustomTemplateCallback callback = new CustomTemplateCallback(editor, psiFile);
              final String key = customLiveTemplate.computeTemplateKey(callback);
              if (key != null) {
                final int offsetBeforeKey = offset - key.length();
                CharSequence text = editor.getDocument().getCharsSequence();
                if (template2argument == null
                        || !containsTemplateStartingBefore(template2argument, offsetBeforeKey, offset, text)) {
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
                }
              }
            }
        }
    }

    private static void addLiveTemplateToCompletionList(final Project project,
                                                        final Template templateImpl,
                                                        final String argument,
                                                        CompletionResultSet result) {
        String key = templateImpl.getKey();
        String description = templateImpl.getDescription();
        String lookupString = (argument == null) ? key : argument;
        LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(description, lookupString);
        lookupElementBuilder = lookupElementBuilder.setInsertHandler(new InsertHandler<LookupElement>() {
            public void handleInsert(InsertionContext context, LookupElement item) {
                final Editor editor2 = context.getEditor();
                final int caretOffset = editor2.getCaretModel().getOffset();
                final Document document = editor2.getDocument();
                final CharSequence text = document.getCharsSequence();
                
                //Debug.out("Caret: ");
                //printContext(text, caretOffset, 10);

                if (!FileDocumentManager.getInstance().requestWriting(editor2.getDocument(), project)) {
                  return;
                }
                TemplateManagerImpl templateManager = (TemplateManagerImpl)TemplateManagerImpl.getInstance(project);
                int templateStart = getTemplateStart(templateImpl, argument, caretOffset, text);

                //Debug.out("template start: ");
                //printContext(text, templateStart, 10);

                templateManager.startTemplateWithPrefix(editor2, (TemplateImpl)templateImpl, templateStart, null, argument);
            }
        });
        // TODO: add icon for live templates. 
        //lookupElementBuilder = lookupElementBuilder.setIcon(....);
        String presentableText = key;
        if (argument != null) {
            // add the argument like a function parameter:
            presentableText = presentableText + "(" + argument + ")";
        }
        lookupElementBuilder = lookupElementBuilder.setPresentableText(presentableText);
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
        if (argument.length() == 0) {
            return caretOffset;
        }
        int argumentOffset = caretOffset - argument.length();
        if (argumentOffset > 0 && text.charAt(argumentOffset - 1) == ' ') {
           if (argumentOffset - 2 >= 0 && Character.isJavaIdentifierPart(text.charAt(argumentOffset - 2))) {
              argumentOffset--;
           }
        }
        return argumentOffset;
    }

    static int getTemplateStart(Template template, String argument, final int caretOffset, CharSequence text) {
      final int templateEnd;
      if (argument == null) {
        templateEnd = caretOffset;
      } else {
        int argOffset = getArgumentOffset(caretOffset, argument, text);
        templateEnd = argOffset;
      }
      int expectedTemplateStart = templateEnd - template.getKey().length();
      if (expectedTemplateStart >= 0) {
         CharSequence expectedKey = text.subSequence(expectedTemplateStart, templateEnd);
         if (template.getKey().equals(expectedKey.toString())) {
             return expectedTemplateStart;
         }
      }
      return caretOffset;
    }

    private Map<TemplateImpl, String> findMatchingTemplates(final PsiFile file,
                                                            Editor editor,
                                                            TemplateSettings templateSettings) {
      final Document document = editor.getDocument();
      final CharSequence text = document.getCharsSequence();
      final int caretOffset = editor.getCaretModel().getOffset();

      final String[] keyContainer = new String[] { null };
      List<TemplateImpl> candidatesWithoutArgument = findMatchingTemplates(text, caretOffset, templateSettings, false, keyContainer);
      final String nonArgKey = keyContainer[0];
      Debug.out("no arg key = ["+nonArgKey+"]");

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
      Debug.out("argument = ["+argument+"]");
      //Debug.out("char at argument offset = ["+text.charAt(argumentOffset)+"]");
      keyContainer[0] = null;
      List<TemplateImpl> candidatesWithArgument = findMatchingTemplates(text, argumentOffset, templateSettings, true, keyContainer);
      final String argKey = keyContainer[0];
      Debug.out("Arg key = ["+argKey+"]");

      if (candidatesWithArgument.isEmpty() && candidatesWithoutArgument.isEmpty()) {
        return null;
      }

      commitDocument(file, document);  

      if (!candidatesWithoutArgument.isEmpty()) {
        //Debug.out("filtering Non-arg candidates, templateStart: ");
        int templateStart = caretOffset - nonArgKey.length();
        //printContext(text, templateStart, 7);
        candidatesWithoutArgument = filterApplicableCandidates(file, templateStart, candidatesWithoutArgument);
      }

      if (!candidatesWithArgument.isEmpty()) {
          //Debug.out("filtering Arg candidates, templateStart: ");
          int templateStart = argumentOffset - argKey.length();
          //printContext(text, templateStart, 7);
        candidatesWithArgument = filterApplicableCandidates(file, templateStart, candidatesWithArgument);
      }

      Map<TemplateImpl, String> candidate2Argument = new com.intellij.util.containers.HashMap<TemplateImpl, String>();
      addToMap(candidate2Argument, candidatesWithoutArgument, null);
      addToMap(candidate2Argument, candidatesWithArgument, argument);
      return candidate2Argument;
    }

    /**
     * NB: diagnostic only
     * @param seq
     * @param offset
     */
    static void printContext(CharSequence seq, int offset, int delta) {
        int minPos = Math.max(0, offset - delta);
        int maxPos = Math.min(seq.length(), offset + delta);
        CharSequence s1 = seq.subSequence(minPos, maxPos);
        s1 = s1.toString().replace('\n','$');
        s1 = s1.toString().replace('\t','_');
        Debug.out("["+s1+"]");
        StringBuilder sb = new StringBuilder(s1.length());
        for (int i=minPos; i<maxPos; i++ ) {
            if (i == offset) {
                sb.append('^');
                break;
            } else {
                sb.append('.');
            }
        }
        String below = sb.toString();
        Debug.out("["+below+"], offset = "+offset);
    }

    /* 4junit */
    protected List<TemplateImpl> filterApplicableCandidates(PsiFile file, int templateStartOffset, List<TemplateImpl> candidates) {
        List<TemplateImpl> result = new ArrayList<TemplateImpl>();
        for (TemplateImpl candidate : candidates) {
          if (TemplateManagerImpl.isApplicable(file, templateStartOffset, candidate)) {
            result.add(candidate);
          }
        }
        return result;
    }

    /* 4junit */
    protected void commitDocument(PsiFile file, final Document document) {
        final Project myProject = file.getProject();
        PsiDocumentManager.getInstance(myProject).commitDocument(document);
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
                                                           TemplateSettings settings,
                                                           boolean hasArgument, String[] keyContainer) {
      String key;
      List<TemplateImpl> candidates = Collections.emptyList();
      final boolean isShowingOnEmptySpace = BeanManager.storedBean().isShowLiveTemplatesOnEmptySpace();
      boolean isAfterNothing =
              /* NB: this mean that we search from the caret position but not from the argument position: */
              !hasArgument
              && ((caretOffset <= 0) || AutoPopupTypedHandlerDelegate.isBlank(text.charAt(caretOffset - 1)));
      final int minKeyLength = (isShowingOnEmptySpace && isAfterNothing) ? 0 : 1;
      for (int keyLength = settings.getMaxKeyLength(); keyLength >= minKeyLength; keyLength--) {
        int wordStart = caretOffset - keyLength;
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
            keyContainer[0] = key;
            break;
        }
      }
      return candidates;
    }

    private static Collection<TemplateImpl> getSemiMatchingTemplates(TemplateSettings settings, String startingWith) {
        TemplateImpl[] templates = settings.getTemplates();
        if (startingWith.length() == 0) {
            // small optimization: in case of empty key just return all:
            return Arrays.asList(templates);
        }
        final Collection<TemplateImpl> coll = new ArrayList<TemplateImpl>();
        // is could be possible to avoid traversing all the templates
        // using a SortedMap and getting by #subMap().
        // however, in such case we need a way to sync this map with the
        // current TemplateManager content, what is not obvious to implement.
        for (TemplateImpl impl: templates) {
            if (impl.getKey().startsWith(startingWith)) {
                 coll.add(impl);
            }
        }
        return coll;       
    }

    private static List<TemplateImpl> collectMatchingCandidates(TemplateSettings settings, String key, boolean hasArgument) {
      final Collection<TemplateImpl> templates = getSemiMatchingTemplates(settings, key);
      List<TemplateImpl> candidates = new ArrayList<TemplateImpl>();
      for (TemplateImpl template : templates) {
        if (template.isDeactivated()) {
          continue;
        }
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
