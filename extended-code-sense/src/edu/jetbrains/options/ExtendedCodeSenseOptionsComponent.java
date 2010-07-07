/*
 * Copyright 2010 Joachim Ansorg, mail@ansorg-it.com
 * File: SmarterEditorSettingsComponent.java, Class: SmarterEditorSettingsComponent
 * Last modified: 2010-03-28
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.jetbrains.options;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@State(
        name = "ExtendedCodeSenseOptions",
        storages = {
                @Storage(id = "default",
                        file = "$APP_CONFIG$/extended-code-sense-options.xml")
        }
)
public class ExtendedCodeSenseOptionsComponent implements PersistentStateComponent<OptionsBean>, ApplicationComponent {

    private OptionsBean settings = BeanManager.defaultBean();

    public ExtendedCodeSenseOptionsComponent() {
    }

    public OptionsBean getState() {
        return settings;
    }

    public void loadState(OptionsBean state1) {
        settings = state1;
    }

    @NotNull
    public String getComponentName() {
        return "ExtendedCodeSenseOptions";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }
}
