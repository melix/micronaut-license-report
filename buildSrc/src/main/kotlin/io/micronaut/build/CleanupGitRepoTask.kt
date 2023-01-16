package io.micronaut.build

import org.eclipse.jgit.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class CleanupGitRepoTask : DefaultTask() {

    @get:Input
    abstract val repoDirectory: DirectoryProperty

    @TaskAction
    fun doGit() {
        val repoDir = repoDirectory.get().asFile
        if (repoDir.exists() && File(repoDir, ".git").exists()) {
            println("Deleting ${repoDirectory.get()}")
            FileUtils.delete(repoDir, 1)
        } else {
            println("Repository ${repoDirectory.get()} does not exist")
        }
    }
}
