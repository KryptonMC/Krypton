package org.kryptonmc.krypton.plugin

import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path

/**
 * The class loader used to load plugins. This is only so we can expose addURL for internal use
 */
class PluginClassLoader(vararg urls: URL) : URLClassLoader(urls) {

    internal fun addPath(path: Path) {
        addURL(path.toUri().toURL())
    }

    companion object {

        init {
            ClassLoader.registerAsParallelCapable()
        }
    }
}
