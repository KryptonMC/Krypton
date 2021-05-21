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
