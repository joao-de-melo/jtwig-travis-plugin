package org.jtwig.plugins.config

import org.gradle.api.Project


public class TriggerTravisExtension {
    public static final String EXTENSION = "triggerTravis";

    public static TriggerTravisExtension retrieve (Project project) {
        return project.getExtensions().getByName(EXTENSION);
    }

    public static TriggerTravisExtension create (Project project) {
        project.getExtensions().create(EXTENSION, TriggerTravisExtension)
    }


    String userOrg;
    String repo;
    String message;
}
