
package org.jtwig.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jtwig.plugins.config.TravisExtension
import org.jtwig.plugins.config.TriggerTravisExtension
import org.jtwig.plugins.task.TriggerBuildTask

public class TravisPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        TravisExtension.create(project);
        TriggerTravisExtension.create(project);

        TriggerBuildTask.create(project);
    }
}
