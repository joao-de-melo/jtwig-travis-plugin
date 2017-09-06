package org.jtwig.plugins.task

import org.apache.http.impl.client.HttpClients
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.jtwig.plugins.config.TravisExtension
import org.jtwig.plugins.config.TriggerTravisExtension
import org.jtwig.plugins.travis.TriggerBuildRequest
import org.jtwig.plugins.travis.TriggerBuildService

public class TriggerBuildTask extends DefaultTask {
    public static final String TASK_NAME = "travisTrigger";
    public static final String GROUP = "Travis"

    public static void create (Project project) {
        project.task(TASK_NAME, type: TriggerBuildTask);
    }

    @Override
    String getGroup() {
        return GROUP
    }

    @Override
    String getDescription() {
        return "Task to trigger travis build"
    }

    @TaskAction
    public void trigger () {
        TravisExtension extension = TravisExtension.retrieve(project);
        TriggerTravisExtension triggerTravisExtension = TriggerTravisExtension.retrieve(project);
        TriggerBuildService triggerBuildService = new TriggerBuildService(extension.travisApiUrl, HttpClients.createDefault());
        triggerBuildService.trigger(new TriggerBuildRequest(
                project.name,
                triggerTravisExtension.userOrg + "/" + triggerTravisExtension.repo,
                extension.travisToken,
                triggerTravisExtension.branch ?: "master",
                "Triggered from ${project.name} with message ${triggerTravisExtension.message}"
        ))
    }
}

