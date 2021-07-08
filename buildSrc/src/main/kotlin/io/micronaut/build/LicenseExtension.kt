package io.micronaut.build

import org.eclipse.jgit.api.Git
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import java.io.File
import javax.inject.Inject

abstract class LicenseExtension(@Inject val initScript: RegularFile) {
    @get:Inject
    abstract val tasks: TaskContainer

    @get:Inject
    abstract val layout: ProjectLayout

    fun checkout(uri: String, branch: String = "master", checkoutDir: Directory = layout.projectDirectory.dir("checkouts/${uri.checkoutDir}")) {
        val checkoutTask = tasks.register("checkout${checkoutDir.asFile.usableName}", GitRepoTask::class.java) {
            this.repoDirectory.set(checkoutDir)
            this.branch.set(branch)
            this.uri.set(uri)
        }
        val reportTask = report(checkoutDir.asFile)
        reportTask.configure {
            projectDirectory.set(checkoutTask.map { it.repoDirectory.get() })
        }
    }

    fun report(projectDir: File) = tasks.register("reportFor${projectDir.usableName}", GenerateReport::class.java) {
        group = "License reporting"
        description = "Generates license report for project ${projectDir.usableName}"
        initScript.set(this@LicenseExtension.initScript)
        projectDirectory.set(projectDir)
        reportFile.set(layout.buildDirectory.file("reports/reportFor${projectDir.usableName}/report.json"))
    }

    private val String.checkoutDir: String
        get() = run {
            var name = substring(lastIndexOf("/") + 1)
            if (name.endsWith(".git")) {
                name = name.substring(0, name.indexOf(".git"))
            }
            name
        }

    private val File.usableName: String
        get() = name.split('-').map(String::capitalize).joinToString("")

}
