/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * Marker annotation indicating that the annotated type is catalogued by the
 * given [catalogue] type.
 *
 * For requirements of catalogue types, see the [Catalogue] annotation.
 *
 * @param catalogue the type of the catalogue, the type that is annotated with
 * [Catalogue]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Inherited
@MustBeDocumented
public annotation class CataloguedBy(public val catalogue: KClass<*>)
