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
package org.kryptonmc.internal.annotations

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * Indicates that the annotated type is catalogued by the
 * given [catalogue] type.
 *
 * For requirements of catalogue types, see the [Catalogue] annotation.
 *
 * @param catalogue the type of the catalogue
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Inherited
@MustBeDocumented
public annotation class CataloguedBy(public val catalogue: KClass<*>)
