import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

class ConventionsPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            extensions.configure(KotlinJvmProjectExtension::class.java) { kotlin ->
                kotlin.jvmToolchain(libs.findVersion("java-compilation").get().requiredVersion.toInt())
                kotlin.compilerOptions.apiVersion.set(KotlinVersion.KOTLIN_2_1)
                kotlin.compilerOptions.languageVersion.set(KotlinVersion.KOTLIN_2_1)
            }
        }

        pluginManager.withPlugin("java") {
            if (hasProperty("skipJarVersion")) {
                val projectName = name
                tasks.named("jar", Jar::class.java) { jar ->
                    jar.archiveFileName.set("$projectName.jar")
                }
            }
        }
    }
}
