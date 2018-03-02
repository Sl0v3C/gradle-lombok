package io.franzbecker.gradle.lombok

import io.franzbecker.gradle.lombok.task.InstallLombokTask
import io.franzbecker.gradle.lombok.task.VerifyLombokTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

/**
 * Plugin for project Lombok support.
 *
 * @see <a href="https://github.com/franzbecker/gradle-lombok">GitHub project</a>
 * @see <a href="https://projectlombok.org">https://projectlombok.org</a>
 */
class LombokPlugin implements Plugin<Project> {

    static final String NAME = "io.franzbecker.gradle-lombok"
    static final String LOMBOK_CONFIGURATION_NAME = JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME

    @Override
    void apply(Project project) {
        // Register extension
        project.extensions.create(LombokPluginExtension.NAME, LombokPluginExtension)

        project.plugins.withType(JavaPlugin) {
            addLombokDependency(project)
            configureTasks(project)
        }
    }

    /**
     * Adds the Lombok dependency as compileOnly
     */
    private void addLombokDependency(Project project) {
        project.afterEvaluate {
            project.dependencies.add(
                LOMBOK_CONFIGURATION_NAME,
                "org.projectlombok:lombok:${project.lombok.version}",
                { transitive = false }
            )
        }
    }

    /**
     * Adds {@link VerifyLombokTask} and {@link InstallLombokTask} and lets installLombok
     * depend on verifyLombok.
     */
    private void configureTasks(Project project) {
        def extension = project.extensions.findByType(LombokPluginExtension)

        // Add VerifyLombokTask
        def verifyLombok = project.task(type: VerifyLombokTask, VerifyLombokTask.NAME)
        verifyLombok.outputs.upToDateWhen { !extension.sha256 }

        // Add InstallLombokTask
        project.task(type: InstallLombokTask, InstallLombokTask.NAME).with {
            outputs.upToDateWhen { false }
            dependsOn verifyLombok
        }
    }

}
