package org.kryptonmc.bootstrap;

import io.github.slimjar.relocation.Relocator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class DummyRelocator implements Relocator {

    @Override
    public void relocate(final File source, final File target) {
        try {
            target.delete();
            Files.copy(source.toPath(), target.toPath());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}
