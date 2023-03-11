package org.kryptonmc.downloads

import org.gradle.api.Plugin
import org.gradle.api.Project

class UploadToApiPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.register("uploadToApi", UploadToApiTask::class.java) {
            group = "upload"
        }
    }
}
