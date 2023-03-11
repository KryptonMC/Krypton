package org.kryptonmc.downloads

import com.google.gson.Gson
import org.ajoberstar.grgit.Grgit
import org.apache.hc.client5.http.entity.mime.FileBody
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.client5.http.entity.mime.StringBody
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.net.URI

abstract class UploadToApiTask : DefaultTask() {

    @get:InputFile
    abstract val file: RegularFileProperty

    private val gson = Gson()
    private val client = HttpClients.createDefault()

    @TaskAction
    fun upload() {
        val commit = Grgit.open { dir = project.rootProject.projectDir }.head()
        val change = UploadData.Change(commit.id, commit.shortMessage, commit.fullMessage)
        val data = UploadData("krypton", project.version.toString(), listOf(change))
        val body = MultipartEntityBuilder.create()
            .addPart("data", StringBody(gson.toJson(data), ContentType.APPLICATION_JSON))
            .addPart("file", FileBody(file.get().asFile, ContentType.APPLICATION_OCTET_STREAM))
            .build()
        val request = ClassicRequestBuilder.post(UPLOAD_URL)
            .addHeader("Authorization", "Token ${System.getenv("DOWNLOADS_API_TOKEN")}")
            .setEntity(body)
            .build()
        client.execute(request) { response ->
            if (response.code != 200) throw IllegalStateException("Failed to upload to API: ${response.code} ${response.reasonPhrase}")
        }
    }

    companion object {

        private val UPLOAD_URL = URI.create("https://api.kryptonmc.org/downloads/v1/upload")
    }
}
