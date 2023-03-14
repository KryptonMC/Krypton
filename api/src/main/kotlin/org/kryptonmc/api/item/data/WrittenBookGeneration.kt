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
package org.kryptonmc.api.item.data

/**
 * A generation that a written book is in. This is used to describe how many
 * times the written book has been copied.
 */
public enum class WrittenBookGeneration {

    /**
     * An original copy. Books in this generation have not been copied from
     * other sources, and are from the source.
     */
    ORIGINAL,

    /**
     * The first copy of a book. Also known as "second-hand".
     */
    COPY_OF_ORIGINAL,

    /**
     * The second copy of a book. This is the last copy that can be made of a
     * book.
     */
    COPY_OF_COPY,

    /**
     * Unused in vanilla, and functions the same as [COPY_OF_COPY].
     */
    TATTERED
}
