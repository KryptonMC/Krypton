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
package org.kryptonmc.krypton.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * This class is a hacky way to avoid copying arrays by avoiding using the
 * spread operator. All the methods in here take arrays and pass them
 * directly as varargs.
 */
public final class NoSpread {

    private NoSpread() {
    }

    // Wrapper for com.google.common.collect.Iterators#forArray
    public static <E> UnmodifiableIterator<E> iteratorsForArray(final E[] array) {
        return Iterators.forArray(array);
    }

    // Wrapper for com.google.inject.Guice#createInjector
    public static Injector guiceCreateInjector(final Module[] modules) {
        return Guice.createInjector(modules);
    }

    // Wrapper for java.util.concurrent.CompletableFuture#allOf
    public static CompletableFuture<Void> completableFutureAllOf(final CompletableFuture<?>[] cfs) {
        return CompletableFuture.allOf(cfs);
    }

    // Wrapper for java.nio.file.FileSystem#getPath
    public static Path fileSystemGetPath(final FileSystem system, final String first, final String[] more) {
        return system.getPath(first, more);
    }
}
