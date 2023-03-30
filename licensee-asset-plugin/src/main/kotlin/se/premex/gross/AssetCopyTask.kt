package se.premex.gross

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class AssetCopyTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @TaskAction
    fun action() {
        inputFile.get().asFile.copyTo(
            target = File(
                outputDirectory.get().asFile,
                inputFile.get().asFile.name
            ), overwrite = true
        )
    }
}