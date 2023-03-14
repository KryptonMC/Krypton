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
package org.kryptonmc.krypton.util

/**
 * Downcasts the given API type [A] to its implementation type [I].
 *
 * This is a common implementation shared between specific downcast functions
 * that are used to downcast a specific API type to its implementation
 * equivalent when implementation-specific information is required.
 */
inline fun <A, reified I : A> A.downcastApiType(name: String): I {
    check(this is I) { "Custom implementations of $name are not supported!" }
    return this
}
