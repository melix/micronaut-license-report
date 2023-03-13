package io.micronaut.build

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.*
import org.gradle.tooling.GradleConnector
import java.io.File
import javax.inject.Inject

abstract class GenerateReport : DefaultTask() {
    @get:InputFile
    abstract val initScript: RegularFileProperty

    @get:InputDirectory
    abstract val projectDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val reportDirectory: DirectoryProperty

    @get:Inject
    abstract val providers: ProviderFactory

    @TaskAction
    fun report() {
        val initScriptPath = initScript.get().asFile.absolutePath
        println("Injecting init script $initScriptPath")
        val projectDir = projectDirectory.get().asFile
        val includeMicronautModules = providers.gradleProperty("includeMicronautModules").map(String::toBoolean).getOrElse(false)
        try {
            GradleConnector.newConnector()
                    .forProjectDirectory(projectDir)
                    .connect().use {
                    it.newBuild()
                        .withArguments("-I", initScriptPath, "--continue", "--parallel", "--no-configuration-cache", "-PincludeMicronautModules=" + includeMicronautModules)
                        .forTasks("cleanGenerateLicense", "generateLicense", "licenseReport", "licenseReportText", "licenseReportAggregatedText", "dependencyTree")
                        .setStandardOutput(System.out)
                        .setStandardError(System.err)
                        .run()
                    }
        } catch (e: Exception) {
            // We intentionally ignore the status of the build result
            System.err.println(e.message)
        }
        reportDirectory.get().asFile.run {
            mkdirs()
            val reportFile = File(projectDir, "build/reports/licenseReport/report.json")
            val targetJson = File(this, "report.json")
            if (reportFile.exists()) {
                targetJson.writeText(reportFile.readText())
            } else {
                targetJson.delete()
            }
            val textReports = File(projectDir, "build/licenses")
            if (textReports.exists()) {
                textReports.listFiles()
                        .filter { it.name.endsWith(".txt") }
                        .forEach {
                            File(this, it.name).writeText(it.readText())
                        }
            }
        }
    }
}
