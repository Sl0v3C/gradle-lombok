package io.franzbecker.gradle.lombok

/**
 * Configuration for the plugin.
 */
class LombokPluginExtension {

    static final String NAME = "lombok"

    /** The version of Lombok to use. */
    String version = "1.18.2"

    /** The SHA-256 hash of the JAR. */
    String sha256 = "f13db210efa2092a58bb7befce58ffa502e5fefc5e1099f45007074008756bc0"

    /** The main class to call when invoking {@linkplain InstallLombokTask#NAME}. */
    String main = "lombok.launch.Main"

}
