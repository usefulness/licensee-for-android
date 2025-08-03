import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import kotlin.jvm.java

class PublishingPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.vanniktech.maven.publish")
        pluginManager.apply("org.jetbrains.dokka")

        extensions.configure<MavenPublishBaseExtension> {
            publishToMavenCentral()
            coordinates(group.toString(), name, version.toString())

            signAllPublications()

            configureBasedOnAppliedPlugins()

            pom { pom ->
                afterEvaluate {
                    pom.name.set("${project.group}:${project.name}")
                    pom.description.set(project.description)
                }
                pom.url.set("https://github.com/usefulness/licensee-for-android")
                pom.licenses { licenses ->
                    licenses.license { license ->
                        license.name.set("MIT")
                        license.url.set("https://github.com/usefulness/licensee-for-android/blob/master/LICENSE")
                    }
                }
                pom.developers { developers ->
                    developers.developer { developer ->
                        developer.id.set("mateuszkwiecinski")
                        developer.name.set("Mateusz Kwiecinski")
                        developer.email.set("36954793+mateuszkwiecinski@users.noreply.github.com")
                    }
                }
                pom.scm { scm ->
                    scm.connection.set("scm:git:github.com/usefulness/licensee-for-android.git")
                    scm.developerConnection.set("scm:git:ssh://github.com/usefulness/licensee-for-android.git")
                    scm.url.set("https://github.com/usefulness/licensee-for-android/tree/master")
                }
            }
        }
        extensions.configure<PublishingExtension> {
            repositories.maven { maven ->
                maven.name = "github"
                maven.setUrl("https://maven.pkg.github.com/usefulness/licensee-for-android")
                with(maven.credentials) {
                    username = "usefulness"
                    password = findConfig("GITHUB_TOKEN")
                }
            }

            tasks.named { it == "javadocJar" }.withType(Jar::class.java).configureEach { javadocJar ->
                javadocJar.from(tasks.named("dokkaGeneratePublicationHtml"))
            }
        }

        pluginManager.withPlugin("com.gradle.plugin-publish") {
            extensions.configure<GradlePluginDevelopmentExtension>("gradlePlugin") { gradlePlugin ->
                gradlePlugin.apply {
                    website.set("https://github.com/usefulness/licensee-for-android")
                    vcsUrl.set("https://github.com/usefulness/licensee-for-android")
                }
            }
        }

        pluginManager.withPlugin("java") {
            tasks.named("processResources", ProcessResources::class.java) { processResources ->
                processResources.from(rootProject.file("LICENSE"))
            }
        }
    }

    private inline fun <reified T : Any> ExtensionContainer.configure(crossinline receiver: T.() -> Unit) {
        configure(T::class.java) { receiver(it) }
    }
}

private fun Project.findConfig(key: String): String = findProperty(key)?.toString() ?: System.getenv(key) ?: ""
