package io.micronaut.build

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.tooling.GradleConnector
import java.io.File

abstract class GenerateReport : DefaultTask() {
    @get:InputFile
    abstract val initScript: RegularFileProperty

    @get:InputDirectory
    abstract val projectDirectory: DirectoryProperty

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    @TaskAction
    fun report() {
        val initScriptPath = initScript.get().asFile.absolutePath
        println("Injecting init script $initScriptPath")
        val projectDir = projectDirectory.get().asFile
        try {
            GradleConnector.newConnector()
                    .forProjectDirectory(projectDir)
                    .connect().use {
                        it.newBuild()
                                .withArguments("-I", initScriptPath, "--continue")
                                .forTasks("cleanGenerateLicense", "generateLicense", "licenseReport")
                                .setStandardOutput(System.out)
                                .setStandardError(System.err)
                                .run()
                    }
        } catch (e: Exception) {
            // We intentionally ignore the status of the build result
            System.err.println(e.message)
        }
        reportFile.get().asFile.run {
            parentFile.mkdirs()
            val reportFile = File(projectDir, "build/reports/licenseReport/report.json")
            if (reportFile.exists()) {
                writeText(reportFile.readText())
            } else {
                delete()
            }
        }
    }
}
