package io.franzbecker.gradle.lombok.task
import io.franzbecker.gradle.lombok.LombokPlugin
import io.franzbecker.gradle.lombok.LombokPluginExtension
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.JavaExec

/**
 * Task type for delomboking. Not added to the project by default,
 * has to be instantiated and configured in the using gradle build.
 */
class DelombokTask extends JavaExec {

    String[] configurationNames = [
        JavaPlugin.API_CONFIGURATION_NAME,
        JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
        LombokPlugin.LOMBOK_CONFIGURATION_NAME
    ]

    DelombokTask() {
        super()
        args "delombok"
    }

    @Override
    void exec() {
        def extension = project.extensions.findByType(LombokPluginExtension)

        // Configure JavaExec
        setMain(extension.main)
        classpath(getConfigurations())
        super.exec()
    }

    private ConfigurationContainer[] getConfigurations() {
        return configurationNames.collect { project.configurations.getByName(it) }
    }

}
