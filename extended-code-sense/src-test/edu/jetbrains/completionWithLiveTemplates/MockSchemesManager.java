package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.openapi.options.ExternalizableScheme;
import com.intellij.openapi.options.Scheme;
import com.intellij.openapi.options.SchemesManager;
import com.intellij.openapi.util.WriteExternalException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 11.07.2010
 * Time: 21:23:19
 * To change this template use File | Settings | File Templates.
 */
public class MockSchemesManager implements SchemesManager {

    public MockSchemesManager() {
    }

    @NotNull
    public Collection loadSchemes() {
        return Collections.emptyList();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public Collection loadSharedSchemes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public Collection loadSharedSchemes(Collection currentSchemeList) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void exportScheme(ExternalizableScheme scheme, String name, String description) throws WriteExternalException, IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isImportAvailable() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isExportAvailable() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isShared(Scheme scheme) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addNewScheme(@NotNull Scheme scheme, boolean replaceExisting) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clearAllSchemes() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public List getAllSchemes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Scheme findSchemeByName(String schemeName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void save() throws WriteExternalException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setCurrentSchemeName(String schemeName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Scheme getCurrentScheme() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeScheme(Scheme scheme) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public Collection<String> getAllSchemeNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public File getRootDirectory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
