package org.kryptonmc.downloads

import net.kyori.mammoth.ProjectPlugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer

class UploadToApiPlugin : ProjectPlugin {

    override fun apply(project: Project, plugins: PluginContainer, extensions: ExtensionContainer, tasks: TaskContainer) {
        tasks.register("uploadToApi", UploadToApiTask::class.java) {
            group = "upload"
        }
    }
}
