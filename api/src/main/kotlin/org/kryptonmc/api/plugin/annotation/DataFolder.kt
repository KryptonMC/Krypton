/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin.annotation

import com.google.inject.BindingAnnotation

/**
 * This is a marker annotation to signal that you want to inject the [java.nio.file.Path]
 * object for your plugin's folder.
 *
 * Example usage:
 * ```kotlin
 * class MyPlugin @Inject constructor(@DataFolder folder: Path)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@BindingAnnotation
annotation class DataFolder
