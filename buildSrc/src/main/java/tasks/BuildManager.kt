package tasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class BuildManager : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register<ManageApk>("renameApk") {
            dependsOn("build")
        }
    }
}