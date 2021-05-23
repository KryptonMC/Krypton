/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.bootstrap;

import io.github.slimjar.app.Application;
import io.github.slimjar.app.ApplicationFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Main {

    public static void main(final String[] args) {
        System.out.println("Loading libraries, please wait...");
        Application application = null;
        try {
            final ApplicationFactory factory = new ApplicationFactory(ApplicationConfigurator.configure());
            final Collection<String> modules = List.of("krypton-api", "krypton-server");
            application = factory.createIsolatedApplication(modules, "org.kryptonmc.krypton.KryptonApplication", Thread.currentThread().getContextClassLoader(), new ArrayList<>(List.of(args)));
            System.out.println("Libraries loaded! Starting server...");
        } catch (final ReflectiveOperationException | IOException exception) {
            System.out.println("Failed to load libraries! Shutting down...");
            exception.printStackTrace();
        }
        if (application != null) application.start();
    }
}
