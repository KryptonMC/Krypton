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
package org.kryptonmc.krypton.util.map

import java.util.function.Function

// We use the platform class here to clean up the generated code, because we don't need to check for KMappedMarker,
// and we ideally want to call the JVM implementation directly, as that's the one with our desired semantics.
@Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN", "NOTHING_TO_INLINE")
inline fun <K, V> MutableMap<K, V>.nullableComputeIfAbsent(key: K, mappingFunction: Function<in K, out V?>): V? =
    (this as java.util.Map<K, V>).computeIfAbsent(key, mappingFunction)
