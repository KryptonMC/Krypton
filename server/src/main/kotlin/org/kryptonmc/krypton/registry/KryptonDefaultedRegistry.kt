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
package org.kryptonmc.krypton.registry

import org.kryptonmc.api.registry.DefaultedRegistry

/**
 * This type exists to unify the APIs of KryptonRegistry and DefaultedRegistry, so we can use internal methods on defaulted registries
 * without having to use the implementation class, which is writable.
 */
interface KryptonDefaultedRegistry<T> : KryptonRegistry<T>, DefaultedRegistry<T>
