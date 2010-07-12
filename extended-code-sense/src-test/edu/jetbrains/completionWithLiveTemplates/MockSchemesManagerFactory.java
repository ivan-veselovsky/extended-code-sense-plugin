package edu.jetbrains.completionWithLiveTemplates;

import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.options.*;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 11.07.2010
 * Time: 21:21:35
 * To change this template use File | Settings | File Templates.
 */
public class MockSchemesManagerFactory extends SchemesManagerFactory{

    public SchemesManager schemesManager;

    @Override
    public <T extends Scheme, E extends ExternalizableScheme> SchemesManager<T, E> createSchemesManager(String fileSpec, SchemeProcessor<E> processor, RoamingType roamingType) {
        return schemesManager;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateConfigFilesFromStreamProviders() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
