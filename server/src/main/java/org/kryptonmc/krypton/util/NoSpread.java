/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
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
}
