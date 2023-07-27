package io.micronaut.build

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.NoHeadException
import org.eclipse.jgit.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

abstract class GitRepoTask : DefaultTask() {
    @get:Input
    abstract val uri: Property<String>

    @get:OutputDirectory
    abstract val repoDirectory: DirectoryProperty

    @get:Input
    abstract val branch: Property<String>

    @get:Inject
    abstract val providers: ProviderFactory

    @TaskAction
    fun doGit() {
        val repoDir = repoDirectory.get().asFile
        val cleanupGitRepo = providers.gradleProperty("cleanupGitRepo").map(String::toBoolean).getOrElse(false)
        val skipCheckouts = providers.gradleProperty("skipCheckouts").map(String::toBoolean).getOrElse(false)
        if (cleanupGitRepo && repoDir.exists() && File(repoDir, ".git").exists()) {
            println("Deleting ${repoDirectory.get()}")
            FileUtils.delete(repoDir, 1)
        }
        if(repoDir.exists() && skipCheckouts) {
            return
        }
        if (repoDir.exists() && File(repoDir, ".git").exists()) {
            println("Updating ${uri.get()}")
            try {
                Git.open(repoDir)
                    .checkout()
                    .setName(branch.get())
                    .call()
                Git.open(repoDir)
                    .pull()
                    .setRebase(true)
                    .call()
            } catch (ex: Exception) {
                System.err.println("Warning: $ex")
            }
        } else {
            if (repoDir.exists()) {
                repoDir.delete()
            }
            println("Checking out ${uri.get()} branch ${branch.get()} in ${repoDirectory.get().asFile}")
            Git.cloneRepository()
                .setURI(uri.get())
                .setBranch(branch.get())
                .setDirectory(repoDir)
                .call()
        }
    }
}
