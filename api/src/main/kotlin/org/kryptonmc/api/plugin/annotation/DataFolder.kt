/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/annotation/DataDirectory.java
 */
package org.kryptonmc.api.plugin.annotation

import com.google.inject.BindingAnnotation

/**
 * This is a marker annotation to signal that you want to inject the
 * [java.nio.file.Path] object for your plugin's folder.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@BindingAnnotation
@MustBeDocumented
public annotation class DataFolder
