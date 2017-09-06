package org.jtwig.plugins.travis;

public class TriggerBuildRequest {
    private final String parentProject;
    private final String project;
    private final String token;
    private final String message;

    public TriggerBuildRequest(String parentProject, String project, String token, String branch, String message) {
        this.parentProject = parentProject;
        this.project = project;
        this.token = token;
        this.message = message;
    }

    public String getParentProject() {
        return parentProject;
    }

    public String getProject() {
        return project;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
