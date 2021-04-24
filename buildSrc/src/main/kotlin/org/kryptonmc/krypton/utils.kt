package org.kryptonmc.krypton

import org.gradle.api.Project

fun Project.property(key: String, default: String?): String? = if (hasProperty(key)) property(key).toString() else default
