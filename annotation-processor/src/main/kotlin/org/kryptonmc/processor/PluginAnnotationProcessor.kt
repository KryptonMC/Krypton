/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.processor

import com.google.gson.stream.JsonWriter
import org.kryptonmc.api.plugin.annotation.Plugin
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation

@SupportedAnnotationTypes("org.kryptonmc.api.plugin.annotation.Plugin")
@SupportedSourceVersion(SourceVersion.RELEASE_16)
class PluginAnnotationProcessor : AbstractProcessor() {

    private var pluginClassFound: String? = null
    private var warnedAboutMultiplePlugins = false

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) return false

        roundEnv.getElementsAnnotatedWith(Plugin::class.java).forEach { element ->
            if (element.kind != ElementKind.CLASS) {
                processingEnv.messager.error("Only classes can be annotated with ${Plugin::class.java.canonicalName}!")
                return false
            }

            element as TypeElement
            val qualifiedName = element.qualifiedName.toString()
            if (pluginClassFound == qualifiedName) {
                if (!warnedAboutMultiplePlugins) {
                    processingEnv.messager.warn("Krypton does not currently support multiple plugins. We are using $pluginClassFound for your " +
                            "plugin's main class.")
                    warnedAboutMultiplePlugins = true
                }
                return false
            }

            val plugin = element.getAnnotation(Plugin::class.java)
            if (!plugin.id.matches(SerializedPluginDescription.ID_REGEX)) {
                processingEnv.messager.error("Invalid ID for plugin $qualifiedName! IDs must start alphabetically, and only contain alphanumeric " +
                        "characters, dashes or underscores! (Regex: ${SerializedPluginDescription.ID_REGEX})")
                return false
            }

            // We're all good to go, let's generate the metadata
            val description = plugin.toDescription(qualifiedName)
            try {
                val fileObject = processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "krypton-plugin-meta.json")
                JsonWriter(fileObject.openWriter()).use { SerializedPluginDescription.write(it, description) }
                pluginClassFound = qualifiedName
            } catch (exception: IOException) {
                processingEnv.messager.error("Unable to generate plugin metadata file!")
            }
        }
        return false
    }
}

private fun Messager.warn(message: String) {
    printMessage(Diagnostic.Kind.WARNING, message)
}

private fun Messager.error(message: String) {
    printMessage(Diagnostic.Kind.ERROR, message)
}
