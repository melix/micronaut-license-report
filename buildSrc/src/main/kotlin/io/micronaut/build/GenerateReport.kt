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
        val cloneOnly = providers.gradleProperty("cloneOnly").map(String::toBoolean).getOrElse(false)
        if(cloneOnly){
            return
        }
        val allTasks = mutableListOf(
            "cleanGenerateLicense",
            "generateLicense",
            "dependencyTree",
            "findCopyrights",
            "licenseReport",
            "licenseReportText",
            "licenseReportAggregatedText",
            "generateCycloneDxBom",
            "filterSbom"
        )
        val includeSbomInReport = providers.gradleProperty("includeSbom").map(String::toBoolean).getOrElse(false)
        val isOnlySbom = providers.gradleProperty("onlySbom").map(String::toBoolean).getOrElse(false)
        if (!includeSbomInReport && !isOnlySbom) {
            val excludedTasks = listOf("generateCycloneDxBom", "filterSbom")
            allTasks.removeIf { task -> excludedTasks.contains(task)}
        }
        if (isOnlySbom) {
            val excludedTasks = listOf("licenseReportText", "licenseReportAggregatedText")
            allTasks.removeIf { task -> excludedTasks.contains(task) }
        }

        val initScriptPath = initScript.get().asFile.absolutePath
        println("Injecting init script $initScriptPath")
        val projectDir = projectDirectory.get().asFile
        val includeMicronautModules = providers.gradleProperty("includeMicronautModules").map(String::toBoolean).getOrElse(false)
        val excludedModuleIds = providers.gradleProperty("excludedModuleIds").getOrElse("")
        val addCopyrightsFromSource = providers.gradleProperty("addCopyrightsFromSource").map(String::toBoolean).getOrElse(false)
        val nettyNotice = providers.gradleProperty("nettyNotice").getOrElse("")
        val heapSize = providers.gradleProperty("heapSize").getOrElse("512")
        try {
            GradleConnector.newConnector()
                    .forProjectDirectory(projectDir)
                    .connect().use {
                    it.newBuild()
                        .withArguments("-I", initScriptPath, "-S", "--continue", "--parallel", "--no-configuration-cache", "-PincludeMicronautModules=" + includeMicronautModules, "-PexcludedModuleIds="+excludedModuleIds, "-PaddCopyrightsFromSource=" + addCopyrightsFromSource, "-PnettyNotice=" + nettyNotice)
                        .forTasks(*allTasks.toTypedArray())
                        .setStandardOutput(System.out)
                        .setStandardError(System.err)
                        .addJvmArguments("-Xmx${heapSize}m")
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

            val sbomReportFile = File(projectDir, "build/reports/sbom/all-sbom.json")
            val targetSbomJson = File(this, "all-sbom.json")
            if (sbomReportFile.exists()) {
                targetSbomJson.writeText(sbomReportFile.readText())
            } else {
                targetSbomJson.delete()
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
