package tasks

import AppDetail.newApkName
import AppDetail.previousName
import AppDetail.previousPath
import AppDetail.targetPath
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ManageApk : DefaultTask() {
    @TaskAction
    fun renameApk() {
        val newPath = File("${project.buildDir.absoluteFile}/$previousPath/$previousName")
        if (newPath.exists()) {
            val newApkName = "${project.buildDir.absoluteFile}/$previousPath/$newApkName"
            newPath.renameTo(File(newApkName))
        } else {
            println("Path not exist!")
        }
//        moveFile()
    }

    private fun moveFile() {
        File("${project.buildDir.absoluteFile}/$previousPath/$newApkName").let { sourceFile ->
            try {
                sourceFile.copyTo(File("$targetPath/$newApkName"))
            } catch (e: Exception) {
                e.printStackTrace()
                val folder = File(targetPath)
                folder.mkdir()
            } finally {
                sourceFile.delete()
            }
        }
    }
}