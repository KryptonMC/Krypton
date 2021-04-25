package org.kryptonmc.krypton

import org.gradle.api.Project
import org.gradle.api.Task

fun Project.property(key: String, default: String?): String? = if (hasProperty(key)) property(key).toString() else default

infix fun Task.dependsOn(other: Task) = dependsOn(other)
