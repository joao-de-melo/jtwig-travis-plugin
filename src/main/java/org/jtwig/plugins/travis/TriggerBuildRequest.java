package org.jtwig.plugins.travis;

public class TriggerBuildRequest {
    private final String parentProject;
    private final String project;
    private final String message;

    public TriggerBuildRequest(String parentProject, String project, String message) {
        this.parentProject = parentProject;
        this.project = project;
        this.message = message;
    }

    public String getParentProject() {
        return parentProject;
    }

    public String getProject() {
        return project;
    }

    public String getMessage() {
        return message;
    }
}
