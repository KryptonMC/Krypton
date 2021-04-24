package org.kryptonmc.krypton

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlinx(module: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$module:$version"

fun DependencyHandler.adventure(module: String) = "net.kyori:adventure-$module:${Versions.ADVENTURE}"

fun DependencyHandler.netty(module: String) = "io.netty:netty-$module:${Versions.NETTY}"

fun DependencyHandler.log4j(module: String) = "org.apache.logging.log4j:log4j-$module:${Versions.LOG4J}"
