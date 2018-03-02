package io.franzbecker.gradle.lombok

import io.franzbecker.gradle.lombok.task.InstallLombokTask
import io.franzbecker.gradle.lombok.task.VerifyLombokTask
import nebula.test.PluginProjectSpec
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.JavaPlugin

/**
 * Unit tests for {@link LombokPlugin}.
 */
class LombokPluginSpec extends PluginProjectSpec {

    @Override
    String getPluginName() {
        return LombokPlugin.NAME
    }

    private def applyJavaAndLombok() {
        project.apply plugin: "java"
        project.apply plugin: pluginName
    }

    def "Does not create new tasks if Java plugin is not applied"() {
        given:
        def beforeTaskMap = project.tasks.getAsMap().collect()

        when:
        project.apply plugin: pluginName

        then:
        def afterTaskMap = project.tasks.getAsMap().collect()
        assert afterTaskMap == beforeTaskMap
    }

    def "Does not add Lombok dependency if Java plugin is not applied"() {
        when:
        project.apply plugin: pluginName

        then:
        def lombokDependency = getCompileOnlyLombokDependency()
        assert !lombokDependency
    }

    def "Lombok dependency is added if Java plugin is applied"() {
        when:
        applyJavaAndLombok()
        project.evaluate()

        then:
        def lombokDependency = getCompileOnlyLombokDependency()
        assert lombokDependency
    }

    def "Added dependency is not transitive"() {
        when:
        applyJavaAndLombok()
        project.evaluate()

        then:
        def lombokDependency = getCompileOnlyLombokDependency()
        assert !lombokDependency.isTransitive()
    }

    def "Add tasks if Java plugin is applied and installLombok depends on verifyLombok"() {
        when:
        applyJavaAndLombok()

        then:
        VerifyLombokTask verifyLombok = project.tasks[VerifyLombokTask.NAME]
        InstallLombokTask installLombok = project.tasks[InstallLombokTask.NAME]
        verifyLombok in installLombok.getDependsOn()
    }

    private def Dependency getCompileOnlyLombokDependency() {
        def compileOnlyConfiguration = project.configurations.findByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
        return compileOnlyConfiguration?.getDependencies()?.find {
            it.group == "org.projectlombok" && it.name == "lombok"
        }
    }

}
