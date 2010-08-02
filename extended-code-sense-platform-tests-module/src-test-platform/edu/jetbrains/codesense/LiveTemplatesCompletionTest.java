/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jetbrains.codesense;

import com.intellij.codeInsight.completion.LightCompletionTestCase;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.util.JDOMUtil;

import java.io.File;
import java.io.InputStream;

public class LiveTemplatesCompletionTest extends LightCompletionTestCase {

  protected static enum Lang {
    java, js, php, groovy, plain, xml;
  }

  private static final String TEST_DATA_PATH_KEY = "test.data.path";

  private static final String BASE_PATH = "/codeInsight/completion/liveTemplates/";

  private String testDataPath;

  protected String getBasePath(Lang lang) {
      return BASE_PATH + lang.name() + "/";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    IdeaPluginDescriptor[] ideaPluginDescriptors = PluginManager.getPlugins();
    boolean found = false;
    for (IdeaPluginDescriptor descriptor: ideaPluginDescriptors) {
      String name = descriptor.getName();
      if ("Extended Code Sense".equals(name)) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new IllegalStateException("The extended-code-sense plugin not found. Please set up the [idea.plugins.path] system property accordingly.");
    }

    // check up the testDataPath:
    testDataPath = System.getProperty(TEST_DATA_PATH_KEY);
    if (testDataPath == null) {
      throw new IllegalStateException("please define system property ["+TEST_DATA_PATH_KEY+"] to point to the test " +
                                      "plugin root (not including the testData/ segment).");
    } else {
      File file = new File(testDataPath);
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalStateException("file ["+file.getAbsolutePath()+"] not found or is not a directory.");
        }
    }

    loadCustomTemplates();
  }

  @Override
  protected String getTestDataPath() {
    return testDataPath + "/testData"; //JavaTestUtil.getJavaTestDataPath();
  }

  protected void loadCustomTemplates() throws Exception {
      TemplateSettings settings = TemplateSettings.getInstance();
      InputStream inputStream = getClass().getResourceAsStream("user.xml");
      settings.readTemplateFile(JDOMUtil.loadDocument(inputStream), "user", false, true, getClass().getClassLoader());
  }

  public void testItarJava() throws Exception {
      configureByFile(getBasePath(Lang.java) + "itar.java");
      checkResultByFile(getBasePath(Lang.java) + "itar_after.java");
  }

  public void testItarJavaInComment() throws Exception {
      configureByFile(getBasePath(Lang.java) + "itar_in_comment.java");
      checkResultByFile(getBasePath(Lang.java) + "itar_in_comment_after.java");
  }

    public void testArgJava() throws Exception {
        configureByFile(getBasePath(Lang.java) + "arg.java");
        checkResultByFile(getBasePath(Lang.java) + "arg_after.java");
    }

    public void testArgPlain() throws Exception {
        configureByFile(getBasePath(Lang.plain) + "a.txt");
        checkResultByFile(getBasePath(Lang.plain) + "a_after.txt");
    }

    public void testZenXml() throws Exception {
        configureByFile(getBasePath(Lang.xml) + "zen.xhtml");
        checkResultByFile(getBasePath(Lang.xml) + "zen_after.xhtml");
    }

    public void testZenXmlInComment() throws Exception {
          configureByFile(getBasePath(Lang.xml) + "zen_in_comment.xhtml");
          checkResultByFile(getBasePath(Lang.xml) + "zen_in_comment_after.xhtml");
    }

    public void testGroovyPsvm() throws Exception {
        configureByFile(getBasePath(Lang.groovy) + "psvm.groovy");
        checkResultByFile(getBasePath(Lang.groovy) + "psvm_after.groovy");
    }

    public void testGroovyArg() throws Exception {
        configureByFile(getBasePath(Lang.groovy) + "arg.groovy");
        checkResultByFile(getBasePath(Lang.groovy) + "arg_after.groovy");
    }

}
