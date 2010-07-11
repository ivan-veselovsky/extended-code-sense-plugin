package edu.jetbrains.options;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.application.ModalityInvokator;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ComponentConfig;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.picocontainer.PicoContainer;

import java.awt.*;
import java.util.concurrent.Future;

public class MockApplication implements Application {

    private ExtendedCodeSenseOptionsComponent codeSenseOptionsComponent;

    public void runReadAction(Runnable action) {
        
    }

    public <T> T runReadAction(Computable<T> computation) {
        return null;  
    }

    public void runWriteAction(Runnable action) {
        
    }

    public <T> T runWriteAction(Computable<T> computation) {
        return null;  
    }

    public Object getCurrentWriteAction(Class actionClass) {
        return null;  
    }

    public void assertReadAccessAllowed() {
        
    }

    public void assertWriteAccessAllowed() {
        
    }

    public void assertIsDispatchThread() {
        
    }

    public void addApplicationListener(ApplicationListener listener) {
        
    }

    public void addApplicationListener(ApplicationListener listener, Disposable parent) {
        
    }

    public void removeApplicationListener(ApplicationListener listener) {
        
    }

    public void saveAll() {
        
    }

    public void saveSettings() {
    }

    public void exit() {
    }

    public boolean isWriteAccessAllowed() {
        return false;  
    }

    public boolean isReadAccessAllowed() {
        return false;  
    }

    public boolean isDispatchThread() {
        return false;  
    }

    @NotNull
    public ModalityInvokator getInvokator() {
        return null;  
    }

    public void invokeLater(Runnable runnable) {
        
    }

    public void invokeLater(Runnable runnable, @NotNull Condition expired) {
    }

    public void invokeLater(Runnable runnable, @NotNull ModalityState state) {
    }

    public void invokeLater(Runnable runnable, @NotNull ModalityState state, @NotNull Condition expired) {
    }

    public void invokeAndWait(Runnable runnable, @NotNull ModalityState modalityState) {
    }

    public ModalityState getCurrentModalityState() {
        return null; 
    }

    public ModalityState getModalityStateForComponent(Component c) {
        return null; 
    }

    public ModalityState getDefaultModalityState() {
        return null; 
    }

    public ModalityState getNoneModalityState() {
        return null; 
    }

    public long getStartTime() {
        return 0; 
    }

    public long getIdleTime() {
        return 0; 
    }

    public boolean isUnitTestMode() {
        return false; 
    }

    public boolean isHeadlessEnvironment() {
        return false; 
    }

    public boolean isCommandLine() {
        return false; 
    }

    public IdeaPluginDescriptor getPlugin(PluginId id) {
        return null; 
    }

    public IdeaPluginDescriptor[] getPlugins() {
        return new IdeaPluginDescriptor[0]; 
    }

    public boolean isDisposed() {
        return false; 
    }

    public Future<?> executeOnPooledThread(Runnable action) {
        return null; 
    }

    public boolean isDisposeInProgress() {
        return false; 
    }

    public boolean isRestartCapable() {
        return false; 
    }

    public void restart() {
    }

    public boolean isActive() {
        return false;  
    }

    public BaseComponent getComponent(String name) {
        return null;  
    }

    public <T> T getComponent(Class<T> interfaceClass) {
        if (interfaceClass == ExtendedCodeSenseOptionsComponent.class) {
            if (codeSenseOptionsComponent == null) {
                codeSenseOptionsComponent = new ExtendedCodeSenseOptionsComponent();
            }
            return (T)codeSenseOptionsComponent;
        }
        return null;  
    }

    public <T> T getComponent(Class<T> interfaceClass, T defaultImplementationIfAbsent) {
        return null;  
    }

    @NotNull
    public Class[] getComponentInterfaces() {
        return new Class[0];  
    }

    public boolean hasComponent(@NotNull Class interfaceClass) {
        return false;  
    }

    @NotNull
    public <T> T[] getComponents(Class<T> baseClass) {
        return null; 
    }

    @NotNull
    public PicoContainer getPicoContainer() {
        return null; 
    }

    public MessageBus getMessageBus() {
        return null; 
    }

    @NotNull
    public ComponentConfig[] getComponentConfigurations() {
        return new ComponentConfig[0]; 
    }

    public Object getComponent(ComponentConfig componentConfig) {
        return null; 
    }

    public <T> T[] getExtensions(ExtensionPointName<T> extensionPointName) {
        return null; 
    }

    public ComponentConfig getConfig(Class componentImplementation) {
        return null; 
    }

    public void dispose() {
    }

    public <T> T getUserData(@NotNull Key<T> key) {
        return null; 
    }

    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
    }
}
