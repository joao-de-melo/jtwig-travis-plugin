package org.jtwig.plugins.config

import org.gradle.api.Project


public class TravisExtension {
    public static final String EXTENSION = "travis";

    public static TravisExtension retrieve (Project project) {
        return project.getExtensions().getByName(EXTENSION);
    }

    public static TravisExtension create (Project project) {
        project.getExtensions().create(EXTENSION, TravisExtension)
    }

    String travisApiUrl = "https://api.travis-ci.org"
    String travisToken;
}
