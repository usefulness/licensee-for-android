package io.github.usefulness.licensee

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
public abstract class LicenseeFileCopyTask : DefaultTask() {
    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFile
    public abstract val inputFile: RegularFileProperty

    @get:Input
    public abstract val targetFileName: Property<String>

    @TaskAction
    public fun action() {
        inputFile.get().asFile.copyTo(
            target = outputDirectory.get().asFile.resolve(targetFileName.get()),
            overwrite = true,
        )
    }
}
